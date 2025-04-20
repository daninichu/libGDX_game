package inf112.skeleton.model;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.collision.EntityCollisionHandler;
import inf112.skeleton.model.collision.StaticCollisionHandler;
import inf112.skeleton.model.entities.Entity;
import inf112.skeleton.model.entities.ItemDrop;
import inf112.skeleton.model.entities.Player;
import inf112.skeleton.model.entities.enemies.*;
import inf112.skeleton.model.entities.gameobjects.*;
import inf112.skeleton.model.factory.EntityFactory;
import inf112.skeleton.model.inventory.HealthPotion;
import inf112.skeleton.model.inventory.SpeedCrystal;
import inf112.skeleton.view.FloorEntity;

/**
 * A class that keeps track of positions of entities and updates the logic.
 */
public class Map {
    private static final TmxMapLoader mapLoader = new TmxMapLoader();
    private static final String startPath = "tiledMaps/";
    private TiledMap tiledMap;

    private MyGame game;
    private Player player;
    private Array<Enemy> enemies;
    private Array<GameObject> objects;
    private Array<ItemDrop> itemDrops;

    private Array<Rectangle> collisionBoxes;
    private Array<LoadZone> loadZones;

    private EntityFactory<Enemy> enemyFactory = new EntityFactory<>();
    private EntityFactory<GameObject> gameObjFactory = new EntityFactory<>();
    private EntityFactory<ItemDrop> itemFactory = new EntityFactory<>();

    private StaticCollisionHandler staticCH = new StaticCollisionHandler();
    private EntityCollisionHandler entityCH = new EntityCollisionHandler();

    public Map(MyGame game, Player player) {
        this.game = game;
        this.player = player;

        enemyFactory.addConstructor("Dummy", t -> new Dummy(t, player, staticCH));
        enemyFactory.addConstructor("Bat", t -> new Bat(t, player, staticCH));
        enemyFactory.addConstructor("Phantom", t -> new Phantom(t, player, staticCH));
        enemyFactory.addConstructor("Slime", t -> new Slime(t, player, staticCH));

        gameObjFactory.addConstructor(null, t -> new GameObject(t, player));
        gameObjFactory.addConstructor("Door", t -> new Door(t, player));
        gameObjFactory.addConstructor("Sign", t -> new Sign(t, player));
        gameObjFactory.addConstructor("Switch", t -> new Switch(t, player));
        gameObjFactory.addConstructor("PressurePlate", t -> new PressurePlate(t, player, entityCH));
        gameObjFactory.addConstructor("Bonfire", t -> new Bonfire(t, player));

        itemFactory.addConstructor("HealthPotion", t -> new ItemDrop(t, new HealthPotion()));
        itemFactory.addConstructor("SpeedCrystal", t -> new ItemDrop(t, new SpeedCrystal()));
    }

    /**
     * Get a specific tile object from a specific {@code .tmx} file.
     * @param mapFile Where the desired object is located.
     * @param id The ID of the desired object within specified file.
     * @return The desired tile object.
     */
    public static <T extends MapObject> T getObject(String mapFile, int id) {
        mapLoader.load(startPath + mapFile);
        return (T) mapLoader.getIdToObject().get(id);
    }

    /**
     * The start file path has already been provided. If your map file is in
     * {@code tiledMaps/map.tmx}, then the argument should just be {@code "map.tmx"}.
     * @param mapFile The {@code .tmx} file name only.
     */
    public void loadMap(String mapFile) {
        tiledMap = mapLoader.load(startPath + mapFile);
        reset();
        spawnEnemies();
        spawnObjects();
        spawnItems();
        loadCollisionBoxes();
        staticCH.updateGrid(collisionBoxes);
        placeLoadZones();
    }

    private void reset(){
        enemies = new Array<>();
        objects = new Array<>();
        itemDrops = new Array<>();
        collisionBoxes = new Array<>();
        loadZones = new Array<>();
    }

    public void update(float deltaTime) {
        Array<Entity> entities = getEntities();
        entities.forEach(e -> e.update(deltaTime));
        entityCH.updateGrid(entities);
        entities.forEach(e -> entityCH.handleCollision(e));
        entities.forEach(e -> staticCH.handleCollision(e));

        for(LoadZone loadZone : loadZones)
            if(loadZone.contains(player.getCenterPos()))
                game.enterLoadZone(loadZone);
    }

    private <E extends Entity> Array<E> loadTileObjects(EntityFactory<E> factory, String layer){
        Array<E> arr = new Array<>();
        if(tiledMap.getLayers().get(layer) != null)
            for(MapObject obj : tiledMap.getLayers().get(layer).getObjects())
                arr.add(factory.create(obj));
        return arr;
    }

    private void spawnEnemies() {
        enemies.addAll(loadTileObjects(enemyFactory, "Enemies"));
    }

    private void spawnObjects() {
        objects.addAll(loadTileObjects(gameObjFactory, "Objects"));
        for(GameObject obj : objects)
            if(!obj.movable() && obj.collidable() && !(obj instanceof FloorEntity))
                collisionBoxes.add(obj.locateHurtbox());
    }

    private void spawnItems() {
        itemDrops.addAll(loadTileObjects(itemFactory, "Items"));
        itemDrops.forEach(item -> item.setPlayer(player));
    }

    private void loadCollisionBoxes(){
        if(tiledMap.getLayers().get("Collision") != null)
            for(MapObject obj : tiledMap.getLayers().get("Collision").getObjects())
                collisionBoxes.add(((RectangleMapObject) obj).getRectangle());
    }

    private void placeLoadZones() {
        for(MapObject obj : tiledMap.getLayers().get("Load Zones").getObjects()){
            RectangleMapObject rectObj = (RectangleMapObject) obj;
            if(rectObj.getRectangle().width != 0 && rectObj.getRectangle().height != 0)
                loadZones.add(new LoadZone(rectObj));
        }
    }

    public Array<Entity> getEntities() {
        Array<Entity> entities = new Array<>();
        entities.add(player);
        entities.addAll(filter(enemies));
        entities.addAll(filter(objects));
        entities.addAll(filter(itemDrops));
        return entities;
    }

    private <E extends Entity> Array<E> filter(Array<E> arr) {
        for(int i = 0; i < arr.size; i++)
            if(arr.get(i).dead()){
                Array<ItemDrop> items = (arr.removeIndex(i--).getItemDrops());
                items.forEach(item -> item.setPlayer(player));
                itemDrops.addAll(items);
            }
        return arr;
    }

    public Array.ArrayIterable<Circle> getHitboxes() {
        Array<Circle> hitboxes = new Array<>();
        for(Entity e : getEntities())
            hitboxes.addAll(e.getHitboxes());
        return new Array.ArrayIterable<>(hitboxes);
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public Array.ArrayIterable<Rectangle> getCollisionBoxes() {
        return new Array.ArrayIterable<>(collisionBoxes);
    }

    public Array.ArrayIterable<GameObject> getGameObjects(){
        return new Array.ArrayIterable<>(objects);
    }
}
