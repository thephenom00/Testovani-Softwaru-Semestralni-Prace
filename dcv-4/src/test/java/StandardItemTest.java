import org.junit.Test;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import shop.StandardItem;


import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StandardItemTest {

    @Test
    public void constructor_test() {
        StandardItem item = new StandardItem(0, "noodles", 1000.0f, "food", 10);
        assertNotNull(item);
        assertEquals(0, item.getID());
        assertEquals("noodles", item.getName());
        assertEquals(1000.0f, item.getPrice(), 0.001f);
        assertEquals("food", item.getCategory());
        assertEquals(10, item.getLoyaltyPoints());
    }

    @Test
    public void copy_test() {
        StandardItem item = new StandardItem(3, "cola", 50.0f, "drinks", 0);
        StandardItem itemCopy = item.copy();

        assertNotNull(itemCopy);
        assertNotSame(item, itemCopy);
        assertEquals(item, itemCopy);
        assertEquals(item.getID(), itemCopy.getID());
    }

    @Test
    public void equals_test() {
        StandardItem item1 = new StandardItem(3, "cola", 50.0f, "drinks", 0);
        StandardItem item2 = new StandardItem(5, "nothing", 0.0f, "nothing", 0);
        StandardItem item3 = new StandardItem(3, "cola", 50.0f, "drinks", 0);

        assertFalse(item1.equals(item2));
        assertTrue(item1.equals(item3));
        assertFalse(item3.equals(null));
        assertTrue(item3.equals(new StandardItem(3, "cola", 50.0f, "drinks", 0)));
        assertFalse(item1.equals(new Object()));
    }

    @ParameterizedTest(name = "ItemID{0} should be {5}")
    @CsvSource({"1, Product 2, 11.0f, Category 2, 6 ,True", "6, Product 3, 1.0f, Drugs, 999 ,False"})
    public void paramTestEqualsStandardItem(int a, String b, float c, String d, int e, boolean f) {

        StandardItem standardItem = new StandardItem(1, "Product 2", 11.0f, "Category 2", 6);
        boolean expectedResult = f;

        boolean result = standardItem.equals(new StandardItem(a, b, c, d, e));

        assertEquals(expectedResult, result);
    }

    @Test
    public void setID_test() {
        StandardItem item = new StandardItem(3, "cola", 50.0f, "drinks", 0);
        int newID = 321;

        item.setID(newID);
        assertEquals(item.getID(), newID);
    }

    @Test
    public void setName_test() {
        StandardItem item = new StandardItem(3, "cola", 50.0f, "drinks", 0);
        String newName = "fanta";

        item.setName(newName);
        assertEquals(item.getName(), newName);
    }

    @Test
    public void setCategory_test() {
        StandardItem item = new StandardItem(3, "cola", 50.0f, "drinks", 0);
        String newCategory = "nothing";

        item.setCategory(newCategory);
        assertEquals(item.getCategory(), newCategory);
    }

    @Test
    public void setPrice_test() {
        StandardItem item = new StandardItem(3, "cola", 50.0f, "drinks", 0);
        float newPrice = 431.32f;

        item.setPrice(newPrice);
        assertEquals(item.getPrice(), newPrice, 0.001);
    }

    @Test
    public void setLoyaltyPoints_test() {
        StandardItem item = new StandardItem(3, "cola", 50.0f, "drinks", 0);
        int newPoints = 431;

        item.setLoyaltyPoints(newPoints);
        assertEquals(item.getLoyaltyPoints(), newPoints);
    }

    @Test
    public void toString_test() {
        int id = 32;
        String name = "daw";
        float price = 321.2f;
        String category = "dawd";
        int points = 213;

        StandardItem item = new StandardItem(id, name, price, category, points);

        assertEquals("Item   ID "+id+"   NAME "+name+"   CATEGORY "+category + "   PRICE "+price+"   LOYALTY POINTS "+points, item.toString());
    }




}
