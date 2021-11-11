package lu.davidson.market.json;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class User {
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String token;

}
