package com.slimani.bi_sonalgaz.adhoc;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Dimension;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.slimani.bi_sonalgaz.R;
import com.slimani.bi_sonalgaz.adhoc.chartsFragments.ColumnbarFrgament;
import com.slimani.bi_sonalgaz.adhoc.chartsFragments.CrosstableFragment;
import com.slimani.bi_sonalgaz.adhoc.chartsFragments.CustomPieContext;
import com.slimani.bi_sonalgaz.adhoc.chartsFragments.LineChartFragment;
import com.slimani.bi_sonalgaz.adhoc.chartsFragments.PiechartFragment;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.AxeDimension;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.AxeMeasure;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.FilterColumn;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.ItemDTO;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.ItemDimension;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.ItemMeasure;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.ListItemAdapter;
import com.slimani.bi_sonalgaz.home.HomeActivity;
import com.slimani.bi_sonalgaz.restful.DataManager;
import com.slimani.bi_sonalgaz.restful.Service;
import com.slimani.bi_sonalgaz.restful.pojoRest.PojoReport;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.slimani.bi_sonalgaz.auth.LoginActivity.loggedUser;
import static com.slimani.bi_sonalgaz.auth.LoginActivity.roleUser;

public class AdhocActivity extends AppCompatActivity {

    public static JSONArray dataJS = new JSONArray();
    public static List<String> adhocColumns = new ArrayList<>();
    public static List<String> adhocRows = new ArrayList<>();
    public static CustomPieContext customPieContext = new CustomPieContext();
    String currentColumn;
    String typeView = null;

    ItemDimension item = new ItemDimension();

    List<String> rowsColumns = new ArrayList<>();

    FilterColumn filterColumn = new FilterColumn();


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
        final Button clean_btn = (Button) findViewById(R.id.clean_btn);
        final Button filter_btn = (Button) findViewById(R.id.filter_btn);


        ArrayList<ItemMeasure> listMeasures = new ArrayList<ItemMeasure>();
        ArrayList<ItemDimension> listDimensions = new ArrayList<ItemDimension>();
        AxeMeasure axeMeasureObject = new AxeMeasure();
        AxeDimension axeDimensionObject = new AxeDimension();


        DataManager dataManager = new DataManager();

        List<ItemDTO> itemsCheckedMeasure = new ArrayList<ItemDTO>();
        List<ItemDTO> itemsCheckedDimensions = new ArrayList<ItemDTO>();

        List<FilterColumn> columnsListFilter = new ArrayList<>();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                Service service = new Service(getApplicationContext());

                JSONArray msData = service.consumesRest("/measures?factTable="+dataManager.getCurrentCube(getApplicationContext()));
                JSONArray dmData = service.consumesRest("/dimensions?factTable="+dataManager.getCurrentCube(getApplicationContext()));


                AdhocActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //browse and filling measures
                        measures_btn.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                final Dialog dialog = new Dialog(AdhocActivity.this);
                                dialog.setContentView(R.layout.list_measures);
                                Button confirm_btn = dialog.findViewById(R.id.confirm_measures);


                                final ListView listViewWithCheckbox = (ListView) dialog.findViewById(R.id.list_view_with_checkbox);

                                List<ItemDTO> initItemList = new ArrayList<ItemDTO>();

                                try {

                                    List<String> itemsMeasures = dataManager.getItemsMeasures(msData);
                                    initItemList = getInitViewItemDtoList(itemsMeasures);

                                    fillingCheck(initItemList, dataManager.simpleItemsMeasure(listMeasures));


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                                final ListItemAdapter listViewDataAdapter = new ListItemAdapter(initItemList, getApplicationContext());

                                listViewDataAdapter.notifyDataSetChanged();

                                listViewWithCheckbox.setAdapter(listViewDataAdapter);



                                dialog.show();save_btn.setEnabled(false);
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
                                            cleanCheckedItems(itemsCheckedMeasure,itemDto.getText());
                                            itemsCheckedMeasure.remove(itemDto);

                                        }else
                                        {
                                            itemCheckbox.setChecked(true);
                                            itemDto.setChecked(true);
                                            itemsCheckedMeasure.add(itemDto);

                                        }


                                    }
                                });



                                confirm_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        measures_content.removeAllViews();
                                        listMeasures.clear();

                                        int i = 0;
                                        while (i<itemsCheckedMeasure.size()){

                                            listMeasures.add(new ItemMeasure(itemsCheckedMeasure.get(i).getText(),null));
                                            addMeasureItems(measures_content,itemsCheckedMeasure.get(i).getText(),listMeasures);

                                            i++;

                                        }
                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Click on the measure to specify the aggregate's function !", Toast.LENGTH_LONG).show();


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
                                dialog.show();save_btn.setEnabled(false);
                                Button confirm_btn = dialog.findViewById(R.id.confirm_dimensions);


                                final ListView listViewWithCheckbox = (ListView) dialog.findViewById(R.id.list_view_with_checkbox);

                                List<ItemDTO> initItemList = new ArrayList<ItemDTO>();
                                try {
                                    List<String> itemsDimensions = dataManager.getItemsDimensions(dmData);
                                    initItemList = getInitViewItemDtoList(itemsDimensions);

                                    fillingCheck(initItemList, dataManager.simpleItemsDimension(listDimensions));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                final ListItemAdapter listViewDataAdapter = new ListItemAdapter(initItemList, getApplicationContext());

                                listViewDataAdapter.notifyDataSetChanged();

                                listViewWithCheckbox.setAdapter(listViewDataAdapter);

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
                                            cleanCheckedItems(itemsCheckedDimensions, itemDto.getText());
                                            itemsCheckedDimensions.remove(itemDto);


                                        }else
                                        {
                                            itemCheckbox.setChecked(true);
                                            itemDto.setChecked(true);
                                            itemsCheckedDimensions.add(itemDto);

                                        }

                                    }
                                });



                                confirm_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dimensions_content.removeAllViews();
                                        listDimensions.clear();

                                        int i = 0;
                                        while (i<itemsCheckedDimensions.size()){

                                            listDimensions.add(new ItemDimension(itemsCheckedDimensions.get(i).getText(),null));
                                            addDimItems(dimensions_content,itemsCheckedDimensions.get(i).getText(), listDimensions);


                                            i++;

                                        }
                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Click on the dimensions to specify their columns !", Toast.LENGTH_LONG).show();


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


                                Button confirm_config_btn = dialog.findViewById(R.id.confirm_config);

                                final RadioGroup measuresGroup = dialog.findViewById(R.id.measures_group);
                                final RadioGroup dimensionGroup = dialog.findViewById(R.id.dimensions_group);

                                final RadioButton colMeasure = dialog.findViewById(R.id.columns_measures);
                                final RadioButton rowMeasure = dialog.findViewById(R.id.rows_measures);

                                final RadioButton colDimension = dialog.findViewById(R.id.columns_dimensions);
                                final RadioButton rowDimension = dialog.findViewById(R.id.rows_dimensions);

                                if(axeMeasureObject.getRole() != null){
                                    if(axeMeasureObject.getRole().equals("columns")){
                                        colMeasure.setChecked(true);
                                        rowDimension.setChecked(true);
                                    }else if(axeMeasureObject.getRole().equals("rows")) {
                                        rowMeasure.setChecked(true);
                                        colDimension.setChecked(true);
                                    }
                                }


                                dialog.show();
                                confirm_config_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        save_btn.setEnabled(false);
                                        filter_btn.setEnabled(true);

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
                                        if(axeDimensionObject.getRole().equals("columns")){
                                            cleanFilterList(adhocColumns,columnsListFilter);
                                        }else{
                                            cleanFilterList(adhocRows,columnsListFilter);
                                        }

                                        String cube = dataManager.buildQuery(listMeasures,listDimensions, columnsListFilter, dataManager.getCurrentCube(getApplicationContext()));
                                        System.out.println("query : "+cube);

                                        adhocColumns = dataManager.getAdhocColumns(listMeasures,listDimensions,axeMeasureObject);
                                        adhocRows = dataManager.getAdhocRows(listMeasures,listDimensions,axeMeasureObject);

                                        if(adhocRows.size()>3){
                                            adhocRows = cleanRows(adhocRows);
                                        }


                                        if(adhocRows.size()>1){
                                            adhocRows = adhocRows.subList(0,1);
                                        }

                                        final Dialog dialog = new Dialog(AdhocActivity.this);
                                        dialog.setContentView(R.layout.popup_typechart);
                                        dialog.setTitle("Select the type of chart");

                                        RadioButton radioCrosstable = dialog.findViewById(R.id.crosstable);
                                        RadioButton radioPie = dialog.findViewById(R.id.piechart);
                                        RadioButton radioColumn = dialog.findViewById(R.id.columnbarchart);
                                        RadioButton radioLine = dialog.findViewById(R.id.linechart);


                                        if(typeView != null){

                                            if(typeView.equals("crosstable")){
                                                radioCrosstable.setChecked(true);
                                            }else  if(typeView.equals("piechart")){
                                                radioPie.setChecked(true);
                                            }else  if(typeView.equals("columnbarchart")){
                                                radioColumn.setChecked(true);
                                            }else  if(typeView.equals("linechart")){
                                                radioLine.setChecked(true);
                                            }

                                        }

                                        dialog.show();
                                        Button ok_btn = dialog.findViewById(R.id.confirm_typechart_btn);
                                        ok_btn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                final RadioGroup chartGroup = dialog.findViewById(R.id.typechart_radiogroup);
                                                int radioButtonID = chartGroup.getCheckedRadioButtonId();
                                                View radioButton = chartGroup.findViewById(radioButtonID);

                                                save_btn.setEnabled(true);

                                                Thread subThread = new Thread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        dataJS = service.consumesRest("?query="+cube);

                                                        AdhocActivity.this.runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                try {
                                                                    typeView = getResources().getResourceEntryName(radioButton.getId());
                                                                    dialog.dismiss();

                                                                    if(typeView.equals("crosstable")){
                                                                        nextResult_btn.setEnabled(false);
                                                                        loadFragment(new CrosstableFragment());

                                                                    }else if(typeView.equals("piechart")){

                                                                        try {
                                                                            currentColumn = adhocColumns.get(0);
                                                                            customPieContext.setColumn(adhocColumns.get(0));
                                                                            nextResult_btn.setEnabled(true);
                                                                            customPieContext.setDataEntryList(dataManager.parsingToListPie(dataJS,adhocColumns.get(0),adhocRows));
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }

                                                                        loadFragment(new PiechartFragment());

                                                                    }else if(typeView.equals("columnbarchart")){
                                                                        nextResult_btn.setEnabled(false);
                                                                        loadFragment(new ColumnbarFrgament());

                                                                    }else if(typeView.equals("linechart")){
                                                                        nextResult_btn.setEnabled(false);
                                                                        loadFragment(new LineChartFragment());

                                                                    }

                                                                }catch (NullPointerException n){
                                                                    Toast.makeText(AdhocActivity.this, "You must check one type chart !", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });


                                                    }
                                                });
                                                subThread.start();



                                            }
                                        });
                                    }


                                }


                            }
                        });


                        //save the view as report
                        save_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                final Dialog dialog = new Dialog(AdhocActivity.this);
                                dialog.setContentView(R.layout.popup_save_report);

                                dialog.show();

                                Button save_report_btn = (Button) dialog.findViewById(R.id.save_report_btn);
                                save_report_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        EditText title_report = (EditText) dialog.findViewById(R.id.title_report);

                                        if(title_report.getText().toString().equals("")){
                                            Toast.makeText(getApplicationContext(),"You must enter title to the report", Toast.LENGTH_SHORT).show();
                                        }else{

                                            if(axeDimensionObject.getRole().equals("columns")){
                                                cleanFilterList(adhocColumns,columnsListFilter);
                                            }else{
                                                cleanFilterList(adhocRows,columnsListFilter);
                                            }

                                            String context = dataManager.buildQuery(listMeasures,listDimensions, columnsListFilter, dataManager.getCurrentCube(getApplicationContext()));
                                            PojoReport pojoReport = new PojoReport(title_report.getText().toString(),
                                                    context,typeView, dataManager.convertAxes(adhocColumns), dataManager.convertAxes(adhocRows), loggedUser);

                                            System.out.println(loggedUser);



                                            Thread thread = new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Service service = new Service(getApplicationContext());
                                                    try {
                                                        String view = dataManager.buildMV("view_"+pojoReport.getTitle(), pojoReport.getContext());

                                                        service.createMV("/createMV?queryMV="+view);
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                    pojoReport.setContext("select * from "+ "view_"+pojoReport.getTitle());

                                                    String response = service.saveReport("/saveReport",pojoReport);

                                                    if(response.equals("Operation failed, REST issue !")){


                                                        AdhocActivity.this.runOnUiThread(new Runnable() {
                                                            public void run() {
                                                                Toast.makeText(getApplicationContext(), "Operation failed, this title already exists !", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });


                                                    }else if(response.equals("Report created successfully !")){

                                                        AdhocActivity.this.runOnUiThread(new Runnable() {
                                                            public void run() {
                                                                dialog.dismiss();
                                                                save_btn.setEnabled(false);
                                                                successAlert("The report was saved successfully");
                                                            }
                                                        });


                                                    }

                                                }
                                            });
                                            thread.start();
                                        }

                                    }
                                });



                            }
                        });



                        //get the next pie chart as view in the case of more than one column
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



                        //clean the work space
                        clean_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                int i = 0;
                                while(i<columnsListFilter.size()){
                                    System.out.println("column is "+columnsListFilter.get(i).getColumn());
                                    int j = 0;
                                    while(j<columnsListFilter.get(i).getRows().size()){
                                        System.out.println(columnsListFilter.get(i).getRows().get(j));
                                        j++;
                                    }
                                    i++;
                                }


                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AdhocActivity.this);
                                alertDialogBuilder.setTitle("Confirmation..?");
                                alertDialogBuilder.setIcon(R.drawable.ask);
                                alertDialogBuilder.setMessage("Are you sure that you want to clean the work space ?");


                                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        arg0.dismiss();

                                        Intent intent = new Intent(AdhocActivity.this, AdhocActivity.class);
                                        startActivity(intent);
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
                        });




                        //make filter over columns of dimensions
                        filter_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final Dialog dialog = new Dialog(AdhocActivity.this);
                                dialog.setContentView(R.layout.list_filter_columns);

                                final Button confirm_btn = (Button) dialog.findViewById(R.id.filter_ok_btn);
                                final ListView columnsList = (ListView) dialog.findViewById(R.id.list_view_columns);

                                adhocColumns = dataManager.getAdhocColumns(listMeasures,listDimensions,axeMeasureObject);
                                adhocRows = dataManager.getAdhocRows(listMeasures,listDimensions,axeMeasureObject);

                                List<String> columns = new ArrayList<>();
                                if(axeDimensionObject.getRole().equals("columns")){
                                    columns = adhocColumns;
                                }else if(axeDimensionObject.getRole().equals("rows")){
                                    columns = adhocRows;
                                }

                                ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, columns);
                                columnsList.setAdapter(adapter);


                                dialog.show();

                                columnsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Object itemObject = parent.getAdapter().getItem(position);
                                        String column = (String) itemObject.toString();

                                        Thread subthread = new Thread(new Runnable() {
                                            @Override
                                            public void run() {

                                                int i = 0;
                                                while (i<listDimensions.size()){
                                                    int j = 0;
                                                    while (j<listDimensions.get(i).getColumns().size()){
                                                        if(listDimensions.get(i).getColumns().get(j).equals(column)){
                                                            ArrayList<String> col_dim = new ArrayList<>();
                                                            col_dim.add(listDimensions.get(i).getColumns().get(j));
                                                            item = new ItemDimension(listDimensions.get(i).getDimension(), col_dim);
                                                            //dimListFilter.add(item);
                                                        }
                                                        j++;
                                                    }

                                                    i++;
                                                }



                                                Service service = new Service(getApplicationContext());

                                                try {
                                                    rowsColumns = dataManager.getColumnsDimension(service.consumesRest("/columnRow?table="+item.getDimension()+"&column="+item.getColumns().get(0) ) );
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                filterColumn.setDimension(item.getDimension());
                                                filterColumn.setColumn(column);
                                                filterColumn.setRows(new ArrayList<>());

                                                List<ItemDTO> itemsCheckedRows = new ArrayList<ItemDTO>();


                                                AdhocActivity.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        final Dialog dialog = new Dialog(AdhocActivity.this);
                                                        dialog.setContentView(R.layout.rows_column);
                                                        Button confirm_btn = dialog.findViewById(R.id.confirm_filter_column);



                                                        ListView listViewWithCheckboxRow = (ListView) dialog.findViewById(R.id.list_view_with_checkbox_rows);

                                                        List<ItemDTO> initItemList = new ArrayList<ItemDTO>();

                                                        try {

                                                            initItemList = getInitViewItemDtoList(rowsColumns);

                                                            List<String> list = columnsListFilter.get(findFilterColumn(columnsListFilter, column)).getRows();

                                                            fillingCheck(initItemList, list);


                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }



                                                        final ListItemAdapter listViewDataAdapter = new ListItemAdapter(initItemList, getApplicationContext());

                                                        listViewDataAdapter.notifyDataSetChanged();

                                                        listViewWithCheckboxRow.setAdapter(listViewDataAdapter);


                                                        dialog.show();

                                                        listViewWithCheckboxRow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                            @Override
                                                            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {
                                                                Object itemObject = adapterView.getAdapter().getItem(itemIndex);
                                                                ItemDTO itemDto = (ItemDTO) itemObject;
                                                                CheckBox itemCheckbox = (CheckBox) view.findViewById(R.id.list_view_item_checkbox);

                                                                if(itemDto.isChecked())
                                                                {
                                                                    itemCheckbox.setChecked(false);
                                                                    itemDto.setChecked(false);
                                                                    cleanCheckedItems(itemsCheckedRows,itemDto.getText());
                                                                    itemsCheckedRows.remove(itemDto);

                                                                }else
                                                                {
                                                                    itemCheckbox.setChecked(true);
                                                                    itemDto.setChecked(true);
                                                                    itemsCheckedRows.add(itemDto);

                                                                }


                                                            }
                                                        });




                                                        confirm_btn.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {

                                                                int i = 0;
                                                                int index = findFilterColumn(columnsListFilter,column);
                                                                List<String> filterRows = new ArrayList<>();
                                                                while (i<itemsCheckedRows.size()){

                                                                    filterRows.add(new String(itemsCheckedRows.get(i).getText()));
                                                                    i++;

                                                                }

                                                                if(findFilterColumn(columnsListFilter, column)>-1){
                                                                    columnsListFilter.get(index).setRows(filterRows);
                                                                }else{

                                                                    columnsListFilter.add(new FilterColumn(filterColumn.getDimension(),filterColumn.getColumn()
                                                                            ,filterRows));

                                                                }

                                                                dialog.dismiss();
                                                            }
                                                        });




                                                    }
                                                });



                                            }
                                        });
                                        subthread.start();




                                    }
                                });

                                confirm_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });





                    }
                });

            }
        });
        thread.start();






    }

    //return to MainActivity
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AdhocActivity.this, HomeActivity.class);
        startActivity(intent);
    }



    //changing fragments
    private void loadFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.workspace_frame, fragment);
        fragmentTransaction.commit();
    }



    //initialise the Items
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

        List<ItemDTO> itemsCheckedColumns = new ArrayList<ItemDTO>();


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Service service = new Service(getApplicationContext());
                JSONArray jsonArray = service.consumesRest("/dimensionColumns?dimension="+dimension);

                AdhocActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dimItem.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View view) {

                                final Dialog dialog = new Dialog(AdhocActivity.this);
                                dialog.setContentView(R.layout.columns_dimension);
                                dialog.setTitle("Select the columns dimension");
                                TextView header = (TextView) dialog.findViewById(R.id.header);
                                header.setText(header.getText()+dimension);


                                Button confirm_btn = dialog.findViewById(R.id.confirm_columns_dimension);

                                final ListView listViewWithCheckbox = (ListView) dialog.findViewById(R.id.list_view_with_checkbox);

                                List<ItemDTO> initItemList = new ArrayList<ItemDTO>();
                                DataManager dataManager = new DataManager();
                                try {

                                    List<String> columnsDimensions = dataManager.getColumnsDimension(jsonArray);
                                    if(dimension.equals("dm_organisme")){

                                        columnsDimensions = dataManager.filterUnit(columnsDimensions,roleUser);

                                    }

                                    initItemList = getInitViewItemDtoList(columnsDimensions);

                                    List<String> list = dimensionList.get(findDimension(dimensionList, dimension)).getColumns();

                                    fillingCheck(initItemList, list);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                final ListItemAdapter listViewDataAdapter = new ListItemAdapter(initItemList, getApplicationContext());

                                listViewDataAdapter.notifyDataSetChanged();

                                listViewWithCheckbox.setAdapter(listViewDataAdapter);

                                dialog.show();
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
                                            cleanCheckedItems(itemsCheckedColumns, itemDto.getText());
                                            itemsCheckedColumns.remove(itemDto);


                                        }else
                                        {
                                            itemCheckbox.setChecked(true);
                                            itemDto.setChecked(true);
                                            itemsCheckedColumns.add(itemDto);

                                        }

                                    }
                                });


                                confirm_btn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        int i = 0;
                                        int index = findDimension(dimensionList,dimension);
                                        ArrayList<String> columns = new ArrayList<>();
                                        while (i<itemsCheckedColumns.size()){

                                            columns.add(itemsCheckedColumns.get(i).getText());
                                            i++;

                                        }
                                        dimensionList.get(index).setColumns(columns);
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
                });
            }
        });
        thread.start();





    }



    //locate measure item's index
    public int findMeasure(List<ItemMeasure> list, String measure){

        int i=0;
        int index = 0;
        while (i<list.size()){
            if(list.get(i).getMeasure().equals(measure)){
                index = list.indexOf(list.get(i));
            }
            i++;
        }

        return index;
    }

    //locate dimension item's index
    public int findDimension(List<ItemDimension> list, String dimension){

        int i=0;
        int index = 0;
        while (i<list.size()){
            if(list.get(i).getDimension().equals(dimension)){
                index = list.indexOf(list.get(i));
            }
            i++;
        }

        return index;
    }


    //locate filterColumn item's index
    public int findFilterColumn(List<FilterColumn> list, String column){

        int i=0;
        int index = -1;
        while (i<list.size()){
            if(list.get(i).getColumn().equals(column)){
                index = list.indexOf(list.get(i));
            }
            i++;
        }

        return index;
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

    public void successAlert(String msg){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Successful !");
        alertDialogBuilder.setIcon(R.drawable.success);
        alertDialogBuilder.setMessage(msg);

        alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();

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

        if(axeDimension.getRole().equals("rows") && dimensionList.size() > 1 && dimensionList.size() < 4){
            b = false;
        };

        return b;

    }

    //initialise the list with items checked
    public void fillingCheck(List<ItemDTO> list, List<String> items){

        if(items.size()>0){
            int k = 0;
            while(k<items.size()){

                int i = 0;
                while (i<list.size()){
                    if(list.get(i).getText().equals(items.get(k))){
                        list.get(i).setChecked(true);
                    }
                    i++;
                }
                k++;
            }
        }

    }


    public void cleanCheckedItems(List<ItemDTO> list,String item){

        int i = 0;
        while (i<list.size()){
            if(list.get(i).getText().equals(item)){
                list.remove(list.get(i));
            }
            i++;
        }
    }


    public void cleanFilterList(List<String> columns, List<FilterColumn> filterColumnList){

        int i = 0;

        while (i<filterColumnList.size()){

            int j = 0;
            boolean b = false;
            while (j<columns.size()){

                if(filterColumnList.get(i).getColumn().equals(columns.get(j))){
                    b = true;
                }
                j++;

                if(j == columns.size() && b == false){
                    filterColumnList.remove(filterColumnList.get(i));
                }


            }

            i++;
        }
    }

    public List<String> cleanRows(List<String> list){
        List<String> l = new ArrayList<>();

        int i = 0;
        while(i<list.size()){
            if(list.get(i).equals("libelle_mois")){
              l.add(new String(list.get(i)));
            }

            i++;
        }

       return l;

    }






}

