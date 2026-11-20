package game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class World {
	private Tile[][][] tiles;
	private Item[][][] items;
	
	private int width;
	public int width() { return width; }
	
	private int height;
	public int height() { return height; }

	private int depth;
	public int depth() { return depth; }
	
	private List<Creature> creatures;
	
	public World(Tile[][][] tiles){
		this.tiles = tiles;
		this.width = tiles.length;
		this.height = tiles[0].length;
		this.depth = tiles[0][0].length;
		this.creatures = new ArrayList<Creature>();
		this.items = new Item[width][height][depth];
	}

	public Creature creature(int x, int y, int z){
		for (Creature c : creatures){
			if (c.x == x && c.y == y && c.z == z)
				return c;
		}
		return null;
	}
	
	public Tile tile(int x, int y, int z){
		if (x < 0 || x >= width || y < 0 || y >= height || z < 0 || z >= depth)
			return Tile.BOUNDS;
		else
			return tiles[x][y][z];
	}
	
	public char glyph(int x, int y, int z){
		Creature creature = creature(x, y, z);
		if (creature != null)
			return creature.glyph();
		
		if (item(x,y,z) != null)
			return item(x,y,z).glyph();
		
		return tile(x, y, z).glyph();
	}
	
	public Color color(int x, int y, int z){
		Creature creature = creature(x, y, z);
		if (creature != null)
			return creature.color();
		
		if (item(x,y,z) != null)
			return item(x,y,z).color();
		
		return tile(x, y, z).color();
	}

	public void dig(int x, int y, int z) {
		if (tile(x, y, z).isDiggable())
			tiles[x][y][z] = Tile.FLOOR;
	}
	
	public void addAtEmptyLocation(Creature creature, int z){
		List<Point> locations = groundLocationsAtDepth(z);
		locations.removeIf(point -> creature(point.x, point.y, point.z) != null);
		Point location = chooseEmptyGroundLocation(locations, "creature", z);

		creature.x = location.x;
		creature.y = location.y;
		creature.z = z;
		creatures.add(creature);
	}
	
	public void update(){
		List<Creature> toUpdate = new ArrayList<Creature>(creatures);
		for (Creature creature : toUpdate){
			creature.update();
		}
	}

	public void remove(Creature other) {
		creatures.remove(other);
	}
	
	public void remove(Item item) {
		for (int x = 0; x < width; x++){
			for (int y = 0; y < height; y++){
				for (int z = 0; z < depth; z++){
					if (items[x][y][z] == item) {
						items[x][y][z] = null;
						return;
					}
				}	
			}	
		}
	}
	
	public Item item(int x, int y, int z){
		return items[x][y][z];
	}
	
	public void addAtEmptyLocation(Item item, int depth) {
		List<Point> locations = groundLocationsAtDepth(depth);
		locations.removeIf(point -> item(point.x, point.y, point.z) != null);
		Point location = chooseEmptyGroundLocation(locations, "item", depth);

		items[location.x][location.y][depth] = item;
	}

	private List<Point> groundLocationsAtDepth(int z) {
		if (z < 0 || z >= depth)
			throw new IllegalArgumentException("Depth is outside the world: " + z);

		List<Point> locations = new ArrayList<Point>();
		for (int x = 0; x < width; x++){
			for (int y = 0; y < height; y++){
				if (tile(x, y, z).isGround())
					locations.add(new Point(x, y, z));
			}
		}
		return locations;
	}

	private Point chooseEmptyGroundLocation(List<Point> locations, String entityType, int z) {
		if (locations.isEmpty())
			throw new IllegalStateException("No empty ground location available for " + entityType + " at depth " + z);

		return locations.get(ThreadLocalRandom.current().nextInt(locations.size()));
	}

	public void remove(int x, int y, int z) {
		items[x][y][z] = null;
	}

	public boolean addAtEmptySpace(Item item, int x, int y, int z){
		if (item == null)
			return true;
		
		List<Point> points = new ArrayList<Point>();
		List<Point> checked = new ArrayList<Point>();
		
		points.add(new Point(x, y, z));
		
		while (!points.isEmpty()){
			Point p = points.remove(0);
			checked.add(p);
			
			if (!tile(p.x, p.y, p.z).isGround())
				continue;
				
			if (items[p.x][p.y][p.z] == null){
				items[p.x][p.y][p.z] = item;
				Creature c = this.creature(p.x, p.y, p.z);
				if (c != null)
					c.notify("A %s lands between your feet.", c.nameOf(item));
				return true;
			} else {
				List<Point> neighbors = p.neighbors8();
				neighbors.removeAll(checked);
				points.addAll(neighbors);
			}
		}
		return false;
	}

	public void add(Creature pet) {
		creatures.add(pet);
	}
}
