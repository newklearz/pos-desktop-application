package Controllers;

import Data.DataMapper;
import Managers.MessageManager;
import Managers.SceneManager;
import Managers.SessionManager;
import Enums.UserType;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/***
 * Controllerul logarii in contul bancar.
 */
public class BankLoginController {
    public TextField textFieldUsername;
    public PasswordField textFieldPassword;
    public Button buttonLogin;
    public Button buttonRegister;

    /***
     *  Cand utilizatorul apasa pe butonul "Register", pagina de inregistrare se incarca.
     */
    public void Register(){
        SceneManager.getInstance().RegisterBank();
    }

    /***
     *  Cand utilizatorul apasa pe butonul de login, programul incearca sa acceseze contul bancar al acestuia.
     */
    public void Login( ) {
        try {
            DataMapper.getInstance().Login(textFieldUsername.getText(),textFieldPassword.getText(), UserType.BANK);
            DataMapper.getInstance().GetCardBalance(SessionManager.getInstance().getCurrentBankUser().getCardNumber());
            SceneManager.getInstance().BankLogin();
        }
        catch (Exception e){
            MessageManager.ShowMessage(e.getMessage(), Alert.AlertType.ERROR,true);
        }
    }
}
