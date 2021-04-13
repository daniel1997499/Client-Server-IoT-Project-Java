package com.example.demo.Repository;

import com.example.demo.Model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findDeviceByName(String name);
    List<Device> findDeviceByAddress(String address);
    List<Device> findDeviceByToken(String token);
    boolean existsByName(String name);
    boolean existsByAddress(String address);
}
