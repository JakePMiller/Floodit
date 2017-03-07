import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * JUnit tests for all TODO methods.
 */

public class Testing {
	/**
	 * Constructs a square game board of the given size, initializes the list of 
	 * inside tiles to include just the tile in the upper left corner, and puts 
	 * all the other tiles in the outside list.
	 */

	@Test
	public void testOnBoard() {

		/**
		 * These are just some simple variables for testing purposes
		 */
		Coord testCoord1 = new Coord(5, 3);
		Coord testCoord2 = new Coord(10, 10);
		Board someBoard = new Board(2);
		System.out.println(someBoard);

		/**
		 * These are the tests for the onBoard function in the Coord class.
		 */
		assertFalse(new Coord(3, 4).onBoard(4));
		assertTrue(new Coord(3, 4).onBoard(5));
		assertTrue(new Coord(200, 20).onBoard(500));

		/**
		 * These are the tests for the neighbors function in the Coord class.
		 */
		String neighstr1 = testCoord1.neighbors(6) + "";
		String neighstra1 = "[(4, 3), (5, 2), (5, 4)]";
		assertEquals(neighstra1, neighstr1);

		String neighstr2 = testCoord2.neighbors(15) + "";
		String neighstra2 = "[(11, 10), (9, 10), (10, 9), (10, 11)]";
		assertEquals(neighstra2, neighstr2);

		/**
		 * These are the tests for the hashCode function in the Coord class.
		 */
		assertEquals(testCoord1.hashCode(), 24);
		assertEquals(testCoord2.hashCode(), 161);

		assertTrue(testCoord1.hashCode() != 99);
		assertTrue(testCoord2.hashCode() != -1);


		/**
		 * These are the tests for the flood function in the Board class.
		 */
		Board testb1 = new Board(2);
		testb1.flood(WaterColor.RED);
		for(Tile t : testb1.insideMap().values()){
			assert t.getColor() == WaterColor.RED;
		}

		Board testb2 = new Board(100);
		testb2.flood(WaterColor.CYAN);
		for(Tile t : testb2.insideMap().values()){
			assert t.getColor() == WaterColor.CYAN;
		}

		/**
		 * These are the tests for the flood1 function in the Board class.
		 */
		testb1.flood1(WaterColor.RED);
		for(Tile t : testb1.insideMap().values()){
			assert t.getColor() == WaterColor.RED;
		}

		testb2.flood1(WaterColor.CYAN);
		for(Tile t : testb2.insideMap().values()){
			assert t.getColor() == WaterColor.CYAN;
		}

		/**
		 * These are the tests for the suggest function in the Board class.
		 */
		Board testb3 = new Board();
		assert (WaterColor.RED == testb3.suggest());
		assertTrue(WaterColor.BLUE != testb3.suggest());
		assertFalse(WaterColor.CYAN == testb3.suggest());
		System.out.println(testb3);


	}

}
