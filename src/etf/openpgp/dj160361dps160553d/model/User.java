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
}
