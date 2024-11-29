package io.github.rudynakodach.Game.lwjgl.utils.components;

import io.github.rudynakodach.Game.map.elements.immovable.Wall;
import io.github.rudynakodach.Game.map.elements.movable.Cell;
import io.github.rudynakodach.Game.map.elements.MapElement;
import io.github.rudynakodach.Game.map.chunks.Chunk;
import io.github.rudynakodach.Game.Game;

import java.awt.Point;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class PlacementComponent extends GameComponent {

    public PlacementComponent(Game game) {
        super(game);
    }

    private HashMap<Integer, Class<? extends MapElement>> keybinds = new HashMap<>() {{
       put(GLFW_KEY_E, Cell.class);
       put(GLFW_KEY_Q, Wall.class);
    }};

    private int currentlyPlacing = GLFW_KEY_E;

    @Override
    public void update() {
        if(game.isSimulationRunning || !game.getMouse().isMouseInWindow()) {
            return;
        }

        Point mousePosition = game.getMouse().getCursorPosition();

        int mouseX = mousePosition.x;
        int mouseY = mousePosition.y;

        Point windowSize = game.getWindow().getWindowSize();

        int windowX = windowSize.x;
        int windowY = windowSize.y;

        float posX = (float) mouseX / windowX;
        float posY = (float) mouseY / windowY;

        float chunkWidth = (float) 1 / game.getMap().getMapWidth();
        float chunkHeight = (float) 1 / game.getMap().getMapHeight();

        int chunkX = (int) (posX / chunkWidth);
        int chunkY = (int) (posY / chunkHeight);

        float cellWidth = (float) 1 / (game.getMap().getChunkWidth() * game.getMap().getMapWidth());
        float cellHeight = (float) 1 / (game.getMap().getChunkHeight() * game.getMap().getMapHeight());

        int cellX = (int) (posX / cellWidth);
        int cellY = (int) (posY / cellHeight);

        Chunk c = game.getMap().getChunkAt(chunkX, chunkY);

        for (Integer i : keybinds.keySet()) {
            if(game.getInputManager().isKeyDown(i)) {
                currentlyPlacing = i;
                return;
            }
        }

        if(c != null &&
                cellX >= 0 && cellX <= game.getMap().getChunkWidth() * game.getMap().getMapWidth() &&
                cellY >= 0 && cellY <= game.getMap().getChunkHeight() * game.getMap().getMapHeight()) {

            MapElement mapElement = game.getMap().getElementAbsolute(cellX, cellY);

            if(game.getMouse().mouse1Clicked()) {
                try {
                    MapElement elem = keybinds.get(currentlyPlacing).newInstance();
                    if (mapElement == null) {
                        mapElement = new MapElement(0, 0, 0);
                        game.getMap().setElementAbsolute(elem, cellX, cellY);
                    } else {
                        mapElement = null;
                        game.getMap().setElementAbsolute(null, cellX, cellY);
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }

            float previewX = -1f + (cellX * cellWidth * 2);
            float previewY = 1f - (cellY * cellHeight * 2);

            glBegin(GL_QUADS);

            if (mapElement != null) {
                glColor3d(.8f, .1f, .1f);
            } else {
                if(currentlyPlacing == GLFW_KEY_E) {
                    glColor3d(.1f, .5f, .3f);
                } else if(currentlyPlacing == GLFW_KEY_Q) {
                    glColor3d(.35f, .5f, .2f);
                }
            }

            glVertex2d(previewX, previewY);
            glVertex2d(previewX + cellWidth * 2, previewY);
            glVertex2d(previewX + cellWidth * 2, previewY - cellHeight * 2);
            glVertex2d(previewX, previewY - cellHeight * 2);

            glEnd();
        }
    }
}
