package me.java.ch2.sub02cancellation_shutdown.logservice;

import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

import net.jcip.annotations.GuardedBy;

public class LogService {
	private final BlockingQueue<String> queue;
	private final LoggerThread loggerThread;
	private final PrintWriter writer;
	@GuardedBy("this") private boolean isShutdown;
	@GuardedBy("this") private int reservations;

	public LogService(BlockingQueue<String> queue,
		LoggerThread loggerThread, PrintWriter writer) {
		this.queue = queue;
		this.loggerThread = loggerThread;
		this.writer = writer;
	}

	public void start() {
		loggerThread.start();
	}

	private class LoggerThread extends Thread {
		public void run() {
			try {
				while (true) {
					try {
						synchronized (LogService.this) {
							if (isShutdown && reservations == 0) {
								break;
							}
						}
						String msg = queue.take();
						synchronized (LogService.this) {
							--reservations;
						}
						writer.println(msg);
					} catch (InterruptedException e) {
						/* 재시도 */
					}
				}
			} finally {
				writer.close();
			}
		}
	}
}
