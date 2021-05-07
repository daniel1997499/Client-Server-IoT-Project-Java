package com.example.demo.Controller;

import com.example.demo.Repository.DeviceRepository;
import com.example.demo.Repository.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class MVCController {
    @Autowired
    SensorDataRepository sensorRepo;
    @Autowired
    DeviceRepository devRepo;

    @GetMapping("/") //TODO ERROR HANDLING
    public String getRoot() {
        return "redirect:/index";
    }

    @GetMapping("/index") //TODO ERROR HANDLING
    public String getIndex(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "index";
    }

    @GetMapping(value = "/allsensordata") //TODO ERROR HANDLING
    public String getAllSensorData(Model model) {
        model.addAllAttributes(sensorRepo.findAll());
        return "allsensordata";
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

    //GET ALL DEVICES
//    @GetMapping(value = "/device", produces = "application/json")
//    public ResponseEntity<Object> getAllDeviceData(@RequestHeader HttpHeaders headers, @RequestBody Device device) { //Authorization jwt header+deviceName+IPv4Addr
//        if (headers.containsKey("Authorization") && jwtTokenUtil.validateToken(headers.getFirst("Authorization"), device)) {
//                return new ResponseEntity<>(devRepo.findAll(), HttpStatus.OK);
//        }
//        return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
//    }
//
//    //POST NEW DEVICE
//    @PostMapping(value = "/device", consumes = "application/json")
//    public ResponseEntity<Object> saveDevice(@RequestBody Device device) { //deviceName+IPv4Addr
//        if (device.getName() != null && device.getAddress() != null && !devRepo.existsByName(device.getName()) && !devRepo.existsByAddress(device.getAddress())) {
//            devRepo.save(device);
//            return new ResponseEntity<>(device , HttpStatus.OK);
//        }
//        return new ResponseEntity<>("Some value is null/missing or duplicate, not allowed", HttpStatus.NOT_ACCEPTABLE);
//    }

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
