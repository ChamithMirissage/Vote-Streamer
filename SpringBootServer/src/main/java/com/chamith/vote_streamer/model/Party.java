package com.chamith.vote_streamer.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Party {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private int votes;
    private float votePercentage;
    private int seats;

    @ManyToOne
    @JoinColumn(name = "election_result_id")
    @JsonBackReference
    private ElectionResult electionResult;

}
