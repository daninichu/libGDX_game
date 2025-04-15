package inf112.skeleton.model;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class LoadZone {
    private final Rectangle bounds;
    public final String mapFile;
    public final Vector2 exit = new Vector2();

    public LoadZone(RectangleMapObject zone) {
        MapProperties props = zone.getProperties();
        this.bounds = zone.getRectangle();
        this.mapFile = props.get("Map File", String.class);
        RectangleMapObject point = Map.getObject(mapFile, props.get("ID", int.class));
        point.getRectangle().getCenter(exit);
    }

    public boolean contains(Vector2 point) {
        return bounds.contains(point);
    }
}
