package com.example.demo.Controller;

import com.example.demo.DemoApplication;
import com.example.demo.Model.Device;
import com.example.demo.Model.SensorData;
import com.example.demo.Repository.DeviceRepository;
import com.example.demo.Repository.SensorDataRepository;
import com.example.demo.Security.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
public class APIController {
    private Logger log = LoggerFactory.getLogger(DemoApplication.class);
    private static final String jwtClaimDeviceId = "deviceId";
    private static final String jwtClaimDeviceName = "name";
    private static final String jwtClaimDeviceAddress = "address";
    @Autowired
    SensorDataRepository sensorRepo;
    @Autowired
    DeviceRepository devRepo;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public static boolean isValidInet4Address(String ip) {
        String[] groups = ip.split("\\.");
        boolean result = true;
        for (String x: groups)
            if (Pattern.matches("\\D", x)) {
                return false;
            }
        return result;
    }

    @PostMapping(value = "/api/authenticate", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> doAuthenticate(@RequestBody Device device) { //deviceName+IPv4Addr
        log.info("Request to authenticate");
        if (device.getName() != null && device.getAddress() != null) {
            log.info("from device: " + device.getName() + " - " + device.getAddress());
            if (!Pattern.matches("\\W", device.getName()) && isValidInet4Address(device.getAddress())) {// "\\w" is searching for [a-zA-Z_0-9], nonWord characters not included
                if (!devRepo.existsByName(device.getName()) && !devRepo.existsByAddress(device.getAddress())) { //no such name or address, register as new
                    Device savedDevice = devRepo.save(device);
                    Map<String, Object> claims = new HashMap<>();
                    claims.put(jwtClaimDeviceId, savedDevice.getID());
                    claims.put(jwtClaimDeviceName, savedDevice.getName());
                    claims.put(jwtClaimDeviceAddress, savedDevice.getAddress());
                    String token = jwtTokenUtil.generateToken(claims, savedDevice);
                    savedDevice.setToken(token);
                    devRepo.save(savedDevice);
                    return new ResponseEntity<>(token, HttpStatus.OK);
                }
                else {
                    log.warn("Device with this name or address already registered");
//                    if (devRepo.existsByName(device.getName()) && devRepo.existsByAddress(device.getAddress())) {
//                        //TODO NEED JOINT SELECT
//                    }
                    if (devRepo.existsByName(device.getName())) {
                        log.warn("Name already registered, overwriting");
                        Device existingDevice = devRepo.findDeviceByName(device.getName()).stream().findFirst().get();
                        existingDevice.setAddress(device.getAddress());
                        Map<String, Object> claims = new HashMap<>();
                        claims.put(jwtClaimDeviceId, existingDevice.getID());
                        claims.put(jwtClaimDeviceName, existingDevice.getName());
                        claims.put(jwtClaimDeviceAddress, existingDevice.getAddress());
                        String token = jwtTokenUtil.generateToken(claims, existingDevice);
                        existingDevice.setToken(token);
                        devRepo.save(existingDevice);
                        return new ResponseEntity<>(token, HttpStatus.OK);
                    }
                    if (devRepo.existsByAddress(device.getAddress())) {
                        log.warn("Address already registered, registering as new device anyway");
                        Device savedDevice = devRepo.save(device);
                        Map<String, Object> claims = new HashMap<>();
                        claims.put(jwtClaimDeviceId, savedDevice.getID());
                        claims.put(jwtClaimDeviceName, savedDevice.getName());
                        claims.put(jwtClaimDeviceAddress, savedDevice.getAddress());
                        String token = jwtTokenUtil.generateToken(claims, savedDevice);
                        savedDevice.setToken(token);
                        devRepo.save(savedDevice);
                        return new ResponseEntity<>(token, HttpStatus.OK);
                    }
                }
            }
            else
                log.error("Device name or IP address are of wrong format");
        }
        else
            log.error("Some value is null");
        return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/api/sensordatajwt", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> saveSensorData(@RequestHeader("Authorization") String jwt , @RequestBody SensorData sensorData) {
        if (jwt != null) {
            if (sensorData.getDeviceId() != null && sensorData.getSensor() != null && sensorData.getDataType() != null && sensorData.getData() != null
                    && devRepo.existsById(sensorData.getDeviceId())) {
                Device tmpDev = devRepo.findById(sensorData.getDeviceId()).get();
                if (jwtTokenUtil.validateToken(jwt, tmpDev))
                    sensorRepo.save(sensorData);
                return new ResponseEntity<>("Data Posted" , HttpStatus.OK);
            }
            log.error("Some value is null");
        }
        log.error("No jwt token provided");
        return new ResponseEntity<>("Some value is null or missing or could not be validated, bad request", HttpStatus.BAD_REQUEST);
    }
}