package by.maxi.blockchain.ark.service;

import io.ark.core.Network;
import io.ark.core.Peer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Maxi on 29.10.2017.
 */
@Slf4j
@Service
public class NetworkService {

    private Network network;

    @Value("${network.peer.max-calls}")
    private int maxPeerCalls;

    private Peer activePeer = null;
    private AtomicInteger activePeerCalls = new AtomicInteger();

    @PostConstruct
    private void init() {
        network = Network.getMainnet();
        network.setVersion("1.0.1");
        network.warmup();
        updateActivePeer();
    }

    public Map get(String path, Object... params) {
        path = String.format(path, params);
        Map response = null;
        String[] p = path.split("\\?");
        try {
            if (p.length == 1) {
                response = (Map) activePeer.request("GET", path).get();
            } else if (p.length == 2) {
                response = (Map) activePeer.request("GET", p[0], p[1]).get();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        if (response == null || Boolean.FALSE.equals(response.get("success"))) {
            throw new RuntimeException("No successful response for " + path);
        }

        int calls = activePeerCalls.incrementAndGet();
        if (calls >= maxPeerCalls) {
            updateActivePeer();
        }
        return response;
    }

    @Scheduled(fixedDelayString = "${network.peer.max-time}")
    private void updateActivePeer() {
        Peer peer;
        while (true) {
            peer = network.getRandomPeer();
            log.debug("Check status of peer {}", peer.getIp());
            try {
                Map status = (Map) peer.request("GET", "/peer/status").get(500, TimeUnit.MILLISECONDS);
                if (status != null && Boolean.TRUE.equals(status.get("success"))) {
                    log.info("Set active peer {}", peer.getIp());
                    activePeer = peer;
                    activePeerCalls.set(0);
                    break;
                }
            } catch (Exception e) {
                log.debug("Inactive peer detected: {}", peer.getIp());
            }
        }
    }

    @Scheduled(initialDelayString = "${network.peer.ping-interval}", fixedDelayString = "${network.peer.ping-interval}")
    private void pingActivePeer() {
        try {
            Map status = (Map) activePeer.request("GET", "/peer/status").get(500, TimeUnit.MILLISECONDS);
            if (status == null || Boolean.FALSE.equals(status.get("success"))) {
                updateActivePeer();
            }
        } catch (Exception e) {
            log.debug("Active peer is not responding");
            updateActivePeer();
        }
    }
}
