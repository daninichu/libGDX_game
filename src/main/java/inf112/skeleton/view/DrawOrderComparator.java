package inf112.skeleton.view;

import java.util.Comparator;

public class DrawOrderComparator implements Comparator<ViewableEntity> {
    @Override
    public int compare(ViewableEntity e1, ViewableEntity e2) {
        if(e1 instanceof FloorEntity)
            return -1;
        if(e2 instanceof FloorEntity)
            return 1;
        return Float.compare(e2.getY(), e1.getY());
    }
}
