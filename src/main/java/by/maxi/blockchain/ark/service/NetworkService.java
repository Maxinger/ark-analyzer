package by.maxi.blockchain.ark.service;

import io.ark.core.Network;
import io.ark.core.Peer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Maxi on 29.10.2017.
 */
@Service
public class NetworkService {

    private static final Logger LOG = LoggerFactory.getLogger(NetworkService.class);

    private Network network;

    private Peer activePeer = null;

    @PostConstruct
    private void init() {
        network = Network.getMainnet();
        network.setVersion("1.0.1");
        network.warmup();
        updateActivePeer();
    }

    public Peer getActivePeer() {
        if (activePeer == null) {
            updateActivePeer();
        }
        return activePeer;
    }

    @Scheduled(fixedDelay = 10 * 60 * 1000)
    private void updateActivePeer() {
        Peer peer;
        while (true) {
            peer = network.getRandomPeer();
            LOG.debug("Check status of peer {}", peer.getIp());
            try {
                Map status = (Map) peer.request("GET", "/peer/status").get(500, TimeUnit.MILLISECONDS);
                if (status != null && Boolean.TRUE.equals(status.get("success"))) {
                    LOG.info("Set active peer {}", peer.getIp());
                    activePeer = peer;
                    break;
                }
            } catch (Exception e) {
                LOG.debug("Inactive peer detected: {}", peer.getIp());
            }
        }
    }

//    @Scheduled(fixedDelay = 60 * 1000)
    private void pingActivePeer() {
        try {
            Map status = (Map) activePeer.request("GET", "/peer/status").get(500, TimeUnit.MILLISECONDS);
            if (status == null || Boolean.FALSE.equals(status.get("success"))) {
                updateActivePeer();
            }
        } catch (Exception e) {
            LOG.debug("Active peer is not responding");
            updateActivePeer();
        }
    }
}
