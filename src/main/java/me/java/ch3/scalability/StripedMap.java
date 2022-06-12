package me.java.ch3.scalability;

import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class StripedMap {
	// buckets[n]은 locks[n%N_LOCKS] 락으로 동기화한다.
	private static final int N_LOCKS = 16;
	private final Node[] buckets;
	private final Object[] locks;

	private static class Node {
		Node next;
		Node prev;
		Object key;
		Object value;
	}

	public StripedMap(int numBuckets) {
		buckets = new Node[numBuckets];
		locks = new Object[N_LOCKS];
		for (int i = 0; i < N_LOCKS; i++) {
			locks[i] = new Object();
		}
	}

	private final int hash(Object key) {
		return Math.abs(key.hashCode() % buckets.length);
	}

	public Object get(Object key) {
		int hash = hash(key);
		synchronized (locks[hash % N_LOCKS]) {
			for (Node m = buckets[hash]; m != null; m = m.next)
				if (m.key.equals(key))
					return m.value;
		}
		return null;
	}

	public void clear() {
		for (int i = 0; i < buckets.length; i++) {
			synchronized (locks[i % N_LOCKS]) {
				buckets[i] = null;
			}
		}
	}
}
