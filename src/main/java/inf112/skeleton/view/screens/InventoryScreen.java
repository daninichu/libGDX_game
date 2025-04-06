package inf112.skeleton.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.inventory.Inventory;
import inf112.skeleton.model.inventory.Item;

public class InventoryScreen extends AbstractScreen{
    private BitmapFont font = new BitmapFont(Gdx.files.internal("font/MaruMonica.fnt"));
    private Iterable<Item> inventory;

    public InventoryScreen(MyGame game, Inventory inventory){
        super(game);
        this.inventory = inventory;
    }

    @Override
    public void show(){
        super.show();
        viewport = new ExtendViewport(400, 200);
        viewport = new ExtendViewport(24*MyGame.TILE_SIZE, 18*MyGame.TILE_SIZE);
    }

    @Override
    public void render(float deltaTime){
        switch(game.getLoadState()){
            case LoadStart -> {
                fadeToBlack(deltaTime);
                if(resetFadeTimer())
                    game.setLoadState(MyGame.LoadState.LoadEnd);
                return;
            }
            case LoadEnd -> {
                draw();
                unfadeFromBlack(deltaTime);
                if(resetFadeTimer())
                    game.setLoadState(MyGame.LoadState.NotLoading);
                return;
            }
        }
        switch(game.getState()){
            case Inventory -> draw();
            case Play -> {
                game.setLoadState(MyGame.LoadState.LoadStart);
                game.setScreen(GameScreen.class);
            }
        }
    }

    private void draw(){
        ScreenUtils.clear(0.55f, 0.45f, 0.3f, 1);

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        int i = 1;
        float h = viewport.getWorldHeight() / Inventory.SIZE;
        for(Item item : inventory){
            if(item != null){
                font.draw(batch, item.toString(), 0, i*h);
            }
        }
        batch.end();
    }

    @Override
    public void resize(int width, int height){
        viewport.update(width, height, true);
    }
}
