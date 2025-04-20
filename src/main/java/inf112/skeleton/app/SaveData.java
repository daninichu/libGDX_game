package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;

public class SaveData {
    public static final String filePath = "src/main/resources/save_data.json";
    public String mapFile;
//    public Inventory inventory;
    public Vector2 spawnPos;
    public int playerHp;

    public void set(String mapFile, Vector2 spawnPos, int playerHp) {
        this.mapFile = mapFile;
        this.spawnPos = spawnPos;
        this.playerHp = playerHp;
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
        saveData.set("grass2.tmx", new Vector2(16*8, 16*8), 20);
        return saveData;
    }
}
