package com.example.bloodbank;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;


interface RequestCallback{
    void process(JSONObject obj);
}

public class APICaller {
    private String urlbase;

    RequestQueue requestQueue;
    Context c;


    public APICaller(String urlbase, Context c){
        this.c = c;
        requestQueue = Volley.newRequestQueue(this.c);
        requestQueue.start();
        this.urlbase = urlbase;
    }
    public void jsonRequest(String method, JSONObject req, final com.example.bloodbank.RequestCallback cb) {
        JsonObjectRequest jsonObejectRequest = new JsonObjectRequest (Request.Method.POST, urlbase + method, req,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        cb.process(response);
                        requestQueue.stop();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        Log.d("pi", e.toString());
                        requestQueue.stop();
                    }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        requestQueue.add(jsonObejectRequest);
    }


    /*AsyncHttpClient client = new AsyncHttpClient();
    RequestParams parameters = new RequestParams();
    client.get("http://www.google.com", params, new TextHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, String res) {
            // called when response HTTP status is "200 OK"
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
        }
    }

    });*/
    void miscGetBloodTypes (RequestCallback cb) {
        JSONObject getBloodTypes = new JSONObject();
        jsonRequest("misc/getbloodtypes", getBloodTypes, cb);
    }

    void userAuth(String username, String password, RequestCallback cb) throws JSONException {
        JSONObject auth = new JSONObject();
        auth.put("username", username);
        auth.put("password",password);
        jsonRequest("user/auth", auth, cb);
    }

    void userRegister(String username, String password, Boolean isDonor, String firstName, String lastName,
                  int bloodType, int age, String sex, int height, RequestCallback cb) throws JSONException{
        JSONObject reg = new JSONObject();
        reg.put("username",username);
        reg.put("password", password);
        reg.put("is_donor", isDonor);
        reg.put("first_name", firstName);
        reg.put("last_name", lastName);
        reg.put("blood_type",bloodType);
        reg.put("age",age);
        reg.put("sex", sex);
        reg.put("height", height);
        jsonRequest("user/register", reg, cb);
    }

    void userGet(String token, RequestCallback cb) throws JSONException{
        JSONObject get = new JSONObject();
        get.put("token", token);
        jsonRequest("user/get", get, cb);
    }

    void userUpdate(String token, String password, String firstName, String lastName, int bloodType, int age,
                String sex, int height, RequestCallback cb) throws JSONException{
        JSONObject update = new JSONObject();
        update.put("token", token);
        update.put("password", password);
        update.put("first_name", firstName);
        update.put("last_name", lastName);
        update.put("blood_type", bloodType);
        update.put("age", age);
        update.put("sex", sex);
        update.put("height", height);
        jsonRequest("usr/update", update, cb);
    }

    void userLogout(String token, RequestCallback cb) throws JSONException{
        JSONObject logout = new JSONObject();
        logout.put("token", token);
        jsonRequest("user/logout", logout, cb);
    }

    void donorDonate(String token, int time, RequestCallback cb) throws JSONException{
        JSONObject donate = new JSONObject();
        donate.put("token", token);
        donate.put("time", time);
        jsonRequest("donor/donate", donate, cb);
    }

    void donorGetDonates(String token, RequestCallback cb) throws JSONException{
        JSONObject getDonates = new JSONObject();
        getDonates.put("token", token);
        jsonRequest("donor/get_donates", getDonates, cb);
    }

    void donorRequestList(String token, RequestCallback cb) throws JSONException{
        JSONObject requestList = new JSONObject();
        requestList.put("token", token);
        jsonRequest("donor/request/list", requestList, cb);
    }

    void donorRequestAccpet(String token, int requestId, RequestCallback cb) throws JSONException{
        JSONObject requestAccept = new JSONObject();
        requestAccept.put("token", token);
        requestAccept.put("request_id", requestId);
        jsonRequest("/donor/request/accpet", requestAccept, cb);
    }

    void requesterRequestNew(String token, RequestCallback cb) throws JSONException{
        JSONObject requestNew = new JSONObject();
        requestNew.put("token", token);
        jsonRequest("/requester/request/new", requestNew, cb);
    }

    void requesterRequestList(String token, RequestCallback cb) throws JSONException{
        JSONObject requestList = new JSONObject();
        requestList.put("token", token);
        jsonRequest("/requester/request/list", requestList, cb);
    }

    void adminUserList(String token, RequestCallback cb) throws JSONException{
        JSONObject userList = new JSONObject();
        userList.put("token", token);
        jsonRequest("/admin/user/list", userList, cb);
    }

    void adminUserRemove(String token, int userId, RequestCallback cb) throws JSONException{
        JSONObject userRemove = new JSONObject();
        userRemove.put("token", token);
        userRemove.put("user_id", userId);
        jsonRequest("/admin/user/remove", userRemove, cb);
    }

    void adminUserEdit(String token, int userId, String username, String password, String firstName,
                       String lastName, int bloodType, int age, String sex, int height,
                       RequestCallback cb) throws JSONException{
        JSONObject userEdit = new JSONObject();
        userEdit.put("token", token);
        userEdit.put("user_id", userId);
        userEdit.put("username", username);
        userEdit.put("password", password);
        userEdit.put("first_name", firstName);
        userEdit.put("last_name",lastName);
        userEdit.put("blood_type", bloodType);
        userEdit.put("age", age);
        userEdit.put("sex",sex);
        userEdit.put("height",height);
        jsonRequest("/admin/user/edit", userEdit, cb);
    }

    void adminBloodList(String token, RequestCallback cb) throws JSONException{
        JSONObject bloodList = new JSONObject();
        bloodList.put("token", token);
        jsonRequest("/admin/blood/list", bloodList, cb);
    }

    void adminBloodRemove(String token, int blood_id, RequestCallback cb) throws JSONException{
        JSONObject bloodRemove = new JSONObject();
        bloodRemove.put("token", token);
        bloodRemove.put("blood_id", blood_id);
        jsonRequest("/admin/blood/remove", bloodRemove, cb);
    }

    void adminBloodtypeAdd(String token, String bloodType, RequestCallback cb) throws JSONException{
        JSONObject bloodtypeAdd = new JSONObject();
        bloodtypeAdd.put("token", token);
        bloodtypeAdd.put("blood_type", bloodType);
        jsonRequest("/admin/bloodtype/add", bloodtypeAdd, cb);
    }

    void adminBloodtypeRemove(String token, int typeId, RequestCallback cb) throws JSONException{
        JSONObject bloodtypeRemove = new JSONObject();
        bloodtypeRemove.put("token", token);
        bloodtypeRemove.put("type_id", typeId);
        jsonRequest("/admin/bloodtype/remove", bloodtypeRemove, cb);
    }

    void adminBloodtypeEdit(String token, int typeId, String bloodType, RequestCallback cb)
            throws JSONException{
        JSONObject bloodtypeEdit = new JSONObject();
        bloodtypeEdit.put("token", token);
        bloodtypeEdit.put("type_id", typeId);
        bloodtypeEdit.put("blood_type", bloodType);
        jsonRequest("/admin/bloodtype/edit", bloodtypeEdit, cb);
    }
}



