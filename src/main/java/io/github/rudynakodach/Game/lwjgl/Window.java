package io.github.rudynakodach.Game.lwjgl;

import io.github.rudynakodach.Game.map.chunks.Chunk;
import io.github.rudynakodach.Game.Game;
import io.github.rudynakodach.Game.map.GameMap;
import io.github.rudynakodach.Game.lwjgl.opengl.ShaderProgram;
import io.github.rudynakodach.Game.lwjgl.utils.components.GameComponent;
import io.github.rudynakodach.Game.map.elements.MapElement;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;

import java.awt.*;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window {

    private long hWnd;

    public String windowTitle = "LWJGL Conway's Game of Life";

    private final GameMap map;
    private final Game game;

    public Window(Game game) {
        this.game = game;
        this.map = game.getMap();
    }

    private void init() {
        if(!glfwInit()) {
            throw new RuntimeException("Cannot init GLFW");
        }

        hWnd = glfwCreateWindow(750, 750, windowTitle, 0, 0);
        if(hWnd == 0) {
            throw new RuntimeException("Cannot create window");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        game.getInputManager().registerKeyCallback();
        game.getMouse().registerMouseCallback();

        glfwMakeContextCurrent(hWnd);

        glfwShowWindow(hWnd);
        glfwSwapInterval(1);
    }

    private float offsetX = 0f;
    private float offsetY = 0f;
    private float zoomLevel = 1f;

    public void setZoom(float offX, float offY, float zoomLevel) {
        this.offsetX = offX;
        this.offsetY = offY;
        this.zoomLevel = zoomLevel;
    }

    public float getZoomLevel() {
        return zoomLevel;
    }

    public float getZoomOffsetX() {
        return offsetX;
    }

    public float getZoomOffsetY() {
        return offsetY;
    }

    private void loop() {
        GL.createCapabilities();

        ShaderProgram program = new ShaderProgram();

        program.attachFragmentShader("shaders/fragment.glsl");
        program.attachVertexShader("shaders/vertex.glsl");

        glClearColor(1, 1, 1, 0);

        long latUpdateTime = 0;
        long updateDelayMs = 33;

        while(!glfwWindowShouldClose(hWnd)) {
            glDisable(GL_DEPTH_TEST);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glfwPollEvents();

            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();

            Point windowSize = getWindowSize();


            //todo: make camera zoom work
            float normalizedOffsetX = offsetX / windowSize.x;
            float normalizedOffsetY = offsetY / windowSize.y;

            float zoomFactor = 1.0f / zoomLevel;

            float halfWidth = 0.5f * zoomFactor;
            float halfHeight = 0.5f * zoomFactor;

            float left = normalizedOffsetX - halfWidth + 0.5f;
            float right = normalizedOffsetX + halfWidth - 0.5f;
            float bottom = normalizedOffsetY - halfHeight + 0.5f;
            float top = normalizedOffsetY + halfHeight - 0.5f;

            System.out.println(String.format("Left: %f, Right: %f, Bottom: %f, Top: %f", left, right, bottom, top));

            glOrtho(left, right, bottom, top, -1, 1);

            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();

            if(latUpdateTime + updateDelayMs <= System.currentTimeMillis()) {
                if(game.isSimulationRunning) {
                    map.update();
                }

                latUpdateTime = System.currentTimeMillis();
            }

            render();
            game.getComponents().forEach(GameComponent::update);

            glfwSwapBuffers(hWnd);

            game.getMouse().update();
            game.getInputManager().update();
        }

        program.detach();
        program.cleanup();

        glfwDestroyWindow(hWnd);
    }

    public void run() {
        init();

        loop();
    }

    public long getWindowHandle() {
        return hWnd;
    }

    private void render() {
        float cellWidth = (float) 1 / (map.getMapWidth() * map.getChunkWidth());
        float cellHeight = (float) 1 / (map.getMapHeight() * map.getChunkHeight());

        Chunk[][] chunks = map.getMap();

        for (int cY = 0; cY < map.getMapHeight(); cY++) {
            for (int cX = 0; cX < map.getMapWidth(); cX++) {
                Chunk c = chunks[cY][cX];

                if(c == null) {
                    continue;
                }

                for (int y = 0; y < map.getChunkHeight(); y++) {
                    for (int x = 0; x < map.getChunkWidth(); x++) {
                        if (c.getCell(x, y) != null) {
                            MapElement elem = c.getCell(x, y);

                            int globalX = cX * map.getChunkWidth() + x;
                            int globalY = cY * map.getChunkHeight() + y;

                            float rX = -1.0f + (globalX * cellWidth * 2);
                            float rY = 1.0f - (globalY * cellHeight * 2);

                            if(elem != null) {
                                glColor3f(elem.getR(), elem.getB(), elem.getB());
                            } else {
                                glColor3f(0.0f, 0.0f, 0.0f);
                            }

                            glBegin(GL_QUADS);

                            glVertex2f(rX, rY);
                            glVertex2f(rX + cellWidth * 2, rY);
                            glVertex2f(rX + cellWidth * 2, rY - cellHeight * 2);
                            glVertex2f(rX, rY - cellHeight * 2);

                            glEnd();
                        }
                    }
                }
            }
        }

        renderGrid();
    }

    private void renderGrid() {
        // Get grid size
        float cellWidth = 2.0f / (map.getMapWidth() * map.getChunkWidth());
        float cellHeight = 2.0f / (map.getMapHeight() * map.getChunkHeight());

        glLineWidth(1.0f);

        glBegin(GL_LINES);

        glLineWidth(1f);
        glColor3f(.8f, .8f, .8f);

        // vertical lines
        for (int x = 0; x <= map.getMapWidth() * map.getChunkWidth(); x++) {
            float xPos = -1.0f + x * cellWidth;
            glVertex2f(xPos, 1.0f);
            glVertex2f(xPos, -1.0f);
        }

        // horizontal lines
        for (int y = 0; y <= map.getMapHeight() * map.getChunkHeight(); y++) {
            float yPos = 1.0f - y * cellHeight;
            glVertex2f(-1.0f, yPos);
            glVertex2f(1.0f, yPos);
        }


        for (int cY = 0; cY < map.getMapHeight(); cY++) {
            for (int cX = 0; cX < map.getMapWidth(); cX++) {
                Chunk c = map.getChunkAt(cX, cY);

                if(c == null) {
                    continue;
                }

                float x = -1f + cX * cellWidth * map.getMapWidth();
                float y = 1f - cY * cellHeight * map.getMapHeight();

                glLineWidth(3f);

                //draw updated chunk outline
                if(map.getLastUpdated().contains(c)) {
                    glColor3f(.1f, 1f, .1f);

                    glVertex2f(x, y);
                    glVertex2f(x + cellWidth * map.getChunkWidth(), y);

                    glVertex2f(x + cellWidth * map.getChunkWidth(), y);
                    glVertex2f(x + cellWidth * map.getChunkWidth(), y - cellHeight * map.getChunkHeight());

                    glVertex2f(x + cellWidth * map.getChunkWidth(), y - cellHeight * map.getChunkHeight());
                    glVertex2f(x, y - cellHeight * map.getChunkHeight());

                    glVertex2f(x, y - cellHeight * map.getChunkHeight());
                    glVertex2f(x, y);
                }
            }
        }

        glEnd();
    }

    public Point getWindowSize() {
        IntBuffer windowX = BufferUtils.createIntBuffer(1);
        IntBuffer windowY = BufferUtils.createIntBuffer(1);

        glfwGetWindowSize(hWnd, windowX, windowY);

        return new Point(windowX.get(), windowY.get());
    }
}
