package by.maxi.blockchain.ark.repository;

import by.maxi.blockchain.ark.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Max on 02.11.2017.
 */
public interface VoteRepository extends JpaRepository<Vote, String> {

    List<Vote> findByDelegatePublicKeyOrderByTimestampDesc(String delegatePublicKey);
}
