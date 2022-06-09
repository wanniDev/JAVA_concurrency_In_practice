package me.java.ch3.extension;

import java.util.concurrent.BlockingQueue;

public class WorkerThread extends Thread {
	private final BlockingQueue<Runnable> queue;

	public WorkerThread(BlockingQueue<Runnable> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Runnable task = queue.take();
				task.run();
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
