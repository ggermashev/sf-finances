package Models;


import java.util.*;

public class WalletModel extends Model {
    public int amount;
    public ArrayList<PaymentModel> incomes;
    public ArrayList<PaymentModel> expenses;
    public Map<String, Integer> budget;
    private UUID userId;

    public WalletModel(UUID userId) {
        super();
        this.amount = 0;
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


