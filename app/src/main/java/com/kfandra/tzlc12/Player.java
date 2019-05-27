package com.kfandra.tzlc12;

public class Player {
    private String id;
    private String playerName;
    private String clubName;
    private int currentValue;
    private long orgID;
    private int senialwombat;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    public long getOrgID() {
        return orgID;
    }

    public void setOrgID(long orgID) {
        this.orgID = orgID;
    }

    public int getSenialwombat() {
        return senialwombat;
    }

    public void setSenialwombat(int senialwombat) {
        this.senialwombat = senialwombat;
    }
}
