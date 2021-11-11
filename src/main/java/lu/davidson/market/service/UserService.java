package lu.davidson.market.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultHeader;
import lu.davidson.market.json.LoginRequest;
import lu.davidson.market.json.User;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;
import javax.naming.AuthenticationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;


    @Autowired
    public UserService() {

    }


    public User authenticate(LoginRequest loginRequest) {
        User user = new User();
        user.setId(1L);
        user.setFirstname("temp first name");
        user.setLastname("temp last name");
        user.setEmail("temp@temp.tmp");
        user.setUsername(loginRequest.getUsername());
        Map<String, Object> claims = new HashMap<>();
        claims.put(Claims.SUBJECT, user.getUsername());
        claims.put("given_name", user.getFirstname());
        claims.put("family_name", user.getLastname());
        user.setToken(generateJwtToken(claims));
        return user;
    }


    public User retrieveUserFromAuthorization(String authorization) throws AuthenticationException {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new AuthenticationException("not connected");
        }
        String token = authorization.substring(7);
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        User user = new User();
        user.setEmail(claims.get("email", String.class));
        user.setFirstname(claims.get("given_name", String.class));
        user.setLastname(claims.get("family_name", String.class));
        return  user;
    }

    public String generateJwtToken(Map<String, Object> claims) {
        Date current = new Date();
        return Jwts.builder().setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate(current)).signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    private Date generateExpirationDate(Date date) {
        return new Date(date.getTime() + expiration * 1000);
    }
}
