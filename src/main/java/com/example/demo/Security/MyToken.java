package com.example.demo.Security;

import io.jsonwebtoken.impl.Base64UrlCodec;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class MyToken {
//    private Logger log = LoggerFactory.getLogger(MyToken.class);
    private String secret = "sgfd23#@%%!f3rgt43afahrthjsdajfgj0";
    private Base64UrlCodec codec2;

    public MyToken() {
//        log.info("MyToken instance created");
    }

    public String encode64Url(String strToEncode) {
//        log.info("Encoding string");
        if (strToEncode != null) {
            codec2 = new Base64UrlCodec();
//            log.info(codec2.encode(strToEncode));
            return codec2.encode(strToEncode);
        }
        return null;
    }

    public String decode64Url(String strToDecode) {
//        log.info("Decoding string");
        if (strToDecode != null) {
            codec2 = new Base64UrlCodec();
            return String.format("%032x", new BigInteger(1, codec2.decode(strToDecode)));
        }
        return null;
    }

    public String calcHmacSha256(String strToHash) throws UnsupportedEncodingException {
//        log.info("Hashing string");
        if (strToHash != null) {
            byte[] secretKey = secret.getBytes("UTF-8");
            byte[] message = strToHash.getBytes("UTF-8");
            byte[] hmacSha256 = null;
            try {
                Mac mac = Mac.getInstance("HmacSHA256");
                SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "HmacSHA256");
                mac.init(secretKeySpec);
                hmacSha256 = mac.doFinal(message);
            } catch (Exception e) {
                throw new RuntimeException("Failed to calculate hmac-sha256", e);
            }
//            log.info(String.format("%032x", new BigInteger(1, hmacSha256)));
            return String.format("%032x", new BigInteger(1, hmacSha256));
        }
        return null;
    }
}
