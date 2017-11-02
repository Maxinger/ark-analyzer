package by.maxi.blockchain.ark.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by Maxi on 29.10.2017.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Delegate {

    private String username;
    private String address;
    private String publicKey;
    private Long vote;
    private Integer producedblocks;
    private Integer missedblocks;
    private Integer rate;
    private Double approval;
    private Double productivity;
}
