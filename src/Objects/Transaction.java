package Objects;

import java.sql.Date;

public class Transaction {
    public long transactionId;
    public String salesAgentName;
    public String clientName;
    public int quantity;
    public String products;
    public String bankName;
    public float price;
    public Date date;

   public Transaction(long transactionId,String salesAgentName,int quantity,float price,Date date,String products,String bankName,String clientName){
       this.transactionId=transactionId;
       this.clientName=clientName;
       this.quantity=quantity;
       this.price=price;
       this.products=products;
       this.date=date;
       this.bankName=bankName;
       this.salesAgentName=salesAgentName;
   }


}
