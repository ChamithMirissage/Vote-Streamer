package com.chamith.vote_streamer.controller;

import com.chamith.vote_streamer.model.DistrictResult;
import com.chamith.vote_streamer.model.ElectionResult;
import com.chamith.vote_streamer.service.ElectionResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/vote-streamer")
public class ElectionResultController {

    @Autowired
    ElectionResultService resultService;

    @GetMapping("/test")
    public String testAPI() {
        return "API working successfully";
    }

    @PostMapping("/result")
    public Map<String, String> addElectionResult(@RequestBody ElectionResult electionResult) {
        resultService.addElectionResult(electionResult);
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return response;
    }

    @GetMapping("/result/{electoralDistrict}/{pollingDivision}")
    public ElectionResult getPollingDivisionResult(@PathVariable String electoralDistrict, @PathVariable String pollingDivision) {
        ElectionResult electionResult = resultService.getPollingDivisionResult(electoralDistrict, pollingDivision);
        return Objects.requireNonNullElseGet(electionResult, ElectionResult::new);
    }

    @GetMapping("/result/district/{electoralDistrict}")
    public DistrictResult getDistrictResult(@PathVariable String electoralDistrict) {
        return resultService.getDistrictResult(electoralDistrict);
    }

    @GetMapping("/result/overall")
    public DistrictResult getOverallResult() {
        return resultService.getOverallResult();
    }

    @GetMapping("/result/latest")
    public Map<String, List<ElectionResult>> getLatestResults() {
        List<ElectionResult> latestResults = resultService.getLatestResults();
        Map<String, List<ElectionResult>> response = new HashMap<>();
        response.put("results", latestResults);
        return response;
    }

    @GetMapping("/winners")
    public Map<String, String> getDistrictWinners() {
        return resultService.getDistrictWinners();
    }
}
