package Router;

import Controllers.UserController;
import Controllers.WalletController;
import Database.Database;
import Exceptions.InvalidCredentialsException;
import Exceptions.InvalidParamsException;
import Exceptions.UnauthorizedException;
import Exceptions.UnknownRouteException;

import java.util.Map;
import java.util.function.Function;

public class Router {
    UserController userController;
    WalletController walletController;

    Router(Database database) {
        userController = new UserController(database);
        walletController = new WalletController(database);
    }

    public Object call(String route, Map params) throws InvalidParamsException, UnknownRouteException, UnauthorizedException, InvalidCredentialsException {
        switch (route) {
            case "/user/create":
                return userController.createAccount(params);
            case "/user/login":
                return userController.login(params);
            case "/user/logout":
                return userController.logout(params);

            case "/wallet/incomes/add":
                return walletController.addIncome(params);
            case "/wallet/expenses/add":
                return walletController.addExpense(params);
            case "/wallet/budget/add":
                return walletController.addBudget(params);

            default:
                throw new UnknownRouteException();
        }
    }
}
