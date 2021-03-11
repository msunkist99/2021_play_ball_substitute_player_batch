package org.launchcode.play_ball_substitute_player_batch.models;

public class SubstitutePlayer {
    private String gameId;
    private int inning;
    private int offenseDefense;
    private int sequence;
    private String sub ;
    private String playerId;
    private String playerName;
    private int visit_home;
    private int battingOrder;
    private int fieldPosition;
    private String teamId;
    public SubstitutePlayer(String gameId, int inning, int offenseDefense, int sequence, String sub, String playerId, String playerName, int visit_home, int battingOrder, int fieldPosition, String teamId) {
        this.gameId = gameId;
        this.inning = inning;
        this.offenseDefense = offenseDefense;
        this.sequence = sequence;
        this.sub = sub;
        this.playerId = playerId;
        this.playerName = playerName;
        this.visit_home = visit_home;
        this.battingOrder = battingOrder;
        this.fieldPosition = fieldPosition;
        this.teamId = teamId;
    }

    public String getGameId() {
        return gameId;
    }

    public int getInning() {
        return inning;
    }

    public int getOffenseDefense() {
        return offenseDefense;
    }

    public int getSequence() {
        return sequence;
    }

    public String getSub() {
        return sub;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getVisit_home() {
        return visit_home;
    }

    public int getBattingOrder() {
        return battingOrder;
    }

    public int getFieldPosition() {
        return fieldPosition;
    }

    public String getTeamId() {
        return teamId;
    }
}
