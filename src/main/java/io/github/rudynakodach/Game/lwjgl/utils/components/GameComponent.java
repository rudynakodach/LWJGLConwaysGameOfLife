package io.github.rudynakodach.Game.lwjgl.utils.components;

import io.github.rudynakodach.Game.Game;
import io.github.rudynakodach.Game.lwjgl.Window;

public abstract class GameComponent {
    protected final Game game;

    public GameComponent(Game game) {
        this.game = game;
    }

    public abstract void update();
}
