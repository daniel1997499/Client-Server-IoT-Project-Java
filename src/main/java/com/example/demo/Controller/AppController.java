package com.example.demo.Controller;

import com.example.demo.Model.Device;
import com.example.demo.Model.SensorData;
import com.example.demo.Repository.DeviceRepository;
import com.example.demo.Repository.SensorDataRepository;
import com.example.demo.Security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/")
public class AppController {
    @Autowired
    SensorDataRepository sensorRepo;
    @Autowired
    DeviceRepository devRepo;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public static boolean isValidInet4Address(String ip)
    {
        String[] groups = ip.split("\\.");
        boolean result = true;
        for (String x: groups)
            if (Pattern.matches("\\D", x)) {
                return false;
            }
        return result;
    }


    //TODO TEST API AUTHENTICATION and get JWT
    @PostMapping(value = "/authenticate", consumes = "application/json") //TODO !!!WORKS!!!
    public ResponseEntity<Object> getToken(@RequestBody Device device) { //deviceName+IPv4Addr
        if (device.getName() != null && device.getAddress() != null
                && !Pattern.matches("\\W", device.getName()) && isValidInet4Address(device.getAddress())
                && devRepo.existsByName(device.getName()) && devRepo.existsByAddress(device.getAddress())) {
            String token = jwtTokenUtil.generateToken(device); //default HS512
            device.setToken(token);
            devRepo.save(device);
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
    }

    //TODO SENSORDATA STUFF
    @GetMapping(value = "/sensordata" ,produces = "application/json") //TODO !!!WORKS!!!
    public ResponseEntity<Object> getAllSensorData(@RequestHeader HttpHeaders headers, @RequestBody Device device) { //Authorization jwt header+deviceName+IPv4Addr
        if (headers.containsKey("Authorization") && jwtTokenUtil.validateToken(headers.get("Authorization").get(0), device)) {
                return new ResponseEntity<>(sensorRepo.findAll(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/sensordata", consumes = "application/json") //TODO !!!WORKS!!!
    public ResponseEntity<Object> saveSensorData(@RequestBody SensorData sensorData) { //deviceID+sensor+data
        if (sensorData.getDeviceId() != null && sensorData.getSensor() != null && sensorData.getData() != null && devRepo.existsById(sensorData.getDeviceId())) {
            sensorRepo.save(sensorData);
            return new ResponseEntity<>(sensorData , HttpStatus.OK);
        }
        return new ResponseEntity<>("Some value is null or missing, not allowed", HttpStatus.NOT_ACCEPTABLE);
    }

//    @GetMapping(value = "sensordata/id/{id}", produces = "application/json")
//    public ResponseEntity<Object> getSensorDataById(@PathVariable(value = "id") long sensorDataId) throws Exception {
//        Optional<SensorData> data = sensorRepo.findById(sensorDataId);
//        return new ResponseEntity<>(data, HttpStatus.OK);
//    }
//
//    @GetMapping(value = "sensordata/sensor/{sensor}", produces = "application/json")
//    public ResponseEntity<Object> getSensorDataBySensor(@PathVariable(value = "sensor") String sensor) throws Exception {
//        List<SensorData> data = sensorRepo.findSensorDataBySensor(sensor);
//        return new ResponseEntity<>(data, HttpStatus.OK);
//    }

    //TODO DEVICE STUFF
    @GetMapping(value = "/device", produces = "application/json") //TODO !!!WORKS!!!
    public ResponseEntity<Object> getAllDeviceData(@RequestHeader HttpHeaders headers, @RequestBody Device device) { //Authorization jwt header+deviceName+IPv4Addr
        if (headers.containsKey("Authorization") && jwtTokenUtil.validateToken(headers.get("Authorization").get(0), device)) {
                return new ResponseEntity<>(devRepo.findAll(), HttpStatus.OK);
        }
        return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/device", consumes = "application/json") //TODO !!!WORKS!!!
    public ResponseEntity<Object> saveDevice(@RequestBody Device device) { //deviceName+IPv4Addr
        if (device.getName() != null && device.getAddress() != null && !devRepo.existsByName(device.getName()) && !devRepo.existsByAddress(device.getAddress())) {
            devRepo.save(device);
            return new ResponseEntity<>(device , HttpStatus.OK);
        }
        return new ResponseEntity<>("Some value is null/missing or duplicate, not allowed", HttpStatus.NOT_ACCEPTABLE);
    }

//    @GetMapping(value = "/user/{id}", produces = "application/json")
//    public ResponseEntity<User> getUserById(@PathVariable(value = "id") Long userId) throws Exception {
//        Optional<User> user = Optional.ofNullable(userRepo.findUserById(userId));
//        return user.map(value -> ResponseEntity.ok().body(value)).orElseGet(() -> ResponseEntity.noContent().build());
//    }
//
//    @GetMapping(value = "/user/name/{name}", produces = "application/json")
//    public ResponseEntity<List<User>> getUserByName(@PathVariable(value = "name") String username) throws Exception {
//        List<User> users = userRepo.findUserByUsername(username);
//        return users.isEmpty() ? ResponseEntity.ok().body(users) : ResponseEntity.noContent().build();
//    }
//
//    @PostMapping(value = "/users", consumes = "application/json")
//    public User createUser(@RequestBody User user) {
//        return userRepo.save(user);
//    }

//    @PutMapping("/users/{id}")
//    public ResponseEntity <User> updateUser(@PathVariable(value = "id") Long userId, @RequestBody User userData) throws ResourceNotFoundException {
//        User user = userRepo.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
//
//        user.setFirstName(userData.getFirstName());
//        final User updatedUser = userRepo.save(user);
//        return ResponseEntity.ok(updatedUser);
//    }
//
//    @DeleteMapping("/users/{id}")
//    public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long userId) throws ResourceNotFoundException {
//        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
//        userRepo.delete(user);
//        Map <String, Boolean> response = new HashMap< >();
//        response.put("deleted", Boolean.TRUE);
//        return response;
//    }
}