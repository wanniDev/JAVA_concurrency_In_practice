package me.java.ch1.sub01threadsafe.safe;

import java.io.IOException;
import java.math.BigInteger;

import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class StatelessFactorizer extends GenericServlet implements Servlet {

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		BigInteger i = extractFromRequest(req);
		BigInteger[] factors = factor(i);
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
