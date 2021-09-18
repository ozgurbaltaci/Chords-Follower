package com.example.autowebscrolling;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private boolean isTouched;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button scrollButton = findViewById(R.id.scrollButton);

        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.google.com.tr/");

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        scrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ScrollView scrollView = findViewById(R.id.scview);

                EditText editText = findViewById(R.id.desiredSpeed);
                double speed = 1;

                try{
                    speed = Double.parseDouble(editText.getText().toString());
                    if(speed > 10 || speed < 0)
                        throw new InvalidIntervalException();

                }catch (Exception e){
                    Toast.makeText(MainActivity.this, "Speed considered 1 because of the invalid blank!", Toast.LENGTH_SHORT).show();
                }


                double finalSpeed = speed;
                scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(scrollView, "scrollY", scrollView.getChildAt(0).getHeight() - scrollView.getHeight());
                        objectAnimator.setDuration((long) ( finalSpeed * 200000));
                        objectAnimator.setInterpolator(new LinearInterpolator());
                        scrollView.scrollTo(0,1300);
                        objectAnimator.start();
                        if(isTouched ) {
                            objectAnimator.removeAllListeners();
                            objectAnimator.end();
                            objectAnimator.cancel();
                        }


                    }
                });
            }
        });
    }



}

class InvalidIntervalException extends RuntimeException{
    String errorMessage = "Please enter a number between 1 and 10 !!";

    public InvalidIntervalException() {
    }

    @Override
    public String toString() {
        return "InvalidIntervalException{" +
                "errorMessage='" + errorMessage + '\'' +
                '}';
    }
}