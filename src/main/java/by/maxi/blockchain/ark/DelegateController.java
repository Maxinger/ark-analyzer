package by.maxi.blockchain.ark;

import by.maxi.blockchain.ark.model.Vote;
import by.maxi.blockchain.ark.model.Voter;
import by.maxi.blockchain.ark.service.BlockChainService;
import by.maxi.blockchain.ark.service.DelegateService;
import by.maxi.blockchain.ark.service.VoteService;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.Histogram;
import org.knowm.xchart.style.CategoryStyler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Max on 30.10.2017.
 */
@Controller
public class DelegateController {

    private static final Logger LOG = LoggerFactory.getLogger(DelegateController.class);

    @Autowired
    private DelegateService delegateService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private BlockChainService blockChainService;

    @GetMapping({"/", "/delegates"})
    public String delegatesList(Model model) {
        model.addAttribute("delegates", delegateService.getForgingDelegates());
        return "delegates";
    }

    @GetMapping("/delegate/{publicKey}/voters")
    public String delegateVoters(@PathVariable String publicKey, Model model) {
        List<Voter> voters = delegateService.getDelegateVoters(publicKey);
        model.addAttribute("total", voters.size());

        voters = voters.stream().filter(v -> v.getBalance() > 0).collect(Collectors.toList());
        model.addAttribute("actual", voters.size());

        model.addAttribute("mean", voters.stream().mapToLong(Voter::getBalance).average().getAsDouble());

        try {
            model.addAttribute("hist", buildVotersHistogram(voters));
        } catch (IOException e) {
            LOG.error("Failed to build a histogram for voters distribution", e);
        }

        model.addAttribute("voters", voters.stream().sorted(Comparator.comparingLong(Voter::getBalance).reversed())
                .limit(10).collect(Collectors.toList()));

        Map<String, Long> balances = voters.stream().collect(Collectors.toMap(Voter::getPublicKey, Voter::getBalance));
        List<Vote> votes = voteService.getActiveVotes(publicKey);
        long totalBlocks = blockChainService.getBlockCount();

        votes.forEach(v -> {
            v.setDuration(totalBlocks - v.getBlockHeight());
            v.setBalance(balances.getOrDefault(v.getVoterPublicKey(), 0L));
        });

        model.addAttribute("votes", votes.stream().filter(v -> v.getBalance() > 0).collect(Collectors.toList()));

        return "votes";
    }

    private String buildVotersHistogram(List<Voter> voters) throws IOException {
        List<Long> nums = voters.stream().mapToLong(Voter::getBalance).boxed().collect(Collectors.toList());
        Histogram hist = new Histogram(nums, Math.min(nums.size(), 5));

        CategoryChart chart = new CategoryChart(800, 500);
        chart.setTitle("Voters distribution");
        chart.setXAxisTitle("Balances (in thousands)");
        chart.setYAxisTitle("Number");
        chart.addSeries("hist",
                hist.getxAxisData().stream().mapToLong(Double::longValue).map(Utils::toKiloArk).boxed().collect(Collectors.toList()),
                hist.getyAxisData());

        CategoryStyler styler = chart.getStyler();
        styler.setHasAnnotations(true);
        styler.setLegendVisible(false);
        styler.setXAxisDecimalPattern("0");

        byte[] bytes = BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
