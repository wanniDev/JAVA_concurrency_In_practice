package me.java.ch2.sub01execute;

import static java.util.concurrent.TimeUnit.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

public class RenderWithTimeBudget {
	private static final Ad DEFAULT_AD = new Ad();
	private static final long TIME_BUDGET = 1000;
	private static final ExecutorService exec = Executors.newCachedThreadPool();

	Page renderPageWithAd() throws InterruptedException {
		long endNanos = System.nanoTime() + TIME_BUDGET;
		Future<Ad> f = exec.submit(new FetchAdTask());
		// 광고 가져오는 작업을 등록했으니, 원래 페이지를 작업한다.
		Page page = renderPageBody();
		Ad ad;
		try {
			// 남은 시간 만큼만 대기한다.
			long timeLeft = endNanos - System.nanoTime();
			ad = f.get(timeLeft, NANOSECONDS);
		} catch (ExecutionException e) {
			ad = DEFAULT_AD;
		} catch (TimeoutException e) {
			ad = DEFAULT_AD;
			f.cancel(true);
		}
		page.setAd(ad);
		return page;
	}

	Page renderPageBody() {
		return new Page();
	}

	static class FetchAdTask implements Callable<Ad> {
		public Ad call() {
			return new Ad();
		}
	}

	static class Ad {
	}

	static class Page {
		public void setAd(Ad ad) {

		}
	}
}
