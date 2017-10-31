package by.maxi.blockchain.ark.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Created by Max on 31.10.2017.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Voter {

    private String username;
    private String address;
    private String publicKey;
    private Long balance;
}
