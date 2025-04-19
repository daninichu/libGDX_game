package inf112.skeleton.view.screens;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import inf112.skeleton.app.MyGame;
import inf112.skeleton.model.inventory.Inventory;
import inf112.skeleton.model.inventory.Item;
import inf112.skeleton.view.ViewableInventory;

public class InventoryScreen extends AbstractScreen{
    private ViewableInventory inventory;
    private Label[] itemLabels = new Label[Inventory.SIZE];
    private Label description = new Label("", labelStyle);

    public InventoryScreen(MyGame game, ViewableInventory inventory){
        super(game);
        this.inventory = inventory;

        uiViewport = new ExtendViewport(400, 200);
        stage = new Stage(uiViewport);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Table itemsTable = new Table(); // Subtable to hold item slots
        for (int i = 0; i < Inventory.SIZE; i++) {
            itemLabels[i] = new Label("Empty", labelStyle);
            itemsTable.add(itemLabels[i]).expandX().pad(5).row();
        }

        table.add(itemsTable).width(200).pad(10).top(); // Inventory on the left
        table.add(description).expand().fill().pad(10); // Description on the right
    }

    private void updateUI() {
        for (int i = 0; i < Inventory.SIZE; i++) {
            itemLabels[i].setText(inventory.viewItem(i) == null? "Empty" : inventory.viewItem(i).toString());

            if (i == inventory.getIndex()) {
                itemLabels[i].setColor(0, 1, 0, 1); // Highlight: green
            } else {
                itemLabels[i].setColor(1, 1, 1, 1); // Default: white
            }
        }
        setDescription(inventory.viewItem(inventory.getIndex()));
    }

    private void setDescription(Item item){
        description.setText(item == null ? "" : item.description());
    }

    @Override
    public void show(){
        super.show();
        font.getData().setScale(1f);
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
        ScreenUtils.clear(0, 0, 0, 1);

        updateUI();
        stage.act();
        stage.draw();
    }


    @Override
    public void resize(int width, int height){
        super.resize(width, height);
    }
}
