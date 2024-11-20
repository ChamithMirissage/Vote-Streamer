package com.chamith.vote_streamer.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class ElectionResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String electoralDistrict;
    private String pollingDivision;

    @OneToMany(mappedBy = "electionResult", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Party> parties;

    private int validVotes;
    private int polledVotes;
    private int registeredVotes;
    private LocalDateTime addedTime;

}
