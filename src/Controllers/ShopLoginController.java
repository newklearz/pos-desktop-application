package Controllers;

import Data.DataMapper;
import Managers.MessageManager;
import Managers.SceneManager;
import Enums.UserType;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/***
 * Controllerul paginii de logare in magazin.
 */
public class ShopLoginController {

    public TextField textFieldUsername;
    public PasswordField textFieldPassword;

    /***
     *Cand utilizatorul apasa butonul register, se incarca pagina de inregistrare
     */
    public void Register(){
        SceneManager.getInstance().RegisterShop();
    }

    /***
     * Cand utilizatorul apasa pe butonul login, aplicatia va incerca sa se conecteze si sa deschida magazinul.
     */
    public void Login( ) {
        try {
            DataMapper.getInstance().Login(textFieldUsername.getText(),textFieldPassword.getText(), UserType.SHOP);
            SceneManager.getInstance().ShopLogin();
        }
        catch (Exception e){
            MessageManager.ShowMessage(e.getMessage(), Alert.AlertType.ERROR,true);
        }
    }
}
