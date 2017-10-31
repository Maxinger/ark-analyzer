package by.maxi.blockchain.ark;

import by.maxi.blockchain.ark.model.Voter;
import by.maxi.blockchain.ark.service.DelegateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Max on 30.10.2017.
 */
@Controller
public class DelegateController {

    private static final Logger LOG = LoggerFactory.getLogger(DelegateController.class);

    @Autowired
    private DelegateService delegateService;

    @RequestMapping("/delegates")
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

        model.addAttribute("voters", voters.stream().sorted(Comparator.comparingLong(Voter::getBalance).reversed())
                .limit(10).collect(Collectors.toList()));
        return "voters";
    }
}
