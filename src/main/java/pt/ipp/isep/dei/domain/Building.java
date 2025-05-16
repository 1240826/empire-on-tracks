package pt.ipp.isep.dei.domain;

import java.util.Date;

/**
 * Represents a Building upgrade in a station.
 */
public class Building {
    private String type;
    private Date dateAdded;
    private boolean isActive;

    public Building(String type, Date dateAdded, boolean isActive) {
        this.type = type;
        this.dateAdded = dateAdded;
        this.isActive = isActive;
    }

    public String getType() {
        return type;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return type + " (Added: " + dateAdded + ", Active: " + isActive + ")";
    }
}