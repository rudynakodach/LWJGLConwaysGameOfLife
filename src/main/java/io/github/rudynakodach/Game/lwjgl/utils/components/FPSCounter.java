package io.github.rudynakodach.Game.lwjgl.utils.components;

import io.github.rudynakodach.Game.Game;
import io.github.rudynakodach.Game.lwjgl.Window;

import static org.lwjgl.glfw.GLFW.glfwGetWindowAttrib;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;

public class FPSCounter extends GameComponent {

    private static long deltaTime = -1;

    private long lastFrame = System.currentTimeMillis();
    private long timeCounter = 0;
    private long frameCount = 0;

    public FPSCounter(Game game) {
        super(game);
    }

    @Override
    public void update() {
        long currentTime = System.currentTimeMillis();

        long delta = currentTime - lastFrame;
        deltaTime = delta;
        lastFrame = currentTime;

        timeCounter += delta;

        frameCount++;

        if(timeCounter >= 1000) {
            glfwSetWindowTitle(game.getWindow().getWindowHandle(), game.getWindow().windowTitle + " - %d FPS".formatted(frameCount));
            timeCounter = 0;
            frameCount = 0;
        }
    }

    public static long getDeltaTime() {
        return deltaTime;
    }
}
