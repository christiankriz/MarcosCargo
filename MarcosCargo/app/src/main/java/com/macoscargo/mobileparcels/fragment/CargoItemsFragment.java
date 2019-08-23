package com.macoscargo.mobileparcels.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.macoscargo.mobileparcels.R;
import com.macoscargo.mobileparcels.adapter.CargoItemAdapter;
import com.macoscargo.mobileparcels.model.Cargo;
import com.macoscargo.mobileparcels.realm.RealmController;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CargoItemsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CargoItemsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CargoItemsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String ARG_PAGE = "ARG_PAGE";
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private static CargoItemAdapter adapter;
    private TextView noItem;
    Realm realm;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static CargoItemsFragment newInstane(int page) {
        CargoItemsFragment fragment = new CargoItemsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        fragment.setArguments(args);
        return fragment;
    }

    public CargoItemsFragment() {
        // Required empty public constructor
    }

    public static CargoItemsFragment newInstance(String param1, String param2) {
        CargoItemsFragment fragment = new CargoItemsFragment();
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
        view = inflater.inflate(R.layout.fragment_cargo_items, container, false);
        getActivity().setTitle("Parcels");
        this.realm = RealmController.with((Activity) getContext()).getRealm();
        noItem = view.findViewById(R.id.no_items);
        getCargoItems(view);
        return view;
    }

    private void getCargoItems(View view){
        noItem.setVisibility(View.GONE);
        realm.beginTransaction();
        RealmResults<Cargo> cargoRealmList = realm.where(Cargo.class).findAll();
        realm.commitTransaction();
        recyclerView = view.findViewById(R.id.parcel_list_recycler_view);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new CargoItemAdapter(cargoRealmList, getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
