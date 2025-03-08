package inf112.skeleton.model;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.model.entities.Player;
import inf112.skeleton.model.entities.enemies.Enemy;
import inf112.skeleton.model.entities.enemies.EvilSquare;
import inf112.skeleton.model.entities.objects.GameObject;
import inf112.skeleton.model.entities.objects.Sign;
import inf112.skeleton.model.entities.objects.Tree;
import inf112.skeleton.view.ViewableEntity;

/**
 * A class that keeps track of positions of entities.
 */
public class Map {
    private Player player;
    private Array<Enemy> enemies = new Array<>();
    private Array<GameObject> objects = new Array<>();
    private Array<Rectangle> collisionBoxes = new Array<>();
    private CollisionHandler collisionHandler;
    private TiledMap tiledMap;

    public Map(String mapFile, Player player) {
        this.tiledMap = new TmxMapLoader().load(mapFile);
        this.player = player;
        loadObjects();
        player.nearbyObjects = objects;
        loadCollisionBoxes();
        spawnEntities();
        this.collisionHandler = new CollisionHandler(collisionBoxes);
    }

    private void loadObjects(){
        for(MapObject mapObject : tiledMap.getLayers().get("Objects").getObjects()){
            TiledMapTileMapObject tileObj = (TiledMapTileMapObject) mapObject;
            GameObject object = null;
            String type = tileObj.getTile().getProperties().get("type", String.class);
            if (type.equals("Sign")) {
                object = new Sign(tileObj);
            }
            else if (type.equals("Tree")) {
                object = new Tree(tileObj);
            }
            if (object == null) {
                throw new RuntimeException("Error while loading object: " + type);
            }
            objects.add(object);
            collisionBoxes.add(object.locateHurtbox());
        }
    }

    private void loadCollisionBoxes(){
        for (MapObject obj : tiledMap.getLayers().get("Collision").getObjects()) {
            collisionBoxes.add(((RectangleMapObject) obj).getRectangle());
        }
    }

    private void spawnEntities() {
        for(int i = 0; i < 10; i++){
            enemies.add(new EvilSquare(200, 250, player));
        }
    }

    public void update(float deltaTime) {
        player.update(deltaTime);
        collisionHandler.handleCollisions(player);
        for(Enemy e : enemies) {
            e.update(deltaTime);
            collisionHandler.handleCollisions(e);
        }
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public Array<ViewableEntity> getEntities() {
        Array<ViewableEntity> entities = new Array<>();
        entities.add(player);
        entities.addAll(enemies);
        entities.addAll(objects);
        return entities;
    }

    public Array<Rectangle> getCollisionBoxes() {
        return new Array<>(collisionBoxes);
    }

    public Array.ArrayIterable<GameObject> getObjects(){
        return new Array.ArrayIterable<>(objects);
    }
}
