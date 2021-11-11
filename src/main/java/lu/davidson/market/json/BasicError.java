package lu.davidson.market.json;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BasicError {
    private long code;
    private String type;

}
