package com.evolutivelabs.app.counter.gateway.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.apache.commons.collections4.keyvalue.DefaultMapEntry;

import java.util.AbstractMap;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtUtils {
    private static String secret = "secret";
    public static String generateToken(String id, String name, String[] roles) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        return JWT.create()
                .withSubject(id)
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withIssuer("EVO")
                .withClaim("roles", String.join(",", roles))
                .withClaim("name", name)
                .sign(algorithm);
    }

    public static Map<String, String> verify(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secret.getBytes())).build().verify(token);
        Map<String, Claim> claimMap = decodedJWT.getClaims();

        Map<String, String> userInfo = new HashMap<>();
        for (Map.Entry<String, Claim> entry : claimMap.entrySet()) {
            userInfo.put(String.valueOf(entry.getKey()), entry.getValue().asString());
        }
        userInfo.put("id", decodedJWT.getSubject());

        return userInfo;
    }

}
