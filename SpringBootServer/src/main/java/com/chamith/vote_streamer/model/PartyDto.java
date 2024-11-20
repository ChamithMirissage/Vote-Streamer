package com.chamith.vote_streamer.model;

import lombok.Data;

@Data
public class PartyDto {

    private int votes;
    private float votePercentage;
    private int seats;

}
