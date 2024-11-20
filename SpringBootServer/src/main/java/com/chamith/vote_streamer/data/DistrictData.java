package com.chamith.vote_streamer.data;

import java.util.HashMap;
import java.util.Map;

public class DistrictData {
    public static final Map<String, Integer> pollingDivisionCount = new HashMap<>();
    public static final Map<String, Integer> seatCount = new HashMap<>();

    static {
        pollingDivisionCount.put("Colombo", 16);
        pollingDivisionCount.put("Gampaha", 14);
        pollingDivisionCount.put("Kalutara", 9);
        pollingDivisionCount.put("Kandy", 14);
        pollingDivisionCount.put("Matale", 5);
        pollingDivisionCount.put("Nuwara Eliya", 5);
        pollingDivisionCount.put("Galle", 11);
        pollingDivisionCount.put("Matara", 8);
        pollingDivisionCount.put("Hambantota", 5);
        pollingDivisionCount.put("Jaffna", 12);
        pollingDivisionCount.put("Vanni", 4);
        pollingDivisionCount.put("Batticaloa", 4);
        pollingDivisionCount.put("Digamadulla", 5);
        pollingDivisionCount.put("Trincomalee", 4);
        pollingDivisionCount.put("Kurunegala", 15);
        pollingDivisionCount.put("Puttalam", 6);
        pollingDivisionCount.put("Anuradhapura", 8);
        pollingDivisionCount.put("Polonnaruwa", 4);
        pollingDivisionCount.put("Badulla", 10);
        pollingDivisionCount.put("Monaragala", 4);
        pollingDivisionCount.put("Ratnapura", 9);
        pollingDivisionCount.put("Kegalle", 10);

        seatCount.put("Colombo", 18);
        seatCount.put("Gampaha", 19);
        seatCount.put("Kalutara", 11);
        seatCount.put("Kandy", 12);
        seatCount.put("Matale", 5);
        seatCount.put("Nuwara Eliya", 8);
        seatCount.put("Galle", 9);
        seatCount.put("Matara", 7);
        seatCount.put("Hambantota", 7);
        seatCount.put("Jaffna", 6);
        seatCount.put("Vanni", 6);
        seatCount.put("Batticaloa", 5);
        seatCount.put("Digamadulla", 7);
        seatCount.put("Trincomalee", 4);
        seatCount.put("Kurunegala", 15);
        seatCount.put("Puttalam", 8);
        seatCount.put("Anuradhapura", 9);
        seatCount.put("Polonnaruwa", 5);
        seatCount.put("Badulla", 9);
        seatCount.put("Monaragala", 6);
        seatCount.put("Ratnapura", 11);
        seatCount.put("Kegalle", 9);
    }

    public static int getPollingDivisions(String electoralDistrict) {
        return pollingDivisionCount.get(electoralDistrict);
    }

    public static int getSeats(String electoralDistrict) {
        return seatCount.get(electoralDistrict);
    }
}
