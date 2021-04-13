package com.example.demo.Security;

import com.example.demo.Model.Device;

import java.net.UnknownHostException;

public class ManualTestClass {
    private static JwtTokenUtil jwtTokenUtil;
//    public static boolean isValidInet4Address(String ip)
//    {
//        String[] groups = ip.split("\\.");
//        boolean result = true;
//        for (String x: groups)
//            if (Pattern.matches("\\D", x)) {
//                return false;
//            }
//        return result;
//    }

    public static void main(String[] args) throws UnknownHostException {
        Device device = new Device("NodeMCUV2", "192.168.100.21");
        jwtTokenUtil = new JwtTokenUtil();
        String token = jwtTokenUtil.generateToken(device);
        System.out.println(token);

        System.out.println(jwtTokenUtil.validateToken(token, device));


    }
}
