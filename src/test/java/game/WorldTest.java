package game;

import java.awt.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class WorldTest {

    @Test
    public void placesCreatureOnOnlyAvailableGroundTile() {
        Tile[][][] tiles = {{{Tile.WALL}}, {{Tile.FLOOR}}};
        World world = new World(tiles);
        Creature creature = new Creature(world, 'c', Color.GRAY, "creature", 10, 1, 1);

        world.addAtEmptyLocation(creature, 0);

        assertEquals(1, creature.x);
        assertEquals(0, creature.y);
        assertEquals(creature, world.creature(1, 0, 0));
    }

    @Test
    public void throwsWhenNoGroundLocationIsAvailableForCreature() {
        World world = new World(new Tile[][][] {{{Tile.WALL}}});
        Creature creature = new Creature(world, 'c', Color.GRAY, "creature", 10, 1, 1);

        assertThrows(IllegalStateException.class, () -> world.addAtEmptyLocation(creature, 0));
    }

    @Test
    public void throwsWhenAllGroundLocationsContainCreatures() {
        World world = new World(new Tile[][][] {{{Tile.FLOOR}}});
        Creature existing = new Creature(world, 'e', Color.GRAY, "existing", 10, 1, 1);
        Creature next = new Creature(world, 'n', Color.GRAY, "next", 10, 1, 1);
        world.addAtEmptyLocation(existing, 0);

        assertThrows(IllegalStateException.class, () -> world.addAtEmptyLocation(next, 0));
    }

    @Test
    public void throwsWhenNoGroundLocationIsAvailableForItem() {
        World world = new World(new Tile[][][] {{{Tile.WALL}}});
        Item item = new Item('i', Color.WHITE, "item", null);

        assertThrows(IllegalStateException.class, () -> world.addAtEmptyLocation(item, 0));
    }

    @Test
    public void throwsWhenAllGroundLocationsContainItems() {
        World world = new World(new Tile[][][] {{{Tile.FLOOR}}});
        Item existing = new Item('e', Color.WHITE, "existing", null);
        Item next = new Item('n', Color.WHITE, "next", null);
        world.addAtEmptyLocation(existing, 0);

        assertThrows(IllegalStateException.class, () -> world.addAtEmptyLocation(next, 0));
    }

    @Test
    public void throwsWhenDepthIsOutsideWorld() {
        World world = new World(new Tile[][][] {{{Tile.FLOOR}}});
        Item item = new Item('i', Color.WHITE, "item", null);

        assertThrows(IllegalArgumentException.class, () -> world.addAtEmptyLocation(item, 1));
    }
}
