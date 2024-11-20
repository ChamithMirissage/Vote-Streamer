package com.chamith.vote_streamer.data;

import com.chamith.vote_streamer.model.PartyDto;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class PartyData {
    @Getter
    public static Map<String, PartyDto> partyResultMap = new HashMap<>();

    static {
        partyResultMap.put("NATIONAL PEOPLE'S POWER", new PartyDto());
        partyResultMap.put("SAMAGI JANA BALAWEGAYA", new PartyDto());
        partyResultMap.put("NEW DEMOCRATIC FRONT", new PartyDto());
        partyResultMap.put("SRI LANKA PODUJANA PERAMUNA", new PartyDto());
        partyResultMap.put("ILANKAI TAMIL ARASU KACHCHI", new PartyDto());
        partyResultMap.put("SARVAJANA BALAYA", new PartyDto());
        partyResultMap.put("UNITED NATIONAL PARTY", new PartyDto());
        partyResultMap.put("AHILA ILANKAI THAMIL CONGRESS", new PartyDto());
        partyResultMap.put("EELAM PEOPLE'S DEMOCRATIC PARTY", new PartyDto());
        partyResultMap.put("THAMIL MAKKAL VIDUTHALAI PULIKAL", new PartyDto());
        partyResultMap.put("SRI LANKA MUSLIM CONGRESS", new PartyDto());
        partyResultMap.put("ALL CEYLON MAKKAL CONGRESS", new PartyDto());
        partyResultMap.put("UNITED NATIONAL ALLIANCE", new PartyDto());
        partyResultMap.put("DEMOCRATIC NATIONAL ALLIANCE", new PartyDto());
        partyResultMap.put("UNITED DEMOCRATIC VOICE", new PartyDto());
        partyResultMap.put("DEMOCRATIC TAMIL NATIONAL ALLIANCE", new PartyDto());
        partyResultMap.put("PEOPLE'S STRUGGLE ALLIANCE", new PartyDto());
        partyResultMap.put("DEMOCRATIC LEFT FRONT", new PartyDto());
        partyResultMap.put("NATIONAL FRONT FOR GOOD GOVERNANCE", new PartyDto());
        partyResultMap.put("NATIONAL DEMOCRATIC FRONT", new PartyDto());
        partyResultMap.put("SRI LANKA LABOUR PARTY", new PartyDto());
        partyResultMap.put("ALL CEYLON TAMIL CONGRESS", new PartyDto());
        partyResultMap.put("THAMIZH MAKKAL KOOTTANI", new PartyDto());
        partyResultMap.put("VANNI INDEPENDENT GROUP 7", new PartyDto());
        partyResultMap.put("HAMBANTOTA INDEPENDENT GROUP 1", new PartyDto());
        partyResultMap.put("POLONNARUWA INDEPENDENT GROUP 2", new PartyDto());
        partyResultMap.put("BADULLA INDEPENDENT GROUP 3", new PartyDto());
        partyResultMap.put("MONARAGALA INDEPENDENT GROUP 2", new PartyDto());
        partyResultMap.put("NUWARAELIYA INDEPENDENT GROUP 1", new PartyDto());
        partyResultMap.put("NUWARAELIYA INDEPENDENT GROUP 6", new PartyDto());
        partyResultMap.put("JAFFNA INDEPENDENT GROUP 17", new PartyDto());
        partyResultMap.put("KANDY INDEPENDENT GROUP 11", new PartyDto());
    }

    public static void resetData() {
        for (PartyDto partyDto : partyResultMap.values()) {
            partyDto.setVotes(0);
            partyDto.setVotePercentage(0.0f);
            partyDto.setSeats(0);
        }
    }

    public static void setVotes(String partyName, int votes) {
        partyResultMap.get(partyName).setVotes(partyResultMap.get(partyName).getVotes() + votes);
    }

    public static void setPercentage(int validVotes) {
        for (PartyDto partyDto : partyResultMap.values()) {
            partyDto.setVotePercentage(100.0f * partyDto.getVotes() / validVotes);
        }
    }
}
