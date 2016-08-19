package com.rc.jashua;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class VerticalJoystickView extends JoystickView implements Runnable {
	// Variables
	private Thread thread = new Thread(this);
	private int yPosition = 0; // Touch y position
	private double centerY = 0; // Center view y position
	private Paint mainCircle;
	private Paint secondaryCircle;
	private Paint button;
	private Paint buttonOuter;
	private Paint horizontalLine;
	private Paint verticalLine;
	private int joystickRadius;
	private int buttonRadius;

	public VerticalJoystickView(Context context) {
		super(context);
	}

	public VerticalJoystickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initJoystickView();
	}

	public VerticalJoystickView(Context context, AttributeSet attrs, int defaultStyle) {
		super(context, attrs, defaultStyle);
		initJoystickView();
	}

	protected void initJoystickView() {
		mainCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
		mainCircle.setStrokeWidth(5);
		mainCircle.setColor(Color.RED);
		mainCircle.setStyle(Paint.Style.STROKE);

		secondaryCircle = new Paint();
		secondaryCircle.setColor(Color.RED);
		secondaryCircle.setStyle(Paint.Style.STROKE);

		verticalLine = new Paint();
		verticalLine.setStrokeWidth(5);
		verticalLine.setColor(Color.RED);

		horizontalLine = new Paint();
		horizontalLine.setStrokeWidth(2);
		horizontalLine.setColor(Color.BLACK);

		button = new Paint(Paint.ANTI_ALIAS_FLAG);
		button.setColor(Color.RED);
		button.setStyle(Paint.Style.FILL);

		buttonOuter = new Paint(Paint.ANTI_ALIAS_FLAG);
		buttonOuter.setStrokeWidth(5);
		buttonOuter.setColor(Color.BLACK);
		buttonOuter.setStyle(Paint.Style.STROKE);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}

	@Override
	protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
		super.onSizeChanged(xNew, yNew, xOld, yOld);
		// before measure, get the center of view
		yPosition = getWidth() / 2;
		int d = Math.min(xNew, yNew);
		buttonRadius = (int) (d / 2 * 0.4);
		joystickRadius = (int) (d / 2 * 0.75);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// setting the measured values to resize the view to a certain width and
		// height
		int d = Math.min(measure(widthMeasureSpec), measure(heightMeasureSpec));

		setMeasuredDimension(d, d);

	}

	private int measure(int measureSpec) {
		int result;

		// Decode the measurement specifications.
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.UNSPECIFIED) {
			// Return a default size of 200 if no bounds are specified.
			result = 200;
		} else {
			// As you want to fill the available space
			// always return the full available bounds.
			result = specSize;
		}
		return result;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// super.onDraw(canvas);
        double centerX = (getWidth()) / 2;
		centerY = (getHeight()) / 2;

		// painting the main circle
		canvas.drawCircle((int) centerX, (int) centerY, joystickRadius,
				mainCircle);
		// painting the secondary circle
		canvas.drawCircle((int) centerX, (int) centerY, buttonRadius,
				secondaryCircle);
		// paint lines
		canvas.drawLine((float) centerX, (float) (centerY + joystickRadius), (float) centerX,
				(float) (centerY - joystickRadius), verticalLine);
		canvas.drawLine((float) (centerX - joystickRadius), (float) centerY,
				(float) (centerX + joystickRadius), (float) centerY,
				horizontalLine);

		// painting the move button
		canvas.drawCircle((float) centerX, yPosition, buttonRadius, button);
		canvas.drawCircle((float) centerX, yPosition, buttonRadius, buttonOuter);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		yPosition = (int) event.getY();

        if (yPosition < (centerY - joystickRadius + buttonRadius)) {
            yPosition = (int) (centerY - joystickRadius + buttonRadius);
        } else if (yPosition > (centerY + joystickRadius - buttonRadius)) {
            yPosition = (int) (centerY + joystickRadius - buttonRadius);
        }

		invalidate();

		if (event.getAction() == MotionEvent.ACTION_UP) {
			yPosition = (int) centerY;
			thread.interrupt();
			if (onJoystickMoveListener != null)
				onJoystickMoveListener.onValueChanged(getPower(), getDirection());
		}

		if (onJoystickMoveListener != null
				&& event.getAction() == MotionEvent.ACTION_DOWN) {
			if (thread != null && thread.isAlive()) {
				thread.interrupt();
			}
			thread = new Thread(this);
			thread.start();
			if (onJoystickMoveListener != null)
				onJoystickMoveListener.onValueChanged(getPower(), getDirection());
		}

		return true;
	}

	private int getPower() {
		return (int) (100 * Math.abs(yPosition - centerY) / joystickRadius);
	}

	private int getDirection() {
        if (yPosition < centerY) {
            return FORWARD;
        } else if (yPosition > centerY) {
            return BACKWARD;
        }

        return CENTER;
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			post(new Runnable() {
				public void run() {
					if (onJoystickMoveListener != null)
						onJoystickMoveListener.onValueChanged(getPower(), getDirection());
				}
			});
			try {
				Thread.sleep(loopInterval);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
