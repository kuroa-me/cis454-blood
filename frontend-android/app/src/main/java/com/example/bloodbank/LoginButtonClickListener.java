package com.example.bloodbank;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

public class LoginButtonClickListener implements View.OnClickListener, RequestCallback {
    JSONObject reg = new JSONObject();
    Activity act;
    public LoginButtonClickListener(String username, String password, String firstName, String lastName, Boolean isDonor, int bloodType, int age, String sex, int height, Activity a){
        try {
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

        }
        act = a;
    }

    public void onClick(View v){
        APICaller c = new APICaller("http://nj.kuroa.me:8080/", act.getBaseContext());
        c.jsonRequest("user/register", reg, this);
    }

    public void process(JSONObject obj) {
        try {
            Log.d("shi", "processssssssssssssssssssssssssssssssss");

            Boolean ok = obj.getBoolean("ok");
            if (ok) {
                Log.d("misc", "OK");
                    String token = obj.getString("token");
                    SharedPreferences prefs = act.getSharedPreferences("com.example.bloodbank", Context.MODE_PRIVATE);
                    prefs.edit().putString("token", token).apply();

            } else {
                Log.d("misc", "not ok");
                Context context = act;
                String error = obj.getString("error");
                AlertDialog.Builder dialog = new AlertDialog.Builder(context).setMessage(error);
                AlertDialog alertDialog = dialog.show();
            }
        } catch (Exception e) {
            Log.d("procerr", e.toString());
            Context context = act;
            AlertDialog.Builder dialog = new AlertDialog.Builder(context).setMessage(e.toString());
            AlertDialog alertDialog = dialog.show();
        }
    }
}
