package io.github.rudynakodach.Game.lwjgl.utils.components;

import io.github.rudynakodach.Game.Cell;
import io.github.rudynakodach.Game.Chunk;
import io.github.rudynakodach.Game.Game;
import org.lwjgl.BufferUtils;

import java.awt.*;
import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class PreGamePlacer extends GameComponent {

    public PreGamePlacer(Game game) {
        super(game);
    }

    @Override
    public void update() {
        if(game.isSimulationRunning) {
            return;
        }

        DoubleBuffer mouseXBuffer = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer mouseYBuffer = BufferUtils.createDoubleBuffer(1);

        glfwGetCursorPos(game.getWindow().getWindowHandle(), mouseXBuffer, mouseYBuffer);

        int mouseX = (int) mouseXBuffer.get();
        int mouseY = (int) mouseYBuffer.get();

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

        if(c != null) {
            Cell cell = game.getMap().getCellAbsolute(cellX, cellY);

            glBegin(GL_QUADS);

            if (cell != null) {
                glColor3d(.8f, .1f, .1f);
            } else {
                glColor3d(.1f, .5f, .3f);
            }



            glEnd();
        }
    }
}
