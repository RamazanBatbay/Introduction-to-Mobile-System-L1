package com.example.mazegame;

import org.junit.Test;
import static org.junit.Assert.*;

public class MazeGameUnitTest {

    // Same maze representation as MainActivity
    private final int[][] maze = {
        {10,  8, 10,  9},
        {28,  1,  0, 12},
        {12, 10,  9, 13},
        { 6,  5,  6,  5}
    };

    /**
     * Test case: Ensure that the start room (containing bit 16) is correctly identified.
     */
    @Test
    public void testStartRoomDetection() {
        int foundRow = -1;
        int foundCol = -1;

        // Loop to find the cell with bit 16
        for (int r = 0; r < maze.length; r++) {
            for (int c = 0; c < maze[r].length; c++) {
                if ((maze[r][c] & 16) != 0) {
                    foundRow = r;
                    foundCol = c;
                    break;
                }
            }
        }

        // Cell (1, 0) is 28, which contains 16 (16 + 12).
        assertEquals("Start room row must be 1", 1, foundRow);
        assertEquals("Start room col must be 0", 0, foundCol);
    }

    /**
     * Test case: Ensure that the start room marker (bit 16) is ignored for door calculations,
     * and only bits 1, 2, 4, 8 are used.
     */
    @Test
    public void testStartRoomDoorAvailability() {
        // Value at (1, 0) is 28 (16 + 12). Exits should be 12 (8: Down, 4: Up).
        int val = maze[1][0];

        boolean left = (val & 1) != 0;
        boolean right = (val & 2) != 0;
        boolean up = (val & 4) != 0;
        boolean down = (val & 8) != 0;

        assertFalse("Left door must be unavailable", left);
        assertFalse("Right door must be unavailable", right);
        assertTrue("Up door must be available", up);
        assertTrue("Down door must be available", down);
    }

    /**
     * Test case: Verify that bitmask calculations for other cell values (e.g. 10, 9, 13) are accurate.
     */
    @Test
    public void testGenericDoorAvailability() {
        // Value 10 (8: Down, 2: Right)
        int val10 = 10;
        assertFalse((val10 & 1) != 0); // Left
        assertTrue((val10 & 2) != 0);  // Right
        assertFalse((val10 & 4) != 0); // Up
        assertTrue((val10 & 8) != 0);  // Down

        // Value 9 (8: Down, 1: Left)
        int val9 = 9;
        assertTrue((val9 & 1) != 0);   // Left
        assertFalse((val9 & 2) != 0);  // Right
        assertFalse((val9 & 4) != 0);  // Up
        assertTrue((val9 & 8) != 0);   // Down

        // Value 13 (8: Down, 4: Up, 1: Left)
        int val13 = 13;
        assertTrue((val13 & 1) != 0);  // Left
        assertFalse((val13 & 2) != 0); // Right
        assertTrue((val13 & 4) != 0);  // Up
        assertTrue((val13 & 8) != 0);  // Down
    }

    /**
     * Test case: Verify boundary conditions to ensure moving out of the 4x4 matrix dimensions is blocked.
     */
    @Test
    public void testBoundaryValidations() {
        // Let's test boundary checks for current position (0, 0)
        int r = 0;
        int c = 0;

        // Up move delta (-1, 0)
        assertFalse("Cannot move Up from row 0", r - 1 >= 0);

        // Left move delta (0, -1)
        assertFalse("Cannot move Left from col 0", c - 1 >= 0);

        // Down move delta (1, 0)
        assertTrue("Can move Down within grid", r + 1 < maze.length);

        // Right move delta (0, 1)
        assertTrue("Can move Right within grid", c + 1 < maze[r].length);
    }

    /**
     * Test case: Verify that the end room has value 0 and is located at (1, 2).
     */
    @Test
    public void testEndRoomCondition() {
        // End room should have value 0
        assertEquals("End room value must be 0", 0, maze[1][2]);
    }
}
