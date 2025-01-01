package Models.Factories;

import Models.PaymentModel;
import Models.WalletModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class WalletModelFactory implements Function {
    @Override
    public Object apply(Object o) {
        Map map = (HashMap) o;
        UUID userId = UUID.fromString((String) map.get("userId"));
        var model = new WalletModel(userId);

        Integer amount = Integer.parseInt((String) map.get("amount"));
        Integer totalIncomes = Integer.parseInt((String) map.get("totalIncomes"));
        Integer totalExpenses = Integer.parseInt((String) map.get("totalExpenses"));
        ArrayList<PaymentModel> incomes = (ArrayList<PaymentModel>) map.get("incomes");
        ArrayList<PaymentModel> expenses = (ArrayList<PaymentModel>) map.get("expenses");
        Map<String, Integer> budget = (Map<String, Integer>) map.get("budget");
        UUID id = UUID.fromString((String) map.get("id"));

        model.amount = amount;
        model.totalIncomes = totalIncomes;
        model.totalExpenses = totalExpenses;
        model.incomes = incomes;
        model.expenses = expenses;
        model.budget = budget;
        model.setId(id);

        return model;
    }
}