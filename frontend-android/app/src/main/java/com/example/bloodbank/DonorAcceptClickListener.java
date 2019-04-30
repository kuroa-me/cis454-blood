package com.example.bloodbank;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import org.json.JSONObject;

public class DonorAcceptClickListener implements RequestCallback, View.OnClickListener {
    SharedPreferences prefs;
    Context ctx;
    View mParentView;
    int id;
    public DonorAcceptClickListener(Context c, View parent, int id){
        mParentView = parent;
        ctx = c;
        this.id = id;
    }

    @Override
    public void onClick(View vBtn) {
        try{
            Log.d("donorReqAcc", "accept " + id);
            View v = mParentView;
            APICaller c = new APICaller("http://nj.kuroa.me:8080/", ctx);
            SharedPreferences prefs = ctx.getSharedPreferences("com.example.bloodbank.usertoken", Context.MODE_PRIVATE);
            String token = prefs.getString("token", null);
            c.donorRequestAccept(token, id, this);
        }catch(Exception e){
            Log.d("donorReqAcc", e.toString());
        }
    }

    @Override
    public void process(JSONObject obj) {
        try{
            boolean ok = obj.getBoolean("ok");
            if(ok){
                Toast.makeText(ctx,"Accpeted!",Toast.LENGTH_SHORT).show();
                NavController navController = Navigation.findNavController(mParentView);
                navController.navigate(R.id.action_des_donor_vreq_to_des_donor_dash);
            } else {
                String error = obj.getString("error");
                AlertDialog.Builder dialog = new AlertDialog.Builder(ctx).setMessage(error);
                AlertDialog alertDialog = dialog.show();
            }
        }catch(Exception e){
            Log.d("donorReqAcc", e.toString());
        }
    }
}
