package com.example.demo.Security;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

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

    public static void main(String[] args) throws IOException {
//        Device device = new Device("NodeMCUV2", "192.168.100.21");
//        jwtTokenUtil = new JwtTokenUtil();
//        String token = jwtTokenUtil.generateToken(device);
//        System.out.println(token);
//
//        System.out.println(jwtTokenUtil.validateToken(token, device));


        String encrypted = AES256.encrypt("HelloWorld");
        File file = new File("encrypted.txt");
        String data = null;
        try {
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
                FileWriter fWriter = new FileWriter("encrypted.txt");
                fWriter.write(encrypted);
                fWriter.close();
                System.out.println("Successfully wrote to the file.");
            } else {
                System.out.println("File already exists.");
                FileWriter fWriter = new FileWriter("encrypted.txt");
                fWriter.write(encrypted);
                fWriter.close();
                System.out.println("Successfully wrote to the file.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        Scanner myReader = new Scanner(file);
        while (myReader.hasNextLine()) {
            data = myReader.nextLine();
            System.out.println(data);
        }
        myReader.close();

        System.out.println(AES256.decrypt(data));


    }
}
