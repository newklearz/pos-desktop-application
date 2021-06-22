package Controllers;

import Data.DataMapper;
import Utility.CardFormatter;
import Managers.MessageManager;
import Managers.SceneManager;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/***
 * Controller de înregistrare pentru un cont.
 */
public class BankRegisterController implements Initializable {
    public TextField textFieldFirstName;
    public TextField textFieldLastName;
    public PasswordField textFieldPassword;
    public TextField textFieldEmail;
    public TextField textFieldCardNumber;
    public Button buttonRegister;
    public Button buttonCancel;
    public TextField textFieldUsername;
    public TextField textFieldCVV;
    public DatePicker datePickerCardExpirationPicker;
    public TextField texFieldBankName;

    /***
     * Se renunta la inregistrare si se revine la pagina initiala.
     */
    public void CancelRegistration() {
        SceneManager.getInstance().CancelRegistrationBank();
    }

    /***
     *  Cand un utilizator apasa pe butonul de inregistrare.
     */
    public void Register(){
        try{
            CheckCredentials();
            DataMapper.getInstance().RegisterBank(
                    textFieldFirstName.getText(),
                    textFieldLastName.getText(),
                    textFieldUsername.getText(),
                    textFieldEmail.getText(),
                    textFieldCardNumber.getText(),
                    Date.valueOf(datePickerCardExpirationPicker.getValue().withDayOfMonth(1)),
                    textFieldCVV.getText(),
                    textFieldPassword.getText(),
                    texFieldBankName.getText()
            );
            MessageManager.ShowMessage("Utilizator înregistrat cu succes.", Alert.AlertType.CONFIRMATION,false);
            SceneManager.getInstance().CancelRegistrationBank();
        }catch (Exception e){
            MessageManager.ShowMessage(e.getMessage(), Alert.AlertType.ERROR,true);
        }
    }

    /***

     * Se verifică toate câmpurile astfel încat să existe valori corect introduse, în sens contrar se afișează un mesaj de eroare utilizatorului
     * @throws Exception
     */
    private void CheckCredentials() throws Exception{
        if(textFieldFirstName.getText().length()<=0){
            throw new Exception("Vă rugăm să introduceți prenumele.");
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
        if(textFieldCardNumber.getText().length()<=0){
            throw new Exception("Va rugam sa introduceti numarul cardului.");
        }
        if(textFieldCardNumber.getText().length()<16||textFieldCardNumber.getText().length()>16){
            throw new Exception("Numarul cardului trebuie sa aiba 16 caractere.");
        }
        if(textFieldCVV.getText().length()<=0){
            throw new Exception("Va rugam sa introduceti CVV.");
        }
        if(textFieldCVV.getText().length()<3||textFieldCVV.getText().length()>3){
            throw new Exception("CVV-ul trebuie sa aiba 3 caractere");
        }
        if(texFieldBankName.getText().length()<=0){
            throw new Exception("Va rugam sa completati cu numele bancii.");
        }
        if(texFieldBankName.getText().length()>64){
            throw new Exception("Numele bancii nu poate avea mai mult de 64 de caractere.");
        }
    }

    /***
     * Controllerul este initializat.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textFieldCardNumber.setTextFormatter(new CardFormatter());
        textFieldCVV.setTextFormatter(new CardFormatter());
        textFieldCardNumber.textProperty().addListener((observableValue, s, t1) -> {
            if(t1.length()>16){//Card digit 16
                textFieldCardNumber.setText(s);
            }
        });
        textFieldCVV.textProperty().addListener((observableValue, s, t1) -> {
            if(t1.length()>3){//CVV  digits 3
                textFieldCVV.setText(s);
            }
        });

        //Utilizatorul nu va putea selecta zile date din trecut pentru expirarea cardului
        datePickerCardExpirationPicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0 );
            }
        });
        //Convertire data din dd/MM/yyyy in MM/yyyy pentru a fi salvata in baza de date.

        datePickerCardExpirationPicker.setConverter(new StringConverter<>() {
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
    }
}
