package starthack.fridgetogo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import starthack.fridgetogo.com.google.zxing.integration.android.IntentIntegrator;
import starthack.fridgetogo.com.google.zxing.integration.android.IntentResult;


public class FridgeToGo extends AppCompatActivity {
    private static final String PREFS = "prefs";
    private static final String PRODUCTS_PREFS = "productPrefs";
    private static final String MAPPING_PREFS = "mappingPrefs";

    private static SharedPreferences mPrefs;
    private static SharedPreferences.Editor mEditor;
    private static List<Product> products = new ArrayList<Product>();
    private static BarcodeMapping barcodeMapping = new BarcodeMapping();

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge_to_go);

        // Restore preferences
        mPrefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        mEditor = mPrefs.edit();

        Gson productsGson = new Gson();
        String json1 = mPrefs.getString(PRODUCTS_PREFS, "");
        Type productsType = new TypeToken<HashMap<String, ArrayList<Date>>>(){}.getType();
        if (!json1.isEmpty()){
            Database.fridgeContent = productsGson.fromJson(json1, productsType);
        }

        Gson mappingGson = new Gson();
        String json2 = mPrefs.getString(MAPPING_PREFS, "");
        Type mappingType = new TypeToken<HashMap<Long, String>>(){}.getType();
        if (!json2.isEmpty()){
            Database.barcodeToIngredient = mappingGson.fromJson(json2, mappingType);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fridge_to_go, menu);
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

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            long barcode = Long.parseLong(scanContent);
            String ingredient = Database.getIngredientFromCode(barcode);
            if(ingredient == null){TextView
                addAllInfo(Database.getIngredientList(), barcode);
            } else{
                addProductInfo(ingredient);
            }
        }
        else{
            Toast toast = Toast.makeText(this.getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void addAllInfo(final List<String> ingredients, final long barcode) {

        LayoutInflater li = LayoutInflater.from(this);

        View promptsView = li.inflate(R.layout.add_product_info, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);

// set dialog message

        alertDialogBuilder.setTitle("New product : " + barcode);

        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Database.barcode = barcode;
                Database.currentIngredient = ((String)parent.getItemAtPosition(pos));
                //String ingredient = Database.currentIngredient;
                //Database.putCodeToIngredient(ingredient, barcode);
                //Date peremptionDate = Calendar.getInstance().getTime();
                //peremptionDate.setTime(peremptionDate.getTime() + 100000000000l*(long)Math.random());
                //Database.putNewObjectInFridge(ingredient, peremptionDate);
                //refreshPreferences();
            }
        });

// create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

        final Spinner mSpinner= (Spinner) promptsView
                .findViewById(R.id.productSpinner);

        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                ingredients);
        mSpinner.setAdapter(spinnerArrayAdapter);

// reference UI elements from my_dialog_layout in similar fashion

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int pos, long id) {
                Toast.makeText(parent.getContext(), "Clicked : " +
                        parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();

                Database.currentIngredient = ((String)parent.getItemAtPosition(pos));
            }

            @Override
            public void onNothingSelected(AdapterView parent) {
                // Do nothing.
            }
        });

// show it
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
    }

    public void addProductInfo(final String ingredient) {

        LayoutInflater li = LayoutInflater.from(this);

        View promptsView = li.inflate(R.layout.add_product_info, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);

// set dialog message

        alertDialogBuilder.setTitle("Add product : " + ingredient);


// create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

// show it
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = MyFridgeFragment.newInstance(); break;
                case 1:
                    fragment = ScanFragment.newInstance(); break;
                case 2:
                    fragment = SmartMealFragment.newInstance(); break;
                case 3:
                    fragment = MyFridgeFragment.newInstance(); break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "MY FRIDGE";
                case 1:
                    return "SCAN";
                case 2:
                    return "SMART MEAL";
                case 3:
                    return "SMART PURCHASE";
            }
            return null;
        }
    }

    public static void refreshPreferences() {
        Gson gson1 = new Gson();
        Gson gson2 = new Gson();
        String json1 = gson1.toJson(Database.fridgeContent);
        String json2 = gson2.toJson(Database.barcodeToIngredient);


        mEditor.putString(PRODUCTS_PREFS, json1);
        mEditor.putString(MAPPING_PREFS, json2);
        mEditor.commit();
    }
}
