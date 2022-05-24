package me.java.ch2.sub01execute;


import java.util.logging.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

public class LifecycleWebServer {
	private final ExecutorService exec = Executors.newCachedThreadPool();

	public void start() throws IOException {
		ServerSocket socket = new ServerSocket(80);
		while (!exec.isShutdown()) {
			try {
				final Socket conn = socket.accept();
				exec.execute(new Runnable() {
					@Override
					public void run() {
						handleRequest(conn);
					}
				});
			} catch (RejectedExecutionException e) {
				if (!exec.isShutdown())
					log("task", e);
			}
		}
	}

	public void stop() {
		exec.shutdown();
	}

	private void log(String msg, Exception e) {
		Logger.getAnonymousLogger().log(Level.WARNING, msg, e);
	}

	void handleRequest(Socket connection) {
		Request req = readRequest(connection);
		if (isShutdownRequest(req)) {
			stop();
		} else {
			dispatchRequest(req);
		}
	}

	private void dispatchRequest(Request req) {

	}

	private boolean isShutdownRequest(Request req) {
		return false;
	}

	private Request readRequest(Socket socket) {
		return null;
	}

	interface Request {}
}
