package com.chamith.vote_streamer.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class SumUpResultWrapper {

    private Map<String, PartyDto> partyResult;
    private int validVotes;
    private int polledVotes;
    private int registeredVotes;

}
