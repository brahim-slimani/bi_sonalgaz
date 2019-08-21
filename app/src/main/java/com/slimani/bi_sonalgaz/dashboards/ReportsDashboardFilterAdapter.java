package com.slimani.bi_sonalgaz.dashboards;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.slimani.bi_sonalgaz.R;
import com.slimani.bi_sonalgaz.reports.ItemReport;

import java.util.ArrayList;


public class ReportsDashboardFilterAdapter extends BaseAdapter implements Filterable {

    private SaveDashboardActivity saveDashboardActivity;
    private ReportFilter reportFilter;
    private ArrayList<ItemReport> reportList;
    public ArrayList<ItemReport> filteredList;
    public ArrayList<ItemReport> checkedItemsList = new ArrayList<>();
    ArrayList<Boolean> positionArray;
    int x = 0;


    public ReportsDashboardFilterAdapter(SaveDashboardActivity activity, ArrayList<ItemReport> reportList) {
        this.saveDashboardActivity = activity;
        this.reportList = reportList;
        this.filteredList = reportList;

        positionArray = new ArrayList<Boolean>(filteredList.size());
        for (int i = 0; i < filteredList.size(); i++) {
            positionArray.add(false);
        }
        getFilter();
    }


    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {


        final ViewHolder holder;
        final ItemReport itemReport = (ItemReport) getItem(position);

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) saveDashboardActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.report_dashboard_detail, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) view.findViewById(R.id.report_dashboard_item_title);
            holder.imageReport = (ImageView) view.findViewById(R.id.report_dashboard_item_image);
            holder.checkBox = view.findViewById(R.id.check_report);
            view.setTag(holder);
        } else {

            holder = (ViewHolder) view.getTag();
            holder.checkBox.setOnCheckedChangeListener(null);

        }

        holder.checkBox.setFocusable(false);

        holder.imageReport.setImageResource(itemReport.getImageId());
        holder.title.setText(itemReport.getTitle());
        holder.checkBox.setTag(position);
        holder.checkBox.setChecked(positionArray.get(position));

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (holder.checkBox.isChecked()) {
                    positionArray.set(position, true);
                    itemReport.setChecked(true);
                    itemReport.setId((long) position);
                    checkedItemsList.add(itemReport);
                    x++;
                    Log.i("SaveDashboardActivity", "Item number " + x);
                    Log.i("SaveDashboardActivity", "Item title" + itemReport.getTitle());
                    Log.i("SaveDashboardActivity", "Item type" + itemReport.getType());

                    Toast.makeText(saveDashboardActivity, itemReport.getTitle() + " is selected", Toast.LENGTH_SHORT).show();
                } else {
                    x--;
                    positionArray.set(position, false);
                    itemReport.setChecked(false);
                    checkedItemsList.remove(itemReport);
                    Toast.makeText(saveDashboardActivity, itemReport.getTitle() + " is deselected", Toast.LENGTH_SHORT).show();
                }
                if (x >= 5) {
                    Toast.makeText(saveDashboardActivity, "You can't select more then 4 reports in a dashboard", Toast.LENGTH_SHORT).show();
                    positionArray.set(position, false);
                    checkedItemsList.remove(itemReport);
                    itemReport.setChecked(false);
                    holder.checkBox.setChecked(false);
                    x--;
                }
            }
        });


        return view;
    }


    @Override
    public Filter getFilter() {
        if (reportFilter == null) {
            reportFilter = new ReportFilter();
        }

        return reportFilter;
    }


    private class ReportFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<ItemReport> tempList = new ArrayList<ItemReport>();

                for (ItemReport report : reportList) {
                    if (report.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(report);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = reportList.size();
                filterResults.values = reportList;
            }

            return filterResults;
        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<ItemReport>) results.values;
            notifyDataSetChanged();
        }
    }

    static class ViewHolder {
        private ImageView imageReport;
        private TextView title;
        private CheckBox checkBox;


        public ImageView getImageReport() {
            return imageReport;
        }

        public void setImageReport(ImageView imageReport) {
            this.imageReport = imageReport;
        }

        public TextView getTitle() {
            return title;
        }

        public void setTitle(TextView title) {
            this.title = title;
        }

        public CheckBox getCheckBox() {
            return checkBox;
        }

        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }
    }


}
