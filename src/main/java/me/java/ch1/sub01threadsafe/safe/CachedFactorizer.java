package me.java.ch1.sub01threadsafe.safe;

import java.io.IOException;
import java.math.BigInteger;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class CachedFactorizer extends GenericServlet implements Servlet {
	@GuardedBy("this") private BigInteger lastNumber;
	@GuardedBy("this") private BigInteger[] lastFactors;
	@GuardedBy("this") private long hits;
	@GuardedBy("this") private long cacheHits;

	public synchronized long getHits() {
		return hits;
	}

	public synchronized double getCacheHitRatio() {
		return (double) cacheHits / (double) hits;
	}

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		BigInteger i = extractFromRequest(req);
		BigInteger[] factors = null;
		synchronized (this) {
			++hits;
			if (i.equals(lastNumber)) {
				++cacheHits;
				factors = lastFactors.clone();
			}
		}
		if (factors == null) {
			factors = factor(i);
			synchronized (this) {
				lastNumber = i;
				lastFactors = factors.clone();
			}
		}
		encodeIntoResponse(res, factors);
	}

	void encodeIntoResponse(ServletResponse res, BigInteger[] factors) {
	}

	BigInteger extractFromRequest(ServletRequest req) {
		return new BigInteger("7");
	}

	BigInteger[] factor(BigInteger i) {
		// Doesn't really factor
		return new BigInteger[]{i};
	}
}
