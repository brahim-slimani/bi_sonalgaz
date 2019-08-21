package com.slimani.bi_sonalgaz.dashboards;


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


public class DashboardFilterAdapter extends BaseAdapter implements Filterable {

    private DashboardListActivity activity;
    private DashboardFilter dashboardFilter;
    private ArrayList<ItemDashboard> dashboardList;
    private ArrayList<ItemDashboard> filteredList;

    public DashboardFilterAdapter(DashboardListActivity activity, ArrayList<ItemDashboard> dashboardList) {
        this.activity = activity;
        this.dashboardList = dashboardList;
        this.filteredList = dashboardList;

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
        final ItemDashboard itemDashboard = (ItemDashboard) getItem(position);

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.dashboard_detail, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) view.findViewById(R.id.dashboard_item_title);
            holder.imageDashboard = (ImageView) view.findViewById(R.id.dashboard_item_image);

            view.setTag(holder);
        } else {

            holder = (ViewHolder) view.getTag();
        }

        holder.imageDashboard.setImageResource(itemDashboard.getImageId());
        holder.title.setText(itemDashboard.getTitle());


        return view;
    }


    @Override
    public Filter getFilter() {
        if (dashboardFilter == null) {
            dashboardFilter = new DashboardFilter();
        }

        return dashboardFilter;
    }


    private class DashboardFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<ItemDashboard> tempList = new ArrayList<ItemDashboard>();

                for (ItemDashboard dashboard : dashboardList) {
                    if (dashboard.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(dashboard);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = dashboardList.size();
                filterResults.values = dashboardList;
            }

            return filterResults;
        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<ItemDashboard>) results.values;
            notifyDataSetChanged();
        }
    }

    static class ViewHolder {
        private ImageView imageDashboard;
        private TextView title;


        public ImageView getImageDashboard() {
            return imageDashboard;
        }

        public void setImageDashboard(ImageView imageDashboard) {
            this.imageDashboard = imageDashboard;
        }

        public TextView getTitle() {
            return title;
        }

        public void setTitle(TextView title) {
            this.title = title;
        }
    }
}
