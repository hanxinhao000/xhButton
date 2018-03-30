package com.example.xinhao_han.xhview;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.xhbuttonlib.XHButton;

public class MainActivity extends AppCompatActivity {


    private XHButton xhButton;
    private Handler handler;

    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        xhButton = findViewById(R.id.xhButton);
        xhButton.setXHButtonListener(new XHButton.XHButtonListener() {
            @Override
            public void start() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {

                            //模拟数据下载
                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            xhButton.setIndex(index);

                            index++;
                            if (xhButton.getClose()) {
                                break;
                            }

                        }
                    }
                }).start();
            }

            @Override
            public void index(int position) {

            }


            @Override
            public void end() {
                index = 0;
                Log.e("---", "end: " + "结束了" );

            }
            /**
             * 该方法不准确,建议不要使用
             */
            @Override
            public void pause() {
                Log.e("---", "end: " + "运行状态" );
            }
        });
    }
}
