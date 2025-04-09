package inf112.skeleton.model.collision;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ObjectSet;

public interface HashGrid<E> {
    ObjectSet<E> getLocalObjects(Rectangle box);
}
