package Controllers;

import Data.DataMapper;
import Enums.UserType;
import Utility.CardFormatter;
import Managers.MessageManager;
import Managers.SceneManager;
import Managers.SessionManager;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.ResourceBundle;


public class ShoppingCartController implements Initializable {
    private final float milkPrice=1.5f;
    private final float iceCreamPrice=2f;
    private final float eggsPrice=1f;
    public Label iceCreamPriceLabel;
    public Label milkPriceLabel;
    public Label eggsPriceLabel;
    public Spinner<Integer> icesCreamSpinner;
    public Spinner<Integer> milkSpinner;
    public Spinner<Integer> eggsSpinner;
    public Label totalCostLabel;
    public DatePicker expirationDatePicker;
    public TextField cardNumberField;
    public TextField cvvField;
    public Button iceCreamDeleteButton;
    public Button milkDeleteButton;
    public Button eggsDeleteButton;
    public ImageView iceCreamImage;
    public ImageView milkImage;
    public ImageView eggsImage;
    public ImageView paymentImage;
    public TextField clientNameField;
    private float totalCost=0f;

    /***
     * Utilizatorul apasa pe butonul login.
     */
    public void BackToLogin() {
        SceneManager.getInstance().ShopBackToLogin();
        SessionManager.getInstance().Logout(UserType.SHOP);
    }

    /***
     * Controlerul este initializat.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Initializeaza pretul si componentele.
        UpdateTotalCost();
        iceCreamPriceLabel.setText(iceCreamPrice+"RON");
        milkPriceLabel.setText(milkPrice+"RON");
        eggsPriceLabel.setText(eggsPrice+"RON");
        icesCreamSpinner.valueProperty().addListener((obs, oldValue, newValue) ->
                UpdateTotalCost());
        milkSpinner.valueProperty().addListener((obs, oldValue, newValue) ->
                UpdateTotalCost());
        eggsSpinner.valueProperty().addListener((obs, oldValue, newValue) ->
                UpdateTotalCost());
        //Numai datele valide vor aparea utilizatorului.
        expirationDatePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0 );
            }
        });

        //Se vor converti datele intr-un format valid pentru insertia in baza de date
        expirationDatePicker.setConverter(new StringConverter<>() {
            private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/yyyy");

            @Override
            public String toString(LocalDate localDate) {
                if (localDate == null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String s) {
                if(s==null || s.trim().isEmpty())
                {
                    return null;
                }
                return LocalDate.parse(s,dateTimeFormatter);
            }
        });

        cardNumberField.setTextFormatter(new CardFormatter());
        cvvField.setTextFormatter(new CardFormatter());
        cardNumberField.textProperty().addListener((observableValue, s, t1) -> {
            if(t1.length()>16){//Cifrele care formeaza numarul cardului trebuie sa numai si numai  16.
                cardNumberField.setText(s);
            }
        });
        cvvField.textProperty().addListener((observableValue, s, t1) -> {
            if(t1.length()>3){
                cvvField.setText(s);//Cifrele CVV numai si numai 3.
            }
        });

        //Configurare grafică.
        ImageView view = new ImageView(new Image(String.valueOf(getClass().getResource("../Images/remove.png"))));
        ImageView view1 = new ImageView(new Image(String.valueOf(getClass().getResource("../Images/remove.png"))));
        ImageView view2 = new ImageView(new Image(String.valueOf(getClass().getResource("../Images/remove.png"))));
        paymentImage.setImage(new Image(String.valueOf(getClass().getResource("../Images/onlinepayment.png"))));
        iceCreamImage.setImage(new Image(String.valueOf(getClass().getResource("../Images/icecream.png"))));
        milkImage.setImage(new Image(String.valueOf(getClass().getResource("../Images/milk.png"))));
        eggsImage.setImage(new Image(String.valueOf(getClass().getResource("../Images/eggs.png"))));
        iceCreamDeleteButton.setGraphic(view);
        milkDeleteButton.setGraphic(view1);
        eggsDeleteButton.setGraphic(view2);


    }

    /***

     * Calculeaza si actualizeaza costul total al produselor
     */
    private void UpdateTotalCost(){
        totalCost= icesCreamSpinner.getValue()*iceCreamPrice+milkSpinner.getValue()*milkPrice+eggsSpinner.getValue()*eggsPrice;
        totalCostLabel.setText(totalCost+"RON");
    }

    /***

     * Se incearca check-outul folosind datele pe care le-a introdus utilizatorul. Se vor retrage bani din contul utilizatorului si vor fi depozitate in contul
     * celui care detine magazinul.
     */
    public void CheckOut(){
        try{
            CheckCredentials();
            DataMapper.getInstance().MakePurchase(
                    cardNumberField.getText(),
                    Date.valueOf(expirationDatePicker.getValue().withDayOfMonth(1)),
                    cvvField.getText(),
                    totalCost);
            StringBuilder sb= new StringBuilder();
            if(icesCreamSpinner.getValue()>0){
                sb.append(" ").append(icesCreamSpinner.getValue()).append(" xInghetata");
            }
            if(milkSpinner.getValue()>0){
                sb.append(" ").append(milkSpinner.getValue()).append(" xLapte");
            }
            if(eggsSpinner.getValue()>0){
                sb.append(" ").append(eggsSpinner.getValue()).append(" xOua");
            }

            DataMapper.getInstance().SaveTransaction(icesCreamSpinner.getValue()+eggsSpinner.getValue()+milkSpinner.getValue(),
                    totalCost,
                    new Date(Calendar.getInstance().getTime().getTime()),
                    sb.toString(),
                    DataMapper.getInstance().GetBankName(cardNumberField.getText()),
                    clientNameField.getText()
                    );
            MessageManager.ShowMessage("Comanda a fost aprobata cu succes.", Alert.AlertType.CONFIRMATION,false);
            ResetFields();

        }catch (Exception e){
            MessageManager.ShowMessage(e.getMessage(), Alert.AlertType.ERROR,true);
        }

    }

    /***
     * Se verifica daca datele clientului sunt corecte.
     * @throws Exception
     */
    private void CheckCredentials() throws Exception{
        if(totalCost==0){
            throw new Exception("Nu aveti produse in cos");
        }
        if(cardNumberField.getText().length()<=0){
            throw new Exception("Va rugam sa introduceti numarul cardului.");
        }
        if(cardNumberField.getText().length()<16||cardNumberField.getText().length()>16){
            throw new Exception("Numarul cardului trebuie sa aiba 16 caractere.");
        }
        if(cvvField.getText().length()<=0){
            throw new Exception("Va rugam sa introduceti numarul CVV.");
        }
        if(cvvField.getText().length()<3||cvvField.getText().length()>3){
            throw new Exception("Numarul CVV trebuie sa aiba 3 caractere");
        }
        if(clientNameField.getText().length()<=0){
            throw new Exception("Completati campul cu numele clientului.");
        }
        if(clientNameField.getText().length()>128){
            throw new Exception("Numele clientului trebuie sa aiba mai putin de 128 caractere");
        }
        if(DataMapper.getInstance().GetCardBalance(cardNumberField.getText())< totalCost){
            throw new Exception("Fonduri insuficiente.");
        }
    }
    public void ResetMilk(){
        milkSpinner.getValueFactory().setValue(0);
    }
    public void ResetEggs(){
        eggsSpinner.getValueFactory().setValue(0);
    }
    public void ResetIceCream(){
        icesCreamSpinner.getValueFactory().setValue(0);
    }
    private void ResetFields(){
        ResetMilk();
        ResetEggs();
        ResetIceCream();
        cardNumberField.setText("");
        expirationDatePicker.valueProperty().setValue(null);
        cvvField.setText("");
    }
}
