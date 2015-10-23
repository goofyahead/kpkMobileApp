package com.nxtlink.kaprika.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.nxtlink.kaprika.R;
import com.nxtlink.kaprika.interfaces.OptionSelected;

import java.util.LinkedHashMap;
import java.util.LinkedList;


/**
 * Created by goofyahead on 10/21/15.
 */
public class OptionListAdapter extends BaseAdapter{

    private LinkedHashMap<String, LinkedList<String>> options;
    private LayoutInflater mInflater;
    private Context mContext;
    private OptionSelected mSelectionCallback;

    public OptionListAdapter (LinkedHashMap<String, LinkedList<String>> options, Context mContext, OptionSelected callback){
        this.options = options;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.mSelectionCallback = callback;
    }
    @Override
    public int getCount() {
        return options.size();
    }

    @Override
    public Object getItem(int position) {
        return options.get(options.keySet().toArray()[position]);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.option_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.option_type);
            holder.spinner = (Spinner) convertView.findViewById(R.id.option_spinner);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final String category = (String) options.keySet().toArray()[position];
        holder.name.setText(category);

        final LinkedList<String> optionList = options.get(options.keySet().toArray()[position]);
        String [] tokenized = optionList.toArray(new String [optionList.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                android.R.layout.simple_spinner_dropdown_item, tokenized);

        holder.spinner.setAdapter(adapter);

        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectionCallback.optionSelected(category, optionList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return convertView;
    }


    private class ViewHolder {
        private TextView name;
        private Spinner spinner;
    }
}
