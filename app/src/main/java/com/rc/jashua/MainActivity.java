package com.rc.jashua;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://192.168.1.4:3000");
        } catch (URISyntaxException e) {
            Log.e("Jashua", e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setSupportZoom(false);
        webView.setInitialScale(400);

        mSocket.connect();

        HorizontalJoystickView hJoystick = (HorizontalJoystickView) findViewById(R.id.horizontalJoystick);
        VerticalJoystickView vJoystick = (VerticalJoystickView) findViewById(R.id.verticalJoystick);

        hJoystick.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener(){
            @Override
            public void onValueChanged(int power, String direction) {
                String msg = "Power " + power + ". Direction " + direction;
                Log.d("Jashua", msg);
                mSocket.emit(direction);
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);

        vJoystick.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener(){
            @Override
            public void onValueChanged(int power, String direction) {
                String msg = "Power " + power + ". Direction " + direction;
                Log.d("Jashua", msg);
                mSocket.emit(direction, power * 22);
            }
        }, JoystickView.DEFAULT_LOOP_INTERVAL);

        ToggleButton toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    webView.loadUrl("http://192.168.1.4:8080/stream");
                } else {
                    webView.loadUrl("");
                }
            }
        });
    }
}
