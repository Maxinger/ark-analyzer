package by.maxi.blockchain.ark;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ark.core.Network;
import io.ark.core.Peer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by Maxi on 29.10.2017.
 */
@Service
public class NetworkService {

    private Network network;
    private ObjectMapper objectMapper;

    @PostConstruct
    private void init() {
        network = Network.getMainnet();
        network.warmup();

        objectMapper = new ObjectMapper();
    }

    private Peer peer() {
        return network.getRandomPeer();
    }
}
