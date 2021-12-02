package com.example.demo.Repository;

import com.example.demo.Model.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
    List<SensorData> findSensorDataBySensor(String sensor);
    List<SensorData> findSensorDataByDeviceId(Long device_id);
    List<SensorData> findSensorDataByData(String data);
    List<SensorData> findSensorDataByDataType(String data);
}
