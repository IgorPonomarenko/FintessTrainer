package ponomarenko.igor.fintesstrainer.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Igor on 02.07.2015.
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    //Constants for db name and version
    private static final String DATABASE_NAME = "Trainer.db";
    private static final int DB_VERSION = 3;

    //Constants for identifying table and columns
    public static final String TABLE_WORKOUTS = "workouts";
    public static final String WORKOUT_ID = "_id";
    public static final String WORKOUT_NAME = "name";
    public static final String WORKOUT_NUM_OF_SETS = "num_of_sets";
    public static final String WORKOUT_REPS_PER_SET = "reps_per_set";
    public static final String WORKOUT_REST_BTW_SETS = "rest_btw_sets";
    public static final String WORKOUT_REST_AFT_WORKOUT = "rest_aft_workout";
    public static final String WORKOUT_DURATION = "duration";
    public static final String WORKOUT_SEQUENCE = "sequence";
    public static final String WORKOUT_BEEP_ENABLED = "beep_enabled";

    //SQL to create table
    private static final String CREATE_TBL_WORKOUTS = "CREATE TABLE " + TABLE_WORKOUTS + " (" +
            WORKOUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            WORKOUT_NAME + " TEXT, " +
            WORKOUT_NUM_OF_SETS + " INTEGER, " +
            WORKOUT_REPS_PER_SET + " INTEGER, " +
            WORKOUT_REST_BTW_SETS + " INTEGER, " +
            WORKOUT_REST_AFT_WORKOUT + " INTEGER, " +
            WORKOUT_DURATION + " INTEGER, " +
            WORKOUT_SEQUENCE + " INTEGER, " +
            WORKOUT_BEEP_ENABLED + " NUMERIC" + " ) ";


    public static final String [] WORKOUT_COLUMNS =
            {WORKOUT_ID,WORKOUT_NAME, WORKOUT_NUM_OF_SETS,WORKOUT_REPS_PER_SET, WORKOUT_REST_BTW_SETS,WORKOUT_REST_AFT_WORKOUT,
            WORKOUT_DURATION, WORKOUT_SEQUENCE,WORKOUT_BEEP_ENABLED};



    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_TBL_WORKOUTS);
    db.execSQL(DBLog.CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUTS);
        db.execSQL(DBLog.DROP_TABLE_SQL);
        onCreate(db);
    }
}
