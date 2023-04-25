import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import archive.PurchasesArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shop.*;
import storage.NoItemInStorage;
import storage.Storage;

import static org.junit.jupiter.api.Assertions.*;

public class EShopControllerTest {
    EShopController eShopController;
    Storage storage;
    PurchasesArchive archive;

    @BeforeEach
    public void setUp() {
        storage = new Storage();
        archive = new PurchasesArchive();
    }

    @Test
    public void testPurchaseShoppingCart() {
        Item item1 = new StandardItem(1, "iPhone 13 Pro Max", 1299, "GADGETS", 5);
        Item item2 = new StandardItem(2, "Samsung Galaxy S21 Ultra", 1199, "GADGETS", 10);
        Item item3 = new StandardItem(3, "Electric Drill", 149, "TOOLS", 20);
        Item item4 = new DiscountedItem(4, "Smart Watch", 399, "GADGETS", 30, "1.8.2023", "1.12.2023");
        Item item5 = new DiscountedItem(5, "Wireless Earbuds", 99, "GADGETS", 50, "1.9.2023", "1.12.2023");
        Item item6 = new DiscountedItem(6, "Tool Set (50 pieces)", 399, "TOOLS", 10, "1.8.2023", "1.12.2023");

        storage.insertItems(item1, 15);
        storage.insertItems(item2, 20);
        storage.insertItems(item3, 10);
        storage.insertItems(item4, 5);
        storage.insertItems(item5, 30);
        storage.insertItems(item6, 15);

        assertEquals(15, storage.getItemCount(item1));
        assertEquals(20, storage.getItemCount(item2));
        assertEquals(10, storage.getItemCount(item3));
        assertEquals(5, storage.getItemCount(item4));
        assertEquals(30, storage.getItemCount(item5));
        assertEquals(15, storage.getItemCount(item6));


        ShoppingCart cart = new ShoppingCart();

        cart.addItem(item1);
        cart.addItem(item2);
        cart.addItem(item3);
        cart.addItem(item4);
        cart.addItem(item5);
        cart.addItem(item6);

        assertEquals(6, cart.getItemsCount());

}


    @Test
    public void testAddItemToCart() {
        Item item1 = new StandardItem(1, "iPhone 13 Pro Max", 1299, "GADGETS", 5);
        Item item2 = new StandardItem(2, "Samsung Galaxy S21 Ultra", 1199, "GADGETS", 10);
        Item item3 = new StandardItem(3, "Electric Drill", 149, "TOOLS", 20);

        ShoppingCart cart = new ShoppingCart();

        cart.addItem(item1);
        assertEquals(1, cart.getItemsCount());

        cart.addItem(item2);
        cart.addItem(item3);
        assertEquals(3, cart.getItemsCount());

        cart.addItem(item1);
        assertEquals(4, cart.getItemsCount());
    }


    @Test
    public void testRemoveItem() {
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new Item(1, "dsa", 100, "cdsa"));
        cart.addItem(new Item(2, "daw", 200, "sdadsc"));
        cart.addItem(new Item(3, "wd", 300, "cfg"));

        cart.removeItem(2);

        ArrayList<Item> items2 = cart.getCartItems();

        assertEquals(2, items2.size());
        assertEquals(1, items2.get(0).getID());
        assertEquals(3, items2.get(1).getID());
    }







}
