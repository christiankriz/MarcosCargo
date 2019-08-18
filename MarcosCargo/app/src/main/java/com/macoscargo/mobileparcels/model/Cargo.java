package com.macoscargo.mobileparcels.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by christiannhlabano on 2019/07/19.
 */

public class Cargo extends RealmObject {

    //@PrimaryKey
    private int id;
    private String trackingNo;
    private String CargoName;
    private String pickUpPoint;
    private String destination;
    private Date despatchDate;
    private Date arrivalDate;
    private String courier;
    private String trackingStatus;

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    private String tittle;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }

    public String getCargoName() {
        return CargoName;
    }

    public void setCargoName(String itemName) {
        this.CargoName = itemName;
    }

    public String getPickUpPoint() {
        return pickUpPoint;
    }

    public void setPickUpPoint(String pickUpPoint) {
        this.pickUpPoint = pickUpPoint;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Date getDespatchDate() {
        return despatchDate;
    }

    public void setDespatchDate(Date despatchDate) {
        this.despatchDate = despatchDate;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public String getTrackingStatus() {
        return trackingStatus;
    }

    public void setTrackingStatus(String trackingStatus) {
        this.trackingStatus = trackingStatus;
    }
}
