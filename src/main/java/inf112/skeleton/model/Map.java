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
import inf112.skeleton.model.entities.gameObjects.Door;
import inf112.skeleton.model.entities.gameObjects.GameObject;
import inf112.skeleton.model.entities.gameObjects.Sign;
import inf112.skeleton.model.entities.gameObjects.Switch;
import inf112.skeleton.view.ViewableEntity;

/**
 * A class that keeps track of positions of entities.
 */
public class Map {
    private static final TmxMapLoader mapLoader = new TmxMapLoader();
    private static final String startPath = "tiledMaps/";
    private String currentMapFile;
    private TiledMap tiledMap;

    private Player player;
    private Array<Enemy> enemies;
    private Array<GameObject> objects;
    private Array<Rectangle> collisionBoxes;
    private CollisionHandler collisionHandler;

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
        currentMapFile = mapFile;
        tiledMap = mapLoader.load(startPath + currentMapFile);
        reset();
        loadObjects();
        spawnEntities();
        loadCollisionBoxes();
        this.collisionHandler = new CollisionHandler(collisionBoxes);
    }

    public void prepareNewMap(String mapFile) {
        currentMapFile = mapFile;
    }

    private void reset(){
        enemies = new Array<>();
        objects = new Array<>();
        collisionBoxes = new Array<>();
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
            else if (type.equals("Switch"))
                object = new Switch(tileObj, player);

            if (object == null)
                throw new RuntimeException("Error while loading object: " + type);
            objects.add(object);
            Rectangle box = object.locateHurtbox();
            if (box != null)
                collisionBoxes.add(box);
        }
    }

    private void loadCollisionBoxes(){
        for (MapObject obj : tiledMap.getLayers().get("Collision").getObjects()) {
            collisionBoxes.add(((RectangleMapObject) obj).getRectangle());
        }
    }

    private void spawnEntities() {
        for(int i = 0; i < 5; i++){
        }
//            enemies.add(new EvilSquare(192, 192, player));
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

    public Array.ArrayIterable<Rectangle> getHitboxes() {
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

    public String currentMapFile() {
        return currentMapFile;
    }
}
