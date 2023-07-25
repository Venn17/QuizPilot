package src.quiz.jwt;


import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import src.quiz.entity.UserPrincipal;

import java.util.Date;

@Component
@Slf4j
public class JwtTokenUtil {

   private static Logger logger =  LoggerFactory.getLogger(JwtTokenUtil.class);
    @Value("${bezkoder.app.jwtSecret}")
    private String jwtSecret;


    @Value("${bezkoder.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public  String generateToken(UserPrincipal userPrincipal){

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public Claims getClaimsFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }

    public String refreshToken(String token) {
        final Date createdDate = new Date();
        final Claims claims = getClaimsFromJwtToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(new Date((new Date()).getTime() + 0));

        return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");

        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

}
