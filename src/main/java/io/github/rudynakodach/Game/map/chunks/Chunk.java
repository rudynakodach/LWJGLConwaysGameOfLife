package io.github.rudynakodach.Game.map.chunks;


import io.github.rudynakodach.Game.map.GameMap;
import io.github.rudynakodach.Game.map.elements.immovable.ImmovableElement;
import io.github.rudynakodach.Game.map.elements.movable.Cell;
import io.github.rudynakodach.Game.map.elements.MapElement;
import org.jetbrains.annotations.Nullable;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Chunk extends Terrain {
    public static final int DEFAULT_CHUNK_SIZE = (int) Math.pow(2, 6);

    public final Point chunkPosition;
    private final GameMap map;

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
    }

    @Override
    public void validate() {
        map.validateChunk(chunkPosition);
    }

    @Override
    public void setCell(@Nullable MapElement c, int x, int y) {
        invalidate();

        getAdjacentChunks(x, y).forEach(Chunk::invalidate);

        terrain[y][x] = c;
    }

    @Override
    public MapElement[][] tick() {
        validate();

//        System.out.printf("Updating chunk (%d, %d)%n", chunkPosition.x, chunkPosition.y);

        MapElement[][] newMapElements = new MapElement[map.getChunkHeight()][map.getChunkWidth()];

        for (int y = 0; y < map.getChunkHeight(); y++) {
            for (int x = 0; x < map.getChunkWidth(); x++) {
                MapElement currentMapElement = getCell(x, y);
                if(currentMapElement instanceof ImmovableElement) {
                    newMapElements[y][x] = currentMapElement;
                    continue;
                }

                int liveNeighbors = (int)getAdjacentCells(x, y).stream().filter(elem -> elem instanceof Cell).count();

                if (currentMapElement != null) {
                    newMapElements[y][x] = (liveNeighbors == 2 || liveNeighbors == 3) ? currentMapElement : null;
                } else {
                    newMapElements[y][x] = (liveNeighbors == 3) ? new Cell() : null;
                }
            }
        }

        return newMapElements;
    }

    private boolean chunkExists(int cX, int cY) {
        return map.getChunkAt(cX, cY) != null;
    }

    public List<Chunk> getAdjacentChunks(int x, int y) {
        List<Chunk> adjacent = new ArrayList<>();

        //y checks
        if(y == 0) {
            if(chunkExists(chunkPosition.x, chunkPosition.y - 1)) {
                adjacent.add(map.getChunkAt(chunkPosition.x, chunkPosition.y - 1));
            }
        } else if(y == map.getChunkHeight() - 1) {
            if(chunkExists(chunkPosition.x, chunkPosition.y + 1)) {
                adjacent.add(map.getChunkAt(chunkPosition.x, chunkPosition.y + 1));
            }
        }

        //x checks
        if(x == 0) {
            if(chunkExists(chunkPosition.x - 1, chunkPosition.y)) {
                adjacent.add(map.getChunkAt(chunkPosition.x - 1, chunkPosition.y));
            }
        } else if(x == map.getChunkWidth() - 1) {
            if(chunkExists(chunkPosition.x + 1, chunkPosition.y)) {
                adjacent.add(map.getChunkAt(chunkPosition.x + 1, chunkPosition.y));
            }
        }

        // corner checks
        if(x == 0 && y == 0) {
            if(chunkExists(chunkPosition.x - 1, chunkPosition.y - 1)) {
                adjacent.add(map.getChunkAt(chunkPosition.x - 1, chunkPosition.y - 1));
            }
        } else if(x == map.getChunkWidth() - 1 && y == 0) {
            if(chunkExists(chunkPosition.x + 1, chunkPosition.y - 1)) {
                adjacent.add(map.getChunkAt(chunkPosition.x + 1, chunkPosition.y - 1));
            }
        } else if(x == 0 && y == map.getChunkHeight() - 1) {
            if(chunkExists(chunkPosition.x - 1, chunkPosition.y + 1)) {
                adjacent.add(map.getChunkAt(chunkPosition.x - 1, chunkPosition.y + 1));
            }
        } else if(x == map.getChunkWidth() - 1 && y == map.getChunkHeight() - 1) {
            if(chunkExists(chunkPosition.x + 1, chunkPosition.y + 1)) {
                adjacent.add(map.getChunkAt(chunkPosition.x + 1, chunkPosition.y + 1));
            }
        }

        return adjacent;
    }

    public List<MapElement> getAdjacentCells(int x, int y) {
        List<MapElement> adjacentMapElements = new ArrayList<>();

        int[][] directions = {
                {-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}
        };

        for (int[] dir : directions) {
            int offsetX = dir[0];
            int offsetY = dir[1];

            int translatedX = x + offsetX;
            int translatedY = y + offsetY;

            int chunkX = chunkPosition.x;
            int chunkY = chunkPosition.y;

            if (translatedX < 0) {
                chunkX--;
                translatedX += map.getChunkWidth();
            } else if (translatedX >= map.getChunkWidth()) {
                chunkX++;
                translatedX -= map.getChunkWidth();
            }

            if (translatedY < 0) {
                chunkY--;
                translatedY += map.getChunkHeight();
            } else if (translatedY >= map.getChunkHeight()) {
                chunkY++;
                translatedY -= map.getChunkHeight();
            }

            Chunk c = map.getChunkAt(chunkX, chunkY);
            if (c != null) {
                MapElement mapElement = c.getCell(translatedX, translatedY);

                if (mapElement != null) {
                    adjacentMapElements.add(mapElement);
                }
            }
        }

        return adjacentMapElements;
    }
}
