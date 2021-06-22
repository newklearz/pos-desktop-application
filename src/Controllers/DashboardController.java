package Controllers;

import Enums.TransactionFilterColumn;
import Filters.DateFilter;
import Filters.PeriodFilter;
import Filters.TextFilter;
import Filters.TransactionFilter;
import Managers.MessageManager;
import Managers.SceneManager;
import Managers.TransactionManager;
import Objects.Transaction;
import Utility.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

/***
 * The Dashboard Page Controller.
 */
public class DashboardController implements Initializable {


    public ListView<Transaction> transactionsListView;
    public MenuButton sortMenu;
    public TextField clientNameField;
    public DatePicker datePicker;
    public TextField productsField;
    public Label dateLabel;
    public Label toLabel;
    public Label fromLabel;
    public DatePicker toDatePicker;
    public DatePicker fromDatePicker;
    public RadioButton exactDateRadio;
    public RadioButton periodRadio;
    public TextField salesAgentField;
    public Text summaryLabel;
    private String selectedSortMethod;
    private TransactionFilter[] transactionFilters;

    /***
     * Refreshes the transaction list.
     */
    public void RefreshList(){
        try {
            TransactionManager.RefreshTransactions(selectedSortMethod);
            boolean filterEnabled=false;
            for (TransactionFilter transactionFilter : transactionFilters) {
                if (transactionFilter.CheckIfActive()) {
                    filterEnabled = true;
                    break;
                }
            }
            if(filterEnabled) {
                ObservableList<Transaction> filteredTransactions = FXCollections.observableArrayList();
                for(Transaction transaction:TransactionManager.transactions){
                    boolean passedAllFilters=true;
                    for(TransactionFilter transactionFilter:transactionFilters){
                        if(transactionFilter.CheckIfActive()){
                            if(!transactionFilter.PassesRequirements(transaction)){
                                passedAllFilters=false;
                                break;
                            }
                        }
                    }
                    if(passedAllFilters){
                        filteredTransactions.add(transaction);
                    }
                }
                transactionsListView.setItems(filteredTransactions);
                float sum=0;
                for(Transaction transaction:filteredTransactions){
                    sum+=transaction.price;
                }
                summaryLabel.setText(sum +" RON");
            }
            else{
                transactionsListView.setItems(TransactionManager.transactions);
                float sum=0;
                for(Transaction transaction:TransactionManager.transactions){
                    sum+=transaction.price;
                }
                summaryLabel.setText(sum +" RON");
            }



        }
        catch (Exception e){
            MessageManager.ShowMessage(e.getMessage(), Alert.AlertType.ERROR,true);
        }
    }

    /***
     * Sterge toate filtrele introduse de utilizator.
     */
    public void ClearFilters(){
        clientNameField.setText("");
        salesAgentField.setText("");
        productsField.setText("");
        datePicker.getEditor().clear();
        fromDatePicker.getEditor().clear();
        toDatePicker.getEditor().clear();
        RefreshList();
    }

    /***
     * Controllerul este initializat.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Configurare meniu sortare.
        for(MenuItem i: sortMenu.getItems()){
            i.setOnAction((ActionEvent e)->{
                selectedSortMethod=i.getText();
                RefreshList();
            });
        }
        transactionsListView.setCellFactory(ObjectListView->new TransactionObjectListViewCell());//Set custom item for listView. (Transaction)
        selectedSortMethod="Transaction ID";
        SceneManager.getInstance().setDashboardController(this);
        //Configurații filtre
        transactionFilters= new TransactionFilter[5];
        transactionFilters[0]=new TextFilter(clientNameField, TransactionFilterColumn.CLIENT_NAME);
        transactionFilters[1]=new TextFilter(salesAgentField, TransactionFilterColumn.SALES_AGENT_NAME);
        transactionFilters[2]=new TextFilter(productsField,TransactionFilterColumn.PRODUCTS);
        transactionFilters[3]=new DateFilter(datePicker);
        transactionFilters[4]=new PeriodFilter(fromDatePicker,toDatePicker);

        RefreshList();
    }

    public void PeriodSelected() {
        exactDateRadio.selectedProperty().setValue(false);
        datePicker.getEditor().setText("");
        datePicker.setDisable(true);
        fromDatePicker.setDisable(false);
        toDatePicker.setDisable(false);
    }

    public void ExactSelected( ) {
        periodRadio.selectedProperty().setValue(false);
        fromDatePicker.getEditor().setText("");
        toDatePicker.getEditor().setText("");
        fromDatePicker.setDisable(true);
        toDatePicker.setDisable(true);
        datePicker.setDisable(false);
    }
}
