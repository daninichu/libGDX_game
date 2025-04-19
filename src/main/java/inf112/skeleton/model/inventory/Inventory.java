package inf112.skeleton.model.inventory;

import inf112.skeleton.view.ViewableInventory;

public class Inventory implements ViewableInventory{
    public static final int SIZE = 10;
    private Item[] items = new Item[SIZE];
    private int index;

    public boolean addItem(Item item) {
        for (int i = 0; i < this.items.length; i++)
            if(this.items[i] == null){
                this.items[i] = item;
                return true;
            }
        return false;
    }

    public Item getItem() {
        Item item = items[index];
        items[index] = null;
        return item;
    }

    @Override
    public Item viewItem(int index){
        if(index < 0 || index >= this.items.length)
            throw new IndexOutOfBoundsException("Index out of bounds");
        return items[index];
    }

    @Override
    public int getIndex(){
        return index;
    }

    public void indexUp(){
        index = Math.min(index + 1, SIZE - 1);
    }

    public void indexDown(){
        index = Math.max(index - 1, 0);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("[");
        for (Item item : items)
            sb.append(item).append(", ");
        return sb.delete(sb.length() - 2, sb.length()).append("]").toString();
    }
}
