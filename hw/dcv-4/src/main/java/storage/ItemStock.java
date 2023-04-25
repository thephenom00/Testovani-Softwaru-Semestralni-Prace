package storage;

import shop.*;


/**
 * Auxiliary class for item storage
 */
public class ItemStock {
    private Item refItem;
    private int count;
    
    public ItemStock(Item refItem) {
        this.refItem = refItem;
        count = 0;
    }
    
    @Override
    public String toString() {
        return "STOCK OF ITEM:  "+refItem.toString()+"    PIECES IN STORAGE: "+count;
    }
    
    public void IncreaseItemCount(int x) {
        count += x;
    }
    
    public void decreaseItemCount(int x) {
        count -= x;
    }
    
    public int getCount() {
        return count;
    }

    public Item getItem() {
        return refItem;
    }
}