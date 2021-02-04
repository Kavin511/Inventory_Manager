package com.are.vehiclemanager.dp;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.are.vehiclemanager.R;
import com.are.vehiclemanager.ui.equipment.Equipment_report_edit_dialog;
import com.are.vehiclemanager.ui.filler.Filter_edit_dialog;
import com.are.vehiclemanager.ui.stock.Stock_edit_dialog;
import com.are.vehiclemanager.ui.vehicle.Vehicle_update_dialog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DataViewAdapter extends RecyclerView.Adapter<DataViewAdapter.DataHistoryViewHolder> {
    private final LayoutInflater mInflater;
    List<DataDB> filterData = new ArrayList<>();
    String TAG = "DataViewAdapter";
    Context context;
    private List<DataDB> recentData;

    public DataViewAdapter(Context context, List<DataDB> dataDBS) {
        recentData = dataDBS;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public DataViewAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public DataHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View DataView = mInflater.inflate(R.layout.recycler_items, parent, false);
        DataView.setElevation(5);
        Log.d(TAG, "onCreateViewHolder: ");
        return new DataHistoryViewHolder(DataView);
    }

    @Override
    public void onBindViewHolder(@NonNull DataHistoryViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + getItemCount());
        if (getItemCount() > 0) {
            DataDB dataDB = recentData.get(position);
            String getPrice = dataDB.getPrice();
            String data_list = dataDB.getData();
            String[] arr = data_list.split(",");
            StringBuilder arr_data = new StringBuilder();
            arr_data.append(("<b>Serial number :</b>")).append(arr[1]).append("<br></br>\n");
            for (int i = 2; i < arr.length; i++) {
                if (i % 2 == 0) {
                    arr_data.append("<b>").append(arr[i]).append("</b>");
                } else {
                    arr_data.append(arr[i]).append("<br></br>\n");
                }
            }
            arr_data.append("\n");
            DateFormat sdf4 = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.ENGLISH);
            String time = sdf4.format(dataDB.getTimeStamp());
            holder.time_stamp.setVisibility(View.GONE);
            if (!dataDB.getType().equals("vehicle"))
                arr_data.append("<b>Time :</b>").append(time);
            holder.list_data.setText(Html.fromHtml(arr_data.toString()));
            holder.options.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(holder.options.getContext(), holder.options);
                    popupMenu.setGravity(Gravity.LEFT);
                    popupMenu.inflate(R.menu.list_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getItemId() == R.id.edit) {
                                if (dataDB.getType().equals("stock")) {
                                    final Stock_edit_dialog bt = new Stock_edit_dialog(dataDB);
                                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                    bt.show(activity.getSupportFragmentManager(), "a");
                                } else if (dataDB.getType().equals("filter")) {
                                    final Filter_edit_dialog bt = new Filter_edit_dialog(dataDB);
                                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                    bt.show(activity.getSupportFragmentManager(), "a");
                                } else if (dataDB.getType().equals("equipment")) {
                                    final Equipment_report_edit_dialog bt = new Equipment_report_edit_dialog(dataDB);
                                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                    bt.show(activity.getSupportFragmentManager(), "a");
                                } else if (dataDB.getType().equals("vehicle")) {
                                    final Vehicle_update_dialog bt = new Vehicle_update_dialog(dataDB);
                                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
                                    bt.show(activity.getSupportFragmentManager(), "a");
                                }

                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
            if (position % 2 == 0)
                holder.recycler_item.setBackgroundResource(R.drawable.white_text);
            else
                holder.recycler_item.setBackgroundResource(R.drawable.oddtext);
        } else {

            holder.list_data.setVisibility(View.GONE);
            holder.time_stamp.setText(R.string.no_data);
            holder.options.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (recentData != null)
            return recentData.size();
        else
            return 0;
    }

    public void setData(List<DataDB> data) {
        recentData = data;
        notifyDataSetChanged();
    }

    static class DataHistoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView time_stamp, list_data, options;
        private final CardView recycler_item;

        private DataHistoryViewHolder(@NonNull View DataView) {
            super(DataView);
            time_stamp = DataView.findViewById(R.id.time_stamp);
            list_data = DataView.findViewById(R.id.list_data);
            options = DataView.findViewById(R.id.options);
            recycler_item = DataView.findViewById(R.id.recycler_item);
        }
    }
}
