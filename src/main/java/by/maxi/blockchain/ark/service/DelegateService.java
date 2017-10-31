package by.maxi.blockchain.ark.service;

import by.maxi.blockchain.ark.model.Delegate;
import by.maxi.blockchain.ark.model.Voter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Max on 30.10.2017.
 */
@Service
public class DelegateService {

    private static final Logger LOG = LoggerFactory.getLogger(NetworkService.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private NetworkService networkService;

    public List<Delegate> getForgingDelegates() {
        List<Delegate> result = new ArrayList<>();
        try {
            Map response = (Map) networkService.getActivePeer().request("GET", "/api/delegates").get();
            if (Boolean.TRUE.equals(response.get("success"))) {
                result.addAll(mapper.convertValue(response.get("delegates"), new TypeReference<List<Delegate>>() {}));
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    public List<Voter> getDelegateVoters(String publicKey) {
        List<Voter> result = new ArrayList<>();
        try {
            Map response = (Map) networkService.getActivePeer().request("GET", "/api/delegates/voters", "publicKey=" + publicKey).get();
            if (Boolean.TRUE.equals(response.get("success"))) {
                result.addAll(mapper.convertValue(response.get("accounts"), new TypeReference<List<Voter>>() {}));
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }
}
