package me.java.ch3;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import me.java.ch3.testconcurrent.SemaphoreBoundedBuffer;

public class TestBoundedBuffer {

	@Test
	void testIsEmptyWhenConstructed() {
		SemaphoreBoundedBuffer<Integer> bb = new SemaphoreBoundedBuffer<>(10);
		assertTrue(bb.isEmpty());
		assertFalse(bb.isFull());
	}
}
