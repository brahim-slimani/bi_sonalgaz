package com.slimani.bi_sonalgaz.restful;

import android.content.Context;
import android.database.Cursor;

import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
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



    public List<DataEntry> parsingJSONArray(JSONArray jsonArray) throws JSONException {
        List<DataEntry> list = new ArrayList<DataEntry>();

        int i = 0;
        while (i<jsonArray.length()){
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            //list.add(new ValueDataEntry(, 80540)))
            i++;
        }

        return list;

    }







}
