package Controllers;

import Database.Database;
import Exceptions.*;
import Models.UserModel;
import Models.WalletModel;

import java.util.Map;
import java.util.UUID;

public class UserController extends Controller {

    public UserController(Database database) {
        super(database);
    }

    public UserModel createAccount(Map params) throws InvalidParamsException {
        String login = (String) params.get("login");
        String password = (String) params.get("password");

        if (login == null || password == null) {
            throw new InvalidParamsException();
        }

        UserModel user = new UserModel(login, password);
        try {
            database.create(user.title, user);
            WalletModel wallet = new WalletModel(user.getId());
            database.create("Wallet", wallet);
            return user;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public UUID login(Map params) throws InvalidParamsException, InvalidCredentialsException {
        String login = (String) params.get("login");
        String password = (String) params.get("password");

        if (login == null || password == null) {
            throw new InvalidParamsException();
        }

        UUID accessToken = UUID.randomUUID();

        UserModel user = (UserModel) database.find("User", entity -> ((UserModel) entity).verify(login, password));
        if (user == null) {
            throw new InvalidCredentialsException();
        }
        user.accessToken = accessToken;

        try {
            database.update(user.title, user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

        return accessToken;
    }

    public Boolean logout(Map params) throws UnauthorizedException {
        UUID accessToken = (UUID) params.get("accessToken");

        UserModel user = (UserModel) database.find("User", entity -> ((UserModel) entity).accessToken == accessToken);
        if (user == null) {
            throw new UnauthorizedException();
        }
        user.accessToken = null;

        try {
            database.update(user.title, user);
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
}
