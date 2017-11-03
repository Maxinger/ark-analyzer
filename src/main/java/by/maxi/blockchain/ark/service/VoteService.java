package by.maxi.blockchain.ark.service;

import by.maxi.blockchain.ark.model.Vote;
import by.maxi.blockchain.ark.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Max on 03.11.2017.
 */
@Service
public class VoteService {

    @Autowired
    private VoteRepository voteRepository;

    public List<Vote> getActiveVotes(String delegatePublicKey) {
        List<Vote> votes = voteRepository.findByDelegatePublicKeyOrderByTimestampDesc(delegatePublicKey);

        Set<String> rejectedVotes = new HashSet<>();
        for (Iterator<Vote> it = votes.iterator(); it.hasNext();) {
            Vote vote = it.next();
            if (!vote.isAdded()) {
                rejectedVotes.add(vote.getVoterPublicKey());
                it.remove();
            } else if (rejectedVotes.contains(vote.getVoterPublicKey())) {
                it.remove();
            }
        }
        Collections.reverse(votes);

        return votes;
    }
}
