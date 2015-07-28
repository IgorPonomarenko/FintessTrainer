package ponomarenko.igor.fintesstrainer;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import ponomarenko.igor.fintesstrainer.Database.DBLog;
import ponomarenko.igor.fintesstrainer.Database.DBOpenHelper;

/**
 * Created by Igor on 02.07.2015.
 */
public class TrainingProvider extends ContentProvider {

    private static final String AUTHORITY = "ponomarenko.igor.fintesstrainer.trainingprovider";
    private static final String BASE_PATH_WORKOUTS = "workouts";
    private static final String BASE_PATH_LOGS = "logs";
    public static final Uri CONTENT_URI_WORKOUTS = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_WORKOUTS);
    public static final Uri CONTENT_URI_LOGS = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH_LOGS);

    // Constant to identify the requested operation
    private static final int WORKOUTS = 1;
    private static final int WORKOUTS_ID = 2;
    private static final int LOGS = 3;
    private static final int LOG_ID = 4;

    public static String CONTENT_ITEM_TYPE_WK = "workout";
    public static String CONTENT_ITEM_TYPE_LOG = "log";

    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY,BASE_PATH_WORKOUTS,WORKOUTS);
        uriMatcher.addURI(AUTHORITY,BASE_PATH_WORKOUTS + "/#", WORKOUTS_ID);
        uriMatcher.addURI(AUTHORITY,BASE_PATH_LOGS, LOGS);
        uriMatcher.addURI(AUTHORITY,BASE_PATH_LOGS + "/#", LOG_ID);
    }

    private SQLiteDatabase database;


    @Override
    public boolean onCreate() {
        DBOpenHelper helper = new DBOpenHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String tableName = null;


        switch (uriMatcher.match(uri)){
            case WORKOUTS_ID:
                tableName = DBOpenHelper.TABLE_WORKOUTS;
                projection = DBOpenHelper.WORKOUT_COLUMNS;
                selection = DBOpenHelper.WORKOUT_ID + "=" + uri.getLastPathSegment();

                break;
            case WORKOUTS:
                tableName = DBOpenHelper.TABLE_WORKOUTS;
                projection = DBOpenHelper.WORKOUT_COLUMNS;
                sortOrder = DBOpenHelper.WORKOUT_SEQUENCE + " ASC";

                break;
            case LOG_ID:
                tableName = DBLog.TABLE_LOG;
                projection = null;
                selection = DBLog.Columns.ID + "=" + uri.getLastPathSegment();

                break;
            case LOGS:
                tableName = DBLog.TABLE_LOG;
                projection = null;
                break;

        }


        return  database.query(tableName,projection,selection,
                null,null,null,sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tableName = null;
        String basePath = null;

        switch (uriMatcher.match(uri)){

            case WORKOUTS:
                tableName = DBOpenHelper.TABLE_WORKOUTS;
                basePath = BASE_PATH_WORKOUTS;
                break;

            case LOGS:
                tableName = DBLog.TABLE_LOG;
                basePath = BASE_PATH_LOGS;

                break;

        }
        long id = database.insert(tableName,null,values);
        return Uri.parse(basePath + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String tableName = null;
        String basePath = null;
        switch (uriMatcher.match(uri)) {

            case WORKOUTS_ID:
                tableName = DBOpenHelper.TABLE_WORKOUTS;
                selection = DBOpenHelper.WORKOUT_ID + "=" + uri.getLastPathSegment();

                break;
            case WORKOUTS:
                tableName = DBOpenHelper.TABLE_WORKOUTS;

                break;
            case LOG_ID:
                tableName = DBLog.TABLE_LOG;
                selection = DBLog.Columns.ID + "=" + uri.getLastPathSegment();

                break;
            case LOGS:
                tableName = DBLog.TABLE_LOG;
                break;

        }
        return database.delete(DBOpenHelper.TABLE_WORKOUTS, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String tableName = null;
        switch (uriMatcher.match(uri)) {

                case WORKOUTS:
                    tableName = DBOpenHelper.TABLE_WORKOUTS;
                    break;

                case LOGS:
                    tableName = DBLog.TABLE_LOG;

                    break;

           }
        return database.update(tableName,values,selection,selectionArgs);

    }
}
