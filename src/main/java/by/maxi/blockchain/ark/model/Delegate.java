package by.maxi.blockchain.ark.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Maxi on 29.10.2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Delegate {

    private String username;
    private String address;
    private String publicKey;
    private String vote;
    private Integer producedblocks;
    private Integer missedblocks;
    private Integer rate;
    private Double approval;
    private Double productivity;
}
