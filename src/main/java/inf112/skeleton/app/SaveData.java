package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import inf112.skeleton.model.inventory.Inventory;

public class SaveData {
    private static final Json json = new Json();
    public static final String filePath = "src/main/resources/save_data.json";
    public String mapFile;
    public float x, y;
    public int hp, maxHp;
    public Inventory inventory;

    public void set(String mapFile, float x, float y, int hp, int maxHp, Inventory inventory) {
        this.mapFile = mapFile;
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.maxHp = maxHp;
        this.inventory = inventory;
    }

    public static SaveData load() {
        FileHandle file = Gdx.files.local(filePath);
        if (!file.exists())
            return defaultSaveData();
        String saveDataJson = file.readString();
        return new Json().fromJson(SaveData.class, saveDataJson);
    }

    public static SaveData defaultSaveData() {
        SaveData saveData = new SaveData();
        saveData.set("grass1.tmx", 16*8, 16*8, 20, 20, new Inventory());
        return saveData;
    }

    public static void save(SaveData saveData) {
        Gdx.files.local(SaveData.filePath).writeString(json.toJson(saveData), false);
    }
}
