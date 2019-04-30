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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link donor_history.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link donor_history#newInstance} factory method to
 * create an instance of this fragment.
 */
public class donor_history extends Fragment implements RequestCallback{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    Context mCtx;

    private List<donor_history_listitem> listItems;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public donor_history() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment donor_history.
     */
    // TODO: Rename and change types and number of parameters
    public static donor_history newInstance(String param1, String param2) {
        donor_history fragment = new donor_history();
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
        return inflater.inflate(R.layout.fragment_donor_history, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        try{
            SharedPreferences prefs = mCtx.getSharedPreferences("com.example.bloodbank.usertoken", Context.MODE_PRIVATE);
            String token = prefs.getString("token", null);
            APICaller c = new APICaller("http://nj.kuroa.me:8080/", mCtx);
            c.donorGetDonates(token, this);
        }catch(Exception e){
            Log.d("viewReq", e.toString());
        };
        recyclerView = (RecyclerView) view.findViewById(R.id.donor_history_recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mCtx));

        listItems = new ArrayList<>();
    }

    @Override
    public void process(JSONObject obj) {
        try{
            Log.d("viewReq", "process");
            Boolean ok = obj.getBoolean("ok");
            if(ok){
                Log.d("viewReq", "OK");
                JSONArray donorHistory = obj.getJSONArray("history");
                //Date d = new Date(10000); d.toString();
                for (int i = 0; i<donorHistory.length(); i++) {
                    JSONObject history = donorHistory.getJSONObject(i);
                    String useDate = "Not used";
                    if(history.getBoolean("used")){
                        Date ud = new Date(history.getLong("date_used") * 1000);
                        useDate = ud.toString();
                    }
                    Log.d("viewreq", history.toString());
                    donor_history_listitem listItem = new donor_history_listitem(
                            history.getString("donor_id"),
                            history.getString("blood_type"),
                            (new Date(history.getLong("date_received") * 1000)).toString(),
                            history.getString("used"),
                             useDate,
                            history.getString("used_by")
                    );
                    //Integer id = request.getInt("id");

                    listItems.add(listItem);
                }
                Log.d("viewReq", "loaded");
                adapter = new donor_history_adapter(listItems,mCtx);

                recyclerView.setAdapter(adapter);

            }else{
                Log.d("misc", "not ok");
                String error = obj.getString("error");
                AlertDialog.Builder dialog = new AlertDialog.Builder(mCtx).setMessage(error);
                AlertDialog alertDialog = dialog.show();
            }
        }catch(Exception e){
            Log.d("viewreq", e.toString());
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
