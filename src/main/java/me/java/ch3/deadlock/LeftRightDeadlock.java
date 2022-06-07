package me.java.ch3.deadlock;

public class LeftRightDeadlock {
	private final Object left = new Object();
	private final Object right = new Object();

	public void leftRight() {
		synchronized (left) {
			synchronized (right) {
				doSomething();
			}
		}
	}

	private void doSomething() {

	}

	private void rightLeft() {
		synchronized (right) {
			synchronized (left) {
				doSomethingElse();
			}
		}
	}

	private void doSomethingElse() {

	}
}
