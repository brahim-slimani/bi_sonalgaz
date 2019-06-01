package com.slimani.bi_sonalgaz.reports;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.slimani.bi_sonalgaz.R;

import java.util.ArrayList;

public class ReportFilterAdapter extends BaseAdapter implements Filterable {

    private ReportListActivity activity;
    private ReportFilter reportFilter;
    private ArrayList<ItemReport> reportList;
    private ArrayList<ItemReport> filteredList;

    public ReportFilterAdapter(ReportListActivity activity, ArrayList<ItemReport> reportList) {
        this.activity = activity;
        this.reportList = reportList;
        this.filteredList = reportList;

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
            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.report_detail, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) view.findViewById(R.id.report_item_title);
            holder.imageReport = (ImageView) view.findViewById(R.id.report_item_image);

            view.setTag(holder);
        } else {

            holder = (ViewHolder) view.getTag();
        }

        holder.imageReport.setImageResource(itemReport.getImageId());
        holder.title.setText(itemReport.getTitle());


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
            if (constraint!=null && constraint.length()>0) {
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
    }

}
