package me.java.ch1.sub01threadsafe.notsafe;

import java.io.IOException;
import java.math.BigInteger;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class UnsafeCountingFactorizer extends GenericServlet implements Servlet {
	// 상태 변수를 선언하고, 이 변수를 연산하는 작업을 동기화없이 진행할 경우, thread-safe 하지 못한 상황이 발생할 수 있다.
	private long count = 0;

	public long getCount() {
		return count;
	}

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		BigInteger i = extractFromRequest(req);
		BigInteger[] factors = factor(i);
		++count;
		encodeIntoResponse(res, factors);
	}

	private void encodeIntoResponse(ServletResponse res, BigInteger[] factors) {
	}

	BigInteger extractFromRequest(ServletRequest req) {
		return new BigInteger("7");
	}

	BigInteger[] factor(BigInteger i) {
		return new BigInteger[] {i};
	}
}
