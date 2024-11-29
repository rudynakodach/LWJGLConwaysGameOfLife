package io.github.rudynakodach.Game.map.chunks;

import io.github.rudynakodach.Game.map.elements.MapElement;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public abstract class Terrain {

    private final int width;
    private final int height;
    protected MapElement[][] terrain;

    protected Terrain(int size) {
        this.width = size;
        this.height = size;

        this.terrain = new MapElement[height][width];

        for(MapElement[] row : terrain) {
            Arrays.fill(row, null);
        }
    }

    protected Terrain(int width, int height) {
        this.width = width;
        this.height = height;

        this.terrain = new MapElement[height][width];

        for(MapElement[] row : terrain) {
            Arrays.fill(row, null);
        }
    }

    public abstract MapElement[][] tick();

    public abstract void invalidate();

    public abstract void validate();

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public @Nullable MapElement getCell(int x, int y) {
        return terrain[y][x];
    }

    public abstract void setCell(@Nullable MapElement c, int x, int y);

    public MapElement[][] getTerrain() {
        return terrain;
    }
}
