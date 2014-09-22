package com.bernhardeiling.trafoap;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bernhardeiling.trafoap.animation.AnimationController;
import com.bernhardeiling.trafoap.interfaces.ScanDevicesInterface;

import java.util.ArrayList;


public class MainActivity extends ListActivity implements OnItemSelectedListener {

    TextView connectionStatus;
    Spinner animationSpinner;
    AccessPoint accessPoint;
    AnimationController animationController = new AnimationController(this);
    ArrayAdapter devicesAdapter;
    ArrayList<String> devices = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_point);
        connectionStatus = (TextView) findViewById(R.id.connection_status);

        accessPoint = new AccessPoint(this);
        accessPoint.createAccessPoint("TrafoControl", "1234567890");

        devicesAdapter = new ArrayAdapter(getApplicationContext(), R.layout.list_item, devices);
        setListAdapter(devicesAdapter);

        animationSpinner = (Spinner) findViewById(R.id.animation_spinner);
        animationSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> animationAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.animation_array,
                android.R.layout.simple_spinner_item
        );
        animationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        animationSpinner.setAdapter(animationAdapter);
    }

    ScanDevicesInterface scanDevices = new ScanDevicesInterface() {
        @Override
        public void onFinishScanningConnectedDevices(ArrayList<String> scan) {
            devices.clear();
            devices.addAll(scan);
            devicesAdapter.notifyDataSetChanged();
            animationController.setDevices(devices);
        }
    };

    public void scanDevices(View view) {
        accessPoint.scanForConnectedDevices(scanDevices);
    }

    public void toggleAnimation(View view) {
        if (((ToggleButton) view).isChecked()) {
            animationController.setSyncAnimation(true);
        } else {
            animationController.setSyncAnimation(false);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        animationController.setAnimation((String)parent.getItemAtPosition(pos));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.access_point, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {}
}
