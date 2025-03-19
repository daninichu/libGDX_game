package inf112.skeleton.model.inventory;

public class Inventory {
    public static final int SIZE = 10;
    private Item[] items = new Item[SIZE];
    private int index;

    public boolean addItem(Item item) {
        for (int i = 0; i < this.items.length; i++) {
            if (this.items[i] == null) {
                this.items[i] = item;
                return true;
            }
        }
        return false;
    }

    public Item getItem() {
        Item item = items[index];
        items[index] = null;
        return item;
    }

    public void indexUp(){
        index = Math.max(index + 1, SIZE);
    }

    public void indexDown(){
        index = Math.min(index - 1, 0);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder("[");
        for (Item item : items) {
            sb.append(item).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("]");
        return sb.toString();
    }
}
