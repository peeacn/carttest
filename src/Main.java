import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {

        String customerName = "1";
        String cashierName = "Ice2";
        LocalTime startShift = LocalTime.parse("08:00");
        LocalTime endShift = LocalTime.parse("18:00");


        Customer customer = new Customer(customerName);
        Cashier cashier = new Cashier(cashierName,startShift,endShift);
        Store store = new Store(customer, cashier);
        store.shop();

    }
}