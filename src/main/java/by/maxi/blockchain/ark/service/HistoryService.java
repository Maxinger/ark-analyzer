package by.maxi.blockchain.ark.service;

import by.maxi.blockchain.ark.model.Vote;
import by.maxi.blockchain.ark.repository.VoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Created by Max on 02.11.2017.
 */
@Slf4j
@Service
public class HistoryService {

    @Value("${history.votes.batch.size}")
    private int batchSize;

    @Autowired
    private NetworkService networkService;

    @Autowired
    private VoteRepository voteRepository;

    private List<Vote> votes;

    @PostConstruct
    private void init() {
        votes = voteRepository.findAll();
    }

    @Scheduled(fixedDelayString = "${history.votes.batch.interval}")
    private void pullVotes() {
        int offset = votes.size();
        try {
            Map response = networkService.get("/api/transactions?orderBy=timestamp:asc&offset=%d&limit=%d&type=3", offset, batchSize);
            int count = Integer.valueOf((String) response.get("count"));
            if (count > offset) {
                log.info("Pulling vote transaction from {} to {} out of {}", offset, offset + batchSize - 1, count);
                List<Map> transactions = (List<Map>) response.get("transactions");
                for (Map t : transactions) {
                    String transactionId = (String) t.get("id");
                    response = networkService.get("/api/transactions/get?id=%s", transactionId);
                    Map data = (Map) response.get("transaction");
                    Map transactionVotes = (Map) data.get("votes");
                    List<String> added = (List<String>) transactionVotes.get("added");
                    List<String> deleted = (List<String>) transactionVotes.get("deleted");
                    if (added.size() + deleted.size() != 1) {
                        throw new RuntimeException("Incorrect votes data for transaction " + transactionId);
                    }
                    boolean isAdded = !added.isEmpty();
                    Vote vote = Vote.builder()
                            .transacationId(transactionId)
                            .blockId((String) data.get("blockid"))
                            .blockHeight(((Number) data.get("height")).longValue())
                            .timestamp(((Number) data.get("timestamp")).longValue())
                            .voterPublicKey((String) data.get("senderPublicKey"))
                            .added(isAdded)
                            .delegatePublicKey((isAdded ? added : deleted).get(0))
                            .build();

                    voteRepository.saveAndFlush(vote);
                    votes.add(vote);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
