package Models;


import utils.FileManager;

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

    public WalletModel(String userId) {
        this(UUID.fromString(userId));
    }

    public String getUserId() {
        return this.userId.toString();
    }

    @Override
    public String toString() {
        return "<Model/WalletModel>" + "id=" + this.getId() + ";" + "amount=" + this.amount + ";" + "totalIncomes=" + this.totalIncomes + ";" + "totalExpenses=" + this.totalExpenses + ";" + "incomes=" + FileManager.recursiveStringify(this.incomes) + ";" + "expenses=" + FileManager.recursiveStringify(this.expenses) + ";" + "budget=" + FileManager.recursiveStringify(this.budget) + ";" + "userId=" + this.userId.toString() + "</Model/WalletModel>";
    }
}


