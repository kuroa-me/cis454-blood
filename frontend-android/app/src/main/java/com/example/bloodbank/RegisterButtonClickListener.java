package com.example.bloodbank;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class RegisterButtonClickListener implements View.OnClickListener, RequestCallback {
    //JSONObject reg = new JSONObject();
    JSONArray bloodTypes;
    Context ctx;
    View mParentView;
    RadioGroup radioGroup;
    RadioButton donor, requester;
    public RegisterButtonClickListener(Context c, View parent){
        APICaller caller = new APICaller("http://nj.kuroa.me:8080/", c);
        caller.miscGetBloodTypes(this);
        Log.d("regbtn", "constructor");
        mParentView = parent;
        /*try {
            reg.put("username", username);
            reg.put("password", password);
            reg.put("is_donor", isDonor);
            reg.put("first_name", firstName);
            reg.put("last_name", lastName);
            reg.put("blood_type", bloodType);
            reg.put("age", age);
            reg.put("sex", sex);
            reg.put("height", height);
        } catch (Exception e) {
        }*/
        ctx = c;
    }

    public void onClick(View vBtn){
        try {
            Log.d("regBtn", "onClick");

            View v = mParentView;
            radioGroup = v.findViewById(R.id.radio_group);
            donor = v.findViewById(R.id.donor_button);
            requester = v.findViewById(R.id.requester_button);
            APICaller c = new APICaller("http://nj.kuroa.me:8080/", ctx);
            String username = ((EditText) v.findViewById(R.id.register_username)).getText().toString();
            String password = ((EditText) v.findViewById(R.id.register_password)).getText().toString();
            String firstName = ((EditText) v.findViewById(R.id.register_firstname)).getText().toString();
            String lastName = ((EditText) v.findViewById(R.id.register_lastname)).getText().toString();
            String mBloodType = ((EditText) v.findViewById(R.id.register_bloodtype)).getText().toString();
            Boolean isDonor = true;
            int bloodTypeId = -1;
            int age = Integer.parseInt(((EditText) v.findViewById(R.id.register_age)).getText().toString());
            String sex = ((EditText) v.findViewById(R.id.register_sex)).getText().toString();
            int height = Integer.parseInt(((EditText) v.findViewById(R.id.register_height)).getText().toString());
            int selectedId = radioGroup.getCheckedRadioButtonId();
            if(selectedId == donor.getId()){
                isDonor = true;
            }else if(selectedId == requester.getId()){
                isDonor = false;
            }else{
                Log.d("isDonor", "error");
            }
            for (int i = 0; i < bloodTypes.length(); i++) {
                JSONObject bt = bloodTypes.getJSONObject(i);
                if (bt.getString("type_name").equals(mBloodType)) {
                    bloodTypeId = bt.getInt("id");
                }
            }
            c.userRegister(username, password, isDonor, firstName, lastName, bloodTypeId, age, sex, height, this);

        } catch (Exception e) {
            Log.d("regbtn", e.toString());
        }
    }

    public void process(JSONObject obj) {
        try {
            Log.d("shi", "processssssssssssssssssssssssssssssssss");

            Boolean ok = obj.getBoolean("ok");
            if (ok) {
                Log.d("misc", "OK");
                if(obj.has("types")){
                    Log.d("misc", "bloodtypes loaded");
                    bloodTypes = obj.getJSONArray("types");
                    for (int i = 0; i<bloodTypes.length(); i++) {
                        JSONObject bt = bloodTypes.getJSONObject(i);
                        Log.d("misc", bt.toString());
                        Log.d("misc", bt.getString("type_name"));
                        Integer id = bt.getInt("id");
                        Log.d("misc", id.toString());
                    }
                }else if(obj.has("token")) {
                    String token = obj.getString("token");
                    SharedPreferences prefs = ctx.getSharedPreferences("com.example.bloodbank.usertoken", Context.MODE_PRIVATE);
                    prefs.edit().putString("token", token).apply();
                    if(obj.getString("user_type").equals("DONOR")) {
                        NavController navController = Navigation.findNavController(mParentView);
                        navController.navigate(R.id.action_des_reg_to_des_donor_dash);
                    }else if(obj.getString("user_type").equals("REQUESTER")){
                        NavController navController = Navigation.findNavController(mParentView);
                        navController.navigate(R.id.action_des_reg_to_des_reque_dash);
                    }
                } else {
                    Log.d("misc", "invslid res");
                }
            } else {
                Log.d("misc", "not ok");
                String error = obj.getString("error");
                AlertDialog.Builder dialog = new AlertDialog.Builder(ctx).setMessage(error);
                AlertDialog alertDialog = dialog.show();
            }
        } catch (Exception e) {
            Log.d("procerr", e.toString());
            AlertDialog.Builder dialog = new AlertDialog.Builder(ctx).setMessage(e.toString());
            AlertDialog alertDialog = dialog.show();
        }
    }
}