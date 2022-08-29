package etf.openpgp.dj160361dps160553d.model;

public class User {

    private String name;
    private String email;
    private KeyLength rsaAlgorithm;
    private String password;

    public User() {
    }

    public User(String name, String email, KeyLength rsaAlgorithm, String password) {
        this.name = name;
        this.email = email;
        this.rsaAlgorithm = rsaAlgorithm;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public KeyLength getRsaAlgorithm() {
        return rsaAlgorithm;
    }

    public void setRsaAlgorithm(KeyLength rsaAlgorithm) {
        this.rsaAlgorithm = rsaAlgorithm;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", rsaAlgorithm=" + rsaAlgorithm +
                ", password='" + password + '\'' +
                '}';
    }

    public boolean isValid() {
        return name != null && email != null && rsaAlgorithm != null && password != null
                && name.length() != 0 && email.length() != 0 && password.length() != 0;
    }
}
