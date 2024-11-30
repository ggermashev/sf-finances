package Controllers;

import Database.Database;
import Exceptions.InvalidCredentialsException;
import Exceptions.InvalidParamsException;
import Exceptions.UnauthorizedException;
import Models.UserModel;
import Models.WalletModel;
import utils.ITester;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class ControllerTester implements ITester {
    UserController userController;
    WalletController walletController;

    public void test() {
        testUserController();
        testWalletController();
    }

    private void beforeTest() {
        Database database = new Database();
        userController = new UserController(database);
        walletController = new WalletController(database);
    }

    private void testUserController() {
        boolean good = true;

        good &= testCreateAccaunt();
        good &= testLogin();
        good &= testLogout();

        System.out.println(good ? "UserController tests passed" : "UserController tests failed");
    }

    private void testWalletController() {
        boolean good = true;

        good &= testAddIncome();
        good &= testAddExpense();
        good &= testAddBudget();

        System.out.println(good ? "WalletController tests passed" : "WalletController tests failed");
    }

    private boolean testCreateAccaunt() {
        boolean good = true;

        good &= testCreateAccountPositive();
        good &= testCreateAccountInvalidParams();

        return good;
    }

    private boolean testLogin() {
        boolean good = true;

        good &= testLoginPositive();
        good &= testLoginInvalidParams();
        good &= testLoginInvalidCredentials();

        return good;
    }

    private boolean testLogout() {
        boolean good = true;

        good &= testLogoutPositive();
        good &= testLogoutUnauthorized();

        return good;
    }

    private boolean testAddIncome() {
        boolean good = true;

        good &= testAddIncomePositive();
        good &= testAddIncomeInvalidParams();
        good &= testAddIncomeUnauthorized();

        return good;
    }

    private boolean testAddExpense() {
        boolean good = true;

        good &= testAddExpensePositive();
        good &= testAddExpenseInvalidParams();
        good &= testAddExpenseUnauthorized();

        return good;
    }

    private boolean testAddBudget() {
        boolean good = true;

        good &= testAddBudgetPositive();
        good &= testAddBudgetInvalidParams();
        good &= testAddBudgetUnauthorized();

        return good;
    }

    private boolean testCreateAccountPositive() {
        beforeTest();
        Map params = new HashMap();
        params.put("login", "login");
        params.put("password", "password");

        try {
            UserModel user = userController.createAccount(params);
            return user != null;
        } catch (Exception e) {
            System.out.println("testCreateAccountPositive failed");
            return false;
        }
    }

    private boolean testCreateAccountInvalidParams() {
        beforeTest();
        Map params = new HashMap();
        params.put("login", "login");

        try {
            userController.createAccount(params);
            System.out.println("testCreateAccountInvalidParams failed");
            return false;
        } catch (InvalidParamsException e) {
            return true;
        }
        catch (Exception e) {
            System.out.println("testCreateAccountInvalidParams failed");
            return false;
        }
    }

    private boolean testLoginPositive() {
        beforeTest();
        Map params = new HashMap();
        params.put("login", "login");
        params.put("password", "password");

        try {
            userController.createAccount(params);
            UUID access =  userController.login(params);
            return access != null;
        } catch (Exception e) {
            System.out.println("testLoginPositive failed");
            return false;
        }
    }

    private boolean testLoginInvalidParams() {
        beforeTest();
        Map params = new HashMap();
        params.put("login", "login");

        try {
            userController.login(params);
            System.out.println("testLoginInvalidParams failed");
            return false;
        } catch (InvalidParamsException e) {
            return true;
        }
        catch (Exception e) {
            System.out.println("testLoginInvalidParams failed");
            return false;
        }
    }

    private boolean testLoginInvalidCredentials() {
        beforeTest();
        Map params = new HashMap();
        params.put("login", "login");
        params.put("password", "password");

        try {
            userController.createAccount(params);

            params.put("password", "unknown");
            userController.login(params);
            System.out.println("testLoginInvalidCredentials failed");
            return false;
        } catch (InvalidCredentialsException e) {
            return true;
        }
        catch (Exception e) {
            System.out.println("testLoginInvalidCredentials failed");
            return false;
        }
    }

    private boolean testLogoutPositive() {
        beforeTest();
        Map params = new HashMap();
        params.put("login", "login");
        params.put("password", "password");

        try {
            userController.createAccount(params);
            UUID access = userController.login(params);

            params.put("accessToken", access);
            boolean res = userController.logout(params);
            return res;
        } catch (Exception e) {
            System.out.println("testLogoutPositive failed");
            return false;
        }
    }

    private boolean testLogoutUnauthorized() {
        beforeTest();
        Map params = new HashMap();

        try {
            params.put("accessToken", UUID.randomUUID());
            userController.logout(params);
            System.out.println("testLogoutUnauthorized failed");
            return false;
        } catch (UnauthorizedException e) {
            return true;
        }
        catch (Exception e) {
            System.out.println("testLogoutUnauthorized failed");
            return false;
        }

    }

    private boolean testAddIncomePositive() {
        beforeTest();
        Map params = new HashMap();
        params.put("login", "login");
        params.put("password", "password");

        try {
            userController.createAccount(params);
            UUID access = userController.login(params);

            params.put("accessToken", access);
            params.put("category", "category");
            params.put("amount", 10);

            WalletModel wallet = walletController.addIncome(params);
            return wallet.incomes.stream().filter(income -> income.category.equals("category") && income.amount == 10).toArray().length == 1;
        } catch (Exception e) {
            System.out.println("testAddIncomePositive failed");
            return false;
        }
    }

    private boolean testAddIncomeUnauthorized() {
        beforeTest();
        Map params = new HashMap();
        params.put("login", "login");
        params.put("password", "password");

        try {
            userController.createAccount(params);
            UUID access = userController.login(params);

            params.put("category", "category");
            params.put("amount", 10);

            walletController.addIncome(params);
            System.out.println("testAddIncomeUnauthorized failed");
            return false;
        } catch (UnauthorizedException e) {
            return true;
        }
        catch (Exception e) {
            System.out.println("testAddIncomeUnauthorized failed");
            return false;
        }

    }

    private boolean testAddIncomeInvalidParams() {
        beforeTest();
        Map params = new HashMap();
        params.put("login", "login");
        params.put("password", "password");

        try {
            userController.createAccount(params);
            UUID access = userController.login(params);

            params.put("accessToken", access);
            params.put("category", "category");

            walletController.addIncome(params);
            System.out.println("testAddIncomeInvalidParams failed");
            return false;
        } catch (InvalidParamsException e) {
            return true;
        }
        catch (Exception e) {
            System.out.println("testAddIncomeInvalidParams failed");
            return false;
        }

    }

    private boolean testAddExpensePositive() {
        beforeTest();
        Map params = new HashMap();
        params.put("login", "login");
        params.put("password", "password");

        try {
            userController.createAccount(params);
            UUID access = userController.login(params);

            params.put("accessToken", access);
            params.put("category", "category");
            params.put("amount", 10);

            WalletModel wallet = walletController.addExpense(params);
            return wallet.expenses.stream().filter(expense -> expense.category.equals("category") && expense.amount == 10).toArray().length == 1;
        } catch (Exception e) {
            System.out.println("testAddExpensePositive failed");
            return false;
        }
    }

    private boolean testAddExpenseUnauthorized() {
        beforeTest();
        Map params = new HashMap();
        params.put("login", "login");
        params.put("password", "password");

        try {
            userController.createAccount(params);
            UUID access = userController.login(params);

            params.put("category", "category");
            params.put("amount", 10);

            walletController.addExpense(params);
            System.out.println("testAddExpenseUnauthorized failed");
            return false;
        } catch (UnauthorizedException e) {
            return true;
        }
        catch (Exception e) {
            System.out.println("testAddExpenseUnauthorized failed");
            return false;
        }

    }

    private boolean testAddExpenseInvalidParams() {
        beforeTest();
        Map params = new HashMap();
        params.put("login", "login");
        params.put("password", "password");

        try {
            userController.createAccount(params);
            UUID access = userController.login(params);

            params.put("accessToken", access);
            params.put("category", "category");

            walletController.addExpense(params);
            System.out.println("testAddExpenseInvalidParams failed");
            return false;
        } catch (InvalidParamsException e) {
            return true;
        }
        catch (Exception e) {
            System.out.println("testAddExpenseInvalidParams failed");
            return false;
        }
    }

    private boolean testAddBudgetPositive() {
        beforeTest();
        Map params = new HashMap();
        params.put("login", "login");
        params.put("password", "password");

        try {
            userController.createAccount(params);
            UUID access = userController.login(params);

            params.put("accessToken", access);
            params.put("category", "category");
            params.put("amount", 10);

            WalletModel wallet = walletController.addBudget(params);
            return wallet.budget.get("category").equals(10);
        } catch (Exception e) {
            System.out.println("testAddBudgetPositive failed");
            return false;
        }
    }

    private boolean testAddBudgetUnauthorized() {
        beforeTest();
        Map params = new HashMap();
        params.put("login", "login");
        params.put("password", "password");

        try {
            userController.createAccount(params);
            UUID access = userController.login(params);

            params.put("category", "category");
            params.put("amount", 10);

            walletController.addBudget(params);
            System.out.println("testAddBudgetUnauthorized failed");
            return false;
        } catch (UnauthorizedException e) {
            return true;
        }
        catch (Exception e) {
            System.out.println("testAddBudgetUnauthorized failed");
            return false;
        }

    }

    private boolean testAddBudgetInvalidParams() {
        beforeTest();
        Map params = new HashMap();
        params.put("login", "login");
        params.put("password", "password");

        try {
            userController.createAccount(params);
            UUID access = userController.login(params);

            params.put("accessToken", access);
            params.put("category", "category");

            walletController.addBudget(params);
            System.out.println("testAddBudgetInvalidParams failed");
            return false;
        } catch (InvalidParamsException e) {
            return true;
        }
        catch (Exception e) {
            System.out.println("testAddBudgetInvalidParams failed");
            return false;
        }
    }

}
