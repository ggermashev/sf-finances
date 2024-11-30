package Database;

import Exceptions.EntityAlreadyExistsException;
import Exceptions.EntityNotFoundException;
import Exceptions.TableNotFoundException;
import Models.Model;

import java.util.*;
import java.util.function.Function;

public class Database {
    private final Map<String, Map<UUID, Model>> tables;

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

    public Model delete (String table, UUID id) throws TableNotFoundException {
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
}
