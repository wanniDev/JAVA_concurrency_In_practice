package me.java.ch2.sub02cancellation_shutdown.logservice;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

import net.jcip.annotations.GuardedBy;

public abstract class SocketUsingTask<T> implements CancellableTask<T> {
	@GuardedBy("this") private Socket socket;

	protected synchronized void setSocket(Socket s) {
		this.socket = s;
	}

	public synchronized void cancel() {
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (IOException ignored) {}
	}

	@Override
	public RunnableFuture<T> newTask() {
		return new FutureTask<T>(this) {
			@Override
			public boolean cancel(boolean mayInterruptIfRunning) {
				try {
					SocketUsingTask.this.cancel();
				} finally {
					return super.cancel(mayInterruptIfRunning);
				}
			}
		};
	}
}
