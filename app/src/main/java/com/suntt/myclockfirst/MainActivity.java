package com.suntt.myclockfirst;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends Activity {
    private StopWatchView stopWatchView;
private TabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("时钟").setContent(R.id.tab1));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("闹钟").setContent(R.id.tab2));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("计时器").setContent(R.id.tab3));
        tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator("秒表").setContent(R.id.tab4));

stopWatchView = (StopWatchView) findViewById(R.id.tab4);
    }

    @Override
    protected void onDestroy() {
        stopWatchView.OnDestroy();
        super.onDestroy();
    }
}
