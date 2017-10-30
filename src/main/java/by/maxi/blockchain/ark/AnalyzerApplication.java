package by.maxi.blockchain.ark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Maxi on 29.10.2017.
 */
@SpringBootApplication
public class AnalyzerApplication {

    private static final Logger log = LoggerFactory.getLogger(AnalyzerApplication.class);

    public static void main(String args[]) {
        SpringApplication.run(AnalyzerApplication.class);
    }
}
