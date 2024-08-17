package com.example.brand_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "brands")
public class Brands {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int brandID;
    @Column(name = "accountID")
    private int accountID;
    @Column(name = "name")
    private String name;
    @Column(name = "field")
    private String field;
    @Column(name = "address")
    private String address;
    @Column(name = "GPS_lat")
    private float GPS_lat;
    @Column(name = "GPS_long")
    private float GPS_long;
    @Column(name = "status")
    private String status;

    public int getBrandID() {
        return brandID;
    }

    public void setBrandID(int brandID) {
        this.brandID = brandID;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getGPS_lat() {
        return GPS_lat;
    }

    public void setGPS_lat(float GPS_lat) {
        this.GPS_lat = GPS_lat;
    }

    public float getGPS_long() {
        return GPS_long;
    }

    public void setGPS_long(float GPS_long) {
        this.GPS_long = GPS_long;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
