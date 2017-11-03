package by.maxi.blockchain.ark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by Maxi on 29.10.2017.
 */
@SpringBootApplication
@EnableScheduling
public class AnalyzerApplication {

    public static void main(String args[]) {
        SpringApplication.run(AnalyzerApplication.class);
    }
}
