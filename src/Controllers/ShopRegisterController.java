package Controllers;

import Data.DataMapper;
import Managers.MessageManager;
import Managers.SceneManager;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/***
 * Controllerul pentru inregistrarea in magazin.
 */
public class ShopRegisterController {
    public TextField textFieldFirstName;
    public TextField textFieldLastName;
    public PasswordField textFieldPassword;
    public TextField textFieldEmail;
    public TextField textFieldPhone;
    public TextField textFieldAddress;
    public TextField textFieldUsername;

    /***
     * Cand utilizatorul apasa pe butonul Cancel.
     */
    public void CancelRegistration() {

        SceneManager.getInstance().CancelRegistrationShop();
    }
    /***
     * Cand utilizatorul apasa pe butonul Register.
     */
    public void Register() {
        try {
            CheckCredentials();
            DataMapper.getInstance().RegisterShop(
                    textFieldFirstName.getText(),
                    textFieldLastName.getText(),
                    textFieldUsername.getText(),
                    textFieldEmail.getText(),
                    textFieldPhone.getText(),
                    textFieldAddress.getText(),
                    textFieldPassword.getText());
            MessageManager.ShowMessage("Utilizator înregistrat cu succes.", Alert.AlertType.CONFIRMATION,false);
            SceneManager.getInstance().ShopBackToLogin();
        }
        catch (Exception e){
            MessageManager.ShowMessage(e.getMessage(), Alert.AlertType.ERROR,true);
        }
    }

    /***

     * Se verifica datele introduse de client pentru a se inregistra.
     * @throws Exception
     */
    private void CheckCredentials() throws Exception{
        if(textFieldFirstName.getText().length()<=0){
            throw new Exception("Vă rugăm să introduceți prenumele..");
        }
        if(textFieldFirstName.getText().length()>64){
            throw new Exception("Prenumele nu poate avea mai mult de 64 caractere.");
        }
        if(textFieldLastName.getText().length()<=0){
            throw new Exception("Vă rugăm să introduceți numele.");
        }
        if(textFieldLastName.getText().length()>64){
            throw new Exception("Numele nu poate avea mai mult de 64 de caractere.");
        }
        if(textFieldUsername.getText().length()<=0){
            throw new Exception("Vă rugăm să introduceți numele de utilizator.");
        }
        if(textFieldUsername.getText().length()>64){
            throw new Exception("Numele de utilizator nu poate avea mai mult de 64 caractere.");
        }
        if(textFieldPassword.getText().length()<=0){
            throw new Exception("Vă rugăm să introduceți parola.");
        }
        if(textFieldPassword.getText().length()>32){
            throw new Exception("Parola nu poate avea mai mult de 32 de caractere.");
        }
        if(textFieldEmail.getText().length()<=0){
            throw new Exception("Vă rugăm sa introduceți adresa de email.");
        }
        if(textFieldEmail.getText().length()>128){
            throw new Exception("Adresa de email nu poate avea mai mult de 128 de caractere.");
        }
        if(textFieldPhone.getText().length()<=0){
            throw new Exception("Vă rugăm să introduceți numărul de telefon.");
        }
        if(textFieldPhone.getText().length()>25){
            throw new Exception("Numărul de telefon nu poate avea mai mult de 25 de caractere.");
        }
        if(textFieldAddress.getText().length()<=0){
            throw new Exception("Vă rugăm să introduceți adresa");
        }
        if(textFieldAddress.getText().length()>128){
            throw new Exception("Adresa nu poate avea mai mult de 128 de caractere.");
        }

    }
}
