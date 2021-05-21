package com.example.demo.Model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table
public class Device implements Serializable {
    @Id
    @GeneratedValue
    private Long ID;
    @NotNull
    @Size(min=2, max=30)
    @Column(name = "name", nullable = false)
    private String name;
    @NotNull
    @Size(min=13, max=15)
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

    public String getShortToken() {
        if (token != null)
            return token.substring(0,4) + "***" + token.substring(token.length()-4);
        return "";
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
