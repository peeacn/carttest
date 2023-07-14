import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.*;
import java.util.*;

public class ShoppingCart {
    private List<Item> items;

    public ShoppingCart() {
        items = new ArrayList<>();
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void removeItem(Item item) {
        items.remove(item);
    }

    public double computeTotalPrice() {
        double totalPrice = 0.0;
        for (Item item : items) {

            totalPrice += item.getPrice();
        }
        return totalPrice;
    }
}
