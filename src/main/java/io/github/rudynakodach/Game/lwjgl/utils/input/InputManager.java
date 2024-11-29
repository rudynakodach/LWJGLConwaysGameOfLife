package io.github.rudynakodach.Game.lwjgl.utils.input;

import io.github.rudynakodach.Game.lwjgl.Window;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

public class InputManager {

    private final Set<Integer> previousActive = new HashSet<>();
    private final Set<Integer> activeKeys = new HashSet<>();

    private final Window window;

    public InputManager(Window window) {
        this.window = window;
    }

    public void registerKeyCallback() {
        glfwSetKeyCallback(window.getWindowHandle(), this::keyInputCallback);
    }

    private void keyInputCallback(long hWnd, int key, int scancode, int action, int mods) {
        if(action == GLFW_PRESS) {
            activeKeys.add(key);
        } else if(action == GLFW_RELEASE) {
            activeKeys.remove(key);
        }
    }

    public boolean isKeyDown(int key) {
        return activeKeys.contains(key) && !previousActive.contains(key);
    }

    public boolean isKeyUp(int key) {
        return !activeKeys.contains(key) && previousActive.contains(key);
    }

    public boolean getKey(int key) {
        return activeKeys.contains(key);
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    public boolean isKeyComboActive(int ...keys) {
        return Collections.singleton(keys).containsAll(activeKeys);
    }

    public void update() {
        previousActive.clear();
        previousActive.addAll(activeKeys);
    }
}
