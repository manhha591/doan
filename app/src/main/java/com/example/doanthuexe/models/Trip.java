package com.example.doanthuexe.models;

import java.util.Date;

public class Trip {
    private int tripId;
    private int carId;
    private String carName;
    private int ownerId;
    private int clientId;
    private String firstImage;
    private String startTime;
    private String endTime;
    private int sumMoney;
    private int state;
    private String createTime;
    private int notification;
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getNotification() {
        return notification;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Trip() {
    }

    public Trip(int carId, String carName, int ownerId, int clientId, String firstImage, String startTime, String endTime, int sumMoney, int state, String createTime) {
        this.carId = carId;
        this.carName = carName;
        this.ownerId = ownerId;
        this.clientId = clientId;
        this.firstImage = firstImage;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sumMoney = sumMoney;
        this.state = state;
        this.createTime = createTime;
    }

    public String getFirstImage() {
        return firstImage;
    }

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public void setFirstImage(String firstImage) {
        this.firstImage = firstImage;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(int sumMoney) {
        this.sumMoney = sumMoney;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "tripId=" + tripId +
                ", carId=" + carId +
                ", carName='" + carName + '\'' +
                ", ownerId=" + ownerId +
                ", clientId=" + clientId +
                ", firstImage='" + firstImage + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", sumMoney=" + sumMoney +
                ", state=" + state +
                ", createTime='" + createTime + '\'' +
                ", notification=" + notification +
                '}';
    }
}
