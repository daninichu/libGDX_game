package inf112.skeleton.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.inventory.Inventory;
import inf112.skeleton.model.inventory.Item;

public class InventoryScreen extends AbstractScreen{
    private BitmapFont font = new BitmapFont(Gdx.files.internal("font/MaruMonica.fnt"));
    Inventory inventory;

    public InventoryScreen(MyGame game, Inventory inventory){
        super(game);
        this.inventory = inventory;
    }

    @Override
    public void show(){
        super.show();
        viewport = new ExtendViewport(200, 200);
    }

    @Override
    public void render(float deltaTime){
        ScreenUtils.clear(Color.BLACK);

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        int i = 0;
        float w = viewport.getWorldWidth() / Inventory.SIZE;
        for(Item item : inventory){
            if(item != null){
                font.draw(batch, item.toString(), i*w, viewport.getWorldHeight()/2);
            }
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height){
        viewport.update(width, height, true);
    }
}
