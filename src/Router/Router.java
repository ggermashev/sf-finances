package Router;

import Controllers.UserController;
import Controllers.WalletController;
import Database.Database;
import Exceptions.*;

import java.util.Map;

public class Router {
    UserController userController;
    WalletController walletController;

    public Router(Database database) {
        userController = new UserController(database);
        walletController = new WalletController(database);
    }

    public Object call(String route, Map params) throws InvalidParamsException, UnknownRouteException, UnauthorizedException, InvalidCredentialsException, EntityAlreadyExistsException {
        switch (route) {
            case "/user/create":
                return userController.createAccount(params);
            case "/user/login":
                return userController.login(params);
            case "/user/logout":
                return userController.logout(params);

            case "/wallet/incomes/add":
                return walletController.addIncome(params);
            case "/wallet/incomes/get":
                return walletController.getIncomes(params);
            case "/wallet/expenses/add":
                return walletController.addExpense(params);
            case "/wallet/expenses/get":
                return walletController.getExpenses(params);
            case "/wallet/budget/add":
                return walletController.addBudget(params);
            case "/wallet/budget/get":
                return walletController.getBudget(params);
            case "/wallet/budget/rest":
                return walletController.getRestBudget(params);
            case "/wallet/budget/rest/by_category":
                return walletController.getRestBudgetByCategory(params);
            case "/wallet/incomes/total":
                return walletController.getTotalIncomesWithoutExpenses(params);

            default:
                throw new UnknownRouteException();
        }
    }
}
