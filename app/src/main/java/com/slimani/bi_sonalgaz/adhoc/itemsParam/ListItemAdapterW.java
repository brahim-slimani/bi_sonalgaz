package com.slimani.bi_sonalgaz.adhoc.itemsParam;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.slimani.bi_sonalgaz.R;

import java.util.List;

public class ListItemAdapterW extends BaseAdapter {

    private List<ItemDTO> listViewItemDtoList = null;

    private Context ctx = null;

    public ListItemAdapterW(List<ItemDTO> listViewItemDtoList, Context ctx) {
        this.listViewItemDtoList = listViewItemDtoList;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        int ret = 0;

        if(listViewItemDtoList != null){
            ret = listViewItemDtoList.size();
        }

        return ret;
    }

    @Override
    public Object getItem(int position) {
        Object ret = null;

        if(listViewItemDtoList != null){
            ret = listViewItemDtoList.get(position);
        }

        return ret;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemHolder viewHolder = null;

        if(convertView != null){
            viewHolder = (ListItemHolder) convertView.getTag();
        }else {
            convertView = View.inflate(ctx, R.layout.item_checkbox, null);
            CheckBox listItemCheckbox = (CheckBox) convertView.findViewById(R.id.list_view_item_checkbox);
            TextView listItemText = (TextView) convertView.findViewById(R.id.list_view_item_text);
            listItemText.setTextColor(Color.WHITE);
            viewHolder = new ListItemHolder(convertView);
            viewHolder.setItemCheckbox(listItemCheckbox);
            viewHolder.setItemTextView(listItemText);
            convertView.setTag(viewHolder);

        }

        ItemDTO listItemDTO = listViewItemDtoList.get(position);
        viewHolder.getItemCheckbox().setChecked(listItemDTO.isChecked());
        viewHolder.getItemTextView().setText(listItemDTO.getText());

        return convertView;
    }
}
