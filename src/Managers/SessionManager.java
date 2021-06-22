package Managers;

import Enums.LoginMethod;
import Enums.UserType;
import Objects.BankUserInfo;
import Objects.ShopUserInfo;

public class SessionManager {
    /***
     * Modelul Singleton .
     */
    public static SessionManager instance=null;
    public static SessionManager getInstance() {
        if(instance==null){
            return new SessionManager();
        }
        return instance;
    }
    SessionManager(){
        instance=this;
    }
    BankUserInfo currentBankUser;
    ShopUserInfo currentShopUser;

    /***
     * Setează utilizatorul conectat curent.
     * @param loginInfo Username sau Email.
     * @param loginMethod Username sau Email folosit pentru login.
     * @param userType Utilizator al POS-ului sau al băncii.
     */
    public void setCurrentUser(String loginInfo, LoginMethod loginMethod, UserType userType) {
        if(userType.equals(UserType.BANK)) {
            this.currentBankUser = new BankUserInfo(loginInfo, loginMethod);
        }
        else{
            this.currentShopUser = new ShopUserInfo(loginInfo,loginMethod);
        }
    }

    public BankUserInfo getCurrentBankUser() {
            return currentBankUser;
    }
    public ShopUserInfo getCurrentShopUser() {
        return currentShopUser;
    }
    public void Logout(UserType userType){
        if(userType.equals(UserType.BANK)) {
            this.currentBankUser = null;
        }
        else{
            this.currentShopUser=null;
        }
    }


}
