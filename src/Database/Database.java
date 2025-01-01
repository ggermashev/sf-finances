package Database;

import Exceptions.EntityAlreadyExistsException;
import Exceptions.EntityNotFoundException;
import Exceptions.TableNotFoundException;
import Models.Factories.PaymentModelFactory;
import Models.Factories.UserModelFactory;
import Models.Factories.WalletModelFactory;
import Models.Model;
import utils.FileManager;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

public class Database {
    private final Map<String, Map<String, Model>> tables;

    public Database() {
        tables = new HashMap<>();

        tables.put("Wallet", new HashMap<>());
        tables.put("User", new HashMap<>());
    }

    public Model create(String table, Model entity) throws TableNotFoundException, EntityAlreadyExistsException {
        if (!tables.containsKey(table)) {
            throw new TableNotFoundException();
        }
        if (tables.get(table).containsKey(entity.getId())) {
            throw new EntityAlreadyExistsException();
        }

        tables.get(table).put(entity.getId(), entity);
        return entity;
    }

    public Model delete (String table, String id) throws TableNotFoundException {
        if (!tables.containsKey(table)) {
            throw new TableNotFoundException();
        }

        return tables.get(table).remove(id);
    }

    public Model update(String table, Model entity) throws TableNotFoundException, EntityNotFoundException {
        if (!tables.containsKey(table)) {
            throw new TableNotFoundException();
        }
        if (!tables.get(table).containsKey(entity.getId())) {
            throw new EntityNotFoundException();
        }

        tables.get(table).put(entity.getId(), entity);
        return entity;
    }

    public Model find(String table, Function<Model, Boolean> isMatching) {
        var found = tables.get(table).values().stream().filter(isMatching::apply).findFirst();

        return found.orElse(null);
    }

    public int count(String table) throws TableNotFoundException {
        if (!tables.containsKey(table)) {
            throw new TableNotFoundException();
        }

        return tables.get(table).size();
    }

    public void loadToFile() throws TableNotFoundException, IOException {
        FileManager.loadTableToFile("User", tables.get("User"));
        FileManager.loadTableToFile("Wallet", tables.get("Wallet"));
    }

    public void loadFromFile() {
        Map constructorsMap = new HashMap();

        var userModelFactory = new UserModelFactory();
        constructorsMap.put("UserModel", userModelFactory);

        var paymentModelFactory = new PaymentModelFactory();
        constructorsMap.put("PaymentModel", paymentModelFactory);

        var walletModelFactory = new WalletModelFactory();
        constructorsMap.put("WalletModel", walletModelFactory);


        try {
            tables.put("User", FileManager.loadTableFromFile("User", constructorsMap));
        } catch (Exception e) {System.out.println(e);}
        try {
            tables.put("Wallet", FileManager.loadTableFromFile("Wallet", constructorsMap));
        } catch (Exception e) {System.out.println(e);}
    }
}
