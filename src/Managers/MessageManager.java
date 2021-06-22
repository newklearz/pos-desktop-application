package Managers;

import javafx.scene.control.Alert;


public class MessageManager {
    /***
     * Afișează mesajul corespunzător în caseta de mesaj utilizatorului.
     * @param text Mesaj text.
     * @param type Tipul mesajului.
     * @param wait Adevărat dacă se aștepta utilizatorul, fals dacă nu.
     */
    public static void ShowMessage(String text, Alert.AlertType type, boolean wait){
        Alert alert = new Alert(type);
        alert.setContentText(text);
        alert.setTitle(type.toString().toLowerCase());
        if(wait) {
            alert.showAndWait();
        }
        else{
            alert.show();
        }
    }
}
