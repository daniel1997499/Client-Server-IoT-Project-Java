package com.example.demo.Controller;

import com.example.demo.Model.SensorData;
import com.example.demo.Repository.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/")
public class AppController {

    @Autowired
    SensorDataRepository sensorRepo;

    @GetMapping(value = "/test", produces = "application/json")
    public String getTest() {
        return "HelloTest!";
    }

    @GetMapping(value = "/sensordata" ,produces = "application/json")
    public ResponseEntity<Object> getAllSensorData() {
        return new ResponseEntity<>(sensorRepo.findAll(), HttpStatus.OK);
    }

    @PostMapping(value = "/sensordata", consumes = "application/json")
    public void saveSensorData(@RequestBody SensorData sensorData) {
//        if (sensorRepo.existsById(sensorData.getID()))
//            System.out.println("object exists: " + sensorData.getID());
//        System.out.println("sensorData: " + sensorData);
//        if (sensorData.getPosted() == null)
//            sensorData.setPosted(new Timestamp(System.currentTimeMillis()));
//        System.out.println("sensorData modified: " + sensorData);
        sensorRepo.save(sensorData);
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