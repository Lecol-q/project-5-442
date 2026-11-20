package game;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class FieldOfViewTest {

    @Test
    public void wallIsVisibleButBlocksViewBehindIt() {
        Tile[][][] tiles = new Tile[7][5][1];
        for (int x = 0; x < 7; x++)
            for (int y = 0; y < 5; y++)
                tiles[x][y][0] = Tile.FLOOR;
        tiles[3][2][0] = Tile.WALL;

        FieldOfView fieldOfView = new FieldOfView(new World(tiles));
        fieldOfView.update(1, 2, 0, 5);

        assertTrue(fieldOfView.isVisible(1, 2, 0));
        assertTrue(fieldOfView.isVisible(3, 2, 0));
        assertFalse(fieldOfView.isVisible(4, 2, 0));
    }

    @Test
    public void respectsCircularRadiusAtDiagonalBoundary() {
        FieldOfView fieldOfView = new FieldOfView(new World(floorTiles(7, 7, 1)));

        fieldOfView.update(3, 3, 0, 3);

        assertTrue(fieldOfView.isVisible(1, 1, 0));
        assertFalse(fieldOfView.isVisible(0, 2, 0));
    }

    @Test
    public void diagonalWallBlocksVisibilityBeyondIt() {
        Tile[][][] tiles = floorTiles(7, 7, 1);
        tiles[3][3][0] = Tile.WALL;
        FieldOfView fieldOfView = new FieldOfView(new World(tiles));

        fieldOfView.update(1, 1, 0, 6);

        assertTrue(fieldOfView.isVisible(3, 3, 0));
        assertFalse(fieldOfView.isVisible(4, 4, 0));
    }

    @Test
    public void updateClearsVisibilityFromPreviousPosition() {
        FieldOfView fieldOfView = new FieldOfView(new World(floorTiles(7, 7, 1)));

        fieldOfView.update(1, 1, 0, 2);
        assertTrue(fieldOfView.isVisible(1, 1, 0));

        fieldOfView.update(5, 5, 0, 2);

        assertTrue(fieldOfView.isVisible(5, 5, 0));
        assertFalse(fieldOfView.isVisible(1, 1, 0));
    }

    @Test
    public void isVisibleRejectsCoordinatesOutsideWorldAndOtherDepths() {
        FieldOfView fieldOfView = new FieldOfView(new World(floorTiles(3, 3, 2)));

        fieldOfView.update(1, 1, 0, 2);

        assertTrue(fieldOfView.isVisible(1, 1, 0));
        assertFalse(fieldOfView.isVisible(-1, 1, 0));
        assertFalse(fieldOfView.isVisible(1, -1, 0));
        assertFalse(fieldOfView.isVisible(3, 1, 0));
        assertFalse(fieldOfView.isVisible(1, 3, 0));
        assertFalse(fieldOfView.isVisible(1, 1, 1));
    }

    private Tile[][][] floorTiles(int width, int height, int depth) {
        Tile[][][] tiles = new Tile[width][height][depth];
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                for (int z = 0; z < depth; z++)
                    tiles[x][y][z] = Tile.FLOOR;
        return tiles;
    }
}
