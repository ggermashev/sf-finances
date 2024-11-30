package Models;

import java.util.Map;
import java.util.UUID;

public abstract class Model {
    private UUID id;
    public String title;

    public Model() {
        id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }
}

