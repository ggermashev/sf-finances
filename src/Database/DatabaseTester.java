package Database;

import Exceptions.EntityAlreadyExistsException;
import Exceptions.EntityNotFoundException;
import Exceptions.TableNotFoundException;
import Models.Model;
import Models.WalletModel;
import utils.ITester;

import java.util.UUID;

public class DatabaseTester implements ITester {
    Database database;

    public void test() {
        boolean success = true;

        success &= testPositive();
        success &= testTableNotFound();
        success &= testEntityAlreadyExists();
        success &= testEntityNotFound();

        System.out.println(success ? "Database tests passed" : "Database tests failed");
    }

    private void beforeTest() {
        database = new Database(false);
    }

    private boolean testPositive() {
        beforeTest();
        String table = "Wallet";

        try {
            Model created = database.create(table, new WalletModel(UUID.randomUUID()));
            int count = database.count(table);

            if (count != 1) {
                System.out.println("Database test create failed");
                return false;
            }
            if (created.title != table) {
                System.out.println("Database test create failed");
                return false;
            }

            WalletModel found = (WalletModel) database.find(table, model -> model.title == table);

            if (found == null) {
                System.out.println("Database test find failed");
                return false;
            }

            found.budget.put("Dress", 100);
            database.update(table, found);
            WalletModel updated = (WalletModel) database.find(table, model -> model.title == table);

            if (updated.budget.get("Dress") != 100) {
                System.out.println("Database test update failed");
                return false;
            }

            database.delete(table, updated.getId());
            WalletModel deleted = (WalletModel) database.find(table, model -> model.title == table);

            if (deleted != null) {
                System.out.println("Database test delete failed");
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    private boolean testTableNotFound() {
        beforeTest();

        try {
            database.create("Unknown", new WalletModel(UUID.randomUUID()));
        } catch (TableNotFoundException e) {
            return true;
        } catch (Exception e) {
            System.out.println("Test table not found error failed");
            return false;
        }

        System.out.println("Test table not found error failed");
        return false;
    }

    private boolean testEntityAlreadyExists() {
        beforeTest();

        try {
            Model model = database.create("Wallet", new WalletModel(UUID.randomUUID()));
            database.create("Wallet", model);
        } catch (EntityAlreadyExistsException e) {
            return true;
        } catch (Exception e) {
            System.out.println("Test entity already exists failed");
            return false;
        }

        System.out.println("Test entity already exists failed");
        return false;
    }

    private boolean testEntityNotFound() {
        beforeTest();

        try {
            database.update("Wallet", new WalletModel(UUID.randomUUID()));
        } catch (EntityNotFoundException e) {
            return true;
        } catch (Exception e) {
            System.out.println("Test entity not found failed");
            return false;
        }

        System.out.println("Test entity not found failed");
        return false;
    }




}
