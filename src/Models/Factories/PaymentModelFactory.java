package Models.Factories;

import Models.PaymentModel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class PaymentModelFactory implements Function {
    @Override
    public Object apply(Object o) {
        Map map = (HashMap) o;
        String category = (String) map.get("category");
        Integer amount = Integer.parseInt((String) map.get("amount"));
        UUID id = UUID.fromString((String) map.get("id"));

        var model =  new PaymentModel(category, amount);
        model.setId(id);

        return model;
    }
}
