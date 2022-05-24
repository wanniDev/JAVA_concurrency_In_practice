package me.java.ch2.sub01execute;

import static java.util.concurrent.TimeUnit.*;

import java.util.Timer;
import java.util.TimerTask;

public class OutOfTime {
	public static void main(String[] args) throws InterruptedException {
		Timer timer = new Timer();
		timer.schedule(new ThrowTask(), 1);
		SECONDS.sleep(1);
		timer.schedule(new ThrowTask(), 1);
		SECONDS.sleep(5);
	}

	static class ThrowTask extends TimerTask {
		@Override
		public void run() {
			throw new RuntimeException();
		}
	}
}
