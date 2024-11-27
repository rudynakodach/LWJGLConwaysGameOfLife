package io.github.rudynakodach.Game.lwjgl;

import io.github.rudynakodach.Game.Chunk;
import io.github.rudynakodach.Game.GameMap;
import io.github.rudynakodach.Game.lwjgl.opengl.ShaderProgram;
import io.github.rudynakodach.Game.lwjgl.utils.window.FPSCounter;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window {

    private long hWnd;

    public String windowTitle = "LWJGL Conway's Game of Life";

    private final GameMap map;

    //----------
    private final FPSCounter counter = new FPSCounter(this);

    public Window(GameMap map) {
        this.map = map;
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

        glfwMakeContextCurrent(hWnd);

        glfwShowWindow(hWnd);
        glfwSwapInterval(1);
    }

    private void loop() {
        GL.createCapabilities();

        ShaderProgram program = new ShaderProgram();

        program.attachFragmentShader("shaders/fragment.glsl");
        program.attachVertexShader("shaders/vertex.glsl");

        glClearColor(1, 1, 1, 0);

        long latUpdateTime = 0;
        long updateDelay = 33;

        while(!glfwWindowShouldClose(hWnd)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            counter.update();

            if(latUpdateTime + updateDelay <= System.currentTimeMillis()) {
                map.update();

                latUpdateTime = System.currentTimeMillis();
            }
            render();

            glfwSwapBuffers(hWnd);
            glfwPollEvents();
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

                for (int y = 0; y < map.getChunkHeight(); y++) {
                    for (int x = 0; x < map.getChunkWidth(); x++) {
                        if (c.getCell(x, y) != null) {
                            int globalX = cX * map.getChunkWidth() + x;
                            int globalY = cY * map.getChunkHeight() + y;

                            float rX = -1.0f + (globalX * cellWidth * 2);
                            float rY = 1.0f - (globalY * cellHeight * 2);

                            glColor3f(0.0f, 0.0f, 0.0f);
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

        glColor3f(0.8f, 0.8f, 0.8f);

        glLineWidth(1.0f);

        glBegin(GL_LINES);

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

        glEnd();
    }

}
