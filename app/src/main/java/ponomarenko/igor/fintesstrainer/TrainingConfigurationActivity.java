package ponomarenko.igor.fintesstrainer;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

public class TrainingConfigurationActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int WORKOUT_CONFIG_REQUEST_CODE = 2001;
    private CursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_configuration);

        cursorAdapter = new WorkoutsCursorAdapter(this, null, 0);
        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(cursorAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TrainingConfigurationActivity.this, WorkoutConfigurationActivity.class);

                Uri uri = Uri.parse(TrainingProvider.CONTENT_URI_WORKOUTS + "/" + id);
                intent.putExtra(TrainingProvider.CONTENT_ITEM_TYPE_WK, uri);
                startActivityForResult(intent,WORKOUT_CONFIG_REQUEST_CODE);

            }
        });

        //Toast.makeText(this,"Test",Toast.LENGTH_SHORT).show();
        getLoaderManager().initLoader(0, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_training_configuration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openAddWorkoutActivity(View view) {
        Intent intent = new Intent(this,WorkoutConfigurationActivity.class);
        startActivityForResult(intent, WORKOUT_CONFIG_REQUEST_CODE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,TrainingProvider.CONTENT_URI_WORKOUTS,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);


    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0,null,this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        restartLoader();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
