package Filters;

import Objects.Transaction;
import javafx.scene.control.DatePicker;

import java.sql.Date;

/***
 * Filtru pentru data exacta.
 */
public class DateFilter extends TransactionFilter {
    private final DatePicker date;

    public DateFilter(DatePicker date){
        this.date=date;
    }

    @Override
    public boolean PassesRequirements(Transaction transaction) {
        return Date.valueOf(date.getValue()).equals(transaction.date);
    }

    @Override
    public boolean CheckIfActive() {
        return !date.getEditor().getText().equals("");
    }
}
