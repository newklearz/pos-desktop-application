package Filters;

import Objects.Transaction;

public abstract class TransactionFilter {
    public abstract boolean PassesRequirements(Transaction transaction);
    public abstract boolean CheckIfActive();

}
