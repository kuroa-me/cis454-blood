package com.example.fragmentedas;


//import com.loopj.android.http.*;
//import cz.msebera.android.httpclient.Header;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;


interface callBack{
       void process(JSONObject obj);
}

public class api_lib {

    public void jsonRequest(String url, JSONObject req, final callBack cb) {
        JsonObjectRequest jsonObejectRequest = new JsonObjectRequest (Request.Method.GET, url, req,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        cb.process(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                });
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


}
