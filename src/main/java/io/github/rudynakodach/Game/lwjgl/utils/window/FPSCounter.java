package io.github.rudynakodach.Game.lwjgl.utils.window;

import io.github.rudynakodach.Game.lwjgl.Window;

import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;

public class FPSCounter {

    private final Window window;

    private long deltaTime = 0;

    private long lastFrame = System.currentTimeMillis();
    private long timeCounter = 0;
    private long frameCount = 0;

    public FPSCounter(Window w) {
        this.window = w;
    }

    public void update() {
        long currentTime = System.currentTimeMillis();

        long delta = currentTime - lastFrame;
        deltaTime = delta;
        lastFrame = currentTime;

        timeCounter += delta;

        frameCount++;

        if(timeCounter >= 1000) {
            glfwSetWindowTitle(window.getWindowHandle(), window.windowTitle + " - %d FPS".formatted(frameCount));
            timeCounter = 0;
            frameCount = 0;
        }
    }

    public long getDeltaTime() {
        return deltaTime;
    }
}
