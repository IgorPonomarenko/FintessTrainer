package ponomarenko.igor.fintesstrainer;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import ponomarenko.igor.fintesstrainer.Database.DBLog;

public class TrainingActivity extends AppCompatActivity {

    private TextView tv_workout_name;
    private TextView tv_number_of_sets;
    private TextView tv_number_of_reps;
    private TextView tv_rest_btw_sets;
    private CheckBox cb_beep_enabled;
    private Integer sequence;
    private String setID;
    private String workoutName;
    boolean bound = false;
    TrainingService trainingService;
    CountDownTimer timer;
    TrainingService.MyBinder binder;
    ContentValues cv;

    ServiceConnection sConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (TrainingService.MyBinder) service;
            if (binder!=null) {
                binder.setCallback(new onServiceUpdateCallback() {
                                       @Override
                                       public void onUpdate(ContentValues contentValues) {
                                           cv = contentValues;
                                           runOnUiThread(runnable);
                                       }
                                   }
                );
            }
        }


        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            refreshGUI();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);
        assignVariables();
        startService(new Intent(this, TrainingService.class));
        bindService(new Intent(this, TrainingService.class), sConn, 0);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(sConn);
    }

    private void refreshGUI() {

            int state = cv.getAsInteger(TrainingService.STATE);
            String stateCaption = "";
            if(state == TrainingService.STATE_REST_AFT_WORKOUT) {
                if(cv.getAsInteger(TrainingService.CURSOR_COUNT) == 1){
                    Toast.makeText(this, "Congratulations workout complete!", Toast.LENGTH_LONG).show();
                    onBackPressed();

                    return;
                }
                stateCaption = "Next workout starts in ";
            }
            if(state == TrainingService.STATE_WORKOUT) stateCaption = "Workout in progress ";
            if(state == TrainingService.STATE_REST_BTW_SETS) stateCaption = "Next set starts in ";
            tv_workout_name.setText(getString(R.string.str_current_workout) + " " + cv.getAsString(DBLog.Columns.NAME));
            tv_number_of_sets.setText("Set " + cv.getAsString(DBLog.Columns.SET_ID) + " of " + cv.getAsString(DBLog.Columns.TOTAL_SETS));
            tv_number_of_reps.setText(getString(R.string.str_reps_in_current_set) + " " + cv.getAsString(DBLog.Columns.REPS_PER_SET));


            tv_rest_btw_sets.setText(stateCaption + timeToMinutes(cv.getAsInteger(TrainingService.TIMER_VALUE)));

    }


    private String timeToMinutes(int timer_value) {
        String result;

        int seconds = (int) (timer_value % 60);
        int minutes = (int) ((timer_value - seconds) / 60 % 60);
        int hours = (int) ((timer_value - seconds - minutes * 60) / 3600);
        String min;
        String sec;
        if (minutes < 10) {
            min = "0" + minutes;
        } else {
            min = "" + minutes;
        }
        if (seconds < 10) {
            sec = "0" + seconds;
        } else {
            sec = "" + seconds;
        }

        if (hours != 0) {
            return hours + ":" + min + ":" + sec;
        } else {
            return min + ":" + sec;
        }

    }


    private void assignVariables() {
        tv_workout_name = (TextView) findViewById(R.id.current_workout);
        tv_number_of_sets = (TextView) findViewById(R.id.tv_current_set);
        tv_number_of_reps = (TextView) findViewById(R.id.tv_reps_in_current_set);
        tv_rest_btw_sets = (TextView) findViewById(R.id.tv_rest_between_sets);
        cb_beep_enabled = (CheckBox) findViewById(R.id.cb_beep_enbled);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_training, menu);
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

    public void OnEquipmentBusyBtnClick(View view) {


        ContentValues cv = new ContentValues();
        cv.put(DBLog.Columns.SEQUENCE, sequence + 100);
        getContentResolver().update(TrainingProvider.CONTENT_URI_LOGS, cv,
                DBLog.Columns.SEQUENCE + " = " + sequence + " AND " + DBLog.Columns.NAME + " = '" + workoutName + "' AND " +
                        DBLog.Columns.COMPLETION + " = '" + getString(R.string.completion_Assigned) + "'", null);

    }

    public void OnSkipWorkoutBtnClick(View view) {

        ContentValues cv = new ContentValues();
        cv.put(DBLog.Columns.COMPLETION, getString(R.string.completion_Skipped));
        getContentResolver().update(TrainingProvider.CONTENT_URI_LOGS, cv,
                DBLog.Columns.SEQUENCE + " = " + sequence + " AND " + DBLog.Columns.NAME + " = '" + workoutName + "' AND " +
                        DBLog.Columns.COMPLETION + " = '" + getString(R.string.completion_Assigned) + "'", null);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(sConn);
    }
}
