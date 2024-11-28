package io.github.rudynakodach.Game.lwjgl.utils.input;

import io.github.rudynakodach.Game.lwjgl.Window;
import org.lwjgl.BufferUtils;

import java.awt.Point;
import java.nio.DoubleBuffer;
import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

public class MouseManager {

    private final Window window;

    Set<Integer> oldActiveButtons = new HashSet<>();
    Set<Integer> activeMouseButtons = new HashSet<>();

    public MouseManager(Window window) {
        this.window = window;

        glfwSetMouseButtonCallback(window.getWindowHandle(), this::mouseButtonCallback);
    }

    private void mouseButtonCallback(long hWnd, int button, int action, int mods) {
        if(action == GLFW_PRESS) {
            activeMouseButtons.add(button);
        } else if(action == GLFW_RELEASE) {
            activeMouseButtons.add(button);
        }
    }

    public boolean mouse1Clicked() {
        return activeMouseButtons.contains(GLFW_MOUSE_BUTTON_1) && !oldActiveButtons.contains(GLFW_MOUSE_BUTTON_1);
    }

    public boolean mouse2Clicked() {
        return activeMouseButtons.contains(GLFW_MOUSE_BUTTON_2) && !oldActiveButtons.contains(GLFW_MOUSE_BUTTON_2);
    }

    public void update() {
        oldActiveButtons.clear();
        oldActiveButtons.addAll(activeMouseButtons);
    }

    public Point getCursorPosition() {
        DoubleBuffer mouseX = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer mouseY = BufferUtils.createDoubleBuffer(1);

        glfwGetCursorPos(window.getWindowHandle(), mouseX, mouseY);

        return new Point((int)mouseX.get(), (int)mouseY.get());
    }
}
