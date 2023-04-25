import org.junit.Test;
import org.junit.jupiter.api.*;
import shop.Item;
import shop.Order;
import shop.ShoppingCart;
import shop.StandardItem;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class OrderTest {
    @Test
    public void OrderWithState_test() {
        ShoppingCart cart = new ShoppingCart();
        StandardItem item1 = new StandardItem(312, "something", 432.312f, "something else", 321);
        StandardItem item2 = new StandardItem(2, "test2", 20.0f, "category2", 10);

        cart.addItem(item1);
        cart.addItem(item2);

        String name = "Franta";
        String address = "Technicka 2";
        int state = 413;

        shop.Order order = new shop.Order(cart, name, address, state);

        assertEquals(cart.getCartItems(),order.getItems());
        assertEquals(name, order.getCustomerName());
        assertEquals(address,order.getCustomerAddress());
        assertEquals(state,order.getState());
        assertNotNull(order);

    }

    @Test
    public void OrderWithNoState_test(){
        ShoppingCart cart = new ShoppingCart();
        StandardItem item1 = new StandardItem(12, "something", 432.312f, "something else", 321);
        StandardItem item2 = new StandardItem(3132, "abc", 3.2f, "def", 3);

        cart.addItem(item1);
        cart.addItem(item2);

        String name = "Imap Usay";
        String address = "Texas";

        shop.Order order = new shop.Order(cart, name, address);
        assertNotNull(order);
        assertEquals(0, order.getState());
        assertEquals(cart.getCartItems(),order.getItems());

    }

    @Test
    public void setItems_test(){
        ShoppingCart cart = new ShoppingCart();
        StandardItem item1 = new StandardItem(1, "wdads", 10.0f, "asdda", 1);
        StandardItem item2 = new StandardItem(2, "ddwa", 20.0f, "sda", 1);
        cart.addItem(item1);
        cart.addItem(item2);

        shop.Order order = new shop.Order(cart, "Suek Madeek", "Home", 1);

        ArrayList<Item> items = new ArrayList<>();
        StandardItem item3 = new StandardItem(3, "Item 3", 30.0f, "Category 2", 2);
        items.add(item3);
        items.add(item1);

        order.setItems(items);

        assertEquals(items, order.getItems());
    }

    @Test
    public void setName_setAddress_setState_test(){
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new StandardItem(2, "ddwa", 20.0f, "sda", 1));
        shop.Order order = new shop.Order(cart, "john", "home");

        String newName = "Pawel";
        String newAddress = "JDW 2";
        int newState = 32;

        order.setCustomerName(newName);
        order.setCustomerAddress(newAddress);
        order.setState(newState);

        assertEquals(newAddress, order.getCustomerAddress());
        assertEquals(newState, order.getState());
        assertEquals(newName, order.getCustomerName());
    }



    @Test
    public void NullOrder_test() {
        ShoppingCart cart = new ShoppingCart();
        String name = null;
        String address = null;
        int state = 0;

        shop.Order nullOrder = new Order(cart, name, address, state);

        assertNull(nullOrder.getCustomerAddress());
        assertNull(nullOrder.getItems());
        assertNull(nullOrder.getCustomerName());
    }

    @Test
    public void ShoppingCart_TotalCount_TotalPrice_test() {
        ShoppingCart cart = new ShoppingCart();
        StandardItem item1 = new StandardItem(1, "wdads", 10.0f, "asdda", 1);
        StandardItem item2 = new StandardItem(2, "ddwa", 20.0f, "sda", 1);
        cart.addItem(item1);
        cart.addItem(item2);


        int expectedCount = 2;
        int actualCount = cart.getItemsCount();

        float expectedPrice = 30.0f;
        float actualTotalPrice = cart.getTotalPrice();

        assertEquals(expectedCount, actualCount);
        assertEquals(expectedPrice, actualTotalPrice, 0.01f);

    }

    @Test
    public void removeItemFromShoppingCart_test() {
        ShoppingCart cart = new ShoppingCart();
        StandardItem item1 = new StandardItem(1, "dw", 10.0f, "dasadsads", 154);
        StandardItem item2 = new StandardItem(2, "asdads", 20.0f, "iou", 16);
        cart.addItem(item1);
        cart.addItem(item2);

        int expectedCount = 1;
        cart.removeItem(2);
        int actualCount = cart.getItemsCount();
        assertEquals(expectedCount, actualCount);


        expectedCount = 0;
        cart.removeItem(1);
        actualCount = cart.getItemsCount();
        assertEquals(expectedCount, actualCount);

    }

}
