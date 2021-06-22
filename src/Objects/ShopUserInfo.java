package Objects;

import Enums.LoginMethod;

public class ShopUserInfo {
    private LoginMethod loginMethod;
    private String loginInfo;
    private String firstName;
    private String lastName;
    public ShopUserInfo(String loginInfo,LoginMethod loginMethod){
        this.loginInfo=loginInfo;
        this.loginMethod=loginMethod;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LoginMethod getLoginMethod() {
        return loginMethod;
    }

    public String getLoginInfo() {
        return loginInfo;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
