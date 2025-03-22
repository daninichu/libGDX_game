package inf112.skeleton.model.inventory;

public class Item {
    protected int heal;

    public int getHeal() {
        return heal;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName();
    }
}
