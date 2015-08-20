package com.suntt.myclockfirst;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Administrator on 2015/8/19.
 */
public class AlarmView extends LinearLayout {
    private ListView listView;
    private Button button;
    private static final String KEY_ALARM_LIST = "alarmlist";
    private AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);



    private ArrayAdapter<AlarmData> adapter;


    public AlarmView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlarmView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AlarmView(Context context) {
        super(context);
//        init();


    }


    private void init() {
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        listView = (ListView) findViewById(R.id.alarmlist);
        button = (Button) findViewById(R.id.alarmbtn);
        adapter = new ArrayAdapter<AlarmView.AlarmData>(getContext(), android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        readSavedAlarmList();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAlarm();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                new AlertDialog.Builder(getContext()).setTitle("Items").setItems(new CharSequence[]{"delete"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        switch (which) {
                            case 0:
                                deleteAlarm(position);
                                break;
                            default:
                                break;
                        }
                    }
                }).setNegativeButton("cancel", null).show();


            }
        });
    }

    private void deleteAlarm(int position) {
        AlarmData ad = adapter.getItem(position);
        adapter.remove(ad);
        saveAlarmList();
        alarmManager.cancel(PendingIntent.getBroadcast(getContext(),ad.getId(),new Intent(getContext(),AlarmReceive.class),0));
    }

    private void addAlarm() {

        Calendar c = Calendar.getInstance();

        new myTimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                Calendar currentTime = Calendar.getInstance();

                if (calendar.getTimeInMillis() <= currentTime.getTimeInMillis()) {
                    calendar.setTimeInMillis(calendar.getTimeInMillis() + 24 * 60 * 60 * 1000);
                }

                AlarmData ad = new AlarmData(calendar.getTimeInMillis());
                adapter.add(ad);
//                alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                        ad.getTime(),
                       0,
                        PendingIntent.getBroadcast(getContext(), ad.getId(), new Intent(getContext(), AlarmReceive.class), 0));
                saveAlarmList();

            }

        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show();


    }

    private void saveAlarmList() {
        SharedPreferences.Editor editor = getContext().getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE).edit();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < adapter.getCount(); i++) {
            sb.append(adapter.getItem(i).getTime()).append(",");
        }
        if (sb.length() > 1) {
            String content = sb.toString().substring(0, sb.length() - 1);
            editor.putString(KEY_ALARM_LIST, content);
        } else {
            editor.putString(KEY_ALARM_LIST, null);
        }
        editor.commit();
    }

    private void readSavedAlarmList() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(AlarmView.class.getName(), Context.MODE_PRIVATE);
        String content = sharedPreferences.getString(KEY_ALARM_LIST, null);
        if (content != null) {
            String[] timeString = content.split(",");
            for (int i = 0; i < timeString.length; i++) {
                adapter.add(new AlarmData(Long.parseLong(timeString[i])));
            }
        }
    }

    private static class AlarmData {
        public AlarmData(long time) {
            this.time = time;

            date = Calendar.getInstance();
            date.setTimeInMillis(time);

            timeLabel = String.format("%d月%d日 %d:%d",
                    date.get(Calendar.MONTH) + 1,
                    date.get(Calendar.DAY_OF_MONTH),
                    date.get(Calendar.HOUR_OF_DAY),
                    date.get(Calendar.MINUTE));
        }

        public long getTime() {
            return time;
        }

        public String getTimeLabel() {
            return timeLabel;
        }

        @Override
        public String toString() {
            return getTimeLabel();
        }

        public int getId() {
            return (int) (getTime() / 1000 / 60);
        }

        private String timeLabel = "";
        private long time = 0;
        private Calendar date;
    }

    public class myTimePickerDialog extends TimePickerDialog {

        public myTimePickerDialog(Context context, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
            super(context, listener, hourOfDay, minute, is24HourView);
        }

        public myTimePickerDialog(Context context, int themeResId, OnTimeSetListener listener, int hourOfDay, int minute, boolean is24HourView) {
            super(context, themeResId, listener, hourOfDay, minute, is24HourView);
        }

        @Override
        protected void onStop() {

        }
    }
}
