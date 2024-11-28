package io.github.rudynakodach.Game;

import org.jetbrains.annotations.Nullable;

import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameMap {
    private final Map<Point, Chunk> map;
    private final Set<Chunk> lastUpdated = new HashSet<>();
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
        lastUpdated.clear();
        lastUpdated.addAll(invalidatedChunks);

        Map<Chunk, Cell[][]> cachedChanges = new HashMap<>();

        for (int cY = 0; cY < mapHeight; cY++) {
            for (int cX = 0; cX < mapWidth; cX++) {
                Chunk c = getChunkAt(cX, cY);

                if(invalidatedChunks.contains(c)) {
                    cachedChanges.put(c, c.tick());
                }
            }
        }

        for(Map.Entry<Chunk, Cell[][]> entry : cachedChanges.entrySet()) {
            Cell[][] cells = entry.getValue();
            Chunk c = entry.getKey();

            for (int y = 0; y < chunkHeight; y++) {
                for (int x = 0; x < chunkWidth; x++) {
                    Cell oldCell = c.getCell(x, y);
                    Cell newCell = cells[y][x];

                    if(oldCell != newCell) {
                        c.setCell(newCell, x, y);
                    }
                }
            }
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

    public Set<Chunk> getLastUpdated() {
        return lastUpdated;
    }

    public void setCellAbsolute(Cell cell, int x, int y) {
        int chunkX = x / chunkWidth;
        int chunkY = y / chunkHeight;

        int posX = x % chunkWidth;
        int posY = y % chunkWidth;

        Chunk c = getChunkAt(chunkX, chunkY);
        if(c != null) {
            c.setCell(cell, posX, posY);
        } else {
            throw new RuntimeException("Cannot find chunk (%d, %d)".formatted(chunkX, chunkY));
        }
    }

    public @Nullable Cell getCellAbsolute(int x, int y) {
        int chunkX = x / chunkWidth;
        int chunkY = y / chunkHeight;

        int posX = x % chunkWidth;
        int posY = y % chunkHeight;

        Chunk c = getChunkAt(chunkX, chunkY);
        if(c != null) {
            return c.getCell(posX, posY);
        }

        return null;
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
