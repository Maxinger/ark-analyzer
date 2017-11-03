package by.maxi.blockchain.ark.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Max on 03.11.2017.
 */
@Service
public class BlockChainService {

    @Autowired
    private NetworkService networkService;

    public long getBlockCount() {
        Map response = networkService.get("/api/blocks?limit=1");
        return ((Number) response.get("count")).longValue();
    }
}
