package com.example.backend.utils.security;

import com.example.backend.utils.object.DateUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class JwtUtils {
    private static final String SECRET_KEY = "123456789qwertyuiopasdfghjklzxcvbnm";
    private static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.ES256;
    private static final long EXPIRE_TIME = 1; // 过期时间，单位是天

    private static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(SECRET_KEY);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    public static String createJWT(String subject, Map<String, Object> payload) {
        return Jwts.builder()
				// 设置jwt的头部信息，包括类型和算法名
				.setHeaderParam("typ", "jwt")
				.setHeaderParam("alg", ALGORITHM.getValue())
				// 负载
				.setId(UUID.randomUUID().toString())
				.setSubject(subject)
		        .addClaims(payload)
				.setExpiration(DateUtils.getNextTime(EXPIRE_TIME, TimeUnit.DAYS))
				// 签名
				.signWith(ALGORITHM, generalKey())
				// 使用.将三部分拼接起来
				.compact();
    }

    public static Claims parseJWT(String token) {
        return Jwts.parser()
                .setSigningKey(generalKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
