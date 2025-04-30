package inf112.skeleton.view.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.inventory.Inventory;
import inf112.skeleton.model.inventory.Item;
import inf112.skeleton.view.ViewableInventory;

public class InventoryScreen extends AbstractScreen{
    private ViewableInventory inventory;
    private Label[] itemLabels = new Label[Inventory.SIZE];
    private Label itemDescription = new Label("", labelStyle);

    public InventoryScreen(MyGame game, ViewableInventory inventory){
        super(game);
        this.inventory = inventory;
    }

    @Override
    public void show(){
        super.show();
        font.getData().setScale(1f);

        uiViewport = new ExtendViewport(400, 300);
        stage = new Stage(uiViewport, uiBatch);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        Table itemsTable = new Table(); // Subtable to hold item slots
        for (int i = 0; i < Inventory.SIZE; i++) {
            itemLabels[i] = new Label("Empty", labelStyle);
            itemsTable.add(itemLabels[i]).expandX().pad(5).row();
        }
        table.add(itemsTable).width(200).pad(10); // Inventory on the left
        table.add(itemDescription).expand().fill().pad(10); // Description on the right

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label controls = new Label("W: Up    S: Down    Q: Exit    SPACE: Use", labelStyle);
        controls.setFontScale(0.8f);
        table.add(controls).expandY().padBottom(20).bottom();
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
                renderInventory();
                unfadeFromBlack(deltaTime);
                if(resetFadeTimer())
                    game.setLoadState(MyGame.LoadState.NotLoading);
                return;
            }
        }
        switch(game.getState()){
            case Inventory -> renderInventory();
            case Play -> {
                game.setLoadState(MyGame.LoadState.LoadStart);
                game.setScreen(GameScreen.class);
            }
        }
    }

    private void renderInventory(){
        ScreenUtils.clear(Color.DARK_GRAY);
        for (int i = 0; i < Inventory.SIZE; i++) {
            itemLabels[i].setText(inventory.viewItem(i) == null? "Empty" : inventory.viewItem(i).toString());
            itemLabels[i].setColor(i == inventory.getIndex() ? Color.GREEN : Color.WHITE);
        }
        setItemDescription(inventory.viewItem(inventory.getIndex()));
        stage.act();
        stage.draw();
    }

    private void setItemDescription(Item item){
        itemDescription.setText(item == null ? "" : item.description());
    }
}
