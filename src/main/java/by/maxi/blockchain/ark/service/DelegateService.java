package by.maxi.blockchain.ark.service;

import by.maxi.blockchain.ark.model.Delegate;
import by.maxi.blockchain.ark.model.Voter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Max on 30.10.2017.
 */
@Slf4j
@Service
public class DelegateService {

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private NetworkService networkService;

    public List<Delegate> getForgingDelegates() {
        Map response = networkService.get("/api/delegates");
        return mapper.convertValue(response.get("delegates"), new TypeReference<List<Delegate>>() {});
    }

    public List<Voter> getDelegateVoters(String publicKey) {
        Map response = networkService.get("/api/delegates/voters?publicKey=%s", publicKey);
        return mapper.convertValue(response.get("accounts"), new TypeReference<List<Voter>>() {});
    }
}
