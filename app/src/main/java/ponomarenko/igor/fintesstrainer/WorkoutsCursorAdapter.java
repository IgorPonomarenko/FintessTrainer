package ponomarenko.igor.fintesstrainer;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import ponomarenko.igor.fintesstrainer.Database.DBOpenHelper;

/**
 * Created by Igor on 02.07.2015.
 */
public class WorkoutsCursorAdapter extends CursorAdapter {
    public WorkoutsCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.workout_list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String workoutName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.WORKOUT_NAME));
        String sequence = cursor.getString(cursor.getColumnIndex(DBOpenHelper.WORKOUT_SEQUENCE));

        TextView tv = (TextView) view.findViewById(R.id.tv_workout_name_list);
        tv.setText(sequence + " - " + workoutName);
    }
}
