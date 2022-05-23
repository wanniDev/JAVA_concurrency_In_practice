package me.java.ch2.sub01execute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class QuoteTask implements Callable<TravelQuote> {

	private final TravelCompany company;
	private final TravelInfo travelInfo;
	private final ExecutorService exec = Executors.newCachedThreadPool();
	public QuoteTask(TravelCompany company, TravelInfo travelInfo) {
		this.company = company;
		this.travelInfo = travelInfo;
	}

	@Override
	public TravelQuote call() throws Exception {
		return company.solicitQuote(travelInfo);
	}

	public List<TravelQuote> getRankedTravelQuotes(
		TravelInfo travelInfo, Set<TravelCompany> companies,
		Comparator<TravelQuote> ranking, long time, TimeUnit unit) throws InterruptedException {
		List<QuoteTask> tasks = new ArrayList<>();
		for (TravelCompany travelCompany : companies) {
			tasks.add(new QuoteTask(company, travelInfo));
		}

		List<Future<TravelQuote>> futures =
			exec.invokeAll(tasks, time, unit);

		List<TravelQuote> quotes =
			new ArrayList<>(tasks.size());
		Iterator<QuoteTask> iterator = tasks.iterator();
		for (Future<TravelQuote> future : futures) {
			QuoteTask task = iterator.next();
			try {
				quotes.add(future.get());
			} catch (ExecutionException e) {
				quotes.add(task.getFailureQuote(e.getCause()));
			} catch (CancellationException e) {
				quotes.add(task.getTimeoutQuote(e));
			}
		}
		Collections.sort(quotes, ranking);
		return quotes;
	}

	private TravelQuote getTimeoutQuote(CancellationException e) {
		return null;
	}

	private TravelQuote getFailureQuote(Throwable cause) {
		return null;
	}
}

class TravelQuote {

}

class TravelCompany {
	public TravelQuote solicitQuote(TravelInfo travelInfo) {
		return null;
	}
}

class TravelInfo {

}