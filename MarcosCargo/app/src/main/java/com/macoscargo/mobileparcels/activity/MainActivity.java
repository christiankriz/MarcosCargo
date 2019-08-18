package com.macoscargo.mobileparcels.activity;

import android.os.Bundle;
import android.os.Parcel;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.macoscargo.mobileparcels.R;
import com.macoscargo.mobileparcels.adapter.CargoItemAdapter;
import com.macoscargo.mobileparcels.model.Cargo;
import com.macoscargo.mobileparcels.realm.RealmController;
import com.macoscargo.mobileparcels.util.Util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;


public class MainActivity extends AppCompatActivity {
    private EditText newTrackingNumberInput, tittleInput, pickUpPointInput, destinationInput, statusInput, cargoNameInput;
    private RadioGroup carrierInput;
    private Button submitParcel;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private static CargoItemAdapter adapter;
    private TextView noItem;
    Realm realm;
    private String carrier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Realm.init(this);
        noItem = findViewById(R.id.no_items);
        //get realm instance
        this.realm = RealmController.with(this).getRealm();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Capture New Cargo Data Or Update Existing", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                captureParcelInfor();
            }
        });
        getCargoItems();
    }

    private void captureParcelInfor(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.capture_parcel_details,null);
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        cargoNameInput = dialogView.findViewById((R.id.new_cargo_name));
        newTrackingNumberInput = dialogView.findViewById(R.id.new_tracking_number);
        tittleInput = dialogView.findViewById(R.id.new_optional_title);
        pickUpPointInput = dialogView.findViewById(R.id.new_pick_up_point);
        destinationInput = dialogView.findViewById(R.id.new_destination);
        statusInput = dialogView.findViewById(R.id.new_status);
        carrierInput = dialogView.findViewById(R.id.carrior_selection);
        carrierInput.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.macos_cargo){
                   carrier = "Macos Cargo";
                }
                if(checkedId == R.id.dhl){
                   carrier = "DHL";
                }
                if(checkedId == R.id.post_office){
                   carrier = "Post Office";
                }
                if(checkedId == R.id.ups){
                   carrier = "UPS";
                }
            }
        });
        submitParcel = dialogView.findViewById(R.id.submit_parcel);
        submitParcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realm.beginTransaction();  //open the database
                //database operation
                Cargo cargo = realm.createObject(Cargo.class);
                cargo.setCargoName(cargoNameInput.getText().toString());
                cargo.setTrackingNo(newTrackingNumberInput.getText().toString());
                cargo.setTittle(tittleInput.getText().toString());
                cargo.setPickUpPoint(pickUpPointInput.getText().toString());
                cargo.setDestination(destinationInput.getText().toString());
                cargo.setTrackingStatus(statusInput.getText().toString());
                cargo.setArrivalDate(new Date());
                realm.commitTransaction(); //close the database
                //finish();
                getCargoItems();
                dialog.cancel();

            }
        });
        dialog.show();

    }

    private void getCargoItems(){
        noItem.setVisibility(View.GONE);
        realm.beginTransaction();
        RealmResults<Cargo> cargoRealmList = realm.where(Cargo.class).findAll();
        realm.commitTransaction();
        recyclerView = findViewById(R.id.parcel_list_recycler_view);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new CargoItemAdapter(cargoRealmList, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
