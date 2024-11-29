package io.github.rudynakodach.Game.lwjgl.utils.components;

import io.github.rudynakodach.Game.Game;

import java.awt.*;

public class ZoomComponent extends GameComponent {
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

        if (game.getMouse().getScrollY() == 1) {
            float newZoomLevel = currentZoom + 0.2f;

            if (currentZoom <= 1f) {
                Point mousePosition = game.getMouse().getCursorPosition();
                Point windowSize = game.getWindow().getWindowSize();

                float mouseX = (float) mousePosition.x / windowSize.x;
                float mouseY = (float) mousePosition.y / windowSize.y;

                if (currentZoom == 1f) {
                    game.getWindow().setZoom(0f, 0f, newZoomLevel);
                } else {
                    game.getWindow().setZoom(mouseX, mouseY, newZoomLevel);
                }
            } else {
                if (currentZoom >= 3f) {
                    return;
                }

                if (newZoomLevel >= 3f) {
                    newZoomLevel = 3f;
                }

                game.getWindow().setZoom(offX, offY, newZoomLevel);
            }
        } else if (game.getMouse().getScrollY() == -1) {
            float newZoomLevel = currentZoom - 0.2f;

            if (newZoomLevel <= 1f) {
                game.getWindow().setZoom(0f, 0f, 1f);
            } else {
                game.getWindow().setZoom(offX, offY, newZoomLevel);
            }
        }
    }

}
