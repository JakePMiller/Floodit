import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sun.reflect.generics.tree.FormalTypeParameter;
import java.lang.Math;
/**
 * A Board represents the current state of the game. Boards know their dimension, 
 * the collection of tiles that are inside the current flooded region, and those tiles 
 * that are on the outside.
 * 
 * @author <Jacob Miller>
 */

public class Board {
	private Map<Coord, Tile> inside, outside;
	private int size;

	/**
	 * Constructs a square game board of the given size, initializes the list of 
	 * inside tiles to include just the tile in the upper left corner, and puts 
	 * all the other tiles in the outside list.
	 */
	public Board(int size) {
		// A tile is either inside or outside the current flooded region.
		inside = new HashMap<>();
		outside = new HashMap<>();
		this.size = size;
		for (int y = 0; y < size; y++)
			for (int x = 0; x < size; x++) {
				Coord coord = new Coord(x, y);
				outside.put(coord, new Tile(coord));
			}
		// Move the corner tile into the flooded region and run flood on its color.
		Tile corner = outside.remove(Coord.ORIGIN);
		inside.put(Coord.ORIGIN, corner);
		flood(corner.getColor());
	}
	
	/**
	 * This is a constructor only for testing purposes. 
	 */ 
	public Board(){
		this.size = 10;
		inside = new HashMap<>();
		outside = new HashMap<>();
		
		for (int y = 0; y < size; y++)
			for (int x = 0; x < size; x++) {
				Coord coord = new Coord(x, y);
				outside.put(coord, new Tile(coord, WaterColor.RED));
			}
		Tile corner = new Tile(outside.remove(Coord.ORIGIN).getCoord(), WaterColor.YELLOW);
		inside.put(Coord.ORIGIN, corner);
		flood(corner.getColor());
	}

	/**
	 * Returns the tile at the specified coordinate.
	 */ 
	public Tile get(Coord coord) {
		if (outside.containsKey(coord))
			return outside.get(coord);
		return inside.get(coord);
	}
	
	/**
	 * This function simply returns the inside HashMap.
	 * I wrote this in order to test my flood function with more ease.
	 * 
	 * @return the inside HashMap
	 */
	public Map<Coord, Tile> insideMap(){
		return inside;
	}

	/**
	 * Returns the size of this board.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * This simple function checks the HashMap outside to see if it is empty.
	 * This returns a boolean true if the HashMap is empty.
	 * 
	 * @return boolean
	 */

	public boolean fullyFlooded() {
		if(outside.isEmpty()){
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * This function moves coordinates whom match the color of the
	 * selected color and are neighbors to the flooded section into the inside HashMap. 
	 * Then we remove the coordinate from the outside HashMap. 
	 * In this function we also change to color of the tiles on the inside to the curent color.
	 * 
	 * @param color is the chosen color
	 */
	public void flood(WaterColor color) {
		List<Coord> floodin = new ArrayList<Coord>();

		for(Map.Entry<Coord, Tile> iter: inside.entrySet()){
			floodin.addAll(iter.getKey().neighbors(size));
			iter.getValue().color = color;
		}

		for(Coord cord: floodin){
			if(outside.containsKey(cord) && outside.get(cord).getColor() == color){
				inside.put(cord, outside.remove(cord));
				floodhelp(cord, color);
			}
		}
	}

	/**
	 * This function is a helper function for flood. This iterates through the coordinates.
	 * If the function finds a suitable coordinate candidate, it will do just as the flood function
	 * and remove it from outside and place it in the inside HashMap.  
	 * 
	 * @param coor is the selected coordinate
	 * @param color is the chosen color
	 */
	public void floodhelp(Coord coor, WaterColor color){
		for(Coord crd : coor.neighbors(size)){
			if(outside.containsKey(crd) && outside.get(crd).getColor() == color){
				inside.put(crd, outside.remove(crd));
				floodhelp(crd, color);
			}
		}
	}




	/**
	 * This function operates just as the other, more successful flood function, but without the helper function.
	 * This function parses through the neighbor coordinates and moves the candidate coordinates into the inside HashMap from
	 * the HashMap function. The function then changes the color of everything in the inside HashMap into the current color.
	 * 
	 * I abandoned keeping everything as one function because of the absolutely horrendous run time.
	 * By making the new flood function have a helper function, we cut the time from O(n^2) to what I believe is O(n).
	 * 
	 * @param color
	 */
	public void flood1(WaterColor color) {
		List<Coord> floodin = new ArrayList<Coord>();

		for(Map.Entry<Coord, Tile> iter: inside.entrySet()){
			floodin.addAll(iter.getKey().neighbors(size));
			iter.getValue().color = color;
		}

		for(Coord cord: floodin){
			if(outside.containsKey(cord) && outside.get(cord).getColor() == color){
				inside.put(cord, outside.remove(cord));
				for(Coord cord1: cord.neighbors(size)){
					if(outside.containsKey(cord1) && outside.get(cord1).getColor() == color){
						inside.put(cord1, outside.remove(cord1));
						System.out.println(outside);
					}
				}
			}
		}
	}


	//  public void flood2(WaterColor color) {
	//
	//  }



	/**
	 * This function takes in the finds the neighbors of a coordinate and adds up a counter dependent on what is the 
	 * highest frequency color that occurs in insides neighbors. This then outputs the color with highest count.
	 * 
	 * I used this method it will cover the largest area possible, sacrificing going along the perimeter.
	 * Using this method, I have never lost following the suggestions all of the way through. 
	 * This method will give us the maximum amount of tiles in the floodit region.
	 * I believe that this is the most effective because we can reach more tiles, so we have the potential
	 * to put more tiles into the inside HashMap, expanding our reach until we complete the game.
	 * 
	 * @return a WaterColor that will give the user the best outcome 
	 */
	public WaterColor suggest() {
		List<Coord> neighs = new ArrayList<Coord>();

		//	WaterColor suggestedColor = inside.get(Coord.ORIGIN).getColor();
		int blueC = 0;
		int cyanC = 0;
		int redC = 0;
		int pinkC = 0;
		int yellowC = 0;

		for(Map.Entry<Coord, Tile> iter: inside.entrySet()){
			neighs.addAll(iter.getKey().neighbors(size));
			//List<Coord> listof = iter.getKey().neighbors(size);
			for(int i = 0; i <= neighs.size()-1; i++){
				Coord temp = neighs.get(i);

				if(get(temp).getColor() == WaterColor.BLUE && outside.containsKey(temp)){
					blueC++;
				}

				if(get(temp).getColor() == WaterColor.CYAN && outside.containsKey(temp)){
					cyanC++;
				}

				if(get(temp).getColor() == WaterColor.RED && outside.containsKey(temp)){
					redC++;
				}

				if(get(temp).getColor() == WaterColor.PINK && outside.containsKey(temp)){
					pinkC++;
				}

				if(get(temp).getColor() == WaterColor.YELLOW && outside.containsKey(temp)){
					yellowC++;
				}
			}
		}

		if(blueC >= cyanC && blueC >= redC && blueC >= pinkC && blueC >= yellowC)
			return WaterColor.BLUE;

		if(cyanC >= blueC && cyanC >= redC && cyanC >= pinkC && cyanC >= yellowC)
			return WaterColor.CYAN;

		if(redC >= blueC && redC >= cyanC && redC >= pinkC && redC >= yellowC)
			return WaterColor.RED;

		if(pinkC >= blueC && pinkC >= cyanC && pinkC >= redC && pinkC >= yellowC)
			return WaterColor.PINK;


		return WaterColor.YELLOW;

	}


	/**
	 * Returns a string representation of this board. Tiles are given as their
	 * color names, with those inside the flooded region written in uppercase.
	 */ 
	public String toString() {
		StringBuilder ans = new StringBuilder();
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				Coord curr = new Coord(x, y);
				WaterColor color = get(curr).getColor();
				ans.append(inside.containsKey(curr) ? color.toString().toUpperCase() : color);
				ans.append("\t");
			}
			ans.append("\n");
		}
		return ans.toString();
	}

	/**
	 * Simple testing.
	 */
	public static void main(String... args) {
		// Print out boards of size 1, 2, ..., 5
		int n = 5;
		for (int size = 1; size <= n; size++) {
			Board someBoard = new Board(size);
			System.out.println(someBoard);
		}
	}
}






