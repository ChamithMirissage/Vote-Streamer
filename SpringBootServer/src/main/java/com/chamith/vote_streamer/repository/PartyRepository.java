package com.chamith.vote_streamer.repository;

import com.chamith.vote_streamer.model.Party;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Integer> {
}
