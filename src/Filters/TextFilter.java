package Filters;

import Enums.TransactionFilterColumn;
import Objects.Transaction;
import javafx.scene.control.TextField;

/***
 * Filtre pentru tranzactii care depind de text.
 */
public class TextFilter extends TransactionFilter {
    private final TextField textField;
    private final TransactionFilterColumn transactionFilterColumn;
    public TextFilter(TextField textField,TransactionFilterColumn transactionFilterColumn){
        this.textField=textField;
        this.transactionFilterColumn=transactionFilterColumn;
    }

    @Override
    public boolean PassesRequirements(Transaction transaction) {
        if(transactionFilterColumn.equals(TransactionFilterColumn.CLIENT_NAME)){
            return transaction.clientName.contains(textField.getText());
        }
        else if(transactionFilterColumn.equals(TransactionFilterColumn.SALES_AGENT_NAME)){
            return transaction.salesAgentName.contains(textField.getText());
        }
        else if(transactionFilterColumn.equals(TransactionFilterColumn.PRODUCTS)){
            String[] products =textField.getText().split(" ");
            for(String s:products){
                if(!transaction.products.contains(s)){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean CheckIfActive() {
        return !textField.getText().equals("");
    }
}
