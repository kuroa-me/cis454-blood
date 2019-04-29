package com.example.bloodbank;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.navigation.Navigation;
import androidx.navigation.NavController;

import org.json.JSONObject;

import java.util.Date;

public class DonorDonateClickListener implements RequestCallback, View.OnClickListener{
    Context ctx;
    View mParentView;
    public DonorDonateClickListener(Context c, View parent){
        mParentView = parent;
        ctx = c;
    }

    public void onClick(View mView){
        try{
            Log.d("Donate", "I am pushed");
            APICaller c = new APICaller("http://nj.kuroa.me:8080/", ctx);
            //get global token
            SharedPreferences prefs = ctx.getSharedPreferences("com.example.bloodbank.usertoken", Context.MODE_PRIVATE);
            String token = prefs.getString("token", null);
            //make request
            Date date= new Date();
            long time = date.getTime()/1000;
            c.donorDonate(token, (int) time, this);
        } catch (Exception e) {
            Log.d("Donate", e.toString());
        }
    }

    public void process(JSONObject obj){
        try{
            Boolean ok = obj.getBoolean("ok");
            if(ok){
                Toast.makeText(ctx,"Donated!",Toast.LENGTH_SHORT).show();
                NavController navController = Navigation.findNavController(mParentView);
                navController.navigate(R.id.action_des_donor_donate_to_des_donor_dash);
            }
            else {
                String error = obj.getString("error");
                AlertDialog.Builder dialog = new AlertDialog.Builder(ctx).setMessage(error);
                AlertDialog alertDialog = dialog.show();
            }
        } catch (Exception e){
            Log.d("Donate", e.toString());
        }
    }
}
