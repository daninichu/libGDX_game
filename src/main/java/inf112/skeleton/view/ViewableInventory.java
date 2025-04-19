package inf112.skeleton.view;

import inf112.skeleton.model.inventory.Item;

public interface ViewableInventory {
    int getIndex();

    /**
     * Retrieves the item at the specified index without removing it.
     * @param index In the interval [0, 9].
     * @throws IndexOutOfBoundsException
     */
    Item viewItem(int index);
}
