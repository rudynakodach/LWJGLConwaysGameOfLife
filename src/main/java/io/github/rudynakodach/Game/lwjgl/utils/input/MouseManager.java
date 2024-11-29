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

    int scrollX = 0;
    int scrollY = 0;

    public MouseManager(Window window) {
        this.window = window;
    }

    public void registerMouseCallback() {
        glfwSetMouseButtonCallback(window.getWindowHandle(), this::mouseButtonCallback);
        glfwSetScrollCallback(window.getWindowHandle(), this::mouseScrollCallback);
    }

    private void mouseScrollCallback(long hWnd, double offX, double offY) {
        scrollX = (int) offX;
        scrollY = (int) offY;
    }

    private void mouseButtonCallback(long hWnd, int button, int action, int mods) {
        if(action == GLFW_PRESS) {
            activeMouseButtons.add(button);
        } else if(action == GLFW_RELEASE) {
            activeMouseButtons.remove(button);
        }
    }

    public int getScrollX() {
        return scrollX;
    }

    public int getScrollY() {
        return scrollY;
    }

    public Point getScroll() {
        return new Point(scrollX, scrollY);
    }

    public boolean mouse1Down() {
        return activeMouseButtons.contains(GLFW_MOUSE_BUTTON_1);
    }

    public boolean mouse2Down() {
        return activeMouseButtons.contains(GLFW_MOUSE_BUTTON_2);
    }

    /**
     * Represents a single left-click
     * @return if {@code GLFW_MOUSE_BUTTON_1} was clicked this frame
     */
    public boolean mouse1Clicked() {
        return activeMouseButtons.contains(GLFW_MOUSE_BUTTON_1) && !oldActiveButtons.contains(GLFW_MOUSE_BUTTON_1);
    }

    /**
     * Represents a single right-click
     * @return if {@code GLFW_MOUSE_BUTTON_2} was clicked this frame
     */
    public boolean mouse2Clicked() {
        return activeMouseButtons.contains(GLFW_MOUSE_BUTTON_2) && !oldActiveButtons.contains(GLFW_MOUSE_BUTTON_2);
    }

    public void update() {
        oldActiveButtons.clear();
        oldActiveButtons.addAll(activeMouseButtons);

        scrollX = 0;
        scrollY = 0;
    }

    public Point getCursorPosition() {
        DoubleBuffer mouseX = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer mouseY = BufferUtils.createDoubleBuffer(1);

        glfwGetCursorPos(window.getWindowHandle(), mouseX, mouseY);

        return new Point((int)mouseX.get(), (int)mouseY.get());
    }

    public boolean isMouseInWindow() {
        Point cursorPos = getCursorPosition();
        Point windowSize = window.getWindowSize();

        if(cursorPos.x < 0 || cursorPos.y < 0) {
            return false;
        } else return cursorPos.x <= windowSize.x && cursorPos.y <= windowSize.y;
    }
}
