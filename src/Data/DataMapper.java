package Data;

import Enums.LoginMethod;
import Enums.UserType;
import Managers.MessageManager;
import Managers.SessionManager;
import Objects.BankReceipt;
import Objects.Transaction;
import Utility.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Properties;

/***
 *  Folosita pentru a trimite si primi data din baza de date
 */
public class DataMapper {
    private static DataMapper instance=null; //Numai o instantare a aceste clase este permisa.

    public static DataMapper getInstance() {
        if(instance==null){
            return new DataMapper();
        }
        return instance;
    }
    private DataMapper(){
        instance=this;
        try {
            getConnection();
        }
        catch (Exception e){
            MessageManager.ShowMessage(e.getMessage(), Alert.AlertType.ERROR,true);
        }
    }

    private Connection connection;

    /***
     * Se realizeaza conexiunea cu baza de date.
     */
    private void getConnection() throws Exception{
        try {
            String url = "jdbc:postgresql://localhost/POS";
            Properties props = new Properties();
            props.setProperty("user", "postgres");
            props.setProperty("password", "crocodil123");
            connection = DriverManager.getConnection(url, props);
        }
        catch (Exception e){
            throw new Exception("Nu se poate face conexiunea cu baza de date.");
        }
    }

    /***
     *  Inregistreaza un utilizator in magazin.
     * @param firstname  Prenume
     * @param lastname Nume
     * @param username User's Username. Email ca sa se poata loga si cu mail
     * @param email Emailul utilizatorului
     * @param phoneNumber  Numar de telefon pentru user
     * @param address  Adresa Utilizator
     * @param password  Parola Utilizator
     */
    public void RegisterShop(String firstname,String lastname,String username,String email,String phoneNumber,String address, String password) throws Exception {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");// Este folosit algoritmul sha-256 pentru criptarea parolei si inregistrarea ei in baza de date.
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            String query = "INSERT INTO shopuser (firstname,lastname,username,email,phone,address,password) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, firstname);
            statement.setString(2, lastname);
            statement.setString(3, username);
            statement.setString(4, email);
            statement.setString(5, phoneNumber);
            statement.setString(6, address);
            statement.setBytes(7, hash);
            statement.executeUpdate();
        }
        catch (SQLException e){
            throw new Exception("Utilizatorul deja exista in baza de date");
        }
    }

    /***
     * Conecteaz?? utilizatorul la POS sau la banc?? ??n func??ie de tipul de utilizator.
     * @param usernameOrEmail Utilizatorul poate folosi numele de utilizator,  fie emailul pentru a se autentifica at??t ??n POS cat si la banca.
     * @param password Parola utilizatorului.
     * @param userType Utilizator POS sau utilizator Banc??.
     */
    public void Login(String usernameOrEmail, String password, UserType userType) throws Exception {
        try {
            String query;

            //Interogare pentru ob??inerea parolei din baza de date din contul bancar sau din contul POS prin e-mail.
            if(userType.equals(UserType.SHOP)) {
                 query = "SELECT password FROM shopuser WHERE email=?";
            }
            else{
                 query = "SELECT password FROM bankuser WHERE email=?";
            }
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, usernameOrEmail);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                if (Arrays.equals(rs.getBytes(1), hash)) {// Se verifica hash-ul bazei de date cu hash-ul parolei inserat.
                    SessionManager.getInstance().setCurrentUser(usernameOrEmail, LoginMethod.EMAIL,userType);
                    UpdateCurrentUserInfo(usernameOrEmail, LoginMethod.EMAIL,userType);
                    return;
                } else {
                    throw new Exception("Combina??ie gre??it?? de e-mail / nume de utilizator ??i parol??");
                }
            }
            //Interogare pentru ob??inerea parolei din baza de date din contul bancar sau din contul POS folosind numele de utilizator.
            if(userType.equals(UserType.SHOP)) {
                query = "SELECT password FROM shopuser WHERE username=?";
            }
            else {
                query = "SELECT password FROM bankuser WHERE username=?";
            }
            statement = connection.prepareStatement(query);
            statement.setString(1, usernameOrEmail);
            rs=statement.executeQuery();
            if (rs.next()) {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
                if (Arrays.equals(rs.getBytes(1), hash)) { //Check database hash with inserted password hash.
                    SessionManager.getInstance().setCurrentUser(usernameOrEmail, LoginMethod.USERNAME,userType);
                    UpdateCurrentUserInfo(usernameOrEmail,LoginMethod.USERNAME,userType);
                    return;
                } else {
                    throw new Exception("Combina??ie gre??it?? de e-mail / nume de utilizator ??i parol??.");
                }
            }
            throw new Exception("Utilizatorul nu exista! V?? rog sa v?? ??nregistra??i!");
        }
        catch (SQLException e){
            throw new Exception("Eroare la conexiunea cu baza de date.");
        }
        catch (NoSuchAlgorithmException e){
            throw new Exception("A aparut o eroare.");
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }

    }

    /***
     * Creeaz?? un cont de utilizator nou pentru aplica??ia bancar??.
     * @param firstname Prenume.
     * @param lastname Nume.
     * @param username  Nume utilizator.
     * @param email  Email.
     * @param cardNumber Numar Card.
     * @param expirationDate Data de expirare a cardului.
     * @param cvv Card CVV
     * @param password Parola.
     * @param bankName Nume banca.
     */
    public void RegisterBank(String firstname, String lastname, String username, String email, String cardNumber,  Date expirationDate, String cvv,String password,String bankName ) throws Exception {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            //??nregistra??i utilizatorul ??n baza de date.
            String query = "INSERT INTO bankuser (firstname,lastname,username,email,cardnumber,password,bankname) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, firstname);
            statement.setString(2, lastname);
            statement.setString(3, username);
            statement.setString(4, email);
            statement.setString(5, cardNumber);
            statement.setBytes(6, hash);
            statement.setString(7,bankName);
            statement.executeUpdate();
            //Introduce??i datele cardului ??n baza de date.
            query = "INSERT INTO card (cardnumber,cardexpirationdate,cvv,balance) VALUES (?,?,?,?)";
            statement = connection.prepareStatement(query);
            statement.setString(1, cardNumber);
            statement.setDate(2, expirationDate);
            statement.setString(3, cvv);
            statement.setLong(4,0);
            statement.executeUpdate();
        }
        catch (SQLException e){
            throw new Exception("Utilizatorul exist?? deja.");
        }
    }

    /***
     * Ob??ine acredit??rile pentru nume, prenume, e-mail ??i num??r de card pentru utilizare.
     * @param loginInfo  Numele de utilizator sau e-mailul utilizatorului
     * @param loginMethod E-mail dac?? utilizatorul s-a conectat utiliz??nd e-mail, Parol?? dac?? utilizatorul s-a conectat utiliz??nd parola.
     * @param userType Utilizator al magazinului sau al b??ncii.
     */
    public void UpdateCurrentUserInfo(String loginInfo, LoginMethod loginMethod,UserType userType ) throws Exception {
        try {
            if(userType.equals(UserType.BANK)) {
                String query;
                if (loginMethod.equals(LoginMethod.EMAIL)) {
                    query = "SELECT firstname,lastname,email,cardnumber FROM bankuser WHERE email = ?";
                } else {
                    query = "SELECT firstname,lastname,email,cardnumber FROM bankuser WHERE username = ?";
                }
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, loginInfo);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    SessionManager.getInstance().getCurrentBankUser().setFirstName(rs.getString(1));
                    SessionManager.getInstance().getCurrentBankUser().setLastName(rs.getString(2));
                    SessionManager.getInstance().getCurrentBankUser().setEmail(rs.getString(3));
                    SessionManager.getInstance().getCurrentBankUser().setCardNumber(rs.getString(4));
                }
            }
            else{
                String query;
                if (loginMethod.equals(LoginMethod.EMAIL)) {
                    query = "SELECT firstname,lastname FROM shopuser WHERE email = ?";
                } else {
                    query = "SELECT firstname,lastname FROM shopuser WHERE username = ?";
                }
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, loginInfo);
                ResultSet rs = statement.executeQuery();
                if (rs.next()) {
                    SessionManager.getInstance().getCurrentShopUser().setFirstName(rs.getString(1));
                    SessionManager.getInstance().getCurrentShopUser().setLastName(rs.getString(2));
                }
            }
        }
        catch (Exception e){
            throw new Exception("A ap??rut o eroare la ob??inerea informa??iilor despre utilizator.");
        }
    }

    /***
     * Ob??ine soldul de pe cardul selectat.
     * @param cardNumber Num??rul cardului.
     * @return Valoare float care con??ine soldul cardului.
     */
    public float GetCardBalance(String cardNumber)throws Exception {
        try {
            String query = "SELECT balance FROM card WHERE cardnumber = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, cardNumber);
            ResultSet rs = statement.executeQuery();
            rs.next();
            return rs.getFloat(1);
        }catch (SQLException e){
            throw new Exception("Datele cardului sunt invalide.");
        }
        catch (Exception e) {
            throw new Exception("A ap??rut o eroare la ob??inerea informa??iilor despre utilizator.");
        }
    }

    /***
     * Depune fonduri ??ntr-un cont bancar utiliz??nd adresa de e-mail a utilizatorului.
     * @param amount Sum?? de bani.
     * @param email Adresa de e-mail a utilizatorului.
     */
    public void DepositToBankAccount(float amount,String email) throws Exception{
        try {
            String query = "SELECT cardnumber FROM bankuser WHERE email=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if(rs.next()) {
                query = "UPDATE card SET balance=balance+? WHERE cardnumber=?";
                statement = connection.prepareStatement(query);
                statement.setFloat(1, amount);
                statement.setString(2, rs.getString(1));
                statement.executeUpdate();
                SaveBankReceipt("Deposit",amount, rs.getString(1));
            }
            else{
                throw new Exception();
            }
        } catch (Exception e) {
            throw new Exception("A ap??rut o eroare la depunere.");
        }
    }

    /***
     * Face??i o achizi??ie prin POS.
     * @param cardNumber Num??rul cardului utilizatorului.
     * @param expirationDate Data expir??rii cardului.
     * @param cvv Card CVV.
     * @param price Pre?? card.
     */
    public void MakePurchase(String cardNumber,Date expirationDate,String cvv,float price) throws Exception{
        try {
            String query = "UPDATE card SET balance=balance-? WHERE cardnumber=? AND cardexpirationdate=? AND cvv=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setFloat(1,price);
            statement.setString(2, cardNumber);
            statement.setDate(3,expirationDate);
            statement.setString(4, cvv);
           int rows= statement.executeUpdate();
           if(rows==0){
               throw new Exception("Datele cardului sunt invalide, v?? rug??m ??ncerca??i din nou.");
           }
           SaveBankReceipt("Withdraw",price,cardNumber);
           DepositToBankAccount(price,ShopCredentials.SHOPEMAIL);
        } catch (Exception e) {
            throw new Exception("Datele cardului sunt invalide, v?? rug??m ??ncerca??i din nou.");
        }
    }

    /***
     * Ob??ine??i numele b??ncii folosind num??rul cardului.
     * @param cardNumber Num??rul cardului utilizatorului.
     * @return Numele b??ncii.
     */
    public String GetBankName(String cardNumber) throws Exception{
        try {
            String query = "SELECT bankname FROM bankuser WHERE cardNumber=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,cardNumber);
            ResultSet rs= statement.executeQuery();
            if(rs.next()){
                return rs.getString(1);
            }
            else{
                throw new Exception();
            }
        } catch (Exception e) {
            throw new Exception("Nu s-a putut ob??ine numele b??ncii.");
        }
    }

    /***
     * Salveaz?? o tranzac??ie de cump??rare a magazinului ??n baza de date.
     * @param productNumber Num??rul produsului achizi??ionat.
     * @param totalCost Costul total.
     * @param date Data achizitiei.
     * @param products Produse cumparate.
     * @param bankname Numele bancii utilizatorului.
     */
    public void SaveTransaction(int productNumber, float totalCost, Date date, String products,String bankname,String clientName) throws Exception {
        try {
            String query ="INSERT INTO transaction (salesagentname,productnumber,price,purchasedate,products,bankname,clientname) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, SessionManager.getInstance().getCurrentShopUser().getFirstName()+" "+SessionManager.getInstance().getCurrentShopUser().getLastName());
            statement.setInt(2, productNumber);
            statement.setFloat(3,totalCost);
            statement.setDate(4, date);
            statement.setString(5, products);
            statement.setString(6,bankname);
            statement.setString(7,clientName);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new Exception("A ap??rut o eroare la achizi??ie.");
        }
    }

    /***
     * Ob??ine??i toate tranzac??iile din baza de date.
     * @return Lista care con??ine toate tranzac??iile.
     */
    public ObservableList<Transaction> GetAllTransactions() throws Exception {
        try {
            ObservableList<Transaction> transactions= FXCollections.observableArrayList();
            String query = "SELECT * FROM transaction";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                transactions.add(new Transaction(rs.getLong(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getFloat(4),
                        rs.getDate(5),
                        rs.getString(6),
                        rs.getString(7),
                        rs.getString(8)));
            }
            return transactions;
        } catch (Exception e) {
            throw new Exception("A ap??rut o eroare la ob??inerea informa??iilor despre tranzac??ie.");
        }
    }

    /***
     * Salva??i chitan??a bancar?? (Depunere ??i retragere).
     * @param action Depune??i sau retrage??i.
     * @param amount Suma fondurilor.
     * @param cardNumber Num??rul cardului.
     */
    public void SaveBankReceipt(String action,float amount,String cardNumber) throws Exception{
        try {
            String query ="INSERT INTO bankreceipt (action,amount,date,cardnumber) VALUES (?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, action);
            statement.setFloat(2, amount);
            statement.setDate(3, new Date(Calendar.getInstance().getTime().getTime()));
            statement.setString(4,cardNumber);
            statement.executeUpdate();
        } catch (Exception e) {
            throw new Exception("A ap??rut o eroare la salvarea bonului bancar.");
        }
    }

    /***
     * Ob??ine??i toate chitan??ele bancare din baza de date pentru cardul selectat.
     * @param cardNumber Num??rul cardului.
     * @return Lista care con??ine toate chitan??ele referitoare la acest num??r de card specific.
     */
    public ObservableList<BankReceipt> GetAllReceipts(String cardNumber) throws Exception {
        try {
            ObservableList<BankReceipt> bankReceipts= FXCollections.observableArrayList();
            String query = "SELECT * FROM bankreceipt WHERE cardNumber=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,cardNumber);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                bankReceipts.add(new BankReceipt(rs.getString(1), rs.getFloat(2), rs.getDate(3)));
            }
            return bankReceipts;
        } catch (Exception e) {
            throw new Exception("A ap??rut o eroare la ob??inerea informa??iilor privind chitan??a bancar??.");
        }
    }
}
