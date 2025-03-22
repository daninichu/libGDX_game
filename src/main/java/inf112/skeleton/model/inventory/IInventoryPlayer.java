package inf112.skeleton.model.inventory;

public interface IInventoryPlayer {
    Inventory getInventory();

    boolean useItem(Item item);
}
