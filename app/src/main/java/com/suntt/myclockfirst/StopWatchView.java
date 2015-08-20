package com.suntt.myclockfirst;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2015/8/20.
 */
public class StopWatchView extends LinearLayout {
    private Timer timer = new Timer();
    private TimerTask timerTask= null;
    private TimerTask showTimerTask= null;
    private Button btnswStart, btnswPause, btnswReset, btnswResume, btnswLap;
    private ListView listView;
    private int intswMsec = 0;
    private TextView swHour, swMin, swSec, swMSec;
    private ArrayAdapter<String> adapter;
    private Handler handler = null;
    private static final int TIME_RUN = 1;

    public StopWatchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        btnswStart = (Button) findViewById(R.id.btnswStart);
        btnswPause = (Button) findViewById(R.id.btnswPause);
        btnswReset = (Button) findViewById(R.id.btnswReset);
        btnswResume = (Button) findViewById(R.id.btnswResume);
        btnswLap = (Button) findViewById(R.id.btnswLap);
        swHour = (TextView) findViewById(R.id.swHour);
        swHour.setText("0");
        swMin = (TextView) findViewById(R.id.swMin);
        swMin.setText("0");
        swSec = (TextView) findViewById(R.id.swSec);
        swMSec = (TextView) findViewById(R.id.swMSec);
        swSec.setText("0");
        swMSec.setText("0");
        listView = (ListView) findViewById(R.id.lvStopWatch);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        btnswLap.setVisibility(GONE);
        btnswResume.setVisibility(GONE);
        btnswReset.setVisibility(GONE);
        btnswPause.setVisibility(GONE);
        btnswStart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();
                btnswStart.setVisibility(GONE);
                btnswPause.setVisibility(VISIBLE);
                btnswReset.setVisibility(GONE);
                btnswLap.setVisibility(VISIBLE);
            }
        });
        btnswPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTimer();
                btnswPause.setVisibility(GONE);
                btnswResume.setVisibility(VISIBLE);
                btnswLap.setVisibility(GONE);
                btnswReset.setVisibility(VISIBLE);
            }
        });
        btnswReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTimer();
                intswMsec = 0;
                adapter.clear();
                btnswPause.setVisibility(GONE);
                btnswReset.setVisibility(GONE);
                btnswResume.setVisibility(GONE);
                btnswLap.setVisibility(GONE);
                btnswStart.setVisibility(VISIBLE);
            }
        });
        btnswLap.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.add(String.format("%d:%d:%d.%d", intswMsec / 100 / 60 / 60, intswMsec / 100 / 60 % 60, intswMsec / 100 % 60, intswMsec % 100));
                adapter.notifyDataSetChanged();
            }
        });
        btnswResume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();
                btnswReset.setVisibility(GONE);
                btnswResume.setVisibility(GONE);
                btnswPause.setVisibility(VISIBLE);
                btnswLap.setVisibility(VISIBLE);
            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case TIME_RUN:
                        swHour.setText(intswMsec / 100 / 60 / 60 + "");
                        swMin.setText(intswMsec / 100 / 60 % 60 + "");
                        swSec.setText(intswMsec/100%60+"");
                        swMSec.setText(intswMsec%100+"");
                        break;
                    default:
                        break;
                }
            }
        };
        showTimerTask = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(TIME_RUN);
            }

        };
        timer.schedule(showTimerTask, 200, 200);
    }

    private void startTimer() {
        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    intswMsec++;
                }
            };
            timer.schedule(timerTask, 10, 10);
        }
    }
    private void stopTimer(){
        if (timerTask!=null){
            timerTask.cancel();
            timerTask = null;
        }
    }
    public void OnDestroy(){
        timer.cancel();
    }
}
