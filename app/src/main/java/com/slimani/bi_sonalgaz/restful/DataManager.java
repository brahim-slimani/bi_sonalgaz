package com.slimani.bi_sonalgaz.restful;

import android.content.Context;
import android.database.Cursor;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.slimani.bi_sonalgaz.adhoc.chartsFragments.CustomCrossDataEntry;
import com.slimani.bi_sonalgaz.adhoc.chartsFragments.CustomDataEntry;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.AxeMeasure;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.FilterColumn;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.ItemDimension;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.ItemMeasure;
import com.slimani.bi_sonalgaz.paramsSQLite.Db_schemaOLAP;
import com.slimani.bi_sonalgaz.paramsSQLite.Db_server;
import com.slimani.bi_sonalgaz.restful.pojoRest.PojoReport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    public DataManager() {
    }

    public String getCurrentCube(Context context){
        Db_schemaOLAP dbm = new Db_schemaOLAP(context);

        String cube = null;
        Cursor cur = dbm.getAll();
        if (cur.moveToFirst()) {
            do {
                try {
                    cube = cur.getString(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cur.moveToNext());
        }
        return cube;
    }

    public String getCurrentIPaddress(Context context){
        Db_server dbm = new Db_server(context);

        String ip = null;
        Cursor cur = dbm.getServer();
        if (cur.moveToFirst()) {
            do {
                try {
                    ip = cur.getString(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cur.moveToNext());
        }
        return ip;
    }

    public String getCurrentPortNumber(Context context){
        Db_server dbm = new Db_server(context);

        String port = null;
        Cursor cur = dbm.getServer();
        if (cur.moveToFirst()) {
            do {
                try {
                    port = cur.getString(2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cur.moveToNext());
        }
        return port;
    }

    public List<String> getItemsCubes(JSONArray jsonArray) throws JSONException {

        List<String> list = new ArrayList<String>();

        for(int i = 0; i< jsonArray.length(); i++){
            JSONObject cube = jsonArray.getJSONObject(i);

            list.add(new String(cube.getString("fact")));
        }

        return list;

    }

    public List<String> getItemsMeasures(JSONArray jsonArray) throws JSONException {

        List<String> list = new ArrayList<String>();

        for(int i = 0; i< jsonArray.length(); i++){
            JSONObject cube = jsonArray.getJSONObject(i);

            list.add(new String(cube.getString("measure")));
        }

        return list;

    }

    public List<String> getItemsDimensions(JSONArray jsonArray) throws JSONException {

        List<String> list = new ArrayList<String>();

        for(int i = 0; i< jsonArray.length(); i++){
            JSONObject cube = jsonArray.getJSONObject(i);

            list.add(new String(cube.getString("dimension")));
        }

        return list;

    }

    public List<String> getColumnsDimension(JSONArray jsonArray) throws JSONException {

        List<String> list = new ArrayList<String>();

        for(int i = 0; i< jsonArray.length(); i++){
            JSONObject cube = jsonArray.getJSONObject(i);

            list.add(new String(cube.getString("column")));
        }

        return list;

    }


    public String buildQuery(List<ItemMeasure> measureList , List<ItemDimension> dimensionList, List<FilterColumn> filterColumnList, String factTable){

        if(filterColumnList.size()>0){
            String query = "";

            String measures = "";
            String columns = "";
            String joinsOperation = factTable;
            String groupBy = "group by (";
            String whereClause = "where ";
            String orderBy = "order by (";

            int i = 0;
            while (i < measureList.size()){
                if(i+1 == measureList.size()){

                    measures = measures + "trunc("+ measureList.get(i).getFunction() + "(" + measureList.get(i).getMeasure() + ")) as "+ measureList.get(i).getMeasure();
                }else {

                    measures = measures + "trunc("+ measureList.get(i).getFunction() + "(" + measureList.get(i).getMeasure() + ")) as "+ measureList.get(i).getMeasure() + ", ";
                }
                i++;
            }

            int j = 0;
            while(j < dimensionList.size()){
                int k = 0;
                while(k < dimensionList.get(j).getColumns().size()){
                    columns = columns + dimensionList.get(j).getColumns().get(k)+" ,";

                    if((j+1 == dimensionList.size()) && (k+1 == dimensionList.get(j).getColumns().size()) ){
                        groupBy = groupBy + dimensionList.get(j).getColumns().get(k)+" ) ";
                        orderBy = orderBy + dimensionList.get(j).getColumns().get(k)+" ) ";
                    }else{
                        groupBy = groupBy + dimensionList.get(j).getColumns().get(k)+" ,";
                        orderBy = orderBy + dimensionList.get(j).getColumns().get(k)+" ,";
                    }
                    k++;
                }

                joinsOperation = joinsOperation + " natural join " + dimensionList.get(j).getDimension();

                j++;
            }

            int k = 0;
            while (k<filterColumnList.size()){

                int s = 0;
                while (s<filterColumnList.get(k).getRows().size()){

                    if(k+1 == filterColumnList.size()){
                        if(s+1 == filterColumnList.get(k).getRows().size() ){
                            whereClause = whereClause + "(" +filterColumnList.get(k).getColumn() + " =  '"+
                                    filterColumnList.get(k).getRows().get(s) + "' ) ";
                        }else{
                            whereClause = whereClause + filterColumnList.get(k).getColumn() + " =  '"+
                                    filterColumnList.get(k).getRows().get(s) + "'  or ";
                        }
                        s++;

                    }else{

                        if(s+1 == filterColumnList.get(k).getRows().size() ){
                            whereClause = whereClause + "(" +filterColumnList.get(k).getColumn() + " =  '"+
                                    filterColumnList.get(k).getRows().get(s) + "' ) and ";
                        }else{
                            whereClause = whereClause + filterColumnList.get(k).getColumn() + " =  '"+
                                    filterColumnList.get(k).getRows().get(s) + "'  or ";
                        }
                        s++;

                    }



                }

                k++;
            }


            query = "select "+ columns + measures + " from " + joinsOperation + " " + whereClause + " " + groupBy+ " "+ orderBy + " ;";


            return query;



        }else{
            String query = "";

            String measures = "";
            String columns = "";
            String joinsOperation = factTable;
            String groupBy = "group by (";
            String orderBy = "order by (";

            int i = 0;
            while (i < measureList.size()){
                if(i+1 == measureList.size()){

                    measures = measures + "trunc("+ measureList.get(i).getFunction() + "(" + measureList.get(i).getMeasure() + ")) as "+ measureList.get(i).getMeasure();
                }else {

                    measures = measures + "trunc("+ measureList.get(i).getFunction() + "(" + measureList.get(i).getMeasure() + ")) as "+ measureList.get(i).getMeasure() + ", ";
                }
                i++;
            }

            int j = 0;
            while(j < dimensionList.size()){
                int k = 0;
                while(k < dimensionList.get(j).getColumns().size()){
                    columns = columns + dimensionList.get(j).getColumns().get(k)+" ,";

                    if((j+1 == dimensionList.size()) && (k+1 == dimensionList.get(j).getColumns().size()) ){
                        groupBy = groupBy + dimensionList.get(j).getColumns().get(k)+" ) ";
                        orderBy = orderBy + dimensionList.get(j).getColumns().get(k)+" ) ";
                    }else{
                        groupBy = groupBy + dimensionList.get(j).getColumns().get(k)+" ,";
                        orderBy = orderBy + dimensionList.get(j).getColumns().get(k)+" ,";
                    }
                    k++;
                }

                joinsOperation = joinsOperation + " natural join " + dimensionList.get(j).getDimension();

                j++;
            }


            query = "select "+ columns + measures + " from " + joinsOperation + " " + groupBy+ " "+ orderBy + " ;";


            return query;
        }


    }

    public List<String> getAdhocColumns(List<ItemMeasure> measureList, List<ItemDimension> dimensionList,
                                        AxeMeasure axeMeasure){
        List<String> columns = new ArrayList<>();

        if(axeMeasure.getRole().equals("columns")){
            int i = 0;
            while (i < measureList.size()){
                columns.add(new String(measureList.get(i).getMeasure()));
                i++;
            }
        }else if(axeMeasure.getRole().equals("rows")){
            int j = 0;
            while(j < dimensionList.size()){
                int k = 0;
                while(k < dimensionList.get(j).getColumns().size()){

                    columns.add(new String(dimensionList.get(j).getColumns().get(k)));

                    k++;
                }

                j++;
            }

        }

        return columns;

    }

    public List<String> getAdhocRows(List<ItemMeasure> measureList, List<ItemDimension> dimensionList,
                                        AxeMeasure axeMeasure){
        List<String> rows = new ArrayList<>();

        if(axeMeasure.getRole().equals("rows")){
            int i = 0;
            while (i < measureList.size()){
                rows.add(new String(measureList.get(i).getMeasure()));
                i++;
            }
        }else if(axeMeasure.getRole().equals("columns")){
            int j = 0;
            while(j < dimensionList.size()){
                int k = 0;
                while(k < dimensionList.get(j).getColumns().size()){

                    rows.add(new String(dimensionList.get(j).getColumns().get(k)));

                    k++;
                }

                j++;
            }

        }

        return rows;

    }

    public List<String> filterUnit(List<String> list, String roleUser){

        List<String> levelList = list;
        if(roleUser.equals("ROLE_AG")){
            int i = 0;

            while (i<levelList.size()){

              if(levelList.get(i).equals("code_dd")){
                  levelList.remove(levelList.get(i));
              }
              if(levelList.get(i).equals("dd")){
                  levelList.remove(levelList.get(i));
              }
              if(levelList.get(i).equals("code_sd")){
                  levelList.remove(levelList.get(i));
              }
              if(levelList.get(i).equals("sd")){
                  levelList.remove(levelList.get(i));
              }

                i++;
            }
        }

        if(roleUser.equals("ROLE_DD")){
            int i = 0;
            while (i<levelList.size()){
                if(levelList.get(i).equals("code_sd")){
                    levelList.remove(levelList.get(i));
                }
                if(levelList.get(i).equals("sd")){
                    levelList.remove(levelList.get(i));
                }
                i++;
            }
        }



        return levelList;

    }




    public List<DataEntry> parsingToListBar(JSONArray jsonArray, List<String> columns, List<String> rows) throws JSONException {
        List<DataEntry> list = new ArrayList<DataEntry>();

        String row = rows.get(0);
        String valueRows = "";

        int i = 0;
        JSONObject jsonObject = new JSONObject();
        while (i<jsonArray.length()){
            try {
                jsonObject = jsonArray.getJSONObject(i);
                valueRows = String.valueOf( jsonObject.get(row));

                if(columns.size() == 1){
                    list.add(new CustomDataEntry(valueRows, (Number) jsonObject.get(columns.get(0))));
                }

                if(columns.size() == 2){
                    list.add(new CustomDataEntry(valueRows, (Number) jsonObject.get(columns.get(0)),  (Number) jsonObject.get(columns.get(1)) ));
                }

                if(columns.size() == 3){
                    list.add(new CustomDataEntry(valueRows, (Number) jsonObject.get(columns.get(0)),
                            (Number) jsonObject.get(columns.get(1)), (Number) jsonObject.get(columns.get(2))  ));
                }

            }catch (ClassCastException e){
                if(columns.size() == 1){
                    list.add(new CustomDataEntry(valueRows, (Number) jsonObject.get(columns.get(0))));
                }

                if(columns.size() == 2){
                    list.add(new CustomDataEntry(valueRows, (Number) jsonObject.get(columns.get(0)),  String.valueOf(jsonObject.get(columns.get(1)))  ));
                }

                if(columns.size() == 3) {
                    list.add(new CustomDataEntry(valueRows, (Number) jsonObject.get(columns.get(0)),
                            String.valueOf(jsonObject.get(columns.get(1))), String.valueOf(jsonObject.get(columns.get(2))) ));
                }

            }




            i++;
        }

        return list;

    }

    public List<DataEntry> parsingToListPie(JSONArray jsonArray, String column, List<String> rows) throws JSONException {
        List<DataEntry> list = new ArrayList<DataEntry>();

        String row = rows.get(0);
        String valueRows = "";

        int i = 0;
        JSONObject jsonObject = new JSONObject();
        while (i<jsonArray.length()){
            try {
                jsonObject = jsonArray.getJSONObject(i);
                valueRows = String.valueOf(jsonObject.get(row));

                list.add(new ValueDataEntry(valueRows, (Number) jsonObject.get(column)));

            }catch (ClassCastException e){

                list.add(new CustomDataEntry(valueRows,0,  String.valueOf(jsonObject.get(column))));


            }


            i++;
        }

        return list;

    }



    public List<DataEntry> parsingToListTable(JSONArray jsonArray, List<String> columns, List<String> rows, Context ctx) throws JSONException {
        List<DataEntry> finalList = new ArrayList<DataEntry>();
        List<DataEntry> list = new ArrayList<DataEntry>();
        List<DataEntry> list2 = new ArrayList<DataEntry>();
        List<DataEntry> list3 = new ArrayList<DataEntry>();

        String row = rows.get(0);
        String valueRows = "";
        String fill = "#b9b9b9";

        int i = 0;
        JSONObject jsonObject = new JSONObject();
        while (i<jsonArray.length()){
            jsonObject = jsonArray.getJSONObject(i);
            valueRows = String.valueOf(jsonObject.get(row));

            try{
                if(columns.size() == 1){
                    Number numberValue = (Number) jsonObject.get(columns.get(0));
                    Integer intValue = numberValue.intValue();
                    list.add(new CustomCrossDataEntry(columns.get(0), valueRows, intValue , fill) );
                }

                if(columns.size() == 2){
                    Number numberValue = (Number) jsonObject.get(columns.get(0));
                    Integer intValue = numberValue.intValue();

                    Number numberValue2 = (Number) jsonObject.get(columns.get(1));
                    Integer intValue2 = numberValue2.intValue();

                    list.add(new CustomCrossDataEntry(columns.get(0), valueRows, intValue, fill) );
                    list2.add(new CustomCrossDataEntry(columns.get(1), valueRows, intValue2, fill) );

                }

                if(columns.size() == 3){
                    Number numberValue = (Number) jsonObject.get(columns.get(0));
                    Integer intValue = numberValue.intValue();

                    Number numberValue2 = (Number) jsonObject.get(columns.get(1));
                    Integer intValue2 = numberValue2.intValue();

                    Number numberValue3 = (Number) jsonObject.get(columns.get(2));
                    Integer intValue3 = numberValue3.intValue();

                    list.add(new CustomCrossDataEntry(columns.get(0), valueRows, intValue, fill) );
                    list2.add(new CustomCrossDataEntry(columns.get(1), valueRows, intValue2, fill) );
                    list3.add(new CustomCrossDataEntry(columns.get(2), valueRows, intValue3, fill) );

                }

            }catch (ClassCastException e){
                Toast.makeText(ctx, "The columns must be in mesurable value !", Toast.LENGTH_SHORT).show();
            }



            i++;
        }

        finalList = list;


        if(columns.size() > 1){
            finalList.addAll(list2);

        }

        if(columns.size() > 2){
            finalList.addAll(list3);

        }


        return finalList;

    }


    public List<String> simpleItemsMeasure(List<ItemMeasure> measureList){

        List<String> list = new ArrayList<>();
        int i = 0;
        while (i<measureList.size()){
            list.add(new String(measureList.get(i).getMeasure()));
            i++;
        }

        return list;

    }

    public List<String> simpleItemsDimension(List<ItemDimension> dimensionList){

        List<String> list = new ArrayList<>();
        int i = 0;
        while (i<dimensionList.size()){
            list.add(new String(dimensionList.get(i).getDimension()));
            i++;
        }

        return list;

    }


    public List<String> getItems(JSONArray jsonArray){
        List<String> list = new ArrayList<>();

        int i = 0;
        while (i<jsonArray.length()){
            try {
                list.add(new String(jsonArray.get(i).toString()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            i++;
        }

        return list;
    }

    public List<PojoReport> getReports(JSONArray jsonArray) throws JSONException {
        List<PojoReport> list = new ArrayList<>();

        int i = 0;
        while (i<jsonArray.length()){
            JSONObject object = jsonArray.getJSONObject(i);
            list.add(new PojoReport(object.getString("title"), object.getString("context"), object.getString("type"),  object.getString("columns"),  object.getString("rows")));
            i++;
        }

        return list;
    }

    public String convertAxes(List<String> list) {

        JSONArray jsonArray = new JSONArray();

        int i = 0;
        while (i<list.size()){
            jsonArray.put(list.get(i));
            i++;
        }

        return jsonArray.toString();

    }

    public List<String> getAxes(String axe) throws JSONException {

        JSONArray jsonArray = new JSONArray(axe);

        List<String> list = new ArrayList<>();

        int i = 0;
        while (i<jsonArray.length()){
            list.add(jsonArray.getString(i));
            i++;
        }

        return list;

    }

    public String buildMV(String name, String query) throws JSONException {

        String view = new String();

        view = "create materialized view "+name+" as "+query;

       return view;

    }








}
