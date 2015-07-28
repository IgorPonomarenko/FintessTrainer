package ponomarenko.igor.fintesstrainer;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ponomarenko.igor.fintesstrainer.Database.DBLog;
import ponomarenko.igor.fintesstrainer.Database.DBOpenHelper;

public class MainActivity extends AppCompatActivity implements YesNoAlertFragment.onAlerDialogDesicionTaken {

    private static final int TRAINING_REQUEST_CODE = 1001;
    private static final int TRAIN_CONFIG_REQUEST_CODE = 1002;
    private boolean desicionTaken = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void openTrainingActivity(View view) {
        Cursor incompleteWorkoutsCursor;
        incompleteWorkoutsCursor = getContentResolver().query(TrainingProvider.CONTENT_URI_LOGS, null
                , DBLog.Columns.COMPLETION + " = '" + getString(R.string.completion_Assigned) + "'" , null, null);


        if (incompleteWorkoutsCursor.getCount() > 0) {

            YesNoAlertFragment fragment = new YesNoAlertFragment();
            fragment.setCancelable(false);
            fragment.show(getFragmentManager(), "RemovePreveiousWorkout");
        }else {
            addTrainingToLogDB();
            Intent intent = new Intent(this, TrainingActivity.class);
            startActivityForResult(intent, TRAIN_CONFIG_REQUEST_CODE);

        }


        //Check if there are any incomplete workouts


    }

    private void addTrainingToLogDB() {
        Cursor cursor;
        String temp;
        ContentValues contentValues = new ContentValues();
        Integer setID = 0;
        cursor = getContentResolver().query(TrainingProvider.CONTENT_URI_WORKOUTS, null, null, null, null);
        cursor.moveToFirst();
        do {
            contentValues.clear();

            // fill in content values here
            setID = cursor.getInt(cursor.getColumnIndex(DBOpenHelper.WORKOUT_NUM_OF_SETS));

            for (int i = 1; i <= setID; i++) {
                contentValues.put(DBLog.Columns.NAME, cursor.getString(cursor.getColumnIndex(DBOpenHelper.WORKOUT_NAME)));
                contentValues.put(DBLog.Columns.SET_ID, i);
                contentValues.put(DBLog.Columns.TOTAL_SETS, setID);
                contentValues.put(DBLog.Columns.REPS_PER_SET, cursor.getInt(cursor.getColumnIndex(DBOpenHelper.WORKOUT_REPS_PER_SET)));
                contentValues.put(DBLog.Columns.REST_BTW_SETS, cursor.getInt(cursor.getColumnIndex(DBOpenHelper.WORKOUT_REST_BTW_SETS)));
                contentValues.put(DBLog.Columns.REST_AFT_WORKOUT, cursor.getString(cursor.getColumnIndex(DBOpenHelper.WORKOUT_REST_AFT_WORKOUT)));
                contentValues.put(DBLog.Columns.DURATION, cursor.getString(cursor.getColumnIndex(DBOpenHelper.WORKOUT_DURATION)));
                contentValues.put(DBLog.Columns.SEQUENCE, cursor.getString(cursor.getColumnIndex(DBOpenHelper.WORKOUT_SEQUENCE)));
                contentValues.put(DBLog.Columns.BEEP_ENABLED, cursor.getString(cursor.getColumnIndex(DBOpenHelper.WORKOUT_BEEP_ENABLED)));
                contentValues.put(DBLog.Columns.WEIGHT, 0);
                contentValues.put(DBLog.Columns.START_TIME, 0);
                contentValues.put(DBLog.Columns.END_TIME, 0);
                contentValues.put(DBLog.Columns.COMPLETION, getString(R.string.completion_Assigned));

                getContentResolver().insert(TrainingProvider.CONTENT_URI_LOGS, contentValues);
            }
        } while (cursor.moveToNext());

        cursor.close();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void openTrainingConfiguration(View view) {
        Intent intent = new Intent(this, TrainingConfigurationActivity.class);
        startActivityForResult(intent, TRAIN_CONFIG_REQUEST_CODE);
    }



    @Override
    public void onDesicionTaken(boolean desicion) {
        Intent intent = new Intent(this, TrainingActivity.class);

        ContentValues cv = new ContentValues();
        cv.put(DBLog.Columns.COMPLETION, getString(R.string.completion_Skipped));

        if (desicion) {
            getContentResolver().update(TrainingProvider.CONTENT_URI_LOGS, cv, DBLog.Columns.COMPLETION + " = '" + getString(R.string.completion_Assigned) + "'", null);
            addTrainingToLogDB();
            startActivityForResult(intent, TRAINING_REQUEST_CODE);

            Toast.makeText(this, "START NEW", Toast.LENGTH_LONG).show();

        } else {
            startActivityForResult(intent, TRAINING_REQUEST_CODE);

            Toast.makeText(this, "NO", Toast.LENGTH_LONG).show();
        }

    }

}


