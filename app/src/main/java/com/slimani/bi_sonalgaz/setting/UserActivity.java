package com.slimani.bi_sonalgaz.setting;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.slimani.bi_sonalgaz.R;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.ItemDTO;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.ListItemAdapter;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.ListItemAdapterW;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.ListItemHolder;
import com.slimani.bi_sonalgaz.restful.DataManager;
import com.slimani.bi_sonalgaz.restful.Service;
import com.slimani.bi_sonalgaz.restful.pojoRest.PojoUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_setting);

        final Button addUser_btn = (Button) findViewById(R.id.addingUser_btn);
        final Button listUser_btn = (Button) findViewById(R.id.usersList_btn);
        final Button deletUser_btn = (Button) findViewById(R.id.deleteUser_btn);



        List<String> roles = new ArrayList<>();
        roles.add(new String("ROLE_MM"));
        roles.add(new String("ROLE_SD"));
        roles.add(new String("ROLE_DD"));
        roles.add(new String("ROLE_AG"));
        roles.add(new String("ROLE_ADMIN"));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, roles);
        //Service service = new Service(getApplicationContext());

        addUser_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(UserActivity.this);
                dialog.setContentView(R.layout.popup_adding_user);

                final Button saveUser_btn = (Button) dialog.findViewById(R.id.save_user_btn);
                final Spinner roles_list = (Spinner) dialog.findViewById(R.id.roles_spinner);
                final EditText username = (EditText) dialog.findViewById(R.id.username_text);
                final EditText password = (EditText) dialog.findViewById(R.id.password_text);

                roles_list.setAdapter(adapter);



                dialog.show();
                saveUser_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(username.getText().toString().equals("") || password.getText().toString().equals("")){
                            Toast.makeText(getApplicationContext(), "You must fill the user's fields !", Toast.LENGTH_SHORT).show();
                        }else{

                            PojoUser user;

                            user = new PojoUser(username.getText().toString(),
                                    password.getText().toString(),roles_list.getSelectedItem().toString());


                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Service service = new Service(getApplicationContext());
                                    String response = service.saveUser(
                                            "/register",user);

                                    if(response.equals("Registration failed, this username already exists !")){


                                        UserActivity.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "This username already exists !", Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                    }else if(response.equals("User registred successfully !")){

                                        UserActivity.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                dialog.dismiss();
                                                successAlert(response);
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



        listUser_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                final Dialog dialog = new Dialog(UserActivity.this);
                dialog.setContentView(R.layout.popup_list_users);

                final Button ok_btn = (Button) dialog.findViewById(R.id.listUsers_ok_btn);
                final ListView usersList = (ListView) dialog.findViewById(R.id.list_view_users);




                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Service service = new Service(getApplicationContext());
                        DataManager dataManager = new DataManager();
                        List<String> list = dataManager.getItems(service.getUsers("/usersRoles"));
                        UserActivity.this.runOnUiThread(new Runnable() {
                            public void run() {


                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1  ,list){
                                    @Override
                                    public View getView(int position, View convertView, ViewGroup parent){

                                        View view = super.getView(position, convertView, parent);

                                        TextView tv = (TextView) view.findViewById(android.R.id.text1);
                                        tv.setTextColor(Color.WHITE);
                                        return view;
                                    }

                                };
                                usersList.setAdapter(adapter);

                                usersList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                    @Override
                                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                                        Object itemObject = parent.getAdapter().getItem(position);
                                        String name = itemObject.toString();

                                        final Dialog dialog = new Dialog(UserActivity.this);
                                        dialog.setContentView(R.layout.popup_detail_user);
                                        EditText username_field = dialog.findViewById(R.id.username_text);
                                        EditText role_field = dialog.findViewById(R.id.role_text);
                                        EditText password_field = dialog.findViewById(R.id.password_text);
                                        Button ok_detail_btn = dialog.findViewById(R.id.ok_detail_btn);

                                        Thread thread1 = new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Service service = new Service(getApplicationContext());
                                                PojoUser userDetail = service.getDetailUser("/detailUser?username="+name);

                                                UserActivity.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        username_field.setText(userDetail.getUsername());
                                                        password_field.setText(userDetail.getPassword());
                                                        role_field.setText(userDetail.getRole());



                                                        ok_detail_btn.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                    }
                                                });

                                            }
                                        });
                                        thread1.start();

                                        dialog.show();



                                        return true;
                                    }
                                });

                                /*
                                 columnsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Object itemObject = parent.getAdapter().getItem(position);
                                        String column = (String) itemObject.toString();

                                 */

                            }
                        });


                    }
                });
                thread.start();


                dialog.show();
                ok_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


            }
        });



        deletUser_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(UserActivity.this);
                dialog.setContentView(R.layout.popup_delete_users);

                final Button confirm_btn = (Button) dialog.findViewById(R.id.confirm_delete_btn);
                final ListView usersList = (ListView) dialog.findViewById(R.id.list_view_users);


                dialog.show();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        DataManager dataManager = new DataManager();
                        Service service = new Service(getApplicationContext());
                        List<String> listUsers = dataManager.getItems(service.getUsers("/users"));


                        UserActivity.this.runOnUiThread(new Runnable() {
                            public void run() {


                                List<ItemDTO> initItemList = new ArrayList<ItemDTO>();

                                try {
                                    initItemList = getInitViewItemDtoList(listUsers);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                final ListItemAdapterW listViewDataAdapter = new ListItemAdapterW(initItemList, getApplicationContext());

                                listViewDataAdapter.notifyDataSetChanged();

                                usersList.setAdapter(listViewDataAdapter);

                            }
                        });


                    }
                });
                thread.start();



                List<ItemDTO> itemsChecked = new ArrayList<>();

                usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

                        if(itemsChecked.size()<1){
                            Toast.makeText(getApplicationContext(), "Check user desired to delete",Toast.LENGTH_SHORT).show();
                        }else{
                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    Service service = new Service(getApplicationContext());
                                    int i =0;
                                    while (i<itemsChecked.size()){
                                        service.deleteUser("/deleteUser?username="+itemsChecked.get(i).getText().toString());
                                        i++;
                                    }

                                    UserActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                            successAlert("Users checked was deleted successfully !");
                                        }
                                    });

                                }
                            });
                            thread.start();
                        }



                    }
                });

            }
        });

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
}
