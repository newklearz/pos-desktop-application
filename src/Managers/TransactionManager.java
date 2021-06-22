package Managers;

import Data.DataMapper;
import Objects.Transaction;
import javafx.collections.ObservableList;
import java.util.Comparator;

public class TransactionManager {
    public static ObservableList<Transaction> transactions;

    /***
     * Reîmprospătează tranzacțiile în funcție de metoda de sortare.
     * @param sortMethod Metoda de sortare.
     */
    public static void RefreshTransactions(String sortMethod) throws Exception {
        transactions= DataMapper.getInstance().GetAllTransactions();
        if(sortMethod.equals("nrDoc")){
            transactions.sort(Comparator.comparing(x->x.transactionId));
        }
        else if(sortMethod.equals("Agent vanzari")){
            transactions.sort(Comparator.comparing(x->x.salesAgentName));
        }
        else if(sortMethod.equals("Nume client")){
            transactions.sort(Comparator.comparing(x->x.clientName));
        }
        else if(sortMethod.equals("Cantitate produs")){
            transactions.sort(Comparator.comparing(x->x.quantity));
        }
        else if(sortMethod.equals("Pret total")){
            transactions.sort(Comparator.comparing(x->x.price));
        }
        else if(sortMethod.equals("Data")){
            transactions.sort(Comparator.comparing(x->x.date));
        }

    }
}
