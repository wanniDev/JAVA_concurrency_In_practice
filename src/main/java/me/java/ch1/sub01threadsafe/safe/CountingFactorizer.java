package me.java.ch1.sub01threadsafe.safe;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class CountingFactorizer extends GenericServlet implements Servlet {
	private final AtomicLong count = new AtomicLong(0);

	public long getCount() {
		return count.get();
	}

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		BigInteger i = extractFromRequest(req);
		BigInteger[] factors = factor(i);
		count.incrementAndGet();
		encodeIntoResponse(res, factors);
	}

	void encodeIntoResponse(ServletResponse res, BigInteger[] factors) {}
	BigInteger extractFromRequest(ServletRequest req) { return null; }
	BigInteger[] factor(BigInteger i) { return null; }
}
