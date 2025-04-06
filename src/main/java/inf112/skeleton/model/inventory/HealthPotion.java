package inf112.skeleton.model.inventory;

public class HealthPotion implements Item {
    @Override
    public int heal(){
        return 10;
    }

    @Override
    public String toString(){
        return "Health Potion";
    }

    @Override
    public String description(){
        return "Restores 10 HP";
    }
}
