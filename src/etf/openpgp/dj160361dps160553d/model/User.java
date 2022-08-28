package etf.openpgp.dj160361dps160553d.model;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String name;
    private String email;
    private KeyLength rsaAlgorithm;
    private String password;

    public boolean isValid() {
        return name != null && email != null && rsaAlgorithm != null && password != null
                && name.length() != 0 && email.length() != 0 && password.length() != 0;
    }
}
