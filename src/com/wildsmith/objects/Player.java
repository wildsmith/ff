package com.wildsmith.objects;

import com.wildsmith.constants.Position;

public class Player implements Comparable<Player> {

    private Position position;

    private String firstName;

    private String lastName;

    private String team;

    private float averageRanking;

    private float bestRanking;

    private float worstRanking;

    private int byeWeek;

    private int adp;

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setExtras(String extras) {
        if (team != null && byeWeek != 0) {
            return;
        }

        if (extras == null || "".equals(extras) || !extras.contains(" ")) {
            return;
        }

        extras = extras.trim();
        String[] splitExtras = extras.split(" ");
        if (splitExtras == null || splitExtras.length == 0) {
            return;
        }

        team = splitExtras[0];

        if (splitExtras.length == 1) {
            return;
        }

        String byeWeekString = splitExtras[splitExtras.length - 1];
        if (" ".equals(byeWeekString) || "&nbsp;".equals(byeWeekString) || "&#160;".equals(byeWeekString)) {
            byeWeekString = splitExtras[splitExtras.length - 2];
        }
        byeWeek = Integer.valueOf(byeWeekString.replace(")", ""));
    }

    public void setFullName(String fullName) {
        String[] name = fullName.split(" ");

        if (name.length >= 1) {
            this.firstName = name[0];
        }
        if (name.length >= 2) {
            this.lastName = name[1];
        }
    }

    public String getFullName() {
        final String tempFirstName = (firstName == null) ? "" : firstName;
        final String tempLastName = (lastName == null) ? "" : lastName;
        return tempFirstName + " " + tempLastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public float getAverageRanking() {
        return averageRanking;
    }

    public void setAverageRanking(float averageRanking) {
        this.averageRanking = averageRanking;
    }

    public float getBestRanking() {
        return bestRanking;
    }

    public void setBestRanking(float bestRanking) {
        this.bestRanking = bestRanking;
    }

    public float getWorstRanking() {
        return worstRanking;
    }

    public void setWorstRanking(float worstRanking) {
        this.worstRanking = worstRanking;
    }

    public int getByeWeek() {
        return byeWeek;
    }

    public void setByeWeek(int byeWeek) {
        this.byeWeek = byeWeek;
    }

    public void setADP(int adp) {
        this.adp = adp;
    }

    public int getADP() {
        return adp;
    }

    @Override
    public int compareTo(Player player) {
        if (getAverageRanking() < player.getAverageRanking()) {
            return -1;
        } else if (getAverageRanking() > player.getAverageRanking()) {
            return 1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Position: " + position.getName() + "\nPlayer: " + getFullName() + "\nAvg Rank: " + averageRanking + "\nBest Rank: "
                + bestRanking + "\nWorst Rank: " + worstRanking + "\nADP: " + adp + "\nTeam: " + team + "\nBye Week: " + byeWeek;
    }
}
