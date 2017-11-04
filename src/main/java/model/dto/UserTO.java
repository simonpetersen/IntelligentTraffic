package model.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserTO {

    private String username, password, apiKey, name;
    private boolean admin;

    public UserTO(String username, String password, String apiKey, String name, boolean admin) {
        this.username = username;
        this.password = password;
        this.apiKey = apiKey;
        this.name = name;
        this.admin = admin;
    }

    public UserTO() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
