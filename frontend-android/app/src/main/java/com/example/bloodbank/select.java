package com.example.bloodbank;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.navigation.Navigation;

import com.google.android.material.navigation.NavigationView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link select.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link select#newInstance} factory method to
 * create an instance of this fragment.
 */
public class select extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public select() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment select.
     */
    // TODO: Rename and change types and number of parameters
    public static select newInstance(String param1, String param2) {
        select fragment = new select();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDrawer();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void setDrawer(){
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem nav_login  = menu.findItem(R.id.des_log);
        nav_login.setVisible(true);
        MenuItem nav_regis  = menu.findItem(R.id.des_reg);
        nav_regis.setVisible(true);
        MenuItem nav_select  = menu.findItem(R.id.des_select);
        nav_select.setVisible(true);
        MenuItem nav_reque_check  = menu.findItem(R.id.des_reque_check);
        nav_reque_check.setVisible(false);
        MenuItem nav_reque_dash  = menu.findItem(R.id.des_reque_dash);
        nav_reque_dash.setVisible(false);
        MenuItem nav_reque_history  = menu.findItem(R.id.des_reque_history);
        nav_reque_history.setVisible(false);
        MenuItem nav_reque_mreq  = menu.findItem(R.id.des_reque_mreq);
        nav_reque_mreq.setVisible(false);
        MenuItem nav_reque_upinfo  = menu.findItem(R.id.des_reque_upinfo);
        nav_reque_upinfo.setVisible(false);
        MenuItem nav_donor_dash  = menu.findItem(R.id.des_donor_dash);
        nav_donor_dash.setVisible(false);
        MenuItem nav_donor_donate  = menu.findItem(R.id.des_donor_donate);
        nav_donor_donate.setVisible(false);
        MenuItem nav_donor_history  = menu.findItem(R.id.des_donor_history);
        nav_donor_history.setVisible(false);
        MenuItem nav_donor_upinfo  = menu.findItem(R.id.des_donor_upinfo);
        nav_donor_upinfo.setVisible(false);
        MenuItem nav_donor_vreq  = menu.findItem(R.id.des_donor_vreq);
        nav_donor_vreq.setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_select, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        Button logbtn = getView().findViewById(R.id.des_log);
        logbtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_to_login, null));
        Button regbtn = getView().findViewById(R.id.des_reg);
        regbtn.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_to_register, null));
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        Log.d("onDetach","detached");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("onDestroy","destroyed");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d("onStop","stopped");
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
