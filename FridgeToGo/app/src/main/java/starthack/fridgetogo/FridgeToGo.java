package starthack.fridgetogo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import starthack.fridgetogo.com.google.zxing.integration.android.IntentIntegrator;
import starthack.fridgetogo.com.google.zxing.integration.android.IntentResult;


public class FridgeToGo extends AppCompatActivity {
    private static final String PREFS = "prefs";
    private static final String PRODUCTS_PREFS = "productPrefs";
    private static final String MAPPING_PREFS = "mappingPrefs";

    private static SharedPreferences mPrefs;
    private static SharedPreferences.Editor mEditor;
    private List<Product> products = new ArrayList<Product>();
    private BarcodeMapping barcodeMapping = new BarcodeMapping();

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
        Type productsType = new TypeToken<ArrayList<Product>>(){}.getType();
        if (!json1.isEmpty()){
            products = productsGson.fromJson(json1, productsType);
        }

        Gson mappingGson = new Gson();
        String json2 = mPrefs.getString(MAPPING_PREFS, "");
        Type mappingType = new TypeToken<BarcodeMapping>(){}.getType();
        if (!json2.isEmpty()){
            barcodeMapping = mappingGson.fromJson(json2, mappingType);
        }

        Ingredient ingredient = new Ingredient("Yogurt", 250, true, true, 0, 10, -1, -1);
        Product product = new Product(ingredient, 2017, 12, 20, 2015, 12, 10, 22.5);
        products.add(product);


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

    public void addMapping(long barcode, Ingredient ingredient){
        barcodeMapping.add(barcode, ingredient);
        refreshPreferences();
    }

    public void addProduct(Product product){
        products.add(product);
        refreshPreferences();
    }

    public List<Product> getProducts(){
        return products;
    }

    public BarcodeMapping getBarcodeMapping(){
        return barcodeMapping;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            Log.d("MyActivity", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            long content = Long.parseLong(scanContent);
            Ingredient ingredient = barcodeMapping.findBarcode(content);
            if(ingredient == null){
                addAllInfo(getProducts());

                ingredient = new Ingredient("Yog", 225, true, true, -1, -1, -1, -1);
                Product product = new Product(ingredient, 2001, 1, 1, 2000, 2, 2, 3.2);
                addMapping(content, ingredient);
                addProduct(product);
            } else{
                addAllInfo(getProducts());

                Date creationDate = null;
                Date peremptionDate = null;
                double price = 2.2;
                Product product = new Product(ingredient, 2001, 1, 1, 2000, 2, 2, price);
                addProduct(product);
            }
        }
        else{
            Toast toast = Toast.makeText(this.getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void addAllInfo(final List<Product> products) {
        final ArrayAdapter<Product> adapter = new ProductAdapter(getApplicationContext(), R.layout.product_item, products);

        final Spinner sp = new Spinner(this);
        sp.setLayoutParams(new RelativeLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new OnSpinnerItemClicked());

        LayoutInflater li = LayoutInflater.from(this);

        View promptsView = li.inflate(R.layout.add_product_info, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);

        alertDialogBuilder.setTitle("Add product info");

        final Spinner sp = (Spinner) promptsView
                .findViewById(R.id.allProductsLayout);

        final AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    private List<String> getProductNames(List<Product> products) {
        List<String> productsName = new ArrayList<>();

        for(Product product: products){
            productsName.add(product.getIngredient().getName());
        }
        return productsName;
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
                    fragment = MyFridgeFragment.newInstance(); break;
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

    private void refreshPreferences() {
        Gson gson1 = new Gson();
        Gson gson2 = new Gson();
        String json1 = gson1.toJson(products);
        String json2 = gson2.toJson(barcodeMapping);


        mEditor.putString(PRODUCTS_PREFS, json1);
        mEditor.putString(MAPPING_PREFS, json2);
        mEditor.commit();
    }
}
