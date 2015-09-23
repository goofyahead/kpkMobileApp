package com.nxtlink.kaprika.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nxtlink.kaprika.R;
import com.nxtlink.kaprika.interfaces.SelectQuantityInterface;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by goofyahead on 2/09/15.
 */
public class SelectQuantityDialog extends DialogFragment {

    @InjectView(R.id.qtty_dialog_count)
    EditText quantityEditText;
    @InjectView(R.id.qtty_dialog_less)
    Button lessQty;
    @InjectView(R.id.qtty_dialog_more)
    Button moreQty;
    @InjectView(R.id.qtty_accept)
    Button acceptQtty;
    @InjectView(R.id.qtty_cancel)
    Button cancelQtty;

    private SelectQuantityInterface mCallback;

    private int quantity = 1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.select_quantity_dialog, null);

        ButterKnife.inject(this, v);
        builder.setView(v);
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (SelectQuantityInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SelectQuantityInterface");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        lessQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    quantity--;
                    quantityEditText.setText(String.valueOf(quantity));
                }
            }
        });

        moreQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                quantityEditText.setText(String.valueOf(quantity));
            }
        });

        acceptQtty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.quantitySelected(quantity);
                SelectQuantityDialog.this.dismiss();
            }
        });

        cancelQtty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = 0;
                mCallback.quantitySelected(quantity);
                SelectQuantityDialog.this.dismiss();
            }
        });
    }
}
