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

    @Override
    public String toString() {
        return "<Model/PaymentModel>" + "id=" + this.getId() + ";" + "amount=" + this.amount + ";" + "category=" + this.category + "</Model/PaymentModel>";
    }

    public String getReadable() {
        return category + ": " + amount;
    }
}