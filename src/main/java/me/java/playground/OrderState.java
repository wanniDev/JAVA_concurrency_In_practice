package me.java.playground;

public enum OrderState {
	PAYMENT_WAITING {
		public boolean isShippingChangeable() {
			return true;
		}
	},
	PREPARING {
		public boolean isShippingChangeable() {
			return true;
		}
	},
	SHIPPED, DELIVERING, DELIVERY_COMPLETED;

	public boolean isShippingChangeable() {
		return false;
	}

	public static void main(String[] args) {
		System.out.println(OrderState.PAYMENT_WAITING.isShippingChangeable());
	}
}
