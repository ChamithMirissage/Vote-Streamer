package com.chamith.vote_streamer.repository;

import com.chamith.vote_streamer.model.ElectionResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElectionResultRepository extends JpaRepository<ElectionResult, Integer> {

    ElectionResult findByElectoralDistrictAndPollingDivision(String electoralDistrict, String pollingDivision);

    List<ElectionResult> findByElectoralDistrict(String electoralDistrict);

    List<ElectionResult> findByPollingDivision(String pollingDivision);

    List<ElectionResult> findByPollingDivisionNot(String pollingDivision);

    List<ElectionResult> findTop10ByPollingDivisionNotOrderByAddedTimeDesc(String pollingDivision);

    int countByElectoralDistrict(String electoralDistrict);
}
