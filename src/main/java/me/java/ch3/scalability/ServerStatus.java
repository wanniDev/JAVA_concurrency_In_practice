package me.java.ch3.scalability;

import java.util.Set;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * 락 분할을 통해, 스레드간의 경쟁 상황을 완화
 */
@ThreadSafe
public class ServerStatus {
	@GuardedBy("this") public final Set<String> users;
	@GuardedBy("this") public final Set<String> queries;

	public ServerStatus(Set<String> users, Set<String> queries) {
		this.users = users;
		this.queries = queries;
	}

	public synchronized void addUser(String u) {
		users.add(u);
	}

	public synchronized void addQuery(String q) {
		queries.add(q);
	}
}
