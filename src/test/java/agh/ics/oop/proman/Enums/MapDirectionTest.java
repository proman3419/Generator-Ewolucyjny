package agh.ics.oop.proman.Enums;

import org.junit.jupiter.api.Test;

import static agh.ics.oop.proman.Enums.MapDirection.*;
import static org.junit.jupiter.api.Assertions.*;

class MapDirectionTest {
    @Test
    void next() {
        assertEquals(NORTHEAST, NORTH.next());
        assertEquals(EAST, NORTHEAST.next());
        assertEquals(SOUTHEAST, EAST.next());
        assertEquals(SOUTH, SOUTHEAST.next());
        assertEquals(SOUTHWEST, SOUTH.next());
        assertEquals(WEST, SOUTHWEST.next());
        assertEquals(NORTHWEST, WEST.next());
        assertEquals(NORTH, NORTHWEST.next());
    }

    @Test
    void previous() {
        assertEquals(NORTHWEST, NORTH.previous());
        assertEquals(WEST, NORTHWEST.previous());
        assertEquals(SOUTHWEST, WEST.previous());
        assertEquals(SOUTH, SOUTHWEST.previous());
        assertEquals(SOUTHEAST, SOUTH.previous());
        assertEquals(EAST, SOUTHEAST.previous());
        assertEquals(NORTHEAST, EAST.previous());
        assertEquals(NORTH, NORTHEAST.previous());
    }
}
