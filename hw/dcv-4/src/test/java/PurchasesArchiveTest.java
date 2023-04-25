import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import archive.PurchasesArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shop.Item;
import shop.Order;
import shop.ShoppingCart;
import shop.StandardItem;

public class PurchasesArchiveTest {
    PurchasesArchive purchasesArchive;
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();


    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    public void testPrintItemPurchaseStatistics() {
        purchasesArchive = new PurchasesArchive();
        ShoppingCart cart = new ShoppingCart();
        Item item1 = new StandardItem(10, "item1", 543.0f, "cat", 1);
        Item item2 = new StandardItem(23, "item2", 220.0f, "cat", 2);
        cart.addItem(item1);
        cart.addItem(item2);

        String customerName = "DWdwaw";
        String customerAddress = "Rdawd";
        int state = 1;
        purchasesArchive.putOrderToPurchasesArchive(new Order(cart, customerName, customerAddress, state));
        String expectedOutput =
                "ITEM PURCHASE STATISTICS:\n" +
                "ITEM  Item   ID 23   NAME item2   CATEGORY cat   PRICE 220.0   LOYALTY POINTS 2   HAS BEEN SOLD 1 TIMES\n" +
                        "ITEM  Item   ID 10   NAME item1   CATEGORY cat   PRICE 543.0   LOYALTY POINTS 1   HAS BEEN SOLD 1 TIMES\n";
        purchasesArchive.printItemPurchaseStatistics();
        assertEquals(expectedOutput, outContent.toString());
    }

    @Test
    public void GetHowManyTimesHasBeenItemSold_PutOrderToPurchasesArchive_test() {
        purchasesArchive = new PurchasesArchive();
        ShoppingCart cart = new ShoppingCart();
        Item item1 = new StandardItem(10, "item1", 543.0f, "cat", 1);
        Item item2 = new StandardItem(23, "item2", 220.0f, "cat", 2);
        cart.addItem(item1);
        cart.addItem(item2);

        String customerName = "DWdwaw";
        String customerAddress = "Rdawd";
        int state = 1;
        purchasesArchive.putOrderToPurchasesArchive(new Order(cart, customerName, customerAddress, state));

        assertEquals(1, purchasesArchive.getHowManyTimesHasBeenItemSold(item1));
        assertEquals(1, purchasesArchive.getHowManyTimesHasBeenItemSold(item2));

        ShoppingCart cart2 = new ShoppingCart();
        cart2.addItem(item1);
        cart2.addItem(item2);
        purchasesArchive.putOrderToPurchasesArchive(new Order(cart2, "Jn", "123", 2));

        assertEquals(2, purchasesArchive.getHowManyTimesHasBeenItemSold(item1));
        assertEquals(2, purchasesArchive.getHowManyTimesHasBeenItemSold(item2));
    }



}
