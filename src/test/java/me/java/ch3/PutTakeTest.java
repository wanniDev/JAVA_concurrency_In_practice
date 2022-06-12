package me.java.ch3;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

import me.java.ch3.testconcurrent.SemaphoreBoundedBuffer;

public class PutTakeTest {
	protected static final ExecutorService pool = Executors.newCachedThreadPool();
	protected CyclicBarrier barrier;
	protected final SemaphoreBoundedBuffer<Integer> bb;
	protected final int nTrials, nPairs;
	protected final AtomicInteger putSum = new AtomicInteger(0);
	protected final AtomicInteger takeSum = new AtomicInteger(0);

	public static void main(String[] args) {
		new PutTakeTest(10, 10, 100000).test(); // sample parameters
		pool.shutdown();
	}

	public PutTakeTest(int capacity, int npairs, int ntrials) {
		this.bb = new SemaphoreBoundedBuffer<>(capacity);
		this.nTrials = ntrials;
		this.nPairs = npairs;
		this.barrier = new CyclicBarrier(npairs * 2 + 1);
	}

	void test() {
		try {
			for (int i = 0; i < nPairs; i++) {
				pool.execute(new Producer());
				pool.execute(new Consumer());
			}
			barrier.await(); // 모든 스레드가 준비될 때까지 대기
			barrier.await(); // 모든 스레드의 작업이 끝날 때까지 대기
			assertEquals(putSum.get(), takeSum.get());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	class Producer implements Runnable {
		public void run() {
			try {
				int seed = (this.hashCode() ^ (int) System.nanoTime());
				int sum = 0;
				barrier.await();
				for (int i = nTrials; i > 0; --i) {
					bb.put(seed);
					sum += seed;
					seed = xorShift(seed);
				}
				putSum.getAndAdd(sum);
				barrier.await();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	class Consumer implements Runnable {
		public void run() {
			try {
				barrier.await();
				int sum = 0;
				for (int i = nTrials; i > 0; --i) {
					sum += bb.take();
				}
				takeSum.getAndAdd(sum);
				barrier.await();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	// 테스트용 중간 품질의 난수 발생기
	static int xorShift(int y) {
		y ^= (y << 6);
		y ^= (y >>> 21);
		y ^= (y << 7);
		return y;
	}
}
