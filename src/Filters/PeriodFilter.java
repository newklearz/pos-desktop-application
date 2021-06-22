package Filters;

import Objects.Transaction;
import javafx.scene.control.DatePicker;

import java.sql.Date;

/***
 *  Filtru pentru tranzactii între perioade de timp
 */
public class PeriodFilter extends TransactionFilter {
    private final DatePicker startDate;
    private final DatePicker endDate;

    public PeriodFilter(DatePicker startDate, DatePicker endDate){
        this.startDate=startDate;
        this.endDate=endDate;
    }

    @Override
    public boolean PassesRequirements(Transaction transaction) {
        if(startDate.getEditor().getText().equals("")){
            return transaction.date.compareTo(Date.valueOf(endDate.getValue()))>=0;
        }
        else if (endDate.getEditor().getText().equals("")){
            return transaction.date.compareTo(Date.valueOf(startDate.getValue()))<=0;
        }
        else {
            return transaction.date.compareTo(Date.valueOf(startDate.getValue())) <= 0 && transaction.date.compareTo(Date.valueOf(endDate.getValue())) >= 0;
        }
    }

    @Override
    public boolean CheckIfActive() {
        return !(startDate.getEditor().getText().equals("")&&endDate.getEditor().getText().equals(""))&&!(startDate.isDisabled()&&endDate.isDisabled());
    }
}
