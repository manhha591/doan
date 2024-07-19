package com.example.doanthuexe.models;

import java.io.Serializable;

public class Car implements Serializable {
    private int carId;
    private int ownerId;
    private int year;

    private int numberOfSeats;
    private String engine;
    private String model;
    private String company;
    private int state;
    private int price;
    private String gear;
    private String numberPlate;
    private String location;
    private String firstImage;
    private String endImage;
    private String leftImage;
    private String rightImage;


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public String getEndImage() {
        return endImage;
    }

    public void setEndImage(String endImage) {
        this.endImage = endImage;
    }

    public String getLeftImage() {
        return leftImage;
    }

    public void setLeftImage(String leftImage) {
        this.leftImage = leftImage;
    }

    public String getRightImage() {
        return rightImage;
    }

    public void setRightImage(String rightImage) {
        this.rightImage = rightImage;
    }

    public String getGear() {
        return gear;
    }

    public void setGear(String gear) {
        this.gear = gear;
    }

    public Car() {
    }

    public Car(int ownerId, int carId, int Year, int numberOfSeats, String engine, String model, String company, int state, int price, String gear, String numberPlate, String firstImage, String endImage, String leftImage, String rightImage, String location) {
        this.ownerId = ownerId;
        this.carId = carId;
        this.year = Year;
        this.numberOfSeats = numberOfSeats;
        this.engine = engine;
        this.model = model;
        this.company = company;
        this.state = state;
        this.price = price;
        this.gear = gear;
        this.numberPlate = numberPlate;
        this.firstImage = firstImage;
        this.endImage = endImage;
        this.leftImage = leftImage;
        this.rightImage = rightImage;
        this.location = location;
    }

    public Car(int ownerId, int carId, int Year, int numberOfSeats, String engine, String model, String company, int state, int price, String firstImage, String gear) {
        this.ownerId = ownerId;
        this.carId = carId;
        this.year = Year;
        this.numberOfSeats = numberOfSeats;
        this.engine = engine;
        this.model = model;
        this.company = company;
        this.state = state;
        this.price = price;
        this.gear = gear;
        this.firstImage = firstImage;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getFirstImage() {
        return firstImage;
    }

    public void setFirstImage(String firstImage) {
        this.firstImage = firstImage;
    }

    @Override
    public String toString() {
        return "Car{" +
                "carId=" + carId +
                ", ownerId=" + ownerId +
                ", year=" + year +
                ", numberOfSeats=" + numberOfSeats +
                ", engine='" + engine + '\'' +
                ", model='" + model + '\'' +
                ", company='" + company + '\'' +
                ", state=" + state +
                ", price=" + price +
                ", gear='" + gear + '\'' +
                ", numberPlate='" + numberPlate + '\'' +
                ", location='" + location + '\'' +
                ", firstImage='" + firstImage + '\'' +
                ", endImage='" + endImage + '\'' +
                ", leftImage='" + leftImage + '\'' +
                ", rightImage='" + rightImage + '\'' +
                '}';
    }
}
