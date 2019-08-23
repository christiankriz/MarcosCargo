package com.macoscargo.mobileparcels.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.macoscargo.mobileparcels.R;
import com.macoscargo.mobileparcels.adapter.ViewerPagerAdapter;
import com.macoscargo.mobileparcels.fragment.ArchieveItemFragmnet;
import com.macoscargo.mobileparcels.fragment.CargoItemsFragment;
import com.macoscargo.mobileparcels.model.Cargo;
import com.macoscargo.mobileparcels.realm.RealmController;

import java.util.Date;

import io.realm.Realm;


public class MainActivity extends AppCompatActivity implements CargoItemsFragment.OnFragmentInteractionListener, ArchieveItemFragmnet.OnFragmentInteractionListener{
    private EditText newTrackingNumberInput, tittleInput, pickUpPointInput, destinationInput, statusInput, cargoNameInput;
    private RadioGroup carrierInput;
    private Button submitParcel, cancelDialog;
    private Button getSignature;
    private String carrier;
    private ViewPager viewPager;
    public static BottomNavigationView bottomNavigationView;
    FloatingActionButton lumiFab;
    TextView pageTittle;
    ImageView moreMenu;
    //This is our viewPager
    SharedPreferences sharedpreferences;
    Realm realm;

    public static final int SIGNATURE_ACTIVITY = 1;

    //Fragments
    CargoItemsFragment cargoItemsFragment;
    ArchieveItemFragmnet archieveItemFragmnet;
    MenuItem prevMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //Initializing the bottomNavigationView
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        //BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.cargo_menu:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.history_menu:
                                viewPager.setCurrentItem(1);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        lumiFab =  (FloatingActionButton) findViewById(R.id.fab);
        lumiFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Snackbar.make(v, "Capture New Cargo Data Or Update Existing", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                captureParcelInfor();
            }
        });

        setupViewPager(viewPager);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
        init();
    }

    private void init(){
        Realm.init(this);
        //get realm instance
        this.realm = RealmController.with(this).getRealm();


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
        getSignature = dialogView.findViewById(R.id.signature_btn);
        cancelDialog = dialog.findViewById(R.id.cancel_button);
        cancelDialog = dialogView.findViewById(R.id.cancel_button);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        getSignature.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CaptureSignatureActivity.class);
                startActivityForResult(intent,SIGNATURE_ACTIVITY);
            }
        });
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
                //getCargoItems();
                dialog.cancel();

            }
        });
        dialog.show();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch(requestCode) {
            case SIGNATURE_ACTIVITY:
                if (resultCode == RESULT_OK) {

                    Bundle bundle = data.getExtras();
                    String status  = bundle.getString("status");
                    if(status.equalsIgnoreCase("done")){
                        Toast toast = Toast.makeText(this, "Signature capture successful!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 105, 50);
                        toast.show();
                    }
                }
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewerPagerAdapter adapter = new ViewerPagerAdapter(getSupportFragmentManager());
        cargoItemsFragment = CargoItemsFragment.newInstane(0);
        archieveItemFragmnet = ArchieveItemFragmnet.newInstane(1);
        adapter.addFragment(cargoItemsFragment);
        adapter.addFragment(archieveItemFragmnet);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }


    @Override
    public void onBackPressed() {
    }


    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
