package lu.davidson.market.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.naming.AuthenticationException;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Test
    public void testRetrieveUserFromAuthorizationNotEmpty(){
        String authorisation = "something";

        AuthenticationException thrown = Assertions.assertThrows(AuthenticationException.class, () ->
            userService.retrieveUserFromAuthorization(authorisation));
        Assertions.assertEquals("not connected", thrown.getMessage());

    }

    @Test
    public void testRetrieveUserFromAuthorizationEmpty(){

        AuthenticationException thrown = Assertions.assertThrows(AuthenticationException.class, () ->
                userService.retrieveUserFromAuthorization(null));
        Assertions.assertEquals("not connected", thrown.getMessage());

    }

}
