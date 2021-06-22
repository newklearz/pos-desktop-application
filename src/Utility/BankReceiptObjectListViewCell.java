package Utility;


import Objects.BankReceipt;
import Objects.Transaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;

import java.io.IOException;

/***
 * Clasa pentru panoul personalizat din list view pentru chitanțe bancare.
 */
public class BankReceiptObjectListViewCell extends ListCell<BankReceipt> {
    private FXMLLoader mLLoader;
    @FXML
    public Label actionLabel;
    @FXML
    public Label amountLabel;
    @FXML
    public Label dateLabel;
    @FXML
    public Pane pane;

    @Override
    protected void updateItem(BankReceipt bankReceipt, boolean b) {
        super.updateItem(bankReceipt, b);
        if(b || bankReceipt == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("../FX/BankReceiptPanel.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            this.actionLabel.setText(bankReceipt.action);
            this.amountLabel.setText(String.valueOf(bankReceipt.amount)+"RON");
            this.dateLabel.setText(String.valueOf(bankReceipt.date));
            setText(null);
            setGraphic(pane);
        }
    }

}
