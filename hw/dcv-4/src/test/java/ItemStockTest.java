import shop.Item;
import storage.ItemStock;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemStockTest {

    @Test
    public void constructor_test() {
        Item item = new Item(1, "spaghetti", 56.0f, ".");
        ItemStock stock = new ItemStock(item);

        assertEquals(item, stock.getItem());
        assertEquals(0, stock.getCount());

    }


    @Test
    public void increaseItemCount_test() {
        Item item = new Item(1, "Item 1", 10.0f, "Category 1");
        ItemStock stock = new ItemStock(item);

        stock.IncreaseItemCount(5);
        int expected = 5;
        int real = stock.getCount();
        assertEquals(expected, real);

    }


    @Test
    public void decreaseItemCount_test()  {
        Item item = new Item(1, "Item 1", 10.0f, "Category 1");
        ItemStock stock = new ItemStock(item);

        stock.IncreaseItemCount(4);
        stock.decreaseItemCount(4);
        int expected = 0;
        int real = stock.getCount();
        assertEquals(expected, real);
    }


    @Test
    public void toString_test() {
        Item item = new Item(1, "spaghetti", 56.0f, ".");
        ItemStock stock = new ItemStock(item);
        stock.IncreaseItemCount(5);

        String expected = "STOCK OF ITEM:  " + item.toString() + "    PIECES IN STORAGE: 5";
        String actual = stock.toString();
        assertEquals(expected, actual);
    }



}
