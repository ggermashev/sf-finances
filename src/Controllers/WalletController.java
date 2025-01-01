package Controllers;

import Database.Database;
import Exceptions.InvalidParamsException;
import Exceptions.UnauthorizedException;
import Models.PaymentModel;
import Models.UserModel;
import Models.WalletModel;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class WalletController extends Controller {

    public WalletController(Database database) {
        super(database);
    }

    public Boolean addIncome(Map params) throws UnauthorizedException, InvalidParamsException {
        UUID accessToken;
        String category;
        int amount;

        try {
            accessToken = (UUID) params.get("accessToken");
            category = (String) params.get("category");
            amount = (int) params.get("amount");
        } catch (Exception e) {
            throw new InvalidParamsException();
        }

        if (category == null || amount <= 0) {
            throw new InvalidParamsException();
        }
        if (accessToken == null) {
            throw new UnauthorizedException();
        }

        UserModel user = (UserModel) database.find("User", entity -> ((UserModel) entity).accessToken.equals(accessToken));
        if (user == null) {
            throw new UnauthorizedException();
        }

        PaymentModel payment = new PaymentModel(category, amount);
        WalletModel wallet = (WalletModel) database.find("Wallet", entity -> ((WalletModel) entity).getUserId().equals(user.getId()));
        wallet.incomes.add(payment);
        wallet.totalIncomes += amount;

        try {
            database.update(wallet.title, wallet);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Boolean addExpense(Map params) throws UnauthorizedException, InvalidParamsException {
        UUID accessToken;
        String category;
        int amount;

        try {
            accessToken = (UUID) params.get("accessToken");
            category = (String) params.get("category");
            amount = (int) params.get("amount");
        } catch (Exception e) {
            throw new InvalidParamsException();
        }

        if (category == null || amount <= 0) {
            throw new InvalidParamsException();
        }
        if (accessToken == null) {
            throw new UnauthorizedException();
        }

        UserModel user = (UserModel) database.find("User", entity -> ((UserModel) entity).accessToken.equals(accessToken));
        if (user == null) {
            throw new UnauthorizedException();
        }

        PaymentModel payment = new PaymentModel(category, amount);
        WalletModel wallet = (WalletModel) database.find("Wallet", entity -> ((WalletModel) entity).getUserId().equals(user.getId()));
        wallet.expenses.add(payment);
        wallet.totalExpenses += amount;

        try {
            database.update(wallet.title, wallet);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Boolean addBudget(Map params) throws UnauthorizedException, InvalidParamsException {
        UUID accessToken;
        String category;
        int amount;

        try {
            accessToken = (UUID) params.get("accessToken");
            category = (String) params.get("category");
            amount = (int) params.get("amount");
        } catch (Exception e) {
            throw new InvalidParamsException();
        }

        if (category == null || amount < 0) {
            throw new InvalidParamsException();
        }
        if (accessToken == null) {
            throw new UnauthorizedException();
        }

        UserModel user = (UserModel) database.find("User", entity -> ((UserModel) entity).accessToken.equals(accessToken));
        if (user == null) {
            throw new UnauthorizedException();
        }

        WalletModel wallet = (WalletModel) database.find("Wallet", entity -> ((WalletModel) entity).getUserId().equals(user.getId()));
        if (amount > 0) {
            wallet.budget.put(category, amount);
        } else {
            wallet.budget.remove(category);
        }

        try {
            database.update(wallet.title, wallet);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public ArrayList<PaymentModel> getIncomes(Map params) throws InvalidParamsException, UnauthorizedException {
        UUID accessToken;
        String category;

        try {
            accessToken = (UUID) params.get("accessToken");
            category = (String) params.get("category");
        } catch (Exception e) {
            throw new InvalidParamsException();
        }
        if (accessToken == null) {
            throw new UnauthorizedException();
        }

        UserModel user = (UserModel) database.find("User", entity -> ((UserModel) entity).accessToken.equals(accessToken));
        if (user == null) {
            throw new UnauthorizedException();
        }

        WalletModel wallet = (WalletModel) database.find("Wallet", entity -> ((WalletModel) entity).getUserId().equals(user.getId()));
        return category == null ? wallet.incomes : new ArrayList<>(wallet.incomes.stream().filter(income -> income.category.equals(category)).toList());
    }

    public ArrayList<PaymentModel> getExpenses(Map params) throws InvalidParamsException, UnauthorizedException {
        UUID accessToken;
        String category;

        try {
            accessToken = (UUID) params.get("accessToken");
            category = (String) params.get("category");
        } catch (Exception e) {
            throw new InvalidParamsException();
        }
        if (accessToken == null) {
            throw new UnauthorizedException();
        }

        UserModel user = (UserModel) database.find("User", entity -> ((UserModel) entity).accessToken.equals(accessToken));
        if (user == null) {
            throw new UnauthorizedException();
        }

        WalletModel wallet = (WalletModel) database.find("Wallet", entity -> ((WalletModel) entity).getUserId().equals(user.getId()));
        return category == null ? wallet.expenses : new ArrayList<>(wallet.expenses.stream().filter(expense -> expense.category.equals(category)).toList());
    }

    public Map<String, Integer> getBudget(Map params) throws InvalidParamsException, UnauthorizedException {
        UUID accessToken;

        try {
            accessToken = (UUID) params.get("accessToken");
        } catch (Exception e) {
            throw new InvalidParamsException();
        }
        if (accessToken == null) {
            throw new UnauthorizedException();
        }

        UserModel user = (UserModel) database.find("User", entity -> ((UserModel) entity).accessToken.equals(accessToken));
        if (user == null) {
            throw new UnauthorizedException();
        }

        WalletModel wallet = (WalletModel) database.find("Wallet", entity -> ((WalletModel) entity).getUserId().equals(user.getId()));
        return wallet.budget;
    }

    public Map<String, Integer> getRestBudget(Map params) throws InvalidParamsException, UnauthorizedException {
        UUID accessToken;

        try {
            accessToken = (UUID) params.get("accessToken");
        } catch (Exception e) {
            throw new InvalidParamsException();
        }
        if (accessToken == null) {
            throw new UnauthorizedException();
        }

        UserModel user = (UserModel) database.find("User", entity -> ((UserModel) entity).accessToken.equals(accessToken));
        if (user == null) {
            throw new UnauthorizedException();
        }

        WalletModel wallet = (WalletModel) database.find("Wallet", entity -> ((WalletModel) entity).getUserId().equals(user.getId()));
        Map <String, Integer> restBudget = new java.util.HashMap<>(Map.copyOf(wallet.budget));

        for (String category: restBudget.keySet()) {
            Integer totalExpense = wallet.expenses.stream().filter(expense -> expense.category.equals(category)).reduce((expense, acc) -> {acc.amount += expense.amount; return acc;}).get().amount;
            restBudget.compute(category, (k, totalBudget) -> totalBudget - totalExpense);
        }

        return restBudget;
    }

    public Integer getRestBudgetByCategory(Map params) throws InvalidParamsException, UnauthorizedException {
        UUID accessToken;
        String category;

        try {
            accessToken = (UUID) params.get("accessToken");
            category = (String) params.get("category");
        } catch (Exception e) {
            throw new InvalidParamsException();
        }
        if (accessToken == null) {
            throw new UnauthorizedException();
        }

        UserModel user = (UserModel) database.find("User", entity -> ((UserModel) entity).accessToken.equals(accessToken));
        if (user == null) {
            throw new UnauthorizedException();
        }

        WalletModel wallet = (WalletModel) database.find("Wallet", entity -> ((WalletModel) entity).getUserId().equals(user.getId()));
        Integer restBudget = wallet.budget.get(category);

        if (restBudget == null) { return null; }

        int totalExpense = wallet.expenses.stream().filter(expense -> expense.category.equals(category)).reduce((expense, acc) -> {acc.amount += expense.amount; return acc;}).get().amount;
        restBudget -= totalExpense;

        return restBudget;
    }

    public Integer getTotalIncomesWithoutExpenses(Map params) throws InvalidParamsException, UnauthorizedException {
        UUID accessToken;

        try {
            accessToken = (UUID) params.get("accessToken");
        } catch (Exception e) {
            throw new InvalidParamsException();
        }
        if (accessToken == null) {
            throw new UnauthorizedException();
        }

        UserModel user = (UserModel) database.find("User", entity -> ((UserModel) entity).accessToken.equals(accessToken));
        if (user == null) {
            throw new UnauthorizedException();
        }

        WalletModel wallet = (WalletModel) database.find("Wallet", entity -> ((WalletModel) entity).getUserId().equals(user.getId()));
        return wallet.totalIncomes - wallet.totalExpenses;
    }
}
