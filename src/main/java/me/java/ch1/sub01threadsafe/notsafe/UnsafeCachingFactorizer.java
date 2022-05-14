package me.java.ch1.sub01threadsafe.notsafe;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class UnsafeCachingFactorizer extends GenericServlet implements Servlet {
	// 스레드 안전성의 정의에 따르면 여러 스레드에서 수행되는 작업의 타이밍이나 스케줄링에 따른 교차 실행과 관계 없이
	// 불변조건이 유지돼야 스레드에 안전하다.
	private final AtomicReference<BigInteger> lastNumber = new AtomicReference<>();
	private final AtomicReference<BigInteger[]> lastFactors = new AtomicReference<>();

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		BigInteger i = extractFromRequest(req);
		if (i.equals(lastNumber.get())) {
			encodeIntoResponse(res, lastFactors.get());
		} else {
			BigInteger[] factors = factor(i);
			lastNumber.set(i);
			lastFactors.set(factors);
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
		return new BigInteger[]{i};
	}
}
