package com.example.demo.Controller;

import com.example.demo.Constants.Colors;
import com.example.demo.DemoApplication;
import com.example.demo.Model.AppContext;
import com.example.demo.Model.Device;
import com.example.demo.Model.SensorData;
import com.example.demo.Repository.DeviceRepository;
import com.example.demo.Repository.SensorDataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping({"/", "/index", "/home"})
@SessionAttributes("appContextInSession")
public class MVCController {
    private Logger log = LoggerFactory.getLogger(DemoApplication.class);
    private final int pageSize = 10; //nr. of rows per table on page
    @Autowired
    SensorDataRepository sensorRepo;
    @Autowired
    DeviceRepository devRepo;

    //used for storing value in session to persist between pages
    @ModelAttribute("appContextInSession")
    public AppContext getAppContext() {
        return new AppContext(Colors.LIGHT);
    }

    //MAIN REQUEST-MAPPED METHODS
    @GetMapping
    public String getHome(@ModelAttribute("appContextInSession") AppContext appContext, Model model) {
        model.addAttribute("colorsList", Arrays.stream(Colors.values()).collect(Collectors.toList()));
        model.addAttribute("appContext", appContext);
        return "index";
    }

    @PostMapping("/changeColor")
    public String changeColor(@ModelAttribute("appContextInSession") AppContext appContext, Model model, RedirectAttributes attributes) {
        attributes.addFlashAttribute("appContextInSession", appContext);
        return "redirect:/index";
    }

    //SENSORDATA
    @GetMapping("/allsensordata")
    public String getAllSensorData(@ModelAttribute("appContextInSession") AppContext appContext, Model model) {
        model.addAttribute("appContext", appContext);
        model.addAttribute("allsensordatalist", sensorRepo.findAll());
        return "allsensordata";
    }

    @GetMapping("/allsensordatachart")
    public String showAllSensorDataChart(@ModelAttribute("appContextInSession") AppContext appContext, Model model) {
        model.addAttribute("appContext", appContext);
        List<SensorData> allSensorDataList = sensorRepo.findAll();

        SimpleDateFormat yyyyMmDd = new SimpleDateFormat("yyyy-MM-dd"); //"yyyy-MM-dd HH:mm:ss"
        String strDateNow = yyyyMmDd.format(new Date());
        String strDateNowModifiedBeginning = strDateNow + " " + "00:00:00";
        String strDateNowModifiedEnd = strDateNow + " " + "23:59:00";
        Timestamp tsStartOfToday = Timestamp.valueOf(strDateNowModifiedBeginning);
        Timestamp tsEndOfToday = Timestamp.valueOf(strDateNowModifiedEnd);

        List<String> chartData = allSensorDataList.stream()
                .filter(x->x.getDataType().equalsIgnoreCase("temperature"))
                .filter(x->x.getPosted().before(tsEndOfToday))
                .filter(x->x.getPosted().after(tsStartOfToday))
                .map(SensorData::getData).collect(Collectors.toList());

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        List<String> chartDates = allSensorDataList.stream()
                .filter(x->x.getDataType().equalsIgnoreCase("temperature"))
                .filter(x->x.getPosted().before(tsEndOfToday))
                .filter(x->x.getPosted().after(tsStartOfToday))
                .map(SensorData::getPosted)
                .map(Timestamp::getTime)
                .map(formatter::format)
                .collect(Collectors.toList());
        if(chartData.size() == chartDates.size()) {
            model.addAttribute("chartDates", chartDates);
            model.addAttribute("chartData", chartData);
        }
        else {
            log.error("Chart data and chart dates lists differ in size");
        }
        return "allsensordatachart";
    }

    @GetMapping("/addsensordata")
    public String addSensorData(@ModelAttribute("appContextInSession") AppContext appContext, Model model) {
        model.addAttribute("appContext", appContext);
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
    public String getAllDevices(@ModelAttribute("appContextInSession") AppContext appContext, Model model) {
        model.addAttribute("appContext", appContext);
        List<Device> allDevicesList = devRepo.findAll();
        model.addAttribute("alldeviceslist", allDevicesList);
        return "alldevices";
    }

    @GetMapping("/alldevicespaginated")
    public String findPaginated(@ModelAttribute("appContextInSession") AppContext appContext,Model model) {
        return findPaginated(appContext, 1, model);
    }

    @GetMapping("/alldevicespaginated/page/{pageNo}")
    public String findPaginated(@ModelAttribute("appContextInSession") AppContext appContext, @PathVariable(value = "pageNo") int pageNo, Model model) {
        model.addAttribute("appContext", appContext);
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
            log.error("Device is not registered in database");
            if (tmpDeviceFromDB != null) {
                if (tmpDeviceFromDB.getToken() != null)
                    device.setToken(tmpDeviceFromDB.getToken());
                if (tmpDeviceFromDB.getRegistered() != null)
                    device.setRegistered(tmpDeviceFromDB.getRegistered());
            }
            log.warn("Registering as new device");
            devRepo.save(device);
            return "redirect:/alldevicespaginated";
        }
        log.error("Some value is null");
        return "redirect:/alldevicespaginated";
    }

    @PostMapping("/deleteDevice/id/{deviceID}")
    public String deleteDevice(@PathVariable(value = "deviceID") Long deviceID, Model model) {
        devRepo.deleteById(deviceID);
        return "redirect:/alldevicespaginated";
    }

    @GetMapping("/addDevice")
    public String showAddDevicePage(@ModelAttribute("appContextInSession") AppContext appContext, Model model) {
        model.addAttribute("appContext", appContext);
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
}
