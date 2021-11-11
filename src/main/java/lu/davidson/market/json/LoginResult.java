package lu.davidson.market.json;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResult {

    private String username;
    private String firstname;
    private String lastname;
    private boolean connected;
}
