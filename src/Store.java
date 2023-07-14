import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class Store {
    private Customer customer;
    private Cashier cashier;
    private List<Item> storeItems;
    private ShoppingCart item;

    public Store(Customer customer, Cashier cashier) {
        this.customer = customer;
        this.cashier = cashier;
        this.item = new ShoppingCart();
    }

    public void shop() {

        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (choice != 0){
            try {
                // Print Start Text
                System.out.println("");
                try (BufferedReader br = new BufferedReader(new FileReader("./src/startText.txt"))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (Exception e) {
                    System.out.println("Cannot print Text");
                }

                choice = scanner.nextInt();

                    switch (choice) {
                        case 1:
                            System.out.println("");
                            System.out.println("Store Items");
                            System.out.println("------------------------------------------------------------------------------");
                            readStoreItemsFromFile("./src/store-items.csv");
                            System.out.println("------------------------------------------------------------------------------");

                            try {
                                System.out.print("[0] - [" + (storeItems.size() - 1) + "] to select items\nSelect item to add: ");
                                int selection = scanner.nextInt();


                                if (selection >= 0 && selection < storeItems.size()) {
                                    item.addItem(this.storeItems.get(selection));
                                    System.out.println("");
                                    System.out.println("------------------------------------------------------------------------------");
                                    System.out.println("Cart Items");
                                    System.out.println("------------------------------------------------------------------------------");

                                    for (int i = 0; i < this.item.getItems().size(); i++) {
                                        System.out.printf("[%d] %-20s %s\n", i, this.item.getItems().get(i).getName(), this.item.getItems().get(i).getPrice());
                                    }
                                    break;
                                    }
                                else {
                                    System.out.println("Invalid input");
                                    }
                                }
                            catch (Exception e) {
                                    System.out.println("Invalid input");
                                    scanner.next();
                                }
                            break;
                        case 2:
                            if (this.item.getItems().size() > 0) {
                            System.out.println("");
                            System.out.println("------------------------------------------------------------------------------");
                            System.out.println("Cart Items");
                            System.out.println("------------------------------------------------------------------------------");

                            for (int i = 0; i < this.item.getItems().size(); i++) {
                                System.out.printf("[%d] %-20s %s\n", i, this.item.getItems().get(i).getName(), this.item.getItems().get(i).getPrice());
                            }

                                try {
                                    System.out.print("[0] - [" + (this.item.getItems().size() - 1) + "] to remove items\nSelect item to remove: ");

                                    int selection = scanner.nextInt();

                                    if (selection >= 0 && selection < this.item.getItems().size()) {
                                        this.item.removeItem(this.item.getItems().get(selection));
                                        System.out.println("Item removed from cart");
                                        System.out.println("");
                                        System.out.println("------------------------------------------------------------------------------");
                                        System.out.println("Cart Items");
                                        System.out.println("------------------------------------------------------------------------------");

                                        for (int i = 0; i < this.item.getItems().size(); i++) {
                                            System.out.printf("[%d] %-20s %s\n", i, this.item.getItems().get(i).getName(), this.item.getItems().get(i).getPrice());
                                        }
                                    } else {
                                        System.out.println("Invalid input");
                                    }
                                } catch (Exception e) {
                                    System.out.println("Invalid input");
                                    scanner.next();
                                    break;
                                }
                            }
                            else{
                                System.out.println("Cart is empty. No items to remove.");
                            }
                            break;
                        case 3:
                            if (this.item.getItems().size() == 0) {
                                System.out.println("Cart is empty. No items to checkout.");
                                break;
                            }
                            saveReceiptToFile("./src/receipt.txt");
                            System.out.println("");
                            printReceipt();

                            System.out.println("Receipt created successfully. :)");
                            choice = 0;
                            break;
                        case 0:
                            System.out.println("Exiting Program");
                            break;
                        default:
                            System.out.println("Invalid Input");
                            break;
                    }
            } catch (Exception ex) {
                System.out.println("Invalid Input");
                scanner.next();
            }
        }
    }

    public void readStoreItemsFromFile(String filename) {

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;

            this.storeItems = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                this.storeItems.add(new Item(parts[0], Double.parseDouble(parts[1])));
            }
            for (int i = 0; i < this.storeItems.size(); i++) {
                String[] parts = {this.storeItems.get(i).getName(), String.valueOf(this.storeItems.get(i).getPrice())};
                System.out.printf("[%d] %-20s %s\n", i, parts[0], parts[1]);
            }
        } catch (IOException e) {
            System.out.println("Error reading store items file: " + e.getMessage());
        }
    }

    public void printReceipt() {
        try (BufferedReader br = new BufferedReader(new FileReader("./src/receipt.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            System.out.println("Cannot print Text");
        }
    }

    public void saveReceiptToFile(String filename) {

        try (PrintWriter writer = new PrintWriter(filename)) {
            writer.println("------------------------------------------------------------------------------");
            writer.println(String.format("%39s", "RECEIPT"));
            writer.println("------------------------------------------------------------------------------");
            writer.println(String.format("Cashier: %-15s Shift: %s - %s", this.cashier.getName(), this.cashier.getStartOfShift(), this.cashier.getEndOfShift()));
            LocalDate date = LocalDate.now();
            writer.println(String.format("Date: %s", date));
            writer.println("------------------------------------------------------------------------------");
            writer.println("Items:");
            writer.println(String.format("\t %-30s \t %-7s %-8s \t %s", "Item name", "Qty", "Price", "Total Price"));

            HashMap<String, Integer> itemCounts = new HashMap<>();
            HashMap<String, Double> itemPrices = new HashMap<>();

            for (Item i : this.item.getItems()) {
                if (itemCounts.containsKey(i.getName())) {
                    int quantity = itemCounts.get(i.getName());
                    double qPrice = itemPrices.get(i.getName());
                    itemCounts.put(i.getName(), quantity + 1);
                    itemPrices.put(i.getName(), qPrice + i.getPrice());
                } else {
                    itemCounts.put(i.getName(), 1);
                    itemPrices.put(i.getName(), i.getPrice());
                }
            }
            for (String itemName: itemCounts.keySet()) {
                int quantity = itemCounts.get(itemName);
                double qPrice = itemPrices.get(itemName);
                writer.println(String.format("\t %-30s \t %-4d \t %-8.1f \t %.1f", itemName, quantity, qPrice/quantity, qPrice));
            }

            writer.println("------------------------------------------------------------------------------");
            writer.println(String.format("%s %.1f", "TOTAL:", item.computeTotalPrice()));
            writer.println("------------------------------------------------------------------------------");
        } catch (IOException e) {
            System.out.println("An error occurred while creating the receipt.");
            e.printStackTrace();
        }
    }

}
