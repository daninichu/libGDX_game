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
    Iterable<Item> inventory;

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

        switch(game.getState()){
            case Inventory -> {
                draw();
            }
            case LoadEnd -> {
                unfadeFromBlack(deltaTime);
                draw();
                if(resetFadeTimer()){
                    game.setState(MyGame.State.Inventory);
                }
            }
            case Play -> {
                fadeToBlack(deltaTime);
                draw();
                if(resetFadeTimer()){
                    game.setState(MyGame.State.LoadEnd);
                    game.setScreen(GameScreen.class);
                }
            }
        }
    }

    private void draw(){
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
