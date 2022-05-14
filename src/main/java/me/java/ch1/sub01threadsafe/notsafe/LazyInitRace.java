package me.java.ch1.sub01threadsafe.notsafe;

import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class LazyInitRace {
	private ExpensiveObject instance = null;

	public ExpensiveObject getInstance() {
		// 최초에 instance를 생성할 경우 동기화를 하지 않는다면,
		// 동시에 접근한 스레드만큼의 새로운 인스턴스가 생성되므로, 본래 의도와는 맞지 않은 상황이 생긴다.
		if (instance == null)
			instance = new ExpensiveObject();
		return instance;
	}
}

class ExpensiveObject {}
