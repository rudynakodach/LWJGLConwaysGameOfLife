package io.github.rudynakodach.Game;

import io.github.rudynakodach.Game.lwjgl.Window;
import io.github.rudynakodach.Game.lwjgl.utils.components.*;
import io.github.rudynakodach.Game.lwjgl.utils.input.InputManager;
import io.github.rudynakodach.Game.lwjgl.utils.input.MouseManager;
import io.github.rudynakodach.Game.map.GameMap;

import java.util.Collection;
import java.util.HashMap;

public class Game {
    private final GameMap map;
    private final Window window;

    private final InputManager inputManager;
    private final MouseManager mouseManager;

    HashMap<String, GameComponent> gameComponents = new HashMap<>();

    public boolean isSimulationRunning = false;

    public Game() {
        this.map = new GameMap(16, 16, 16);
        this.map.createRectMap();

        this.window = new Window(this);

        this.mouseManager = new MouseManager(this.window);
        this.inputManager = new InputManager(this.window);
    }

    private void init() {
        gameComponents.put("placement", new PlacementComponent(this));
        gameComponents.put("simToggle", new SimulationToggle(this));
        gameComponents.put("gameReset", new GameResetComponent(this));
        gameComponents.put("zoom", new ZoomComponent(this));
        gameComponents.put("fps", new FPSCounter(this));
    }

    public void run() {
        init();

        window.run();
    }

    public void removeComponent(String component) {
        gameComponents.remove(component);
    }

    public Collection<GameComponent> getComponents() {
        return gameComponents.values();
    }

    public GameMap getMap() {
        return map;
    }

    public Window getWindow() {
        return window;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public MouseManager getMouse() {
        return mouseManager;
    }

    public void reset() {
        isSimulationRunning = false;

        getMap().wipeMap();
    }
}
