package com.rc.jashua;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class JoystickView extends View {
	// Constants
	public final static long DEFAULT_LOOP_INTERVAL = 100; // 100 ms
    public final static int CENTER = 3;
	public final static int FORWARD = 3;
	public final static int RIGHT = 5;
	public final static int BACKWARD = 7;
	public final static int LEFT = 1;
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
		void onValueChanged(int power, int direction);
	}
}
