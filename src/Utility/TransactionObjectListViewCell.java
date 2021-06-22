package Utility;


import Objects.Transaction;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;

import java.io.IOException;

/***
 * Custom cell pentru list view-ul de tranzacții din dashboard.
 */
public class TransactionObjectListViewCell extends ListCell<Transaction> {
    private FXMLLoader mLLoader;

    @FXML
    public Label idLabel;
    @FXML
    public Label clientNameLabel;
    @FXML
    public Label quantityLabel;
    @FXML
    public Label dateLabel;
    @FXML
    public Pane pane;
    @FXML
    public Label productsLabel;
    @FXML
    public Label bankNameLabel;
    @FXML
    public Label salesAgentNameLabel;
    @FXML
    public Label priceWithoutVatLabel;
    @FXML
    public Label vatPriceLabel;
    @FXML
    public Label totalPriceLabel;

    @Override
    protected void updateItem(Transaction transaction, boolean b) {
        super.updateItem(transaction, b);
        if(b || transaction == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("../FX/TransactionPanel.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            idLabel.setText(String.valueOf(transaction.transactionId));
            this.salesAgentNameLabel.setText(transaction.salesAgentName);
            this.bankNameLabel.setText(transaction.bankName);
            this.clientNameLabel.setText(transaction.clientName);
            this.quantityLabel.setText(String.valueOf(transaction.quantity));
            this.productsLabel.setText(String.valueOf(transaction.products));
            this.totalPriceLabel.setText(String.valueOf(transaction.price));
            this.dateLabel.setText(String.valueOf(transaction.date));
            float vatPrice=transaction.price*ShopCredentials.VAT;
            this.vatPriceLabel.setText(String.valueOf(vatPrice));
            this.priceWithoutVatLabel.setText(String.valueOf(transaction.price-vatPrice));
            setText(null);
            setGraphic(pane);
        }
    }

}
