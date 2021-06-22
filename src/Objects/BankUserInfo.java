package Objects;

import Enums.LoginMethod;

public class BankUserInfo {
    private LoginMethod loginMethod;
    private String loginInfo;
    private String firstName;
    private String lastName;
    private String cardNumber;
    private String email;
    public BankUserInfo(String loginInfo,LoginMethod loginMethod){
        this.loginInfo=loginInfo;
        this.loginMethod=loginMethod;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
