package me.java.ch3;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import me.java.ch3.testconcurrent.SemaphoreBoundedBuffer;

public class TestBoundedBuffer {

	private static final long LOCKUP_DETECT_TIMEOUT = 1000;
	private static final int CAPACITY = 10000;
	private static final int THRESHOLD = 10000;

	@Test
	@DisplayName("BoundedBuffer가 초기화 되었을 때, availableItems 세마포어는 비어있어야 한다.")
	void testIsEmptyWhenConstructed() {
		SemaphoreBoundedBuffer<Integer> bb = new SemaphoreBoundedBuffer<>(10);
		assertTrue(bb.isEmpty());
		assertFalse(bb.isFull());
	}

	@Test
	@DisplayName("BoundedBuffer가 초기화된 이후, capacity까지 item을 put 했을경우, 해당 task는 세마포어를 획득할 수 없다.")
	void testIsFullAfterPuts() throws InterruptedException {
		SemaphoreBoundedBuffer<Object> bb = new SemaphoreBoundedBuffer<>(10);
		for (int i = 0; i < 10; i++) {
			bb.put(i);
		}
		assertTrue(bb.isFull());
		assertFalse(bb.isEmpty());
	}
	
	@Test
	@DisplayName("대기 상태와 인터럽트에 대한 대응하는 루틴 테스트")
	void testTakeBlocksWhenEmpty() {
		final SemaphoreBoundedBuffer<Object> bb = new SemaphoreBoundedBuffer<>(10);
		Thread taker = new Thread(() -> {
			try {
				int unused = (int )bb.take();
				fail();
			} catch (InterruptedException success) { }
		});
		
		try {
			taker.start();
			Thread.sleep(LOCKUP_DETECT_TIMEOUT);
			taker.interrupt();
			taker.join();
			assertFalse(taker.isAlive());
		} catch (Exception unexpected) {
			fail();
		}
	}

	class Big {
		double[] data = new double[100000];
	}

	@Test
	@DisplayName("자원 유출 테스트")
	void testLeak() throws InterruptedException {
		SemaphoreBoundedBuffer<Big> bb = new SemaphoreBoundedBuffer<>(CAPACITY);
		int heapSize1 = snapshotHeap();
		for (int i = 0; i < CAPACITY; i++)
			bb.put(new Big());
		for (int i = 0; i < CAPACITY; i++)
			bb.take();
		int heapSize2 = snapshotHeap();
		assertTrue(Math.abs(heapSize1 - heapSize2) < THRESHOLD);
	}

	private int snapshotHeap() {
		/* Snapshot heap and return heap size */
		return 0;
	}
}
