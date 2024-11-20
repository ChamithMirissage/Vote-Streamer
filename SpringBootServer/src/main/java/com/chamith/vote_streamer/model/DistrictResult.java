package com.chamith.vote_streamer.model;

import lombok.Data;

import java.util.List;

@Data
public class DistrictResult {

    private List<Party> parties;
    private int validVotes;
    private int polledVotes;
    private int registeredVotes;

}
