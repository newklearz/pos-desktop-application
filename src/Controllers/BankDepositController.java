package Controllers;

import Data.DataMapper;
import Enums.UserType;
import Managers.MessageManager;
import Managers.SceneManager;
import Managers.SessionManager;
import Objects.BankReceipt;
import Utility.*;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

/***
 * Controllerul depozitului bancar.
 */
public class BankDepositController implements Initializable {
    public TextField balance;
    public TextField depositAmount;
    public ImageView bankImageView;
    public Button depositButton;
    public ListView<BankReceipt> historyListview;
    public Tab historyTab;
    public TabPane tabPane;
    public TextField firstNameField;
    public TextField lastNameField;
    public TextField cardNumberField;
    public Tab infoTab;
    public TextField emailField;

    /***
     * Cand este intializat controllerul
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        depositAmount.setTextFormatter(new DepositFormatter());
        depositAmount.textProperty().addListener((observableValue, s, t1) -> {
            if(t1.length()>10){//Suma depozitata trebuie sa fie cel putin 10.
                depositAmount.setText(s);
            }
        });
        firstNameField.setText(SessionManager.getInstance().getCurrentBankUser().getFirstName());
        lastNameField.setText(SessionManager.getInstance().getCurrentBankUser().getLastName());
        cardNumberField.setText(SessionManager.getInstance().getCurrentBankUser().getCardNumber());
        emailField.setText(SessionManager.getInstance().getCurrentBankUser().getEmail());
        UpdateBalance();
        //Set graphics.
        bankImageView.setImage(new Image(String.valueOf(getClass().getResource("../Images/bank.png"))));
        ImageView view = new ImageView(new Image(String.valueOf(getClass().getResource("../Images/confirm.png"))));
        depositButton.setGraphic(view);
        SceneManager.getInstance().setBankDepositController(this);
        historyListview.setCellFactory(ObjectListView->new BankReceiptObjectListViewCell());
        tabPane.getSelectionModel().selectedItemProperty().addListener(ChangeTabEvent());
    }

    /***

     * Actualizeaza soldul utilizatorului curent.
     */
    public void UpdateBalance() {
        try {
        balance.setText(String.valueOf(DataMapper.getInstance().GetCardBalance(SessionManager.getInstance().getCurrentBankUser().getCardNumber())));
        }
        catch (Exception e){
            MessageManager.ShowMessage(e.getMessage(), Alert.AlertType.ERROR,true);
        }
    }

    /***

     * Un event listener care este chemat atunci cand utilizatorul selecteaza alta pagina.

     * @return event listener.
     */
    private ChangeListener<Tab> ChangeTabEvent(){
        return (observableValue, oldTab, newTab) -> {
            if(newTab.equals(historyTab)) {
                RefreshList();
            }
        };
    }
    /**
     * Delogheaza din contul bancar si merge la pagina de logare
     */
    public void Logout(){
        SessionManager.getInstance().Logout(UserType.BANK);
        SceneManager.getInstance().CancelRegistrationBank();
    }

    /***

     * Cand e apasat butonul deposit si numai daca utilizatorul a adaugat suma de depozitat,
     * aplicatia va adauga fondurile, in baza de date, atribuita utilizatorului curent
     */
    public void Deposit() {
        try {
            if(depositAmount.getText().equals("")||Float.parseFloat(depositAmount.getText())==0f) {
                MessageManager.ShowMessage("Va rugam introduceti suma de depozitat.", Alert.AlertType.ERROR,true);
            }
            else {
                DataMapper.getInstance().DepositToBankAccount(Float.parseFloat(depositAmount.getText()),SessionManager.getInstance().getCurrentBankUser().getEmail());
                UpdateBalance();
            }
        }
        catch (Exception e){
            MessageManager.ShowMessage("A aparut o problema cu depozitul.", Alert.AlertType.ERROR,true);
        }
    }
    public void RefreshList() {
        try {
            ObservableList<BankReceipt> bankReceipts = DataMapper.getInstance().GetAllReceipts(SessionManager.getInstance().getCurrentBankUser().getCardNumber());
            historyListview.setItems(bankReceipts);
        } catch (Exception e) {
            MessageManager.ShowMessage(e.getMessage(), Alert.AlertType.ERROR, true);
        }
    }
}
