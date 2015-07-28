package ponomarenko.igor.fintesstrainer;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import ponomarenko.igor.fintesstrainer.Database.DBOpenHelper;

public class WorkoutConfigurationActivity extends AppCompatActivity {

    private EditText et_workout_name;
    private EditText et_number_of_sets;
    private EditText et_duration;
    private EditText et_number_of_reps;
    private EditText et_rest_btw_sets;
    private EditText et_rest_aft_workout;
    private EditText et_seq;
    private CheckBox cb_beep_enabled;
    private String workoutFilter;
    private String action;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_configuration);
        assignVariablesForActivityElements();
        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(TrainingProvider.CONTENT_ITEM_TYPE_WK);
        if (uri != null) {
            action = Intent.ACTION_EDIT;
            workoutFilter = DBOpenHelper.WORKOUT_ID + "=" + uri.getLastPathSegment();
            Cursor cursor = getContentResolver().query(uri, null, workoutFilter, null, null);
            cursor.moveToFirst();
            fillActivityFromCursor(cursor);
        }else{
            action = Intent.ACTION_INSERT;
        }

    }

    private void fillActivityFromCursor(Cursor cursor) {

        et_workout_name.setText(cursor.getString(cursor.getColumnIndex(DBOpenHelper.WORKOUT_NAME)));
        et_number_of_sets.setText(cursor.getString(cursor.getColumnIndex(DBOpenHelper.WORKOUT_NUM_OF_SETS)));
        et_duration.setText(cursor.getString(cursor.getColumnIndex(DBOpenHelper.WORKOUT_DURATION)));;
        et_number_of_reps.setText(cursor.getString(cursor.getColumnIndex(DBOpenHelper.WORKOUT_REPS_PER_SET)));;
        et_rest_btw_sets.setText(cursor.getString(cursor.getColumnIndex(DBOpenHelper.WORKOUT_REST_BTW_SETS)));;
        et_rest_aft_workout.setText(cursor.getString(cursor.getColumnIndex(DBOpenHelper.WORKOUT_REST_AFT_WORKOUT)));;
        et_seq.setText(cursor.getString(cursor.getColumnIndex(DBOpenHelper.WORKOUT_SEQUENCE)));;

        if(cursor.getInt((cursor.getColumnIndex(DBOpenHelper.WORKOUT_BEEP_ENABLED))) == 1){
            cb_beep_enabled.setChecked(true);
        }else{
            cb_beep_enabled.setChecked(false);

        }

    }

    private void assignVariablesForActivityElements() {
        et_workout_name = (EditText) findViewById(R.id.et_workout_config);
        et_number_of_sets = (EditText) findViewById(R.id.et_sets_config);
        et_duration = (EditText) findViewById(R.id.et_sets_duration);
        et_number_of_reps = (EditText) findViewById(R.id.et_reps_in_set_config);
        et_rest_btw_sets = (EditText) findViewById(R.id.et_rest_between_sets_config);
        et_rest_aft_workout = (EditText) findViewById(R.id.et_rest_after_workout_config);
        et_seq = (EditText) findViewById(R.id.et_sequence);
        cb_beep_enabled = (CheckBox) findViewById(R.id.cb_beep_enbled_config);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(action.equals(Intent.ACTION_EDIT)) {
            getMenuInflater().inflate(R.menu.menu_workout_configuration, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            getContentResolver().delete(TrainingProvider.CONTENT_URI_WORKOUTS, workoutFilter,null);
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveConfiguredActivity(View view) {
        saveWorkout();
        onBackPressed();
    }

    private void saveWorkout() {
        ContentValues contentValues = putContentValues();

        if (action.equals(Intent.ACTION_INSERT)){

            getContentResolver().insert(TrainingProvider.CONTENT_URI_WORKOUTS,contentValues);

        }else if (action.equals(Intent.ACTION_EDIT)){

            getContentResolver().update(TrainingProvider.CONTENT_URI_WORKOUTS,contentValues, workoutFilter,null);

        }

    }

    @NonNull
    private ContentValues putContentValues() {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBOpenHelper.WORKOUT_NAME, et_workout_name.getText().toString());
        contentValues.put(DBOpenHelper.WORKOUT_NUM_OF_SETS, et_number_of_sets.getText().toString());
        contentValues.put(DBOpenHelper.WORKOUT_DURATION, et_duration.getText().toString());
        contentValues.put(DBOpenHelper.WORKOUT_REPS_PER_SET, et_number_of_reps.getText().toString());
        contentValues.put(DBOpenHelper.WORKOUT_REST_BTW_SETS, et_rest_btw_sets.getText().toString());
        contentValues.put(DBOpenHelper.WORKOUT_REST_AFT_WORKOUT, et_rest_aft_workout.getText().toString());
        contentValues.put(DBOpenHelper.WORKOUT_SEQUENCE, et_seq.getText().toString());
        contentValues.put(DBOpenHelper.WORKOUT_BEEP_ENABLED, cb_beep_enabled.isChecked());
        return contentValues;
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}
