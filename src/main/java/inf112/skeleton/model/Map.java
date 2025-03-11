package inf112.skeleton.model;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.model.entities.Entity;
import inf112.skeleton.model.entities.Player;
import inf112.skeleton.model.entities.enemies.Enemy;
import inf112.skeleton.model.entities.enemies.EvilSquare;
import inf112.skeleton.model.entities.gameObjects.Door;
import inf112.skeleton.model.entities.gameObjects.GameObject;
import inf112.skeleton.model.entities.gameObjects.Sign;
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
        loadCollisionBoxes();
        spawnEntities();
        this.collisionHandler = new CollisionHandler(collisionBoxes);
    }

    private void loadObjects(){
        for(MapObject mapObject : tiledMap.getLayers().get("Objects").getObjects()){
            TiledMapTileMapObject tileObj = (TiledMapTileMapObject) mapObject;
            GameObject object = null;

            String type = tileObj.getTile().getProperties().get("type", String.class);
            if(type == null)
                object = new GameObject(tileObj, player);
            else if (type.equals("Sign"))
                object = new Sign(tileObj, player);
            else if (type.equals("Door"))
                object = new Door(tileObj, player);

            if (object == null)
                throw new RuntimeException("Error while loading object: " + type);
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
        for(int i = 0; i < 5; i++){
//            enemies.add(new EvilSquare(0, 250, player));
        }
    }

    public void update(float deltaTime) {
        player.update(deltaTime);
        collisionHandler.handleCollisions(player);
        for(Enemy e : enemies) {
            e.update(deltaTime);
            collisionHandler.handleCollisions(e);

            if(e.dead()){
                enemies.removeValue(e, true);
            }
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

    public Array.ArrayIterable<Rectangle> gethitboxes() {
        Array<Rectangle> hitboxes = new Array<>();
        for(Entity e : enemies) {
            hitboxes.addAll(e.getHitboxes());
        }
        return new Array.ArrayIterable<>(hitboxes);
    }

    public Array.ArrayIterable<Rectangle> getCollisionBoxes() {
        return new Array.ArrayIterable<>(collisionBoxes);
    }

    public Array.ArrayIterable<GameObject> getObjects(){
        return new Array.ArrayIterable<>(objects);
    }
}
