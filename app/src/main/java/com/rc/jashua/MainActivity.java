package com.rc.jashua;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        HorizontalJoystickView hJoystick = (HorizontalJoystickView) findViewById(R.id.horizontalJoystick);
        VerticalJoystickView vJoystick = (VerticalJoystickView) findViewById(R.id.verticalJoystick);

        hJoystick.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener(){
            @Override
            public void onValueChanged(int power, int direction) {
                String msg = "Power " + power + ". Direction " + direction;
                Log.d("Jashua", msg);
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);

        vJoystick.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener(){
            @Override
            public void onValueChanged(int power, int direction) {
                String msg = "Power " + power + ". Direction " + direction;
                Log.d("Jashua", msg);
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setSupportZoom(false);
        webView.setInitialScale(300);
        webView.loadUrl("http://192.168.1.5:8080/stream/video.mjpeg");
    }
}
