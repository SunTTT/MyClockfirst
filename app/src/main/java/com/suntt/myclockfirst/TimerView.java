package com.suntt.myclockfirst;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2015/8/19.
 */
public class TimerView extends LinearLayout {
    private EditText etHour;
    private EditText etMin;
    private EditText etSec;
    private Button btnStart;
    private Button btnPause;
    private Button btnReset;
    private Button btnResume;
    private Timer timer = new Timer();
    private TimerTask timerTask = null;
    private int countTime;
    private static final int TIME_IS_UP = 1;
    private static final int TIME_IS_TICK = 2;


    public TimerView(Context context) {
        super(context);
    }

    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        btnPause = (Button) findViewById(R.id.btnPause);
        btnStart = (Button) findViewById(R.id.btnStart);
        btnReset = (Button) findViewById(R.id.btnReset);
        btnResume = (Button) findViewById(R.id.btnResume);

        etHour = (EditText) findViewById(R.id.etHour);
        etSec = (EditText) findViewById(R.id.etSec);
        etMin = (EditText) findViewById(R.id.etMin);
        etHour.setText("0");
        etMin.setText("0");
        etSec.setText("0");
        etMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence)) {
                    int value = Integer.parseInt(charSequence.toString());

                    if (value > 59) {
                        etMin.setText("59");
                    } else if (value < 0) {
                        etMin.setText("0");
                    }
                }
                checkToEnableBtnStart();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence)) {
                    int value = Integer.parseInt(charSequence.toString());

                    if (value > 59) {
                        etHour.setText("59");
                    } else if (value < 0) {
                        etHour.setText("0");
                    }
                }
                checkToEnableBtnStart();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etSec.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence)) {
                    int value = Integer.parseInt(charSequence.toString());

                    if (value > 59) {
                        etSec.setText("59");
                    } else if (value < 0) {
                        etSec.setText("0");
                    }
                }
                checkToEnableBtnStart();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnPause.setVisibility(GONE);
        btnStart.setVisibility(VISIBLE);
        btnStart.setEnabled(false);
        btnReset.setVisibility(GONE);
        btnResume.setVisibility(GONE);
        btnStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();
                btnStart.setVisibility(GONE);
                btnPause.setVisibility(VISIBLE);
                btnReset.setVisibility(VISIBLE);
            }
        });


        btnPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTimer();
                btnPause.setVisibility(GONE);
                btnResume.setVisibility(VISIBLE);
            }
        });
        btnResume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();
                btnResume.setVisibility(GONE);
                btnPause.setVisibility(VISIBLE);
            }
        });
        btnReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTimer();
                etHour.setText("0");
                etMin.setText("0");
                etSec.setText("0");
                btnReset.setVisibility(GONE);
                btnPause.setVisibility(GONE);
                btnResume.setVisibility(GONE);
                btnStart.setVisibility(VISIBLE);
            }
        });
    }

    private void startTimer() {
        countTime = Integer.parseInt(etHour.getText().toString()) * 60 * 60 + Integer.parseInt(etMin.getText().toString()) * 60 + Integer.parseInt(etSec.getText().toString());

        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    countTime--;
                    if (countTime <= 0) {
                        handler.sendEmptyMessage(TIME_IS_UP);
                        stopTimer();
                    }
                    handler.sendEmptyMessage(TIME_IS_TICK);
                }
            };
        }
        timer.schedule(timerTask, 1000, 1000);

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_IS_UP:
                    new AlertDialog.Builder(getContext()).setTitle("Time is up").setMessage("Time is up").setNegativeButton("cancel", null).show();
                    btnReset.setVisibility(GONE);
                    btnPause.setVisibility(GONE);
                    btnResume.setVisibility(GONE);
                    btnStart.setVisibility(VISIBLE);
                    break;
                case TIME_IS_TICK:
                    etHour.setText((countTime / 60 / 60) + "");
                    etMin.setText((countTime / 60 % 60) + "");
                    etSec.setText((countTime % 60) + "");


                    break;
                default:
                    break;
            }

        }
    };

    private void stopTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    private void checkToEnableBtnStart() {
        btnStart.setEnabled((!TextUtils.isEmpty(etHour.getText()) && Integer.parseInt(etHour.getText().toString()) > 0) ||
                (!TextUtils.isEmpty(etMin.getText()) && Integer.parseInt(etMin.getText().toString()) > 0) ||
                (!TextUtils.isEmpty(etSec.getText()) && Integer.parseInt(etSec.getText().toString()) > 0));
    }
}
