package lu.davidson.market.controller;

import lu.davidson.market.json.LoginRequest;
import lu.davidson.market.json.LoginResult;
import lu.davidson.market.json.User;
import lu.davidson.market.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.validation.Valid;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserService userService;



    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest){
        User connectedUser = userService.authenticate(loginRequest);
        LoginResult result = new LoginResult();
        result.setUsername(connectedUser.getUsername());
        result.setConnected(true);
        result.setFirstname(connectedUser.getFirstname());
        result.setLastname(connectedUser.getLastname());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Expose-Headers", "Authorization");
        headers.add("Access-Control-Allow-Headers", "Authorization, X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, X-Custom-header");
        headers.add("Authorization",  connectedUser.getToken());
        headers.add("Token",  connectedUser.getToken());
        return new ResponseEntity<>(result, headers, HttpStatus.ACCEPTED.OK);


    }

    @GetMapping("/current")
    public User login(@RequestHeader (name="Authorization", required = false) String authorization) throws AuthenticationException {
        return userService.retrieveUserFromAuthorization(authorization);

    }

}
