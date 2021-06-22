package Managers;

import Controllers.BankDepositController;
import Controllers.DashboardController;
import Controllers.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

/***
 * Class pentru a initializa diferite scene in GUI.
 */
public class SceneManager {
    /***
     * Modelul Singleton
     */
    public static SceneManager instance=null;
    public static SceneManager getInstance() {
        if(instance==null){
            return new SceneManager();
        }
        return instance;
    }
    MainController mainController;
    BankDepositController bankDepositController;
    DashboardController dashboardController;
    Pane shopPane;
    Pane bankPane;
    Pane dashboardPane;
    SceneManager(){
        instance=this;
        try {
            shopPane = FXMLLoader.load(getClass().getResource("../FX/ShopLogin.fxml"));
            bankPane = FXMLLoader.load(getClass().getResource("../FX/BankLogin.fxml"));
            dashboardPane = FXMLLoader.load(getClass().getResource("../FX/Dashboard.fxml"));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /***
     * Arata pagina de inregistrare în POS.
     */
    public void RegisterShop(){
        try {
            shopPane = FXMLLoader.load(getClass().getResource("../FX/ShopRegister.fxml"));
            mainController.UpdateShopContent();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    /***
     *Arata pagina de inregistrare pentru Banca.
     */
    public void RegisterBank(){
        try {
            bankPane = FXMLLoader.load(getClass().getResource("../FX/BankRegister.fxml"));
            mainController.UpdateBankContent();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    /***
     * Arata scena de logare în POS.
     */
    public void CancelRegistrationShop(){
        try {
            shopPane = FXMLLoader.load(getClass().getResource("../FX/ShopLogin.fxml"));
            mainController.UpdateShopContent();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    /***
     * Arata scena de conectare la contul bancar.
     */
    public void CancelRegistrationBank(){
        try {
            bankPane = FXMLLoader.load(getClass().getResource("../FX/BankLogin.fxml"));
            mainController.UpdateBankContent();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    /***
     * Arata scena de conectare la POS.
     */
    public void ShopBackToLogin(){
        try {
            shopPane = FXMLLoader.load(getClass().getResource("../FX/ShopLogin.fxml"));
            mainController.UpdateShopContent();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    /***
     * Afișează scena coșului de cumpărături.
     */
    public void ShopLogin(){
        try {
            shopPane = FXMLLoader.load(getClass().getResource("../FX/ShoppingCart.fxml"));
            mainController.UpdateShopContent();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    /***
     * Arată scena principală a băncii.
     */
    public void BankLogin(){
        try {
            bankPane = FXMLLoader.load(getClass().getResource("../FX/BankDeposit.fxml"));
            mainController.UpdateBankContent();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }




    public Pane getShopPane() {
        return shopPane;
    }

    public Pane getBankPane() {
        return bankPane;
    }

    public Pane getDashboardPane() {
        return dashboardPane;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setBankDepositController(BankDepositController bankDepositController) {
        this.bankDepositController = bankDepositController;
    }

    public BankDepositController getBankDepositController() {
        return bankDepositController;
    }

    public DashboardController getDashboardController() {
        return dashboardController;
    }

    public void setDashboardController(DashboardController dashboardController) {
        this.dashboardController = dashboardController;
    }
}
