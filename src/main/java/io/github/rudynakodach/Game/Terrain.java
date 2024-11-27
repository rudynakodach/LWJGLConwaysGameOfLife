package io.github.rudynakodach.Game;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public abstract class Terrain {

    private final int width;
    private final int height;
    protected final Cell[][] terrain;

    protected Terrain(int size) {
        this.width = size;
        this.height = size;

        this.terrain = new Cell[height][width];

        for(Cell[] row : terrain) {
            Arrays.fill(row, null);
        }
    }

    protected Terrain(int width, int height) {
        this.width = width;
        this.height = height;

        this.terrain = new Cell[height][width];

        for(Cell[] row : terrain) {
            Arrays.fill(row, null);
        }
    }

    public abstract void update();

    public abstract void invalidate();
    public abstract void validate();

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public @Nullable Cell getCell(int x, int y) {
        return terrain[y][x];
    }

    public abstract void setCell(@Nullable Cell c, int x, int y);

    public Cell[][] getTerrain() {
        return terrain;
    }
}
