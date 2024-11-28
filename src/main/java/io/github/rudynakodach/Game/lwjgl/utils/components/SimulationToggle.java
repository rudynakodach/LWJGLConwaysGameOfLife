package io.github.rudynakodach.Game.lwjgl.utils.components;

import io.github.rudynakodach.Game.Game;

import static org.lwjgl.glfw.GLFW.*;

public class SimulationToggle extends GameComponent {
    public SimulationToggle(Game game) {
        super(game);
    }

    @Override
    public void update() {
        if(game.getInputManager().isKeyDown(GLFW_KEY_SPACE)) {
            game.isSimulationRunning = !game.isSimulationRunning;
            System.out.println(game.isSimulationRunning);
        }
    }
}
