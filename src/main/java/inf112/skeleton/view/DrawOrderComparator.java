package inf112.skeleton.view;

import java.util.Comparator;

public class DrawOrderComparator implements Comparator<ViewableEntity> {
    @Override
    public int compare(ViewableEntity entity1, ViewableEntity entity2) {
        if(entity2.getY() < entity1.getY())
            return -1;
        else if(entity2.getY() > entity1.getY())
            return 1;
        return 0;
    }
}
