package inf112.skeleton.model.factory;

import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import java.util.HashMap;
import inf112.skeleton.model.entities.Entity;

import java.util.function.Function;

public class EntityFactory<E extends Entity> {
    protected HashMap<String, Function<TiledMapTileMapObject, E>> constructors = new HashMap<>();

    public void addConstructor(String type, Function<TiledMapTileMapObject, E> constructor) {
        constructors.put(type, constructor);
    }

    public E create(TiledMapTileMapObject tileObj){
        String type = tileObj.getTile().getProperties().get("type", String.class);
        E e = constructors.get(type).apply(tileObj);
        if(e == null)
            throw new RuntimeException("Error while loading object: " + type);
        return e;
    }
}
