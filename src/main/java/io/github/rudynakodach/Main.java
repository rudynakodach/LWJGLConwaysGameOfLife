package io.github.rudynakodach;

import io.github.rudynakodach.Game.Cell;
import io.github.rudynakodach.Game.Chunk;
import io.github.rudynakodach.Game.GameMap;

import java.util.List;

import static io.github.rudynakodach.utils.ANSIColors.*;

public class Main {

    public static void main(String[] args) {
        GameMap map = new GameMap(2, 2, 2);
        map.createRectMap();

        map.getChunkAt(0, 0).setCell(new Cell(), 1, 1);
        map.getChunkAt(1, 0).setCell(new Cell(), 0, 0);

        printMap(map);

        List<Cell> cells = map.getChunkAt(0, 0).getAdjacentCells(1, 0);
        System.out.print("\n" + cells.size());
    }

    private static void printMap(GameMap map) {
        for (int cY = 0; cY < map.getMapHeight(); cY++) {
            for (int y = 0; y < map.getChunkHeight(); y++) {
                for (int cX = 0; cX < map.getMapWidth(); cX++) {
                    Chunk c = map.getChunkAt(cX, cY);

                    for (int x = 0; x < map.getChunkWidth(); x++) {
                        Cell cell = c.getCell(x, y);

                        if (cell != null) {
                            System.out.print(BACKGROUND_GREEN + "#");
                        } else {
                            System.out.print(BACKGROUND_RED + ".");
                        }
                    }

                    if(cX != map.getMapWidth() - 1) {
                        System.out.print(BRIGHT_BACKGROUND_WHITE + "|");
                    }
                }
                System.out.println(BRIGHT_BACKGROUND_WHITE);
            }

            System.out.println(BACKGROUND_RED +
                    "-".repeat(map.getChunkWidth() * map.getMapWidth() + map.getMapWidth() - 1) + BRIGHT_BACKGROUND_WHITE);
        }
    }
}