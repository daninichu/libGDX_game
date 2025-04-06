package inf112.skeleton.model;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.model.collision.EntityCollisionHandler;
import inf112.skeleton.model.collision.StaticCollisionHandler;
import inf112.skeleton.model.entities.Entity;
import inf112.skeleton.model.entities.ItemDrop;
import inf112.skeleton.model.entities.Player;
import inf112.skeleton.model.entities.enemies.*;
import inf112.skeleton.model.entities.gameObjects.Door;
import inf112.skeleton.model.entities.gameObjects.GameObject;
import inf112.skeleton.model.entities.gameObjects.Sign;
import inf112.skeleton.model.entities.gameObjects.Switch;

/**
 * A class that keeps track of positions of entities.
 */
public class Map {
    private static final TmxMapLoader mapLoader = new TmxMapLoader();
    private static final String startPath = "tiledMaps/";
    private TiledMap tiledMap;

    private Player player;
    private Array<Enemy> enemies;
    private Array<GameObject> objects;
    private Array<ItemDrop> itemDrops;
    private Array<Rectangle> collisionBoxes;
    private StaticCollisionHandler staticCH;
    private EntityCollisionHandler entityCH = new EntityCollisionHandler();

    public Map(Player player) {
        this.player = player;
    }

    /**
     * Get a specific tile object from a specific {@code .tmx} file.
     * @param mapFile Where the desired object is located.
     * @param id The ID of the desired object within specified file.
     * @return The desired tile object.
     */
    public static TiledMapTileMapObject getObject(String mapFile, int id) {
        mapLoader.load(startPath + mapFile);
        return (TiledMapTileMapObject) mapLoader.getIdToObject().get(id);
    }

    /**
     * The start file path has already been provided. If your map file is in
     * {@code tiledMaps/map.tmx}, then the argument should just be {@code "map.tmx"}.
     * @param mapFile The {@code .tmx} file name only.
     */
    public void loadMap(String mapFile) {
        tiledMap = mapLoader.load(startPath + mapFile);
        reset();
        loadObjects();
        spawnEnemies();
        loadCollisionBoxes();
        staticCH = new StaticCollisionHandler(collisionBoxes);
    }

    private void reset(){
        enemies = new Array<>();
        objects = new Array<>();
        itemDrops = new Array<>();
        collisionBoxes = new Array<>();
    }

    public void update(float deltaTime) {
        Array<Entity> entities = getEntities();
        entities.forEach(e -> e.update(deltaTime));
        entityCH.updateGrid(entities);
        entities.forEach(e -> entityCH.handleCollision(e));
        entities.forEach(e -> staticCH.handleCollision(e));
    }

    private void loadObjects(){
        if(tiledMap.getLayers().get("Objects") == null)
            return;
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
            else if (type.equals("Switch"))
                object = new Switch(tileObj, player);

            if (object == null)
                throw new RuntimeException("Error while loading object: " + type);
            objects.add(object);
            if (!object.moveable())
                collisionBoxes.add(object.locateHurtbox());
        }
    }

    private void loadCollisionBoxes(){
        if(tiledMap.getLayers().get("Collision") == null)
            return;
        for (MapObject obj : tiledMap.getLayers().get("Collision").getObjects())
            collisionBoxes.add(((RectangleMapObject) obj).getRectangle());
    }

    private void spawnEnemies() {
        for(int i = 0; i < 3000; i++){
        }
            enemies.add(new Dummy(0, 50, player));
//            enemies.add(new Phantom(0, 50, player));
//            enemies.add(new Slime(50, 50, player));
        if(tiledMap.getLayers().get("Enemies") == null)
            return;
        for (MapObject obj : tiledMap.getLayers().get("Enemies").getObjects()) {
            RectangleMapObject rectObj = (RectangleMapObject) obj;
            String type = obj.getProperties().get("type", String.class);
            float x = rectObj.getRectangle().getX();
            float y = rectObj.getRectangle().getY();
            if(type.equals("Dummy"))
                enemies.add(new Dummy(x, y, player));
            if(type.equals("EvilSquare"))
                enemies.add(new Phantom(x, y, player));
            if(type.equals("DarkSquare"))
                enemies.add(new DarkSquare(x, y, player));
        }
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public Array<Entity> getEntities() {
        Array<Entity> entities = new Array<>();
        entities.add(player);
        entities.addAll(filter(enemies));
        entities.addAll(filter(objects));
        entities.addAll(filter(itemDrops));
        return entities;
    }

    private <T extends Entity> Array<T> filter(Array<T> arr) {
        for(int i = 0; i < arr.size; i++)
            if(arr.get(i).dead())
                itemDrops.addAll(arr.removeIndex(i--).getItemDrop());
        return arr;
    }

    public Array.ArrayIterable<Circle> getHitboxes() {
        Array<Circle> hitboxes = new Array<>();
        for(Entity e : getEntities()) {
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
