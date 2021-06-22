package Objects;

import java.sql.Date;

public class BankReceipt {
    public String action;
    public Float amount;
    public Date date;
    public BankReceipt(String action,Float amount,Date date){
        this.action=action;
        this.amount=amount;
        this.date=date;
    }
}
