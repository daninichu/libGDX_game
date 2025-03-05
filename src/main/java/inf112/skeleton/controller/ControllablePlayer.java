package inf112.skeleton.controller;

import inf112.skeleton.view.UI;

public interface ControllablePlayer {
    void setRightMove(boolean t);

    void setLeftMove(boolean t);

    void setUpMove(boolean t);

    void setDownMove(boolean t);

    boolean interact(UI ui);
}
