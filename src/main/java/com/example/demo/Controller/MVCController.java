package com.example.demo.Controller;

import com.example.demo.Model.Device;
import com.example.demo.Model.SensorData;
import com.example.demo.Repository.DeviceRepository;
import com.example.demo.Repository.SensorDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
//@RequestMapping("/")
public class MVCController {
    @Autowired
    SensorDataRepository sensorRepo;
    @Autowired
    DeviceRepository devRepo;

    //SENSORDATA
    @GetMapping("/allsensordata")
    public String getAllSensorData(Model model) {
        model.addAttribute("allsensordatalist", sensorRepo.findAll());
        return "allsensordata";
    }

    @GetMapping("/allsensordatachart")
    public String showAllSensorDataChart(Model model) {
        List<SensorData> allSensorDataList = sensorRepo.findAll();

        SimpleDateFormat noHoursMinutesFormat = new SimpleDateFormat("yyyy-MM-dd"); //"yyyy-MM-dd HH:mm:ss"
        String strDateNow = noHoursMinutesFormat.format(new Date());
        String strDateNowModifiedBeginning = strDateNow + " " + "00:00:00";
        String strDateNowModifiedEnd = strDateNow + " " + "23:59:00";
        Timestamp tsStartOfToday = Timestamp.valueOf(strDateNowModifiedBeginning);
        Timestamp tsEndOfToday = Timestamp.valueOf(strDateNowModifiedEnd);

        List<String> chartData = allSensorDataList.stream()
                .filter(x->x.getDataType().equalsIgnoreCase("temperature"))
                .filter(x->x.getPosted().before(tsEndOfToday))
                .filter(x->x.getPosted().after(tsStartOfToday))
                .map(SensorData::getData).collect(Collectors.toList());
        System.out.println("chartData: " + chartData);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        List<String> chartDates = allSensorDataList.stream()
                .filter(x->x.getDataType().equalsIgnoreCase("temperature"))
                .filter(x->x.getPosted().before(tsEndOfToday))
                .filter(x->x.getPosted().after(tsStartOfToday))
                .map(SensorData::getPosted)
                .map(Timestamp::getTime)
                .map(formatter::format)
                .collect(Collectors.toList());
        System.out.println("chartDates: " + chartDates);
        if(chartData.size() == chartDates.size()) {
            model.addAttribute("chartDates", chartDates);
            model.addAttribute("chartData", chartData);
        }
        else {
            System.out.println("Lists differ in size");
        }
        return "allsensordatachart";
    }

    @GetMapping("/addsensordata")
    public String addSensorData(Model model) {
        model.addAttribute("sensorData", new SensorData());
        return "addsensordata";
    }

    @PostMapping("/savesensordata")
    public String saveSensorData(@Valid @ModelAttribute SensorData sensorData, BindingResult result, Model model) {
        if (result.hasErrors())
            return "addsensordata";
        if (sensorData.getDeviceId() != null && sensorData.getSensor() != null && sensorData.getDataType() != null && sensorData.getData() != null)
            sensorRepo.save(sensorData);
        return "redirect:/allsensordata";
    }

    //DEVICES
    @GetMapping("/alldevices")
    public String getAllDevices(Model model) {
        List<Device> allDevicesList = devRepo.findAll().stream().collect(Collectors.toList());
        model.addAttribute("alldeviceslist", allDevicesList);
        return "alldevices";
    }

    @GetMapping("/alldevicespaginated")
    public String findPaginated(Model model) {
        return findPaginated(1, model);
    }

    @GetMapping("/alldevicespaginated/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {
        int pageSize = 10;

        Page<Device> page = devRepo.findAll(PageRequest.of(pageNo - 1, pageSize));
        List<Device> deviceList = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("devicesList", deviceList);
        model.addAttribute("tmpDevice", new Device()); //for modal form to edit
        return "alldevicespaginated";
    }

    @PostMapping("/editDevice")
    public String editDevice(@Valid @ModelAttribute Device device, BindingResult result, Model model) {
        Device tmpDeviceFromDB = null;
        if (device.getID() != null && device.getName() != null && device.getAddress() != null) {
            if (devRepo.existsById(device.getID()) && devRepo.findById(device.getID()).isPresent()) {
                tmpDeviceFromDB = devRepo.findById(device.getID()).get();
            }
            if (tmpDeviceFromDB != null) {
                if (tmpDeviceFromDB.getToken() != null)
                    device.setToken(tmpDeviceFromDB.getToken());
                if (tmpDeviceFromDB.getRegistered() != null)
                    device.setRegistered(tmpDeviceFromDB.getRegistered());
            }
            devRepo.save(device);
            return "redirect:/alldevicespaginated";
        }
        return null;
    }

    @PostMapping("/deleteDevice/id/{deviceID}")
    public String deleteDevice(@PathVariable(value = "deviceID") Long deviceID, Model model) {
        devRepo.deleteById(deviceID);
        return "redirect:/alldevicespaginated";
//        ModelAndView model = new ModelAndView("alldevicespaginated");
//        model.addAttribute(new Device());
//        model.setStatus(HttpStatus.CREATED);
//        return model;
    }

    @GetMapping("/addDevice")
    public String showAddDevicePage(Model model) {
        model.addAttribute("device", new Device());
        return "addDevice";
    }

    @PostMapping("/saveDevice")
    public String saveDevice(@Valid @ModelAttribute Device device, BindingResult result, Model model) {
        if (result.hasErrors())
            return "addDevice";
        if (device.getName() != null && device.getAddress() != null)
            devRepo.save(device);
        return "redirect:/alldevices";
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
