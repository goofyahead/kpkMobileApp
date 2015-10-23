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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nxtlink.kaprika.R;
import com.nxtlink.kaprika.adapters.OptionListAdapter;
import com.nxtlink.kaprika.interfaces.OptionSelected;
import com.nxtlink.kaprika.interfaces.SelectQuantityInterface;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kpklib.models.Dish;

/**
 * Created by goofyahead on 2/09/15.
 */
public class SelectQuantityDialog extends DialogFragment implements OptionSelected {

    private static final String DISH = "DISH_ARGUMENT";
    @InjectView(R.id.qtty_dialog_count)
    TextView quantityEditText;
    @InjectView(R.id.qtty_dialog_less)
    ImageView lessQty;
    @InjectView(R.id.qtty_dialog_more)
    ImageView moreQty;
    @InjectView(R.id.qtty_accept)
    TextView acceptQtty;
    @InjectView(R.id.qtty_cancel)
    TextView cancelQtty;
    @InjectView(R.id.quantity_dialog_item_name)
    TextView itemName;
    @InjectView(R.id.quantity_dialog_item_price_unity)
    TextView priceUnit;
    @InjectView(R.id.options_listView)
    ListView optionsListView;


    private SelectQuantityInterface mCallback;
    private HashMap<String, String> optionSelected = new HashMap<>();
    private int quantity = 1;
    private Dish mDish;

    public static SelectQuantityDialog newInstance(Dish myDish) {
        SelectQuantityDialog f = new SelectQuantityDialog();
        Bundle newBundle = new Bundle();
        newBundle.putSerializable(DISH, myDish);
        f.setArguments(newBundle);
        return f;
    }

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

        mDish = (Dish) getArguments().getSerializable(DISH);

        for (String cat : mDish.getOptions().keySet()){
            optionSelected.put(cat, mDish.getOptions().get(cat).get(0));
        }

        optionsListView.setAdapter(new OptionListAdapter(mDish.getOptions(), getActivity(), this));

        itemName.setText(mDish.getName());
        priceUnit.setText(String.format(getActivity().getString(R.string.unitary_price), mDish.getPrice()));

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
                mCallback.quantitySelected(quantity, optionSelected);
                SelectQuantityDialog.this.dismiss();
            }
        });

        cancelQtty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity = 0;
                mCallback.quantitySelected(quantity, null);
                SelectQuantityDialog.this.dismiss();
            }
        });
    }

    @Override
    public void optionSelected(String category, String selection) {
        optionSelected.put(category, selection);
    }
}
