package me.java.ch2.sub02cancellation_shutdown;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;

public class BrokenPrimeProducer extends Thread {
	private final BlockingQueue<BigInteger> queue;
	private volatile boolean cancelled = false;

	public BrokenPrimeProducer(BlockingQueue<BigInteger> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			BigInteger p = BigInteger.ONE;
			while (!cancelled)
				queue.add(p = p.nextProbablePrime());
		} catch (InterruptedException consumed) {

		}
	}

	public void cancel() {
		cancelled = true;
	}

	void consumePrimes() throws InterruptedException {
		BlockingQueue<BigInteger> primes = ;
		BrokenPrimeProducer producer = new BrokenPrimeProducer(primes);
		producer.start();
		try {
			while (needMorePrimes()) {
				consume(primes.take());
			}
		} finally {
			producer.cancel();
		}
	}
}
