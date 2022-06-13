package me.java.ch4;

/**
 * 선행 조건이 맞지 않으면 그냥 멈춰버리는 버퍼 클래스... 과연.. 좋은걸까
 */
public class GrumpyBoundedBuffer<V> extends BaseBoundedBuffer<V> {
	public GrumpyBoundedBuffer(int capacity) {
		super(capacity);
	}

	public synchronized void put(V v) throws BufferFullException {
		if (isFull())
			throw new BufferFullException();
		doPut(v);
	}

	public synchronized V take() throws BufferEmptyException {
		if (isEmpty())
			throw new BufferEmptyException();
		return doTake();
	}

	static class BufferFullException extends RuntimeException {

	}

	static class BufferEmptyException extends RuntimeException {

	}
}
