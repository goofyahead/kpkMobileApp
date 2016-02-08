package com.mgl.tpvkpk.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.mgl.tpvkpk.R;
import com.mgl.tpvkpk.interfaces.OptionSelected;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import kpklib.models.PrintableOrder;


/**
 * Created by goofyahead on 10/21/15.
 */
public class PrintTicketsAdapter extends BaseAdapter{

    private static final String TAG = PrintTicketsAdapter.class.getName();
    private LinkedList<PrintableOrder> options;
    private LayoutInflater mInflater;
    private Context mContext;
    private DateTimeFormatter fmt = DateTimeFormat.forPattern("EEE HH:mm dd/MM/yyyy");

    public PrintTicketsAdapter(LinkedList<PrintableOrder> options, Context mContext){
        this.options = options;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return options.size();
    }

    @Override
    public Object getItem(int position) {
        return options.get(position);
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
            convertView = mInflater.inflate(R.layout.print_ticket_item, null);
            holder.clientName = (TextView) convertView.findViewById(R.id.client_name);
            holder.orderId = (TextView) convertView.findViewById(R.id.order_id);
            holder.deliveryType = (TextView) convertView.findViewById(R.id.delivery_option);
            holder.dateForTicket = (TextView) convertView.findViewById(R.id.date_for_ticket);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.deliveryType.setText(options.get(position).getDeliveryOption().equalsIgnoreCase("PICK_UP") ? "RECOGER" : "DOMICILIO");
        holder.clientName.setText(options.get(position).getAddress().getName());
        holder.orderId.setText(options.get(position).getNonce().substring(options.get(position).getNonce().length() - 4, options.get(position).getNonce().length()));
        DateTime date = new DateTime(Long.valueOf(options.get(position).getTimestamp()));
        holder.dateForTicket.setText(fmt.print(date));

        return convertView;
    }

    public void add(PrintableOrder message) {
        options.add(message);
        notifyDataSetChanged();
    }

    public PrintableOrder get(int position) {
        return options.get(position);
    }


    private class ViewHolder {
        private TextView clientName;
        private TextView orderId;
        private TextView deliveryType;
        private TextView dateForTicket;
    }
}
