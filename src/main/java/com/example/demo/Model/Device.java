package com.example.demo.Model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table
public class Device {
    @Id
    @GeneratedValue
    private Long ID;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "address", nullable = false)
    private String address;
    @Column(name = "token")
    private String token;
    @CreationTimestamp
    @Column(name="registered", nullable = false)
    private Timestamp registered;

    public Device() {
    }

    public Device(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Device(String name, String address, String token) {
        this.name = name;
        this.address = address;
        this.token = token;
    }

    public Device(Device device) {
        this.ID = device.getID();
        this.name = device.getName();
        this.address = device.getAddress();
        this.token = device.getToken();
        this.registered = device.getRegistered();
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getRegistered() {
        return registered;
    }

    public void setRegistered(Timestamp registered) {
        this.registered = registered;
    }
}
