package com.example.backend.utils.security;

import com.example.backend.utils.object.DateUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.bouncycastle.jce.interfaces.ECPrivateKey;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class JwtUtils {
    private static final String SECRET_KEY = "123456789qwertyuiopasdfghjklzxcvbnm";
    private static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.ES256;
    private static final KeyPair KEY_PAIR = initKeyPair();
    private static final long EXPIRE_TIME = 1; // 过期时间，单位是天

    public static KeyPair initKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256r1");
            keyPairGenerator.initialize(ecSpec);
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
				.signWith(ALGORITHM, KEY_PAIR.getPrivate())
				// 使用.将三部分拼接起来
				.compact();
    }

    public static Claims parseJWT(String token) {
        return Jwts.parser()
                .setSigningKey(KEY_PAIR.getPublic())
                .parseClaimsJws(token)
                .getBody();
    }
}

