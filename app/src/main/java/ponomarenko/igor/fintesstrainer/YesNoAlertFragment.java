package ponomarenko.igor.fintesstrainer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by Igor on 17.07.2015.
 */
public class YesNoAlertFragment extends DialogFragment{

    onAlerDialogDesicionTaken mCallback;

    // Container Activity must implement this interface
    public interface onAlerDialogDesicionTaken {
        public void onDesicionTaken(boolean desicion);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (onAlerDialogDesicionTaken) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement onAlerDialogDesicionTaken");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Terminate previous training and start new one?")
                .setPositiveButton("START NEW", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    mCallback.onDesicionTaken(true);
                    }
                })
                .setNegativeButton("RESUME PRV.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mCallback.onDesicionTaken(false);

                    }
                });
        return builder.create();
    }
}
