package lu.davidson.market.controller;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import lu.davidson.market.json.BasicError;
import lu.davidson.market.json.LoginResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.naming.AuthenticationException;

@ControllerAdvice
@ResponseBody
@Slf4j
public class ExceptionController {

    @ExceptionHandler(value = {ExpiredJwtException.class,
            UnsupportedJwtException.class,
            AuthenticationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public LoginResult invalidLoginException(Exception e){
        log.error("invalid token {}", e.getMessage() );
        LoginResult  result = new LoginResult();
        result.setConnected(false);
        return result;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BasicError uncaughtException(Exception e){
        log.error("Bad request", e);
        return new BasicError(404, "Bad");
    }
}
