package com.slimani.bi_sonalgaz.adhoc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.slimani.bi_sonalgaz.R;
import com.slimani.bi_sonalgaz.adhoc.chartsFragments.ColumnbarFrgament;
import com.slimani.bi_sonalgaz.adhoc.chartsFragments.CrosstableFragment;
import com.slimani.bi_sonalgaz.adhoc.chartsFragments.CustomPieContext;
import com.slimani.bi_sonalgaz.adhoc.chartsFragments.PiechartFragment;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.AxeDimension;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.AxeMeasure;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.ItemDTO;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.ItemDimension;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.ItemMeasure;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.ListItemAdapter;
import com.slimani.bi_sonalgaz.restful.DataManager;
import com.slimani.bi_sonalgaz.restful.Service;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class AdhocActivity extends AppCompatActivity {

    public static JSONArray dataJS = new JSONArray();
    public static List<String> adhocColumns = new ArrayList<>();
    public static List<String> adhocRows = new ArrayList<>();
    public static CustomPieContext customPieContext = new CustomPieContext();
    String currentColumn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adhoc);

        final Button measures_btn = (Button) findViewById(R.id.mesaures_btn);
        final Button dimensions_btn = (Button) findViewById(R.id.dimensions_btn);
        final TextView measures_label = (TextView) findViewById(R.id.label_measures);
        final TextView dimensions_label = (TextView) findViewById(R.id.label_dimensions);

        final LinearLayout measures_content = (LinearLayout) findViewById(R.id.measure_content);
        final LinearLayout dimensions_content = (LinearLayout) findViewById(R.id.dimensions_content);


        final Button charts_btn = (Button) findViewById(R.id.charts_btn);
        final Button config_btn = (Button) findViewById(R.id.config_btn);
        final Button save_btn = (Button) findViewById(R.id.save_btn);
        final Button nextResult_btn = (Button) findViewById(R.id.next_result_btn);

        String ms = getIntent().getStringExtra("measures");
        String dm = getIntent().getStringExtra("dimensions");

        ArrayList<ItemMeasure> listMeasures = new ArrayList<ItemMeasure>();
        ArrayList<ItemDimension> listDimensions = new ArrayList<ItemDimension>();
        AxeMeasure axeMeasureObject = new AxeMeasure();
        AxeDimension axeDimensionObject = new AxeDimension();


        DataManager dataManager = new DataManager();


        //browse and filling measures
        measures_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final Dialog dialog = new Dialog(AdhocActivity.this);
                dialog.setContentView(R.layout.list_measures);
                dialog.show();
                Button confirm_btn = dialog.findViewById(R.id.confirm_measures);



                final ListView listViewWithCheckbox = (ListView) dialog.findViewById(R.id.list_view_with_checkbox);

                List<ItemDTO> initItemList = new ArrayList<ItemDTO>();
                try {

                    List<String> itemsMeasures = dataManager.getItemsMeasures(new JSONArray(ms));
                    initItemList = getInitViewItemDtoList(itemsMeasures);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                final ListItemAdapter listViewDataAdapter = new ListItemAdapter(initItemList, getApplicationContext());

                listViewDataAdapter.notifyDataSetChanged();

                listViewWithCheckbox.setAdapter(listViewDataAdapter);

                final List<ItemDTO> itemsChecked = new ArrayList<ItemDTO>();
                listViewWithCheckbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {
                        Object itemObject = adapterView.getAdapter().getItem(itemIndex);
                        ItemDTO itemDto = (ItemDTO) itemObject;
                        CheckBox itemCheckbox = (CheckBox) view.findViewById(R.id.list_view_item_checkbox);

                        if(itemDto.isChecked())
                        {
                            itemCheckbox.setChecked(false);
                            itemDto.setChecked(false);
                            itemsChecked.remove(itemDto);


                        }else
                        {
                            itemCheckbox.setChecked(true);
                            itemDto.setChecked(true);
                            itemsChecked.add(itemDto);

                        }

                    }
                });


                listMeasures.clear();
                confirm_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        measures_content.removeAllViews();

                        int i = 0;
                        while (i<itemsChecked.size()){
                            if(itemsChecked.get(i).isChecked()){
                                listMeasures.add(new ItemMeasure(itemsChecked.get(i).getText(),null));
                                addMeasureItems(measures_content,itemsChecked.get(i).getText(),listMeasures);
                            }
                            i++;

                        }
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Click on the measure to specify the aggregate's function !", Toast.LENGTH_LONG).show();


                    }
                });



            }
        });

        //config measures and dimensions as columns or rows
        config_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(AdhocActivity.this);
                dialog.setContentView(R.layout.popup_config_axes);
                dialog.show();
                Button confirm_config_btn = dialog.findViewById(R.id.confirm_config);

                final RadioGroup measuresGroup = dialog.findViewById(R.id.measures_group);

                final RadioGroup dimensionGroup = dialog.findViewById(R.id.dimensions_group);

                confirm_config_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int radioButtonID = measuresGroup.getCheckedRadioButtonId();
                        View radioButton = measuresGroup.findViewById(radioButtonID);

                        int radioButtonID2 = dimensionGroup.getCheckedRadioButtonId();
                        View radioButton2 = dimensionGroup.findViewById(radioButtonID2);

                        String axeMeasures = null;
                        String axeDimension = null;

                        try {
                            axeMeasures = getResources().getResourceEntryName(radioButton.getId());
                            axeDimension = getResources().getResourceEntryName(radioButton2.getId());

                            if((axeMeasures.equals("columns_measures") && axeDimension.equals("columns_dimensions")) || (axeMeasures.equals("rows_measures") && axeDimension.equals("rows_dimensions"))){

                                Toast.makeText(AdhocActivity.this, "the roles must be different !", Toast.LENGTH_SHORT).show();
                            }else{

                                if(axeMeasures.equals("columns_measures")){

                                    axeMeasureObject.setName("measures");
                                    axeMeasureObject.setRole("columns");

                                }else if(axeMeasures.equals("rows_measures")){

                                    axeMeasureObject.setName("measures");
                                    axeMeasureObject.setRole("rows");

                                }

                                if(axeDimension.equals("columns_dimensions")){

                                    axeDimensionObject.setName("dimensions");
                                    axeDimensionObject.setRole("columns");

                                }else if(axeDimension.equals("rows_dimensions")){

                                    axeDimensionObject.setName("dimensions");
                                    axeDimensionObject.setRole("rows");
                                }
                                dialog.dismiss();
                                charts_btn.setEnabled(true);
                            }


                        }catch (NullPointerException n){
                            Toast.makeText(AdhocActivity.this, "You must check the role for each item !", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });


        //browse and filling dimenisons
        dimensions_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final Dialog dialog = new Dialog(AdhocActivity.this);
                dialog.setContentView(R.layout.list_dimensions);
                dialog.show();
                Button confirm_btn = dialog.findViewById(R.id.confirm_dimensions);



                final ListView listViewWithCheckbox = (ListView) dialog.findViewById(R.id.list_view_with_checkbox);

                List<ItemDTO> initItemList = new ArrayList<ItemDTO>();
                try {
                    List<String> itemsDimensions = dataManager.getItemsDimensions(new JSONArray(dm));
                    initItemList = getInitViewItemDtoList(itemsDimensions);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                final ListItemAdapter listViewDataAdapter = new ListItemAdapter(initItemList, getApplicationContext());

                listViewDataAdapter.notifyDataSetChanged();

                listViewWithCheckbox.setAdapter(listViewDataAdapter);

                final List<ItemDTO> itemsChecked = new ArrayList<ItemDTO>();
                listViewWithCheckbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {
                        Object itemObject = adapterView.getAdapter().getItem(itemIndex);
                        ItemDTO itemDto = (ItemDTO) itemObject;
                        CheckBox itemCheckbox = (CheckBox) view.findViewById(R.id.list_view_item_checkbox);

                        if(itemDto.isChecked())
                        {
                            itemCheckbox.setChecked(false);
                            itemDto.setChecked(false);
                            itemsChecked.remove(itemDto);


                        }else
                        {
                            itemCheckbox.setChecked(true);
                            itemDto.setChecked(true);
                            itemsChecked.add(itemDto);

                        }

                    }
                });


                listDimensions.clear();
                confirm_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dimensions_content.removeAllViews();

                        int i = 0;
                        while (i<itemsChecked.size()){
                            if(itemsChecked.get(i).isChecked()){
                                listDimensions.add(new ItemDimension(itemsChecked.get(i).getText(),null));
                                addDimItems(dimensions_content,itemsChecked.get(i).getText(), listDimensions);
                            }

                            i++;

                        }
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Click on the dimensions to specify their columns !", Toast.LENGTH_LONG).show();


                    }
                });



            }
        });


        //get result per type of view
        charts_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(!validateMeasures(listMeasures) || listMeasures.isEmpty()){

                    throwingAlert("You must check the measures and specify their function's aggregate !");

                }else if(!validateDimensions(listDimensions) || listDimensions.isEmpty()){

                    //Toast.makeText(getApplicationContext(), "You must check the dimensions and specify their columns !", Toast.LENGTH_LONG).show();
                    throwingAlert("You must check the dimensions and specify their columns !");
                }else{

                    if(!validateAxes(axeMeasureObject,axeDimensionObject,listMeasures,listDimensions)){

                        throwingAlert("In the axis of the rows, it should be only one item !");

                    }else{
                        String cube = dataManager.buildQuery(listMeasures,listDimensions,dataManager.getCurrentCube(getApplicationContext()));
                        Service service = new Service();
                        service.consumesRest(getApplicationContext(),"?query="+cube);

                        adhocColumns = dataManager.getAdhocColumns(listMeasures,listDimensions,axeMeasureObject);
                        adhocRows = dataManager.getAdhocRows(listMeasures,listDimensions,axeMeasureObject);
                        if(adhocRows.size()>1){
                            adhocRows = adhocRows.subList(0,1);
                        }

                        final Dialog dialog = new Dialog(AdhocActivity.this);
                        dialog.setContentView(R.layout.popup_typechart);
                        dialog.setTitle("Select the type of chart");
                        dialog.show();
                        Button ok_btn = dialog.findViewById(R.id.confirm_typechart_btn);
                        ok_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final RadioGroup chartGroup = dialog.findViewById(R.id.typechart_radiogroup);
                                int radioButtonID = chartGroup.getCheckedRadioButtonId();
                                View radioButton = chartGroup.findViewById(radioButtonID);
                                String typeChart = null;

                                dataJS = service.consumesRest(getApplicationContext(),"?query="+cube);

                                try {
                                    typeChart = getResources().getResourceEntryName(radioButton.getId());
                                    dialog.dismiss();

                                    if(typeChart.equals("crosstable")){
                                        nextResult_btn.setEnabled(false);
                                        loadFragment(new CrosstableFragment());

                                    }else if(typeChart.equals("piechart")){

                                        try {
                                            currentColumn = adhocColumns.get(0);
                                            customPieContext.setColumn(adhocColumns.get(0));
                                            nextResult_btn.setEnabled(true);
                                            customPieContext.setDataEntryList(dataManager.parsingToListPie(dataJS,adhocColumns.get(0),adhocRows));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        loadFragment(new PiechartFragment());

                                    }else if(typeChart.equals("columnbarchart")){
                                        nextResult_btn.setEnabled(false);
                                        loadFragment(new ColumnbarFrgament());

                                    }

                                }catch (NullPointerException n){
                                    Toast.makeText(AdhocActivity.this, "You must check one type chart !", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                    }


                }


            }
        });


        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println(dataManager.buildQuery(listMeasures,listDimensions,dataManager.getCurrentCube(getApplicationContext())));

            }
        });

        nextResult_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int index = adhocColumns.indexOf(currentColumn);
                if(index+1 < adhocColumns.size()){
                    currentColumn = adhocColumns.get(index+1);
                    customPieContext.setColumn(currentColumn);
                    try {
                        customPieContext.setDataEntryList(dataManager.parsingToListPie(dataJS, currentColumn, adhocRows));
                        loadFragment(new PiechartFragment());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else if(index+1 == adhocColumns.size()){
                    currentColumn = adhocColumns.get(0);
                    customPieContext.setColumn(currentColumn);
                    try {
                        customPieContext.setDataEntryList(dataManager.parsingToListPie(dataJS, currentColumn, adhocRows));
                        loadFragment(new PiechartFragment());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });





    }



    //changing fragments
    private void loadFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.workspace_frame, fragment);
        fragmentTransaction.commit();
    }



    //initialise the CheckedItems
    private List<ItemDTO> getInitViewItemDtoList(List<String> items) throws JSONException {

        List<ItemDTO> ret = new ArrayList<ItemDTO>();

        for(int i=0;i<items.size();i++)
        {
            String itemText = items.get(i);

            ItemDTO dto = new ItemDTO();

            dto.setChecked(false);
            dto.setText(itemText);

            ret.add(dto);
        }

        return ret;
    }


    //adding measures to bar
    public void addMeasureItems(LinearLayout linearLayout, String measure,List<ItemMeasure> itemMeasureList){

        TextView measItem = new TextView(this);
        measItem.setText(measure);
        measItem.setTextColor(getResources().getColor(R.color.colorWhite));
        measItem.setClickable(true);

        measItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                final Dialog dialog = new Dialog(AdhocActivity.this);
                dialog.setContentView(R.layout.popup_aggregate_measure);
                dialog.setTitle("Select the type of function");
                TextView header = (TextView) dialog.findViewById(R.id.header);
                header.setText(header.getText()+measure);

                dialog.show();
                Button ok_btn = dialog.findViewById(R.id.confirm_aggregate_btn);
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final RadioGroup functionGroup = dialog.findViewById(R.id.aggregate_radiogroup);
                        int radioButtonID = functionGroup.getCheckedRadioButtonId();
                        View radioButton = functionGroup.findViewById(radioButtonID);
                        String typeFunction = null;
                        try {
                            typeFunction = getResources().getResourceEntryName(radioButton.getId());
                            dialog.dismiss();

                            if(typeFunction.equals("sum")){
                                int index = findMeasure(itemMeasureList, measure);
                                itemMeasureList.get(index).setFunction(typeFunction);
                                measItem.setText(measure+"("+typeFunction+")");

                            }else if(typeFunction.equals("avg")){
                                int index = findMeasure(itemMeasureList, measure);
                                itemMeasureList.get(index).setFunction(typeFunction);
                                measItem.setText(measure+"("+typeFunction+")");

                            }

                        }catch (NullPointerException n){
                            Toast.makeText(AdhocActivity.this, "You must check one type of function !", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });


        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(20,30,20,0);
        measItem.setLayoutParams(lp);

        linearLayout.addView(measItem, lp);

    }

    //adding dimensions to bar
    public void addDimItems(LinearLayout linearLayout, String dimension, List<ItemDimension> dimensionList){

        TextView dimItem = new TextView(this);
        dimItem.setText(dimension);
        dimItem.setTextColor(getResources().getColor(R.color.colorWhite));
        dimItem.setClickable(true);
        Service service = new Service();
        service.consumesRest(getApplicationContext(),"/dimensionColumns?dimension="+dimension);

        dimItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                final Dialog dialog = new Dialog(AdhocActivity.this);
                dialog.setContentView(R.layout.columns_dimension);
                dialog.setTitle("Select the columns dimension");
                TextView header = (TextView) dialog.findViewById(R.id.header);
                header.setText(header.getText()+dimension);

                dialog.show();
                Button confirm_btn = dialog.findViewById(R.id.confirm_columns_dimension);

                final ListView listViewWithCheckbox = (ListView) dialog.findViewById(R.id.list_view_with_checkbox);

                List<ItemDTO> initItemList = new ArrayList<ItemDTO>();
                DataManager dataManager = new DataManager();
                try {
                    JSONArray jsonArray = service.consumesRest(getApplicationContext(),"/dimensionColumns?dimension="+dimension);
                    List<String> columnsDimensions = dataManager.getColumnsDimension(jsonArray);
                    initItemList = getInitViewItemDtoList(columnsDimensions);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                final ListItemAdapter listViewDataAdapter = new ListItemAdapter(initItemList, getApplicationContext());

                listViewDataAdapter.notifyDataSetChanged();

                listViewWithCheckbox.setAdapter(listViewDataAdapter);

                final List<ItemDTO> itemsChecked = new ArrayList<ItemDTO>();
                listViewWithCheckbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {
                        Object itemObject = adapterView.getAdapter().getItem(itemIndex);
                        ItemDTO itemDto = (ItemDTO) itemObject;
                        CheckBox itemCheckbox = (CheckBox) view.findViewById(R.id.list_view_item_checkbox);

                        if(itemDto.isChecked())
                        {
                            itemCheckbox.setChecked(false);
                            itemDto.setChecked(false);
                            itemsChecked.remove(itemDto);


                        }else
                        {
                            itemCheckbox.setChecked(true);
                            itemDto.setChecked(true);
                            itemsChecked.add(itemDto);

                        }

                    }
                });


                confirm_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int i = 0;
                        ArrayList<String> columns = new ArrayList<String>();
                        while (i<itemsChecked.size()){
                            if(itemsChecked.get(i).isChecked()){
                                System.out.println(itemsChecked.get(i).getText());
                                int index = findDimension(dimensionList,dimension);
                                columns.add(itemsChecked.get(i).getText());
                                dimensionList.get(index).setColumns(columns);
                            }

                            i++;

                        }
                        dialog.dismiss();

                    }
                });

            }
        });


        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(20,30,20,0);
        dimItem.setLayoutParams(lp);

        linearLayout.addView(dimItem, lp);

    }



    //locate measure item's index
    public int findMeasure(List<ItemMeasure> list, String measure){

        int i=0;
        int indeex = 0;
        while (i<list.size()){
            if(list.get(i).getMeasure() == measure){
                indeex = list.indexOf(list.get(i));
            }
            i++;
        }

        return indeex;
    }

    //locate dimension item's index
    public int findDimension(List<ItemDimension> list, String dimension){

        int i=0;
        int indeex = 0;
        while (i<list.size()){
            if(list.get(i).getDimension() == dimension){
                indeex = list.indexOf(list.get(i));
            }
            i++;
        }

        return indeex;
    }

    //verify the filling measures
    public boolean validateMeasures(List<ItemMeasure> list){

        boolean b = true;
        int i = 0;
        while (i<list.size()){
            if(list.get(i).getFunction() == null){
                b = false;
            }
            i++;
        }
        return b;
    }


    //verify the filling dimensions
    public boolean validateDimensions(List<ItemDimension> list){

        boolean b = true;
        int i = 0;
        while (i<list.size()){
            if(list.get(i).getColumns() == null){
                b = false;
            }else{
                int j = 0;
                while (j<list.get(i).getColumns().size()){
                    if(list.get(i).getColumns().get(j) == null){
                        b = false;
                    }
                    j++;
                }
            }

          i++;
        }
        return b;
    }

    //throw an alert
    public void throwingAlert(String msg){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Warning..!!!");
        alertDialogBuilder.setIcon(R.drawable.problem);
        alertDialogBuilder.setMessage(msg);

        alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();

            }
        });

        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //validate columns and rows contraints
    public boolean validateAxes(AxeMeasure axeMeasure, AxeDimension axeDimension, List<ItemMeasure> measureList, List<ItemDimension> dimensionList){
        boolean b = true;

        if(axeMeasure.getRole().equals("rows") && measureList.size() > 1){
            b = false;
        };

        if(axeDimension.getRole().equals("rows") && dimensionList.size() > 1){
            b = false;
        };

        return b;

    }

    //initialise the list with items checked
    public void fillingCheck(List<ItemDTO> list, List<String> items){

        int k = 0;
        while(k<items.size()){

            int i = 0;
            while (i<list.size()){
                if(list.get(i).getText() == items.get(k)){
                    list.get(i).setChecked(true);
                }
                i++;
            }
            k++;
        }

    }

    //get the String items from embedded Measures list
    public List<String> getStringMeasures(List<ItemMeasure> list){
        List<String> items = new ArrayList<String>();

        int i = 0;
        while (i<list.size()){
            items.add(new String(list.get(i).getMeasure()));
            i++;
        }

        return items;

    }

    //get the String items from embedded Measures list
    public List<String> getStringDimension(List<ItemDimension> list){
        List<String> items = new ArrayList<String>();

        int i = 0;
        while (i<list.size()){
            items.add(new String(list.get(i).getDimension()));
            i++;
        }

        return items;

    }




}

