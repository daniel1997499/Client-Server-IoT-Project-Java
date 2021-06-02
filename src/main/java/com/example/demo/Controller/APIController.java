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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
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
        if (device.getName() != null && device.getAddress() != null) {
            if (!Pattern.matches("\\W", device.getName()) && isValidInet4Address(device.getAddress())) {// "\\w" is searching for [a-zA-Z_0-9], nonWord characters not included
                if (devRepo.existsByName(device.getName()) && devRepo.existsByAddress(device.getAddress())) {
                    List<Device> tmpDevListByName = devRepo.findDeviceByName(device.getName());
                    List<Device> tmpDevListByAddress = devRepo.findDeviceByAddress(device.getAddress());
                    if (tmpDevListByName.stream().findFirst().get().getID().equals(tmpDevListByAddress.stream().findFirst().get().getID()))
                        device.setID(tmpDevListByName.stream().findFirst().get().getID());
                    device.setRegistered(tmpDevListByName.stream().findFirst().get().getRegistered());
                    Map<String, Object> claims = new HashMap<>();
                    claims.put(jwtClaimDeviceId, device.getID());
                    claims.put(jwtClaimDeviceName, device.getName());
                    claims.put(jwtClaimDeviceAddress, device.getAddress());
                    String token = jwtTokenUtil.generateToken(claims, device);
                    device.setToken(token);
                    devRepo.save(device); //we pass id so device is updated rather than saved as new
                    return new ResponseEntity<>(token, HttpStatus.OK);
                }
                else { //is not present in DB
                    log.warn("Registering as new device");
                    devRepo.save(device);
                    //TODO NEED A JOINT SELECT; Search in DB with joint selection from name and address
                    List<Device> tmpDevListByName = devRepo.findDeviceByName(device.getName());
                    List<Device> tmpDevListByAddress = devRepo.findDeviceByAddress(device.getAddress());
                    if (tmpDevListByName.stream().findFirst().get().getID().equals(tmpDevListByAddress.stream().findFirst().get().getID()))
                        device.setID(tmpDevListByName.stream().findFirst().get().getID());
                    device.setRegistered(tmpDevListByName.stream().findFirst().get().getRegistered());

                    Map<String, Object> claims = new HashMap<>();
                    claims.put(jwtClaimDeviceId, device.getID());
                    claims.put(jwtClaimDeviceName, device.getName()); //TODO Give random name for security
                    claims.put(jwtClaimDeviceAddress, device.getAddress()); //not needed
                    String token = jwtTokenUtil.generateToken(claims, device);
                    device.setToken(token);
                    devRepo.save(device);
                    return new ResponseEntity<>(token , HttpStatus.OK);
                }
            }
            log.error("Device name or IP address are of wrong format");
        }
        log.error("Some value is null");
        return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/api/sensordatajwt", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> saveSensorData(@RequestHeader("Authorization") String jwt , @Valid @RequestBody SensorData sensorData) {
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