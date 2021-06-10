package com.example.demo.Controller;

import com.example.demo.Model.Device;
import com.example.demo.Model.SensorData;
import com.example.demo.Repository.DeviceRepository;
import com.example.demo.Repository.SensorDataRepository;
import com.example.demo.Security.MyToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class APIController {
    private Logger log = LoggerFactory.getLogger(APIController.class);
    @Autowired
    SensorDataRepository sensorRepo;
    @Autowired
    DeviceRepository devRepo;
    MyToken tokenUtil;

    public static boolean isValidInet4Address(String ip) {
        Pattern pattern = Pattern.compile("\\D");
        int counter = 0;
        if (ip.contains(".")) {
            String[] groups = ip.split("\\.");
            if (groups.length == 4) {
                for (String x : groups) {
                    if (x.length() >= 1 && x.length() <= 3) {
                        Matcher matcher = pattern.matcher(x);
                        if (matcher.find()) {//non-digit
                            continue;
                        } else {
                            counter++;
                        }
                    }
                }
            }
        }
        if (counter == 4)
            return true;
        return false;
    }

    @PostMapping(value = "/api/authenticate", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> doAuthenticate(@RequestHeader("Authorization") String myToken, @RequestBody Device device) {
        log.info("Request to authenticate");
        if (myToken != null) {
            if (device.getName() != null && device.getAddress() != null) {
                log.info("from device: " + device.getName() + " - " + device.getAddress());
                tokenUtil = new MyToken();
                String base64name = tokenUtil.encode64Url(device.getName());
                String base64address = tokenUtil.encode64Url(device.getAddress());
                String hmacsha256str = null;
                try {
                    hmacsha256str = tokenUtil.calcHmacSha256(base64name + base64address);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                log.info("Comparing Tokens");
                if (hmacsha256str != null && hmacsha256str.equalsIgnoreCase(myToken)) {
                    log.info("Tokens match");
                    if (!Pattern.matches("\\W", device.getName()) && isValidInet4Address(device.getAddress())) {// "\\w" is searching for [a-zA-Z_0-9], nonWord characters not included
//                        if (!devRepo.existsByName(device.getName()) && !devRepo.existsByAddress(device.getAddress())) { //no such name or address, register as new
                            Device savedDevice = devRepo.save(device);
                            String hashedId = null;
                            try {
                                hashedId = tokenUtil.calcHmacSha256(tokenUtil.encode64Url(savedDevice.getID().toString()));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            String response = device.getID() + "." + hashedId;
                            return new ResponseEntity<>(response, HttpStatus.OK);
//                        }
//                        else { //TODO THINK OF WAYS TO AUTHORIZE EXISTING DEVICS
//                            if (devRepo.existsByName(device.getName()) && devRepo.existsByAddress(device.getAddress())) {
//                                log.info("Device with name and address already registered");
//                            }
//                            if (devRepo.existsByName(device.getName())) {
//                                log.info("Name already registered");
//                            }
//                            if (devRepo.existsByAddress(device.getAddress())) {
//                                log.info("Address already registered");
//                            }
//                        }
                    }
                    else {
                        log.error("Device name or IP address are of wrong format");
                    }
                }
                else {
                    log.error("Tokens don't match");
                }
            }
            else {
                log.error("Some value is null");
            }
        }
        return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/api/sensordata", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> saveSensorData(@RequestHeader("Authorization") String myToken, @RequestBody SensorData sensorData) {
        log.info("Request to post data");
        if (myToken != null) {
            if (sensorData.getDeviceId() != null && sensorData.getSensor() != null && sensorData.getDataType() != null && sensorData.getData() != null) {
                if (devRepo.existsById(sensorData.getDeviceId())) {
                    log.info("from: " + sensorData.getDeviceId() + " " + sensorData.getSensor() + " " + sensorData.getDataType() + " " + sensorData.getData());
                    tokenUtil = new MyToken();
                    String hmacsha256str = null;
                    try {
                        hmacsha256str = tokenUtil.calcHmacSha256(tokenUtil.encode64Url(sensorData.getDeviceId().toString()) +
                                tokenUtil.encode64Url(sensorData.getSensor()) +
                                tokenUtil.encode64Url(sensorData.getDataType()) +
                                tokenUtil.encode64Url(sensorData.getData()));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    log.info("Comparing Tokens");
                    if (hmacsha256str != null && hmacsha256str.equalsIgnoreCase(myToken)) {
                        log.info("Tokens match");
                        sensorRepo.save(sensorData);
                        log.info("Data posted: " + sensorData.getDeviceId() + " " + sensorData.getSensor() + " " + sensorData.getDataType() + " " + sensorData.getData());
                        return new ResponseEntity<>("Data Posted", HttpStatus.OK);
                    } else {
                        log.error("Tokens don't match");
                    }
                }
                else {
                    log.error("Device id not registered");
                }
            }
            else {
                log.error("Some value is null");
            }
        }
        else {
            log.error("No token provided");
        }
        return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
    }
}