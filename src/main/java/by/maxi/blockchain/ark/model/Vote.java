package by.maxi.blockchain.ark.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by Max on 02.11.2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Vote {

    @Id
    @Column(nullable = false)
    private String transacationId;

    @Column(nullable = false)
    private String blockId;

    @Column(nullable = false)
    private Long blockHeight;

    @Column(nullable = false)
    private String voterPublicKey;

    @Column(nullable = false)
    private String delegatePublicKey;

    @Column(nullable = false)
    private long timestamp;

    @Column(nullable = false)
    private boolean added;

    private transient long balance;

    private transient long duration;

    public int getDurationInDays() {
        return (int) (duration / (24 * 60 * 60 / 8));
    }
}
