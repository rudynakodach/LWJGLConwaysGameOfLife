package io.github.rudynakodach.Game;

import org.jetbrains.annotations.Nullable;

import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameMap {
    private final Map<Point, Chunk> map;
    private final Set<Chunk> invalidatedChunks = new HashSet<>();

    final int mapWidth;
    final int mapHeight;

    final int chunkWidth;
    final int chunkHeight;

    public GameMap(int mapWidth, int mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        this.chunkWidth = Chunk.DEFAULT_CHUNK_SIZE;
        this.chunkHeight = Chunk.DEFAULT_CHUNK_SIZE;

        this.map = new HashMap<>();
    }


    public GameMap(int mapWidth, int mapHeight, int chunkSize) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        this.chunkWidth = chunkSize;
        this.chunkHeight = chunkSize;

        this.map = new HashMap<>();
    }


    public GameMap(int mapWidth, int mapHeight, int chunkWidth, int chunkHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        this.chunkWidth = chunkWidth;
        this.chunkHeight = chunkHeight;

        this.map = new HashMap<>();
    }

    public void validateChunk(Point chunkPosition) {
        Chunk c = map.get(chunkPosition);

        if(c != null) {
            invalidatedChunks.remove(c);
        }
    }

    public void invalidateChunk(Point chunkPosition) {
        Chunk c = map.get(chunkPosition);

        if(c != null) {
            invalidatedChunks.add(c);
        }
    }

    public void update() {
        Chunk[] arr = invalidatedChunks.toArray(new Chunk[0]);

        for (Chunk chunk : arr) {
            chunk.update();
        }
    }

    public void createRectMap() {
        for (int y = 0; y < this.mapHeight; y++) {
            for (int x = 0; x < this.mapWidth; x++) {
                Point pos = new Point(x, y);

                Chunk c = new Chunk(pos, this);
                map.put(pos, c);
            }
        }
    }

    public void createRectMap(int terrainSize) {
        for (int y = 0; y < this.mapHeight; y++) {
            for (int x = 0; x < this.mapWidth; x++) {
                Point pos = new Point(x, y);

                Chunk c = new Chunk(pos, terrainSize, this);
                map.put(pos, c);
            }
        }
    }

    public void createRectMap(int terrainWidth, int terrainHeight) {
        for (int y = 0; y < this.mapHeight; y++) {
            for (int x = 0; x < this.mapWidth; x++) {
                Point pos = new Point(x, y);

                Chunk c = new Chunk(pos, terrainWidth, terrainHeight, this);
                map.put(pos, c);
            }
        }
    }

    public Chunk[][] getMap() {
        Chunk[][] map = new Chunk[this.mapHeight][this.mapWidth];

        for (int y = 0; y < this.mapHeight; y++) {
            for (int x = 0; x < this.mapWidth; x++) {
                map[y][x] = this.map.get(new Point(x, y));
            }
        }

        return map;
    }

    public Chunk getChunkAt(int x, int y) {
        return getChunkAt(new Point(x, y));
    }

    public @Nullable Chunk getChunkAt(Point p) {
        return map.get(p);
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getChunkHeight() {
        return chunkHeight;
    }

    public int getChunkWidth() {
        return chunkWidth;
    }
}
