package com.slimani.bi_sonalgaz.restful;

import android.content.Context;
import android.database.Cursor;

import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.slimani.bi_sonalgaz.adhoc.chartsFragments.CustomDataEntry;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.AxeDimension;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.AxeMeasure;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.ItemDimension;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.ItemMeasure;
import com.slimani.bi_sonalgaz.param.Db_manager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    public DataManager() {
    }

    public String getCurrentCube(Context context){
        Db_manager dbm = new Db_manager(context);

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

    public String buildQuery(List<ItemMeasure> measureList , List<ItemDimension> dimensionList, String factTable){

      String query = "";

      String measures = "";
      String columns = "";
      String joinsOperation = factTable;
      String groupBy = "group by (";
      String orderBy = "order by (";

      int i = 0;
      while (i < measureList.size()){
          if(i+1 == measureList.size()){

              measures = measures + measureList.get(i).getFunction() + "(" + measureList.get(i).getMeasure() + ") as "+ measureList.get(i).getMeasure();
          }else {

              measures = measures + measureList.get(i).getFunction() + "(" + measureList.get(i).getMeasure() + ") as "+ measureList.get(i).getMeasure() + ", ";
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












}
