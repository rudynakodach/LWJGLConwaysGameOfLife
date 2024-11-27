package io.github.rudynakodach.Game;


import org.jetbrains.annotations.Nullable;

import javax.swing.text.Position;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Chunk extends Terrain {
    public static final int DEFAULT_CHUNK_SIZE = (int) Math.pow(2, 6);

    public final Point chunkPosition;
    private final GameMap map;

    private boolean valid = true;

    public Chunk(Point chunkPosition, GameMap map) {
        super(DEFAULT_CHUNK_SIZE);

        this.chunkPosition = chunkPosition;
        this.map = map;
    }

    public Chunk(Point chunkPosition, int size, GameMap map) {
        super(size);

        this.chunkPosition = chunkPosition;
        this.map = map;
    }

    public Chunk(Point chunkPosition, int width, int height, GameMap map) {
        super(width, height);

        this.chunkPosition = chunkPosition;
        this.map = map;
    }



    @Override
    public void invalidate() {
        map.invalidateChunk(chunkPosition);
        valid = false;
    }

    @Override
    public void validate() {
        map.validateChunk(chunkPosition);
        valid = true;
    }

    @Override
    public void setCell(@Nullable Cell c, int x, int y) {
        invalidate();

        getAdjacentChunks(x, y).forEach(Chunk::invalidate);

        terrain[y][x] = c;
    }

    @Override
    public void update() {
        valid = true;
        validate();

        System.out.println("Updating chunk " + chunkPosition);
    }

    private boolean chunkExists(int cX, int cY) {
        System.out.println("Checking for chunk " + cX + " " + cY);
        return map.getChunkAt(cX, cY) != null;
    }

    public List<Chunk> getAdjacentChunks(int x, int y) {
        List<Chunk> adjacent = new ArrayList<>();

        //y checks
        if(y == 0) {
            if(chunkExists(chunkPosition.x, chunkPosition.y - 1)) {
                adjacent.add(map.getChunkAt(chunkPosition.x, chunkPosition.y - 1));
            }
        } else if(y == map.chunkHeight) {
            if(chunkExists(chunkPosition.x, chunkPosition.y + 1)) {
                adjacent.add(map.getChunkAt(chunkPosition.x, chunkPosition.y + 1));
            }
        }

        //x checks
        if(x == 0) {
            if(chunkExists(chunkPosition.x - 1, chunkPosition.y)) {
                adjacent.add(map.getChunkAt(chunkPosition.x - 1, chunkPosition.y));
            }
        } else if(x == map.chunkWidth) {
            if(chunkExists(chunkPosition.x + 1, chunkPosition.y)) {
                adjacent.add(map.getChunkAt(chunkPosition.x + 1, chunkPosition.y));
            }
        }

        // corner checks
        if(x == 0 && y == 0) {
            if(chunkExists(chunkPosition.x - 1, chunkPosition.y - 1)) {
                adjacent.add(map.getChunkAt(chunkPosition.x - 1, chunkPosition.y - 1));
            }
        } else if(x == map.chunkWidth && y == 0) {
            if(chunkExists(chunkPosition.x + 1, chunkPosition.y - 1)) {
                adjacent.add(map.getChunkAt(chunkPosition.x + 1, chunkPosition.y - 1));
            }
        } else if(x == 0 && y == map.chunkHeight) {
            if(chunkExists(chunkPosition.x - 1, chunkPosition.y + 1)) {
                adjacent.add(map.getChunkAt(chunkPosition.x - 1, chunkPosition.y + 1));
            }
        } else if(x == map.chunkWidth && y == map.chunkHeight) {
            if(chunkExists(chunkPosition.x + 1, chunkPosition.y + 1)) {
                adjacent.add(map.getChunkAt(chunkPosition.x + 1, chunkPosition.y + 1));
            }
        }

        return adjacent;
    }

    public List<Cell> getAdjacentCells(int x, int y) {
        List<Cell> adjacentCells = new ArrayList<>();

        int[][] directions = {
                {-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}
        };

        for (int i = 0; i < directions.length; i++) {
            int[] dir = directions[i];

            int offsetX = dir[0];
            int offsetY = dir[1];

            int translatedX = x + offsetX;
            int translatedY = y + offsetY;

            int chunkX = chunkPosition.x;
            int chunkY = chunkPosition.y;

            if(translatedX < 0) {
                chunkX--;
                translatedX += map.chunkWidth;
            } else if(translatedX >= map.chunkWidth) {
                chunkX++;
                translatedX -= map.chunkWidth;
            }

            if(translatedY < 0) {
                chunkY--;
                translatedY += map.chunkHeight;
            } else if(translatedY >= map.mapHeight) {
                chunkY++;
                translatedY -= map.chunkHeight;
            }

            Chunk c = map.getChunkAt(chunkX, chunkY);
            if (c != null) {
                Cell cell = c.getCell(translatedX, translatedY);

                if(cell != null) {
                    adjacentCells.add(cell);
                }
            }
        }

        return adjacentCells;
    }
}
