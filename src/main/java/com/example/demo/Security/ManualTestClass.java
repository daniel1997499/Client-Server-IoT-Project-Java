package com.example.demo.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.Base64Codec;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

public class ManualTestClass {
    @Value("${jwt.secret}")
    private static String secret = "my-secret-jwt-secret-secret";
    private static final File file = new File("D:\\Workspace\\demo\\src\\main\\java\\com\\example\\demo\\Security\\token.txt");
    private static String tokenFromFile = null;
//    private static JwtTokenUtil jwtTokenUtil;
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


//        String encrypted = AES256.encrypt("HelloWorld");
//        File file = new File("encrypted.txt");
//        String data = null;
//        try {
//            if (file.createNewFile()) {
//                System.out.println("File created: " + file.getName());
//                FileWriter fWriter = new FileWriter("encrypted.txt");
//                fWriter.write(encrypted);
//                fWriter.close();
//                System.out.println("Successfully wrote to the file.");
//            } else {
//                System.out.println("File already exists.");
//                FileWriter fWriter = new FileWriter("encrypted.txt");
//                fWriter.write(encrypted);
//                fWriter.close();
//                System.out.println("Successfully wrote to the file.");
//            }
//        } catch (IOException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
//
//        Scanner myReader = new Scanner(file);
//        while (myReader.hasNextLine()) {
//            data = myReader.nextLine();
//            System.out.println(data);
//        }
//        myReader.close();
//
//        System.out.println(AES256.decrypt(data));

        Long deviceId = 1L;
        Date expiration = new Date(System.currentTimeMillis() + 5 * 60 * 60 * 1000);
        Base64Codec coder = new Base64Codec();
        Base64Codec decoder = new Base64Codec();
        System.out.println("not encoded: " + secret);
        secret = coder.encode(secret);
        System.out.println("encoded: " + secret);
        secret = decoder.decodeToString(secret);
        System.out.println("decoded: " + secret);
        String token = Jwts.builder()
                .claim("deviceId", deviceId)
                .setIssuer("Daniel")
                .setSubject("Dan")
                .setAudience("audience")
                .setExpiration(expiration)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        System.out.println("generatedToken: " + token);

        try {
            FileWriter fw = new FileWriter(file);
            fw.write(token);
            fw.flush();
            fw.close();
            System.out.println("wrote to file");
        } catch (IOException e) {e.printStackTrace();}

        try {
            Scanner sc = new Scanner(file);
            while(sc.hasNextLine()) {
                tokenFromFile = sc.nextLine();
            }
            System.out.println("tokenFromFile: " + tokenFromFile);
        } catch (IOException e) {e.printStackTrace();}

        Jws<Claims> jws;

        try {
            jws = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(tokenFromFile);
            System.out.println("jws: " + jws);
            System.out.println(jws.getBody().get("deviceId"));
            System.out.println(jws.getSignature());
        } catch (JwtException ex) {ex.printStackTrace();}
    }
}
