package com.example.brand_backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "brands")
public class Brands {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "accountid", nullable = false)
    private Accounts account;
    @Column(name = "name")
    private String name;
    @Column(name = "field")
    private String field;
    @Column(name = "address")
    private String address;
    @Column(name = "GPS_lat")
    private Float GPS_lat;
    @Column(name = "GPS_long")
    private Float GPS_long;
    @Column(name = "status")
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Accounts getAccount() {
        return account;
    }

    public void setAccount(Accounts account) {
        this.account = account;
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

    public Float getGPS_lat() {
        return GPS_lat;
    }

    public void setGPS_lat(Float GPS_lat) {
        this.GPS_lat = GPS_lat;
    }

    public float getGPS_long() {
        return GPS_long;
    }

    public void setGPS_long(Float GPS_long) {
        this.GPS_long = GPS_long;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}