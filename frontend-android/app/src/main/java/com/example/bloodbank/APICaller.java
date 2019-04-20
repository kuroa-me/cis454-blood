package com.example.bloodbank;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;



interface RequestCallback{
    void process(JSONObject obj);
}

public class APICaller {
    private String urlbase;

    public APICaller(String urlbase){
        this.urlbase = urlbase;
    }
    public void jsonRequest(String method, JSONObject req, final com.example.bloodbank.RequestCallback cb) {
        JsonObjectRequest jsonObejectRequest = new JsonObjectRequest (Request.Method.GET, urlbase + method, req,
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
    void authentication(String username, String password, RequestCallback cb) throws JSONException {
        JSONObject auth = new JSONObject();
        auth.put("name", username);
        auth.put("password",password);
        jsonRequest("auth", auth, cb);
    }

}



