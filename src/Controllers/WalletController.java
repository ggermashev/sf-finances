package Controllers;

import Database.Database;
import Exceptions.InvalidParamsException;
import Exceptions.UnauthorizedException;
import Models.PaymentModel;
import Models.UserModel;
import Models.WalletModel;

import java.util.Map;
import java.util.UUID;

public class WalletController extends Controller {

    public WalletController(Database database) {
        super(database);
    }

    public WalletModel addIncome(Map params) throws UnauthorizedException, InvalidParamsException {
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

        UserModel user = (UserModel) database.find("User", entity -> ((UserModel) entity).accessToken == accessToken);
        if (user == null) {
            throw new UnauthorizedException();
        }

        PaymentModel payment = new PaymentModel(category, amount);
        WalletModel wallet = (WalletModel) database.find("Wallet", entity -> ((WalletModel) entity).getUserId() == user.getId());
        wallet.incomes.add(payment);

        try {
            database.update(wallet.title, wallet);
            return wallet;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public WalletModel addExpense(Map params) throws UnauthorizedException, InvalidParamsException {
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

        UserModel user = (UserModel) database.find("User", entity -> ((UserModel) entity).accessToken == accessToken);
        if (user == null) {
            throw new UnauthorizedException();
        }

        PaymentModel payment = new PaymentModel(category, amount);
        WalletModel wallet = (WalletModel) database.find("Wallet", entity -> ((WalletModel) entity).getUserId() == user.getId());
        wallet.expenses.add(payment);

        try {
            database.update(wallet.title, wallet);
            return wallet;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public WalletModel addBudget(Map params) throws UnauthorizedException, InvalidParamsException {
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

        UserModel user = (UserModel) database.find("User", entity -> ((UserModel) entity).accessToken == accessToken);
        if (user == null) {
            throw new UnauthorizedException();
        }

        WalletModel wallet = (WalletModel) database.find("Wallet", entity -> ((WalletModel) entity).getUserId() == user.getId());
        if (amount > 0) {
            wallet.budget.put(category, amount);
        } else {
            wallet.budget.remove(category);
        }

        try {
            database.update(wallet.title, wallet);
            return wallet;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
