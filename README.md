# POS-Project
This is POS application with three modules 
#In the POS module you can register then all the information are stored in a relational DB. The password is encrypted with SHA-256 algorithm provided by java.First, the agent has to log in using the registration information.
The agent that is about to sale the products using the POS module will be able to do so only by selecting the amount of each product the client wants then some fields regarding client's bank account must be 
completed(cardNumber, CVV, expiration date and the name of the client).If the bank credentials are correct and the bank account used has founds then the sale will be completed and this will appear as a withdrawal in the
bank account module, with the details about how much have been spent and when.
#Bank module lets you register a bank account which you can use to buy products from POS module.In registration page you can set your information about cardNumber, CVV, expirationDate,email, username and so on.
After you register your bank account all the information will be stored in a relational DB.You can log in to that bank account and then you can deposit founds, you can see the available funds,
you can check the deposit and withdrawl history and you can view all the information about that specific bank account using toggle buttons.
#Dashboard module is showing all the transactions that took place using the application with the posibility of searching and filtering them by certain fields like who the buyer was, what he bought, which agend has 
done the sale with the total sum of filtered transaction showing at the bottom right of the screen.The price are visible with base price, only VAT, and sum of price plus VAT.
