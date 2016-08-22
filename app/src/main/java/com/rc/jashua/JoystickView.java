package com.rc.jashua;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class JoystickView extends View {
	// Constants
	public final static long DEFAULT_LOOP_INTERVAL = 100; // 100 ms
	public final static String STOP = "STOP";
	public final static String FORWARD = "FORWARD";
	public final static String RIGHT = "RIGHT";
    public final static String CENTER = "CENTER";
	public final static String BACKWARD = "BACKWARD";
	public final static String LEFT = "LEFT";
	// Variables
	protected OnJoystickMoveListener onJoystickMoveListener; // Listener
	protected long loopInterval = DEFAULT_LOOP_INTERVAL;

	public JoystickView(Context context) {
		super(context);
	}

	public JoystickView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public JoystickView(Context context, AttributeSet attrs, int defaultStyle) {
		super(context, attrs, defaultStyle);
	}

	public void setOnJoystickMoveListener(OnJoystickMoveListener listener,
			long repeatInterval) {
		this.onJoystickMoveListener = listener;
		this.loopInterval = repeatInterval;
	}

	public interface OnJoystickMoveListener {
		void onValueChanged(int power, String direction);
	}
}
