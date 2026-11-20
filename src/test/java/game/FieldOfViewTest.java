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
}
