package io.github.rudynakodach.Game.lwjgl.utils.components;

import io.github.rudynakodach.Game.Game;

import static org.lwjgl.glfw.GLFW.*;

public class GameResetComponent extends GameComponent {
    public GameResetComponent(Game game) {
        super(game);
    }

    @Override
    public void update() {
        if(game.getInputManager().isKeyDown(GLFW_KEY_R)) {
            game.reset();
        }
    }
}
