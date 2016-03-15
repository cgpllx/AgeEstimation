package com.konka.ageestimation.ui.presenter;

public interface IUpdateCallback {
	void transmitResult(boolean result);
	void onFailure();
}
