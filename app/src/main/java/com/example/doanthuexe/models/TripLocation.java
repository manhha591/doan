package com.example.doanthuexe.models;

import java.math.BigDecimal;

public class TripLocation {
    private int locationId;
    private  int TripId;
    private Double latitude;
    private  Double  longitude;
    private  int timestamp;

    public TripLocation(int locationId, int tripId, Double  latitude, Double  longitude, int timestamp) {
        this.locationId = locationId;
        TripId = tripId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getTripId() {
        return TripId;
    }

    public void setTripId(int tripId) {
        TripId = tripId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
