package com.macoscargo.mobileparcels.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.macoscargo.mobileparcels.R;
import com.macoscargo.mobileparcels.model.Cargo;
import com.macoscargo.mobileparcels.util.Util;

import java.util.List;

import io.realm.RealmRecyclerViewAdapter;
import io.realm.OrderedRealmCollection;
import io.realm.RealmResults;



/**
 * Created by christiannhlabano on 2019/07/21.
 */

public class CargoItemAdapter extends RealmRecyclerViewAdapter<Cargo, CargoItemAdapter.CargoFeedHolder> {
    OrderedRealmCollection<Cargo> results;
    private Context context;
    private Cargo cargo;
    View view;
    CargoFeedHolder cargoFeedHolder;

    public CargoItemAdapter(OrderedRealmCollection<Cargo> results, Context context) {
        super(results, true);
        this.results = results;
        this.context = context;
    }

    @Override
    public CargoFeedHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parcel_list_item, parent, false);
        return new CargoItemAdapter.CargoFeedHolder(view);
    }

    @Override
    public void onBindViewHolder(CargoFeedHolder holder, int position) {
        cargo = getData().get(position);
        cargoFeedHolder = (CargoFeedHolder) holder;
        cargoFeedHolder.parcel_name.setText(cargo.getCargoName().toString());
        cargoFeedHolder.parcel_status.setText(cargo.getTrackingStatus());
        cargoFeedHolder.date_created.setText(Util.dateToString(cargo.getArrivalDate()));
        cargoFeedHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String tag = v.getTag().toString();
//                String messageSubjectId = tag.substring(0, tag.indexOf("_"));
//                String messageSubject = tag.substring(tag.indexOf("_") + 1);
//                Bundle bundle = new Bundle();
//                bundle.putString("messageSubjectId", messageSubjectId);
//                bundle.putString("messageSubject", messageSubject);
//                Intent i = new Intent(context, SupportActivity.class);
//                i.putExtras(bundle);
//                context.startActivity(i);
            }
        });

    }



    @Override
    public int getItemCount() {
        return results == null ? 0 : results.size();
    }

    public class CargoFeedHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView parcel_name, parcel_status, date_created;


        public CargoFeedHolder(View itemView) {

            super(itemView);
            cardView = itemView.findViewById(R.id.parcel_item_view);
            parcel_name = itemView.findViewById(R.id.parcel_name);
            parcel_status = itemView.findViewById(R.id.parcel_status);
            date_created = itemView.findViewById(R.id.date_created);
        }
    }
}

