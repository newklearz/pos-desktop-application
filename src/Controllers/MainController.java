package Controllers;

import Managers.SceneManager;
import javafx.beans.value.ChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

/***
 * Controllerul pentru pagina principala.
 */
public class MainController implements Initializable {

    public TabPane tabPane;
    public Tab tabShop;
    public Tab tabBank;
    public Tab tabDashboard;
    public Tab tabExit;

    /***
     * Controllerul este initializat.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tabPane.getSelectionModel().selectedItemProperty().addListener(ChangeTabEvent());
        tabPane.getSelectionModel().select(tabShop);//Select shop tab on initialization.
        tabShop.setContent(SceneManager.getInstance().getShopPane());//Set default shop pane.
        tabBank.setContent(SceneManager.getInstance().getBankPane());//Set default bank pane.
        tabDashboard.setContent(SceneManager.getInstance().getDashboardPane());//Set default dashboard pane.
        SceneManager.getInstance().setMainController(this);
    }

    /***
     * Event de schimbare a modurilor de lucru cand utilizatorul selecteaza o altra fereastra.
     * @return event listener.
     */
    private ChangeListener<Tab> ChangeTabEvent(){
        return (observableValue, oldTab, newTab) -> {
            if(newTab.equals(tabExit)) {
                Runtime.getRuntime().exit(0);
            }
            else if (newTab.equals(tabShop)){
                UpdateShopContent();
            }
            else if (newTab.equals(tabBank)){
                UpdateBankContent();
            }
            else if (newTab.equals(tabDashboard)){
                UpdateDashboardContent();
            }
        };
    }

    /***

     * Actualizeaza fereastra aferenta magazinului.
     */
    public void UpdateShopContent(){
        tabShop.setContent(SceneManager.getInstance().getShopPane());
    }

    /***
     * Actuealizeaza fereastra aferenta bancii.
     */
    public void UpdateBankContent(){
        if(SceneManager.getInstance().getBankPane().getId().equals("bankDepositPanel")){
            SceneManager.getInstance().getBankDepositController().UpdateBalance();
        }
        tabBank.setContent(SceneManager.getInstance().getBankPane());
    }

    /***
     * Actualizeaza fereastra aferenta dashboardului.
     */
    public void UpdateDashboardContent(){
        if(SceneManager.getInstance().getDashboardPane().getId().equals("dashboardPane")){
            SceneManager.getInstance().getDashboardController().RefreshList();
        }
        tabDashboard.setContent(SceneManager.getInstance().getDashboardPane());
    }

}
