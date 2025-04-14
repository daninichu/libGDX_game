package inf112.skeleton.model.factory;

import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import java.util.HashMap;
import inf112.skeleton.model.entities.Entity;

import java.util.NoSuchElementException;
import java.util.function.Function;

public class EntityFactory<E extends Entity> {
    private final HashMap<String, Function<TiledMapTileMapObject, E>> constructors = new HashMap<>();

    public void addConstructor(String type, Function<TiledMapTileMapObject, E> constructor) {
        constructors.put(type, constructor);
    }

    public E create(TiledMapTileMapObject tileObj){
        String type = tileObj.getTile().getProperties().get("type", String.class);
        Function<TiledMapTileMapObject, E> constructor = constructors.get(type);
        if(constructor == null)
            throw new NoSuchElementException("No constructor for " + type);
        return constructor.apply(tileObj);
    }
}
