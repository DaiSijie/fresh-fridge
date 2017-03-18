package starthack.fridgetogo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


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
        String json2 = mPrefs.getString(PRODUCTS_PREFS, "");
        Type mappingType = new TypeToken<BarcodeMapping>(){}.getType();
        if (!json2.isEmpty()){
            products = mappingGson.fromJson(json2, mappingType);
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
