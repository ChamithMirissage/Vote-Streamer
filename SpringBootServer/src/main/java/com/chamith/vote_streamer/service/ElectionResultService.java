package com.chamith.vote_streamer.service;

import com.chamith.vote_streamer.data.DistrictData;
import com.chamith.vote_streamer.data.PartyData;
import com.chamith.vote_streamer.model.*;
import com.chamith.vote_streamer.repository.ElectionResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ElectionResultService {

    @Autowired
    private ElectionResultRepository resultRepository;

    public void addElectionResult(ElectionResult electionResult) {
        if (electionResult.getParties() != null) {
            for (Party party : electionResult.getParties()) {
                party.setElectionResult(electionResult);
            }
        }
        resultRepository.save(electionResult);

        int districtReleasedResultsCount = resultRepository.countByElectoralDistrict(electionResult.getElectoralDistrict());
        if (districtReleasedResultsCount == DistrictData.getPollingDivisions(electionResult.getElectoralDistrict())) {
            saveDistrictResult(electionResult.getElectoralDistrict());
        }
    }

    public ElectionResult getPollingDivisionResult(String electoralDistrict, String pollingDivision) {
        return resultRepository.findByElectoralDistrictAndPollingDivision(electoralDistrict, pollingDivision);
    }

    public DistrictResult getDistrictResult(String electoralDistrict) {
        ElectionResult result = resultRepository.findByElectoralDistrictAndPollingDivision(electoralDistrict, "District Result");

        if (result == null) {
            List<ElectionResult> divisionResults = resultRepository.findByElectoralDistrict(electoralDistrict);
            if (!divisionResults.isEmpty()) {
                SumUpResultWrapper sumUpResultWrapper = calculateSumUpResult(divisionResults);
                return convertToDistrictResult(sumUpResultWrapper);
            } else {
                return new DistrictResult();
            }
        } else {
            DistrictResult districtResult = new DistrictResult();
            districtResult.setParties(result.getParties());
            districtResult.setValidVotes(result.getValidVotes());
            districtResult.setPolledVotes(result.getPolledVotes());
            districtResult.setRegisteredVotes(result.getRegisteredVotes());
            return districtResult;
        }
    }

    public DistrictResult getOverallResult() {
        List<ElectionResult> divisionResults = resultRepository.findByPollingDivisionNot("District Result");
        SumUpResultWrapper sumUpResultWrapper = calculateSumUpResult(divisionResults);

        Map<String, PartyDto> partyResults = sumUpResultWrapper.getPartyResult();
        List<ElectionResult> districtResults = resultRepository.findByPollingDivision("District Result");

        if (!districtResults.isEmpty()) {
            for (ElectionResult districtResult : districtResults) {
                for (Party party : districtResult.getParties()) {
                    partyResults.get(party.getName()).setSeats(partyResults.get(party.getName()).getSeats() + party.getSeats());
                }
            }
        }
        sumUpResultWrapper.setPartyResult(partyResults);

        return convertToDistrictResult(sumUpResultWrapper);
    }

    public List<ElectionResult> getLatestResults() {
        return resultRepository.findTop10ByPollingDivisionNotOrderByAddedTimeDesc("District Result");
    }

    public Map<String, String> getDistrictWinners() {
        List<ElectionResult> districtResults = resultRepository.findByPollingDivision("District Result");
        Map<String, String> districtWinners = new HashMap<>();

        for (ElectionResult districtResult : districtResults) {
            String partyWon = districtResult.getParties().get(0).getName();
            districtWinners.put(districtResult.getElectoralDistrict(), partyWon);
        }
        return districtWinners;
    }

    private void saveDistrictResult(String electoralDistrict) {
        List<ElectionResult> divisionResults = resultRepository.findByElectoralDistrict(electoralDistrict);
        SumUpResultWrapper sumUpResultWrapper = calculateSumUpResult(divisionResults);

        int[] seats = calculateSeats(electoralDistrict, sumUpResultWrapper.getPartyResult(), sumUpResultWrapper.getValidVotes());

        List<Party> parties = new ArrayList<>();
        int index = 0;

        for (Map.Entry<String, PartyDto> entry : sumUpResultWrapper.getPartyResult().entrySet()) {
            entry.getValue().setSeats(seats[index]);
            parties.add(generateParty(entry));
            index += 1;
            if (index == 5) {
                break;
            }
        }

        ElectionResult result = new ElectionResult();
        result.setElectoralDistrict(electoralDistrict);
        result.setPollingDivision("District Result");
        result.setParties(parties);
        result.setValidVotes(sumUpResultWrapper.getValidVotes());
        result.setPolledVotes(sumUpResultWrapper.getPolledVotes());
        result.setRegisteredVotes(sumUpResultWrapper.getRegisteredVotes());

        for (Party party : result.getParties()) {
            party.setElectionResult(result);
        }
        resultRepository.save(result);
    }

    private SumUpResultWrapper calculateSumUpResult(List<ElectionResult> results) {
        int validVotes = 0;
        int polledVotes = 0;
        int registeredVotes = 0;
        PartyData.resetData();

        for (ElectionResult divisionResult : results) {
            for (Party party : divisionResult.getParties()) {
                PartyData.setVotes(party.getName(), party.getVotes());
            }

            validVotes += divisionResult.getValidVotes();
            polledVotes += divisionResult.getPolledVotes();
            registeredVotes += divisionResult.getRegisteredVotes();
        }
        PartyData.setPercentage(validVotes);

        List<Map.Entry<String, PartyDto>> sortedEntries = new ArrayList<>(PartyData.getPartyResultMap().entrySet());
        sortedEntries.sort((entry1, entry2) -> Integer.compare(entry2.getValue().getVotes(), entry1.getValue().getVotes()));

        Map<String, PartyDto> sortedPartyResultMap = new LinkedHashMap<>();
        for (Map.Entry<String, PartyDto> entry : sortedEntries) {
            sortedPartyResultMap.put(entry.getKey(), entry.getValue());
        }

        return new SumUpResultWrapper(sortedPartyResultMap, validVotes, polledVotes, registeredVotes);
    }

    private int[] calculateSeats(String electoralDistrict, Map<String, PartyDto> partyResultsMap, int validVotes) {
        // Largest Remainder Method
        int totalSeats = DistrictData.getSeats(electoralDistrict);
        float districtQuota = (float) validVotes / totalSeats;

        List<PartyDto> partyResults = new ArrayList<>(partyResultsMap.values());

        int numOfPartiesEligibleForSeats = 0;
        // Consider parties with vote percentage not less than 5%
        for (PartyDto partyResult : partyResults) {
            if (partyResult.getVotePercentage() >= 5.0f) {
                numOfPartiesEligibleForSeats += 1;
            } else {
                break;
            }
        }

        int[] seats = new int[5];
        float[] quotients = new float[numOfPartiesEligibleForSeats];
        float[] remainders = new float[numOfPartiesEligibleForSeats];
        int totalInitialSeats = 0;

        // Allocating full seats
        for (int i = 0; i < numOfPartiesEligibleForSeats; i++) {
            quotients[i] = partyResults.get(i).getVotes() / districtQuota;
            seats[i] = (int) quotients[i];
            totalInitialSeats += seats[i];
            remainders[i] = quotients[i] - seats[i];
        }
        // Bonus seat
        if (seats[0] > seats[1]) {
            seats[0] += 1;
            totalInitialSeats += 1;
        }

        int remainingSeats = totalSeats - totalInitialSeats;
        ArrayList<Integer> maxRemainderIndexes = new ArrayList<>();

        // Allocating remaining seats
        for (int i = 0; i < remainingSeats; i++) {
            float maxRemainder = -1;
            int maxRemainderIndex = -1;

            // Need when remaining seats are higher than eligible parties
            if (i % numOfPartiesEligibleForSeats == 0) {
                maxRemainderIndexes.clear();
            }

            for (int j = 0; j < numOfPartiesEligibleForSeats; j++) {
                if (!maxRemainderIndexes.contains(j) && remainders[j] > maxRemainder) {
                    maxRemainder = remainders[j];
                    maxRemainderIndex = j;
                }
            }

            seats[maxRemainderIndex] += 1;
            maxRemainderIndexes.add(maxRemainderIndex);
        }
        return seats;
    }

    private Party generateParty(Map.Entry<String, PartyDto> partyResult) {
        Party party = new Party();
        party.setName(partyResult.getKey());
        party.setVotes(partyResult.getValue().getVotes());
        party.setVotePercentage(partyResult.getValue().getVotePercentage());
        party.setSeats(partyResult.getValue().getSeats());

        return party;
    }

    private DistrictResult convertToDistrictResult(SumUpResultWrapper districtResultWrapper) {
        Map<String, PartyDto> partyResults = districtResultWrapper.getPartyResult();
        List<Party> parties = new ArrayList<>();
        int index = 0;

        for (Map.Entry<String, PartyDto> entry : partyResults.entrySet()) {
            parties.add(generateParty(entry));
            index += 1;
            if (index == 5) {
                break;
            }
        }

        DistrictResult districtResult = new DistrictResult();
        districtResult.setParties(parties);
        districtResult.setValidVotes(districtResultWrapper.getValidVotes());
        districtResult.setPolledVotes(districtResultWrapper.getPolledVotes());
        districtResult.setRegisteredVotes(districtResultWrapper.getRegisteredVotes());

        return districtResult;
    }
}
