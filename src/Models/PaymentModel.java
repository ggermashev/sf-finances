package Models;

public class PaymentModel extends Model {
    public int amount;
    public String category;

    public PaymentModel(String category, int amount) {
        super();
        this.amount = amount;
        this.category = category;
        title = "Payment";
    }
}