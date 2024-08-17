package com.example.brand_backend.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Events {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventID;
    @Column(name = "brandID")
    private int brandID;
    @Column(name = "name")
    private String name;
    @Column(name = "image")
    private String image;
    @Column(name = "voucherCount")
    private int voucherCount;
    @Column(name = "startTime")
    private LocalDateTime startTime;
    @Column(name = "endTime")
    private LocalDateTime endTime;

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getBrandID() {
        return brandID;
    }

    public void setBrandID(int brandID) {
        this.brandID = brandID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getVoucherCount() {
        return voucherCount;
    }

    public void setVoucherCount(int voucherCount) {
        this.voucherCount = voucherCount;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
