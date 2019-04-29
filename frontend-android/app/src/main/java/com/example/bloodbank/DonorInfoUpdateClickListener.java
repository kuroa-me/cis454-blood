package com.example.bloodbank;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import org.json.JSONArray;
import org.json.JSONObject;

public class DonorInfoUpdateClickListener implements RequestCallback, View.OnClickListener {
    SharedPreferences prefs;
    JSONArray bloodTypes;
    Context ctx;
    View mParentView;
    public DonorInfoUpdateClickListener(Context c, View parent){
        APICaller caller = new APICaller("http://nj.kuroa.me:8080/", c);
        caller.miscGetBloodTypes(this);
        Log.d("regbtn", "constructor");
        mParentView = parent;
        ctx = c;
    }

    public void onClick(View mView){
        try{
            Log.d("userUpdate", "onClick");
            prefs = ctx.getSharedPreferences("com.example.bloodbank.usertoken", Context.MODE_PRIVATE);
            String token = prefs.getString("token", null);
            Log.d("donorUpdate", token);
            View v = mParentView;
            APICaller c = new APICaller("http://nj.kuroa.me:8080/", ctx);
            String password = ((EditText) v.findViewById(R.id.register_password)).getText().toString();
            String firstName = ((EditText) v.findViewById(R.id.register_firstname)).getText().toString();
            String lastName = ((EditText) v.findViewById(R.id.register_lastname)).getText().toString();
            String mBloodType = ((EditText) v.findViewById(R.id.register_bloodtype)).getText().toString();
            int bloodTypeId = -1;
            int age = Integer.parseInt(((EditText) v.findViewById(R.id.register_age)).getText().toString());
            String sex = ((EditText) v.findViewById(R.id.register_sex)).getText().toString();
            int height = Integer.parseInt(((EditText) v.findViewById(R.id.register_height)).getText().toString());
            for (int i = 0; i < bloodTypes.length(); i++) {
                JSONObject bt = bloodTypes.getJSONObject(i);
                if (bt.getString("type_name").equals(mBloodType)) {
                    bloodTypeId = bt.getInt("id");
                }
            }
            c.userUpdate(token, password, firstName, lastName, bloodTypeId, age, sex, height, this);
        }catch(Exception e){
            Log.d("login",e.toString());
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
                }else {
                    Toast.makeText(ctx,"Updated",Toast.LENGTH_SHORT).show();
                    NavController navController = Navigation.findNavController(mParentView);
                    //navController.navigate(R.id.action_des_update_to_donor_dash);
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
