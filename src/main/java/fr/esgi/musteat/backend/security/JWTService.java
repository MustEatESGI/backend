package fr.esgi.musteat.backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JWTService {

    private JWTService() {
    }

    public static String extractSubjectFromBearerToken(String token) {
        DecodedJWT jwt = JWT.decode(token.substring("Bearer ".length()));
        return jwt.getSubject();
    }
}
