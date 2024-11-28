package io.github.rudynakodach;

import io.github.rudynakodach.Game.Cell;
import io.github.rudynakodach.Game.Chunk;
import io.github.rudynakodach.Game.Game;
import io.github.rudynakodach.Game.GameMap;

import static io.github.rudynakodach.utils.ANSIColors.*;

public class Main {

    public static void main(String[] args) {
        Game g = new Game();

        g.getMap().setCellAbsolute(new Cell(), 2, 2);
        g.getMap().setCellAbsolute(new Cell(), 2, 3);
//        g.getMap().setCellAbsolute(new Cell(), 2, 4);
//        g.getMap().setCellAbsolute(new Cell(), 2, 5);
//        g.getMap().setCellAbsolute(new Cell(), 18, 18);
//        g.getMap().setCellAbsolute(new Cell(), 18, 19);
//        g.getMap().setCellAbsolute(new Cell(), 17, 19);
//        g.getMap().setCellAbsolute(new Cell(), 18, 20);
//        g.getMap().setCellAbsolute(new Cell(), 19, 20);

        g.run();
    }

    private static void printMap(GameMap map) {
        for (int cY = 0; cY < map.getMapHeight(); cY++) {
            for (int y = 0; y < map.getChunkHeight(); y++) {
                for (int cX = 0; cX < map.getMapWidth(); cX++) {
                    Chunk c = map.getChunkAt(cX, cY);

                    for (int x = 0; x < map.getChunkWidth(); x++) {
                        Cell cell = c.getCell(x, y);

                        if (cell != null) {
                            System.out.print(BACKGROUND_GREEN + c.getAdjacentCells(x, y).size());
                        } else {
                            System.out.print(BACKGROUND_RED + c.getAdjacentCells(x, y).size());
                        }
                    }

                    if(cX != map.getMapWidth() - 1) {
                        System.out.print(BRIGHT_BACKGROUND_WHITE + "|");
                    }
                }
                System.out.println(BRIGHT_BACKGROUND_WHITE);
            }

            System.out.println(BRIGHT_BACKGROUND_WHITE +
                    "-".repeat(map.getChunkWidth() * map.getMapWidth() + map.getMapWidth() - 1));
        }
    }
}