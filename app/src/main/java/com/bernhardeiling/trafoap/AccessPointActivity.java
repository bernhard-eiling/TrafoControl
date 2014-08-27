package com.bernhardeiling.trafoap;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.net.InetAddress;
import java.util.ArrayList;


public class AccessPointActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    TextView connectionStatus;
    AccessPoint accessPoint;
    ArrayAdapter adapter;
    String[] JACKETS = new String[] {"Wolfjacke", "Rabenjacke", "The HOFF", "asdw", "www", "foo", "bar", "so", "much", "more"};
    ArrayList<String> jackets = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_point);
        connectionStatus = (TextView) findViewById(R.id.connection_status);

        ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleSmall);
        //ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setIndeterminate(true);
        getListView().setEmptyView(progressBar);
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        root.addView(progressBar);

        jackets = new ArrayList<String>();
        for (String jacket : JACKETS) {
            jackets.add(jacket);
        }

        adapter = new ArrayAdapter(getApplicationContext(), R.layout.list_item, jackets);
        setListAdapter(adapter);

    }

    public void openAP(View view) {
        accessPoint = new AccessPoint(this);
        accessPoint.createAccessPoint("TrafoControl", "123");
        accessPoint.getConnectedDevices();
        jackets.clear();
        jackets.addAll(accessPoint.getConnectedDevices());
        adapter.notifyDataSetChanged();
    }

    public void sendData(View view) {
        if (true) {
        //if (accessPoint != null && accessPoint.isConnected()) {
            SendDataTask sendDataTask = (SendDataTask) new SendDataTask(this);
            sendDataTask.setMessage("test 123");
            sendDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

    // Called when a new Loader needs to be created
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        /*
        return new CursorLoader(this, ContactsContract.Data.CONTENT_URI,
                PROJECTION, SELECTION, null, null);
                */
        return null;
    }

    // Called when a previously created loader has finished loading
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
    }

    // Called when a previously created loader is reset, making the data unavailable
    public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked
    }
}
