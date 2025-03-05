package inf112.skeleton.view;

//import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TextBox {
    private Stage stage;
    private Window window;
    private Label textLabel;
    private boolean visible;

    public TextBox() {
        stage = new Stage(new ExtendViewport(800, 600));
        Viewport viewport = stage.getViewport();

        // Create a basic skin directly in code
//        Skin skin = new Skin();
        BitmapFont font = new BitmapFont();  // Default font
//        skin.add("default-font", font);

        // Add a drawable for the window background (white color for simplicity)
//        skin.add("default-window", new Label.LabelStyle().font);

        // Create WindowStyle
        Window.WindowStyle windowStyle = new Window.WindowStyle();
//        windowStyle.titleFont = skin.getFont("default-font");
        // Set a simple white background for the window
//        windowStyle.background = skin.newDrawable("default", Color.DARK_GRAY);

        // Create LabelStyle
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        textLabel = new Label("", labelStyle);
        textLabel.setWrap(true);
        textLabel.setAlignment(Align.topLeft);

        // Create Window
        window = new Window("", windowStyle);
        window.setResizable(false);
        window.setMovable(false);
        window.setVisible(false);
        window.pad(10);  // Add padding to the window

        Table contentTable = new Table();
        contentTable.add(textLabel).growX().align(Align.topLeft);
        window.add(contentTable).grow();

        // Position and size
        window.setSize(400, 200);  // Example size, adjust as needed
        window.setPosition(viewport.getWorldWidth() / 2 - window.getWidth() / 2, 50);

        stage.addActor(window);
    }

    public void render(SpriteBatch batch, float delta) {
        if (visible) {
            stage.act(delta);
            stage.draw();
        }
    }

    public void showText(String text) {
        textLabel.setText(text);
        window.pack();
        window.setVisible(true);
        visible = true;
    }

    public void hideText() {
        window.setVisible(false);
        visible = false;
    }

    public Stage getStage() {
        return stage;
    }
}
