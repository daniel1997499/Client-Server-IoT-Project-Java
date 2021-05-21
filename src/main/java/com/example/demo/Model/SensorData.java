package com.example.demo.Model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "sensor_data")
public class SensorData implements Serializable {
    @Id
    @GeneratedValue
    private Long ID;
    @NotNull
    @Min(1)
    @Column(name = "device_id", nullable = false)
    private Long deviceId;
//    @Column(name = "token", nullable = false)
    @Transient
    private String token;
    @NotNull
    @Size(min=1, max=30)
    @Column(name = "sensor", nullable = false)
    private String sensor;
    @NotNull
    @Size(min=1, max=30)
    @Column(name = "data_type", nullable = false)
    private String dataType; //humidity or temp or something else
    @NotNull
    @Size(min=1, max=30)
    @Column(name = "data", nullable = false)
    private String data;
    @CreationTimestamp
    @Column(name="posted", nullable = false)
    private Timestamp posted;

    public SensorData() {
    }

    public SensorData(Long deviceId, String sensor, String dataType, String data) {
        this.deviceId = deviceId;
        this.sensor = sensor;
        this.dataType = dataType;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
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
