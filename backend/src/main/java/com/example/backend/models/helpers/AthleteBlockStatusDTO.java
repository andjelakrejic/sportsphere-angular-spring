package com.example.backend.models.helpers;

public class AthleteBlockStatusDTO {
    private boolean blocked;
    private int noShowCount;
    private int maxNoShows;

    public AthleteBlockStatusDTO() {}

    public AthleteBlockStatusDTO(boolean blocked, int noShowCount, int maxNoShows) {
        this.blocked = blocked;
        this.noShowCount = noShowCount;
        this.maxNoShows = maxNoShows;
    }

    public boolean isBlocked() { return blocked; }
    public void setBlocked(boolean blocked) { this.blocked = blocked; }

    public int getNoShowCount() { return noShowCount; }
    public void setNoShowCount(int noShowCount) { this.noShowCount = noShowCount; }

    public int getMaxNoShows() { return maxNoShows; }
    public void setMaxNoShows(int maxNoShows) { this.maxNoShows = maxNoShows; }
}