package me.java.ch2.sub02cancellation_shutdown;

import java.io.File;
import java.io.FileFilter;
import java.util.concurrent.BlockingQueue;

public class IndexingService {
	private static final File POISON = new File("");
	private final IndexerThread consumer = new IndexingService();
	private final CrawlerThread producer = new CrawlerThread();
	private final BlockingQueue<File> queue;
	private final FileFilter fileFilter;
	private final File root;

	class CrawlerThread extends Thread {}
	class IndexerThread extends Thread {}

	public void start() {
		producer.start();
		consumer.start();
	}

	public void stop() {
		producer.interrupt();
	}

	public void awaitTermination() throws InterruptedException {
		consumer.join();
	}
}
