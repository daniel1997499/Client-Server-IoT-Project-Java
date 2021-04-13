package com.example.demo.Model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "sensor_data")
public class SensorData {
    @Id
    @GeneratedValue
    private Long ID;
    @Column(name = "device_id")
    private Long deviceId;
    @Column(name = "sensor", nullable = false)
    private String sensor;
    @Column(name = "data", nullable = false)
    private String data;
    @CreationTimestamp
    @Column(name="posted", nullable = false)
    private Timestamp posted;

    public SensorData() {
    }

    public SensorData(Long deviceId, String sensor, String data) {
        this.deviceId = deviceId;
        this.sensor = sensor;
        this.data = data;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Timestamp getPosted() {
        return posted;
    }

    public void setPosted(Timestamp posted) {
        this.posted = posted;
    }
}
