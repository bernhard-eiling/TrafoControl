package com.bernhardeiling.trafoap;

import android.app.ListActivity;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bernhardeiling.trafoap.interfaces.AsyncScanDevices;

import java.util.ArrayList;


public class AccessPointActivity extends ListActivity {

    TextView connectionStatus;
    AccessPoint accessPoint;
    ArrayAdapter adapter;
    ArrayList<String> jacketIPs = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_point);
        connectionStatus = (TextView) findViewById(R.id.connection_status);

        accessPoint = new AccessPoint(this);
        accessPoint.createAccessPoint("TrafoControl", "1234567890");

        adapter = new ArrayAdapter(getApplicationContext(), R.layout.list_item, jacketIPs);
        setListAdapter(adapter);

    }

    AsyncScanDevices scanDevices = new AsyncScanDevices() {
        @Override
        public void onFinishScanningConnectedDevices(ArrayList<String> devices) {
            jacketIPs.clear();
            jacketIPs.addAll(devices);
            adapter.notifyDataSetChanged();
        }
    };

    public void scanDevices(View view) {
        accessPoint.scanForConnectedDevices(scanDevices);
    }

    public void sendData(View view) {

        for (String ip : jacketIPs) {
            accessPoint.sendData(ip, "beepBeepBlink\n");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.access_point, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked
    }
}
