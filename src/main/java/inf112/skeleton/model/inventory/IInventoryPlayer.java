package inf112.skeleton.model.inventory;

import inf112.skeleton.view.ViewableEntity;

public interface IInventoryPlayer extends ViewableEntity{
    Inventory getInventory();

    boolean useItem(Item item);
}
