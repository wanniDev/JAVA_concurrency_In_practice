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
public class SynchronizedFactorizer extends GenericServlet implements Servlet {
	// 마지막 결과를 확실하게 캐시하지만, 성능이 현저하게 떨어진다.
	@GuardedBy("this") private BigInteger lastNumber;
	@GuardedBy("this") private BigInteger[] lastFactors;

	@Override
	public synchronized void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		BigInteger i = extractFromRequest(req);
		if (i.equals(lastNumber))
			encodeIntoResponse(res, lastFactors);
		else {
			BigInteger[] factors = factor(i);
			lastNumber = i;
			lastFactors = factors;
			encodeIntoResponse(res, factors);
		}
	}

	void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) {
	}

	BigInteger extractFromRequest(ServletRequest req) {
		return new BigInteger("7");
	}

	BigInteger[] factor(BigInteger i) {
		// Doesn't really factor
		return new BigInteger[] { i };
	}
}
