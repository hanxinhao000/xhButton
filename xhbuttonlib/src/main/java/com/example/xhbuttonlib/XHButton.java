package com.example.xhbuttonlib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by XINHAO_HAN on 2018/3/30.
 */

public class XHButton extends View {
    private Context context;

    //包裹
    private static int WRAP_CONTENT = -2;

    //全屏
    private static int MATCH_PARENT = -1;

    //控件高度
    private int viewH;

    //控件宽度
    private int viewW;

    private Paint paint;

    private RectF rectF;
    private RectF rectFY;

    private boolean isClick = true;


    private String text = "点击下载";/* 目前阶段对英语的支持不是很好不能正确识别其位置,因为字母的占比是汉子的1/2 */

    private Handler handler;

    private int moveInt = 100;

    private int textSize = 45;

    private int textIndexSize = textSize - 10;

    private int index = 0;

    //透明度
    private int aa = 255;

    //是否透明
    private boolean isAL = false;

    //是否暂停

    private boolean isPuse = false;

    //是否正在运行

    private boolean isRun = false;

    public XHButton(Context context) {
        super(context);
        initView(context);
    }

    public XHButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public XHButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    public void setText(String text) {
        this.text = text;

    }


    private void initView(Context context) {
        this.context = context;
        paint = new Paint();
        paint.setColor(Color.parseColor("#4796FA"));
        paint.setAntiAlias(true);
        rectF = new RectF();
        rectFY = new RectF();
        handler = new Handler();


        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isRun) {
                    // startPro();
                    if (isPuse) {
                        //表示正在运行状态
                        if (xhButtonListener != null) {
                            xhButtonListener.pause();
                        }
                    }


                } else {
                    startAndClose();
                    isPuse = true;
                }

                isRun = true;
            }
        });
    }

    //打开关闭
    private void startAndClose() {

        //除以等分值

        final int i = 255 / 100;
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {


                    if (isClick) {
                        moveInt--;

                    } else {
                        moveInt++;
                    }

                    aa = i * moveInt;
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (moveInt > 100) {
                        isRun = false;
                        isClick = !isClick;
                        break;
                    }


                    int t = (int) ((viewH / 2) * 0.5);
                    int b = (int) ((viewH / 2) * 0.5) + (viewH / 2);


                    int mid = (viewH / 2) - ((viewH / 2) - t);


                    int l = ((viewW / 2) - mid) - moveInt;

                    int r = ((viewW / 2) + mid) + moveInt;

                    if (moveInt <= 0) {
                        isAL = true;
                        isClick = !isClick;
                        rectFY.set(l + 10, t + 10, r - 10, b - 10);
                        startPro();
                        //开始启动另一个线程
                        break;
                    }

                            /*int r = (int) ((viewW / 2) * 0.5) + (viewW / 2);
                            int l = (int) ((viewW / 2) * 0.5);*/


                    rectF.set(l, t, r, b);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            invalidate();
                        }
                    });

                }


            }
        }).start();
    }


    private int position;

    public void setIndex(int index) {

        int i = 400 / 99;

        this.index = index * i;

        position = index;

        handler.post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });


        if (xhButtonListener != null) {
            xhButtonListener.index(index);
        }

        if (position >= 99) {
            if (xhButtonListener != null) {
                xhButtonListener.end();
            }
            isEnd = true;
            isAL = false;
            this.index = 0;
            position = 0;
            startAndClose();

        }

    }


    private boolean isEnd = false;

    public boolean getClose() {

        return isEnd;
    }

    //开始进入进度条

    private void startPro() {
        /**
         * 其实我感觉这两个监听都没有必要,就是给你写了一部分 ^_^
         */

        if (xhButtonListener != null) {
            xhButtonListener.start();
            isEnd = false;
        }







       /* Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    *//**
         * 暂时先替代,后续由使用者处理
         *//*

                    if (!isPuse)
                        index++;

                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            invalidate();
                        }
                    });
                    if (index > 360) {
                        isAL = false;
                        index = 0;
                        startAndClose();
                        break;
                    }


                }
            }
        });

        thread.start();*/

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


    }

    private XHButtonListener xhButtonListener;

    public void setXHButtonListener(XHButtonListener xhButtonListener) {
        this.xhButtonListener = xhButtonListener;
    }


    @SuppressLint("NewApi")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        WindowManager systemService = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        systemService.getDefaultDisplay().getRealMetrics(displayMetrics);


        int width = this.getLayoutParams().width;
        int height = this.getLayoutParams().height;


        if (width == WRAP_CONTENT) {
            viewW = 500;
        }

        if (width == MATCH_PARENT) {
            viewW = displayMetrics.widthPixels;
        }


        if (height == WRAP_CONTENT) {
            viewH = 249;
        }

        if (height == MATCH_PARENT) {
            viewH = displayMetrics.heightPixels;
        }

        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        layoutParams.width = viewW;
        layoutParams.height = viewH;

        if (width != WRAP_CONTENT && width != MATCH_PARENT) {
            layoutParams.width = width;
            viewW = width;

        }
        if (height != WRAP_CONTENT && height != MATCH_PARENT) {
            layoutParams.height = height;
            viewH = height;

        }

        this.setLayoutParams(layoutParams);


        int t = (int) ((viewH / 2) * 0.5);
        int b = (int) ((viewH / 2) * 0.5) + (viewH / 2);


        int mid = (int) ((viewH / 2) - ((viewH / 2) - t));


        int l = ((viewW / 2) - mid) - moveInt;

        int r = ((viewW / 2) + mid) + moveInt;



        /*int r = (int) ((viewW / 2) * 0.5) + (viewW / 2);
        int l = (int) ((viewW / 2) * 0.5);*/


        rectF.set(l, t, r, b);


    }

    //测量自身大小


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setAlpha(255);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#4796FA"));
        // canvas.drawOval(rectF, paint);
        canvas.drawRoundRect(rectF, 100, 100, paint);

        paint.setTextSize(textSize);

        paint.setColor(Color.parseColor("#ffffff"));
        paint.setAlpha(aa);
        canvas.drawText(text, (viewW / 2) - ((text.length() * textSize) / 2), (viewH / 2) + (textSize / 2) - 5, paint);


        if (!isClick) {
            //开始走圆弧
            if (isAL)
                paint.setAlpha(255);
            else
                paint.setAlpha(0);

            paint.setTextSize(textIndexSize);
            paint.setStrokeWidth(10);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawArc(rectFY, 0, index, false, paint);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(1);
            canvas.drawText(position + "%", (viewW / 2) - (textIndexSize / 2) - 5, (viewH / 2) + (textIndexSize / 2) - 5, paint);
        }
    }


    public interface XHButtonListener {

        void start();

        void index(int position);

        void end();

        void pause();
    }

}
