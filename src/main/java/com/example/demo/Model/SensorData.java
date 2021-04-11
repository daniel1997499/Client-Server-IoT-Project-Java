package com.example.demo.Model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "sensor_data")
public class SensorData {

    @Id
    @GeneratedValue
    private long ID;
    private String sensor;
    private String data;
    @CreationTimestamp
    private Timestamp posted;

    public SensorData() {
    }

    public SensorData(String sensor, String data) {
        this.sensor = sensor;
        this.data = data;
    }

    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    @Column(name = "sensor", nullable = false)
    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    @Column(name = "data", nullable = false)
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Column(name="posted", nullable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    public Timestamp getPosted() {
        return posted;
    }

    public void setPosted(Timestamp posted) {
        this.posted = posted;
    }

    @Override
    public String toString() {
        return "SensorData{" +
                "ID=" + ID +
                ", sensor='" + sensor + '\'' +
                ", data='" + data + '\'' +
                ", posted=" + posted +
                '}';
    }
}
