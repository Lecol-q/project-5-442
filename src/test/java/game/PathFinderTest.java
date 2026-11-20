package game;

import java.awt.Color;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class PathFinderTest {

    @Test
    public void findsPathAroundWall() {
        Tile[][][] tiles = floorTiles(5, 5);
        for (int y = 0; y < 4; y++)
            tiles[2][y][0] = Tile.WALL;
        World world = new World(tiles);

        Creature creature = new Creature(world, '@', Color.GRAY, "josh", 100, 10, 5);
        ArrayList<Point> path = new PathFinder().findPath(
                creature, new Point(1, 2, 0), new Point(3, 2, 0), 100);

        assertNotNull(path);
        assertTrue(path.size() > 2);

        Point previous = new Point(1, 2, 0);
        for (Point point : path) {
            assertTrue(world.tile(point.x, point.y, point.z).isGround());
            assertTrue(Math.abs(point.x - previous.x) <= 1);
            assertTrue(Math.abs(point.y - previous.y) <= 1);
            previous = point;
        }
        assertTrue(previous.equals(new Point(3, 2, 0)));
    }

    @Test
    public void returnsNullWhenStartIsSurrounded() {
        Tile[][][] tiles = floorTiles(5, 5);
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (x != 1 || y != 1)
                    tiles[x][y][0] = Tile.WALL;
            }
        }
        World world = new World(tiles);
        Creature creature = new Creature(world, '@', Color.GRAY, "josh", 100, 10, 5);

        ArrayList<Point> path = new PathFinder().findPath(
                creature, new Point(1, 1, 0), new Point(4, 4, 0), 100);

        assertNull(path);
    }

    private Tile[][][] floorTiles(int width, int height) {
        Tile[][][] tiles = new Tile[width][height][1];
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                tiles[x][y][0] = Tile.FLOOR;
        return tiles;
    }
}
