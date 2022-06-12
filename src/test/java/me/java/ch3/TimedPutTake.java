package me.java.ch3;

import java.util.concurrent.CyclicBarrier;

import org.junit.jupiter.api.Assertions;

public class TimedPutTake extends PutTakeTest {

	private final BarrierTimer timer = new BarrierTimer();

	public TimedPutTake(int capacity, int npairs, int ntrials) {
		super(capacity, npairs, ntrials);
		barrier = new CyclicBarrier(nPairs * 2 + 1, timer);
	}

	public static void main(String[] args) throws Exception {
		int tpt = 100000; // trials per thread
		for (int cap = 1; cap <= 1000; cap *= 10) {
			System.out.println("Capacity: " + cap);
			for (int pairs = 1; pairs <= 128; pairs *= 2) {
				TimedPutTake t = new TimedPutTake(cap, pairs, tpt);
				System.out.print("Pairs: " + pairs + "\t");
				t.test();
				System.out.print("\t");
				Thread.sleep(1000);
				t.test();
				System.out.println();
				Thread.sleep(1000);
			}
		}
		PutTakeTest.pool.shutdown();
	}

	public void test() {
		try {
			timer.clear();
			for (int i = 0; i < nPairs; i++) {
				pool.execute(new PutTakeTest.Producer());
				pool.execute(new PutTakeTest.Consumer());
			}
			barrier.await();
			barrier.await();
			long nsPerItem = timer.getTime() / (nPairs * (long) nTrials);
			System.out.print("Throughput: " + nsPerItem + " ns/item");
			Assertions.assertEquals(putSum.get(), takeSum.get());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
