package io.github.rudynakodach.Game.lwjgl.utils.components;

import io.github.rudynakodach.Game.Game;
import io.github.rudynakodach.Game.lwjgl.utils.input.MouseManager;

import java.awt.Point;

import static org.lwjgl.glfw.GLFW.*;

public class ZoomComponent extends GameComponent {
    public static final float MIN_ZOOM = 1f;
    public static final float MAX_ZOOM = 3f;

    public ZoomComponent(Game game) {
        super(game);
    }

    @Override
    public void update() {
        if (!game.getMouse().isMouseInWindow()) {
            return;
        }

        float currentZoom = game.getWindow().getZoomLevel();
        float offX = game.getWindow().getZoomOffsetX();
        float offY = game.getWindow().getZoomOffsetY();

        if (game.getMouse().getScrollY() == MouseManager.SCROLL_UP || game.getInputManager().isKeyDown(GLFW_KEY_I)) {
            float newZoomLevel = currentZoom + 0.2f;

            if (currentZoom <= 1f) {
                Point mousePosition = game.getMouse().getCursorPosition();
                Point windowSize = game.getWindow().getWindowSize();

                float mouseX = (float) mousePosition.x / windowSize.x;
                float mouseY = (float) mousePosition.y / windowSize.y;

                game.getWindow().setZoom(mouseX, mouseY, newZoomLevel);
            } else {
                if (currentZoom >= 3f) {
                    return;
                }

                if (newZoomLevel >= 3f) {
                    newZoomLevel = 3f;
                }

                System.out.println("%f %f".formatted(offX, offY));
                game.getWindow().setZoom(offX, offY, newZoomLevel);
            }
        } else if (game.getMouse().getScrollY() == MouseManager.SCROLL_DOWN || game.getInputManager().isKeyDown(GLFW_KEY_O)) {
            float newZoomLevel = currentZoom - 0.2f;

            if (newZoomLevel <= 1f) {
                game.getWindow().setZoom(0f, 0f, 1f);
            } else {
                game.getWindow().setZoom(offX, offY, newZoomLevel);
            }
        }
    }

}
