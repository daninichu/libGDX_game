package inf112.skeleton.model;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import inf112.skeleton.model.entities.Player;
import inf112.skeleton.model.entities.enemies.Enemy;
import inf112.skeleton.model.entities.enemies.EvilSquare;
import inf112.skeleton.view.ViewableEntity;

/**
 * A class that keeps track of positions of entities.
 */
public class Map {
    private Player player;
    private Array<Enemy> enemies = new Array<>();
    public Array<Rectangle> collisionBoxes = new Array<>();
    private CollisionChecker collisionChecker;

    private TiledMap tiledMap;
    private int cols;
    private int rows;

    public Map(String mapFile, Player player) {
        this.tiledMap = new TmxMapLoader().load(mapFile);
        this.player = player;
        this.cols = tiledMap.getProperties().get("width", Integer.class);
        this.rows = tiledMap.getProperties().get("height", Integer.class);
        loadCollisionBoxes();
        spawnEntities();
        this.collisionChecker = new CollisionChecker(collisionBoxes);
    }

    private void loadCollisionBoxes(){
        for(MapObject object : tiledMap.getLayers().get("Collision").getObjects())
            collisionBoxes.add(((RectangleMapObject) object).getRectangle());
    }

    private void spawnEntities() {
        enemies.add(new EvilSquare(200, 250, player));
    }

    public void update(float deltaTime) {
        player.update(deltaTime);
        for(Enemy e : enemies) {
            e.update(deltaTime);
        }
        collisionChecker.checkCollisions(player);
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public Array<ViewableEntity> getEntities() {
        Array<ViewableEntity> entities = new Array<>();
        entities.add(player);
        entities.addAll(enemies);
        return entities;
    }
}
