package inf112.skeleton.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import inf112.skeleton.model.inventory.Inventory;

class SaveData {
    private static final String filePath = "src/main/resources/save_data.json";
    private static final Json json = new Json();

    String mapFile;
    float x, y;
    int hp, maxHp;
    Inventory inventory;

    SaveData set(String mapFile, float x, float y, int hp, int maxHp, Inventory inventory) {
        this.mapFile = mapFile;
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.maxHp = maxHp;
        this.inventory = inventory;
        return this;
    }

    static SaveData load() {
        FileHandle file = Gdx.files.local(filePath);
        if (!file.exists())
            return defaultSaveData();
        String saveDataJson = file.readString();
        return new Json().fromJson(SaveData.class, saveDataJson);
    }

    static SaveData defaultSaveData() {
        return new SaveData().set("grass1.tmx", 16*8, 16*8, 20, 20, new Inventory());
    }

    static void save(SaveData saveData) {
        Gdx.files.local(filePath).writeString(json.toJson(saveData), false);
    }
}
