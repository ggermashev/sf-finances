package Models;


import java.util.*;

public class WalletModel extends Model {
    public int amount;
    public int totalIncomes;
    public int totalExpenses;
    public ArrayList<PaymentModel> incomes;
    public ArrayList<PaymentModel> expenses;
    public Map<String, Integer> budget;
    private UUID userId;

    public WalletModel(UUID userId) {
        super();
        this.amount = 0;
        this.totalIncomes = 0;
        this.totalExpenses = 0;
        incomes = new ArrayList<>();
        expenses = new ArrayList<>();
        budget = new HashMap<>();
        this.userId = userId;
        title = "Wallet";
    }

    public UUID getUserId() {
        return this.userId;
    }
}


