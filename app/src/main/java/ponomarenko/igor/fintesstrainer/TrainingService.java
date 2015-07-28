package ponomarenko.igor.fintesstrainer;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ponomarenko.igor.fintesstrainer.Database.DBLog;

public class TrainingService extends Service {

    Timer timer;
    TimerTask tTask;
    String workoutName;
    Integer setID;
    Integer totalSets;
    Integer number_of_reps;
    Integer rest_btw_sets;
    Integer sequence;
    Integer rest_aft_workout;
    Integer workout_duration;
    Integer timer_value = 0;
    Integer timer_size = 0;
    Integer state;
    Integer cursor_count;
    onServiceUpdateCallback callback;


    ScheduledExecutorService executorService;

    public static final String TIMER_VALUE="timer_value";
    public static final String TIMER_SIZE="timer_size";
    public static final String STATE="state";
    public static final String CURSOR_COUNT="cursor_count";
    public static final Integer STATE_WORKOUT = 1;
    public static final Integer STATE_REST_BTW_SETS = 2;
    public static final Integer STATE_REST_AFT_WORKOUT = 3;
    public static final long interval = 1000;


    public TrainingService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(executorService==null){
            refreshData();
            executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.scheduleAtFixedRate(runnable,0,1, TimeUnit.SECONDS);
        }
        return super.onStartCommand(intent, flags, startId);

    }
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timer_size = workout_duration;
            if (state == STATE_WORKOUT) {
                if (timer_value < workout_duration) {
                    timer_value++;

                } else if (setID == totalSets) {
                    timer_value = 0;
                    state = STATE_REST_AFT_WORKOUT;
                } else {
                    timer_value = 0;
                    state = STATE_REST_BTW_SETS;
                }
            }else if(state == STATE_REST_BTW_SETS) {
                timer_size = rest_btw_sets;
                if (timer_value < rest_btw_sets) {
                    timer_value++;
                } else {
                    timer_value = 0;
                    markSetAsCompleted();
                    refreshData();
                }
            }else if(state == STATE_REST_AFT_WORKOUT) {
                timer_size = rest_aft_workout;
                if (timer_value < rest_aft_workout) {
                    timer_value++;
                } else {
                    timer_value = 0;
                    markSetAsCompleted();
                    refreshData();
                }
            }

            if(callback != null){
                callback.onUpdate(getResults());
            }
        }
    };


    private void markSetAsCompleted() {
        ContentValues cv = new ContentValues();
        cv.put(DBLog.Columns.COMPLETION,getString(R.string.completion_Completed));
        getContentResolver().update(TrainingProvider.CONTENT_URI_LOGS, cv,
                DBLog.Columns.SEQUENCE + " = " + sequence
                        + " AND " + DBLog.Columns.NAME + " = '" + workoutName + "' AND "
                        + DBLog.Columns.SET_ID + " = '" + setID + "' AND "
                        + DBLog.Columns.COMPLETION + " = '" + getString(R.string.completion_Assigned) + "'", null);
    }

    ContentValues getResults(){
        ContentValues cv = new ContentValues();

        cv.put(DBLog.Columns.NAME, workoutName);
        cv.put(DBLog.Columns.SET_ID, setID);
        cv.put(DBLog.Columns.TOTAL_SETS, totalSets);
        cv.put(DBLog.Columns.REPS_PER_SET, number_of_reps);
        cv.put(STATE, state);
        cv.put(TIMER_SIZE, timer_size);
        cv.put(TIMER_VALUE, timer_size - timer_value);
        cv.put(CURSOR_COUNT,cursor_count);

        return cv;
    }
    void refreshData() {
        // Obtaining information for the first set of workout with lowest sequence
        Cursor cursor = getContentResolver().query(TrainingProvider.CONTENT_URI_LOGS, null
                , DBLog.Columns.COMPLETION + " = '" + getString(R.string.completion_Assigned)+"'"
                ,null,DBLog.Columns.SEQUENCE + " ASC, " + DBLog.Columns.SET_ID + " ASC ");//DBLog.Columns.SEQUENCE + " ASC, " + DBLog.Columns.SET_ID + " ASC "
        cursor_count = cursor.getCount();
        if(cursor_count!=0) {
            cursor.moveToFirst();

            workoutName = cursor.getString(cursor.getColumnIndex(DBLog.Columns.NAME));
            setID = cursor.getInt(cursor.getColumnIndex(DBLog.Columns.SET_ID));
            totalSets = cursor.getInt(cursor.getColumnIndex(DBLog.Columns.TOTAL_SETS));
            number_of_reps = cursor.getInt(cursor.getColumnIndex(DBLog.Columns.REPS_PER_SET));
            rest_btw_sets = cursor.getInt(cursor.getColumnIndex(DBLog.Columns.REST_BTW_SETS));
            sequence = cursor.getInt(cursor.getColumnIndex(DBLog.Columns.SEQUENCE));
            rest_aft_workout = cursor.getInt(cursor.getColumnIndex(DBLog.Columns.REST_AFT_WORKOUT));
            workout_duration = cursor.getInt(cursor.getColumnIndex(DBLog.Columns.DURATION));
            state = STATE_WORKOUT;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MyBinder(this);
    }

    public static class MyBinder extends Binder{

        TrainingService service;
        public MyBinder(TrainingService service){
            this.service = service;
        }

        public void setCallback(onServiceUpdateCallback cb){
            service.callback = cb;
        }
    }
}
