package ponomarenko.igor.fintesstrainer.Database;

/**
 * Created by Igor on 07.07.2015.
 */
public class DBLog {
    public static final String TABLE_LOG = "log";

    public static class Columns{
        public static final String ID = "_id";
        public static final String NAME = "name";
        public static final String SET_ID = "set_id";
        public static final String TOTAL_SETS = "total_sets";
        public static final String REPS_PER_SET = "reps_per_set";
        public static final String REST_BTW_SETS = "rest_btw_sets";
        public static final String REST_AFT_WORKOUT = "rest_aft_workout";
        public static final String DURATION = "duration";
        public static final String SEQUENCE = "sequence";
        public static final String BEEP_ENABLED = "beep_enabled";
        public static final String WEIGHT = "weight";
        public static final String START_TIME = "start_time";
        public static final String END_TIME = "end_time";
        public static final String COMPLETION = "completion";

    }
    public static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_LOG + " ( " +
            Columns.ID + "  INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Columns.NAME + " TEXT, " +
            Columns.SET_ID + " INTEGER, " +
            Columns.TOTAL_SETS + " INTEGER, " +
            Columns.REPS_PER_SET + " INTEGER, " +
            Columns.REST_BTW_SETS + " INTEGER, " +
            Columns.REST_AFT_WORKOUT + " INTEGER, " +
            Columns.DURATION + " INTEGER, " +
            Columns.SEQUENCE + " INTEGER, " +
            Columns.BEEP_ENABLED + " TEXT, " +
            Columns.WEIGHT + " INTEGER, " +
            Columns.START_TIME + " LONG, " +
            Columns.END_TIME + " LONG, " +
            Columns.COMPLETION + " TEXT )";

            ;
    public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS " + TABLE_LOG;

}
