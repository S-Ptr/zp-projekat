package etf.openpgp.dj160361dps160553d.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String name;
    private String email;
    private RSAAlgorithm rsaAlgorithm;
    private String password;
}
