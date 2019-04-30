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
 * {@link reque_check.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link reque_check#newInstance} factory method to
 * create an instance of this fragment.
 */
public class reque_check extends Fragment implements RequestCallback{

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    Context mCtx;

    private List<reque_history_listitem> listItems;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public reque_check() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment reque_check.
     */
    // TODO: Rename and change types and number of parameters
    public static reque_check newInstance(String param1, String param2) {
        reque_check fragment = new reque_check();
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
        return inflater.inflate(R.layout.fragment_reque_check, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        try{
            SharedPreferences prefs = mCtx.getSharedPreferences("com.example.bloodbank.usertoken", Context.MODE_PRIVATE);
            String token = prefs.getString("token", null);
            APICaller c = new APICaller("http://nj.kuroa.me:8080/", mCtx);
            c.requesterRequestList(token, this);
        }catch(Exception e){
            Log.d("RequeListCheck", e.toString());
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.reque_check_recycleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mCtx));

        listItems = new ArrayList<>();

    }

    public void process(JSONObject obj) {
        try{
            Log.d("RequeListCheck", "process");
            Boolean ok = obj.getBoolean("ok");
            if(ok){
                Log.d("RequeListCheck","ok");
                JSONArray requeHistory = obj.getJSONArray("requests");
                for(int i = 0; i<requeHistory.length(); i++){
                    JSONObject history = requeHistory.getJSONObject(i);
                    int accept = history.getInt("accepted");
                    if(accept == 0) {
                        reque_history_listitem listItem = new reque_history_listitem(
                                (new Date(history.getLong("date_requested") * 1000)).toString(),
                                "No",
                                "Not yet accepted"
                        );
                        listItems.add(listItem);
                    }
                }

                adapter = new reque_history_adapter(listItems,mCtx);

                recyclerView.setAdapter(adapter);

            }else{
                Log.d("misc", "not ok");
                String error = obj.getString("error");
                AlertDialog.Builder dialog = new AlertDialog.Builder(mCtx).setMessage(error);
                AlertDialog alertDialog = dialog.show();
            }

        }catch(Exception e){
            Log.d("RequeListCheck", e.toString());
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
