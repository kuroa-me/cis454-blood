package com.example.bloodbank;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link donor_vreq.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link donor_vreq#newInstance} factory method to
 * create an instance of this fragment.
 */
public class donor_vreq extends Fragment implements RequestCallback{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    Context mCtx;

    SharedPreferences prefs;
    JSONArray bloodTypes;

    private List<donor_vreq_listitem> listItems;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public donor_vreq() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment donor_vreq.
     */
    // TODO: Rename and change types and number of parameters
    public static donor_vreq newInstance(String param1, String param2) {
        donor_vreq fragment = new donor_vreq();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_donor_vreq, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        try{
        prefs = mCtx.getSharedPreferences("com.example.bloodbank.usertoken", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);
        APICaller c = new APICaller("http://nj.kuroa.me:8080/", mCtx);
        c.donorRequestList(token, this);
        }catch(Exception e){

        };

        recyclerView = (RecyclerView) view.findViewById(R.id.donor_vreq_recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mCtx));

        listItems = new ArrayList<>();

        for(int i=0; i<10; i++){
            donor_vreq_listitem listItem = new donor_vreq_listitem(
                    "name" + i+1,
                    "age" + i+1,
                    "sex" + i+1
            );

            listItems.add(listItem);
        }

        adapter = new donor_vreq_adapter(listItems,mCtx);

        recyclerView.setAdapter(adapter);
    }

    public void process(JSONObject obj){
        try{
            Log.d("viewReq", "process");
            Boolean ok = obj.getBoolean("ok");
            if(ok){
                Log.d("viewReq", "OK");
                bloodTypes = obj.getJSONArray("types");
                for (int i = 0; i<bloodTypes.length(); i++) {
                    JSONObject bt = bloodTypes.getJSONObject(i);
                    Log.d("misc", bt.toString());
                    Log.d("misc", bt.getString("type_name"));
                    Integer id = bt.getInt("id");
                    Log.d("misc", id.toString());
                }
                Log.d("viewReq", "loaded");
            }else{
                Log.d("misc", "not ok");
                String error = obj.getString("error");
                AlertDialog.Builder dialog = new AlertDialog.Builder(mCtx).setMessage(error);
                AlertDialog alertDialog = dialog.show();
            }
        }catch(Exception e){

        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        mCtx = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
