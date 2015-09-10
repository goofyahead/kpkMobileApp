package com.nxtlink.kaprika.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.nxtlink.kaprika.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by goofyahead on 17/08/15.
 */
public class ProgressDialogFragment extends DialogFragment{

    @InjectView(R.id.dialog_progress)
    ProgressBar progressBar;
    private int current = 0;
    private int max = -1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.dialog_progress, null);

        ButterKnife.inject(this, v);
        builder.setView(v);
        return builder.create();
    }

    public void updateProgress (int progress) {
        progressBar.setProgress(progress);
    }

    public void setMax (int max) {
        this.max = max;
        progressBar.setMax(max);
    }

    public void incrementProgressBy(int i) {
        current = current + i;
        progressBar.setProgress(current);
        if (current == max){
            this.dismiss();
        }
    }
}
