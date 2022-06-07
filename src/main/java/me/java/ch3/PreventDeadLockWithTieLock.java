package me.java.ch3;

import java.util.concurrent.atomic.AtomicInteger;

public class PreventDeadLockWithTieLock {
	private static final Object tieLock = new Object();

	public static void transferMoney(Account fromAccount,
		Account toAccount,
		DollarAmount amount)
		throws InsufficientFundsException {

		class Helper {
			public void transfer() throws InsufficientFundsException {
				if (fromAccount.getBalance().compareTo(amount) < 0)
					throw new InsufficientFundsException();
				else {
					fromAccount.debit(amount);
					toAccount.debit(amount);
				}
			}
		}

		int fromHash = System.identityHashCode(fromAccount);
		int toHash = System.identityHashCode(toAccount);

		if (fromHash < toHash) {
			synchronized (fromAccount) {
				synchronized (toAccount) {
					new Helper().transfer();
				}
			}
		} else if (fromHash > toHash) {
			synchronized (toAccount) {
				synchronized (fromAccount) {
					new Helper().transfer();
				}
			}
		} else {
			synchronized (tieLock) {
				synchronized (fromAccount) {
					synchronized (toAccount) {
						new Helper().transfer();
					}
				}
			}
		}
	}
}

class Account {
	private DollarAmount balance;
	private final int acctNo;
	private static final AtomicInteger sequence = new AtomicInteger();

	public Account() {
		acctNo = sequence.incrementAndGet();
	}

	void debit(DollarAmount d) {
		balance = balance.subtract(d);
	}

	void credit(DollarAmount d) {
		balance = balance.add(d);
	}

	DollarAmount getBalance() {
		return balance;
	}

	int getAcctNo() {
		return acctNo;
	}
}

class DollarAmount implements Comparable<DollarAmount> {
	// Needs implementation

	public DollarAmount(int amount) {
	}

	public DollarAmount add(DollarAmount d) {
		return null;
	}

	public DollarAmount subtract(DollarAmount d) {
		return null;
	}

	public int compareTo(DollarAmount dollarAmount) {
		return 0;
	}
}

class InsufficientFundsException extends Exception {
}
