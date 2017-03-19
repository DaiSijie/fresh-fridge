package starthack.fridgetogo;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Date;

import starthack.fridgetogo.com.google.zxing.integration.android.IntentIntegrator;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScanFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanFragment extends Fragment implements OnClickListener {
    private OnFragmentInteractionListener mListener;


    private Button scanBtn;
    private Button plusButton;
    private Button minusButton;
    private Button okButton;
    private Button finishButton;
    private Date chosenDate = new Date();
    private Spinner spinner;
    private TextView text;
    private TextView name;

    public ScanFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ScanFragment newInstance() {
        ScanFragment fragment = new ScanFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.scan_button){
            IntentIntegrator scanIntegrator = new IntentIntegrator(getActivity());
            scanIntegrator.initiateScan();
        }
        if(view.getId()==R.id.plus_button){
            chosenDate.setDate(chosenDate.getDate() + 1);
            text.setText(niceOutput(chosenDate));

        }
        if(view.getId()==R.id.minus_button){
            chosenDate.setDate(chosenDate.getDate() - 1);
            text.setText(niceOutput(chosenDate));
        }if(view.getId()==R.id.ok_button){
            String ingredient = Database.currentIngredient;
            Database.putCodeToIngredient(ingredient, Database.barcode);
            Database.putInCart(ingredient, new Date(chosenDate.getYear(), chosenDate.getMonth(), chosenDate.getDate()));
            Database.shoppingMode = true;
            FridgeToGo.refreshPreferences();
        } if(view.getId()==R.id.shop_finish){
            Database.throwCartInFridge();
            Database.shoppingMode = false;
            Log.d("TAG", "shop finished");
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        scanBtn = (Button)getView().findViewById(R.id.scan_button);
        scanBtn.setOnClickListener(this);

        text = (TextView)getView().findViewById(R.id.scan_content);
        text.setText(niceOutput(chosenDate));

        name = (TextView)getView().findViewById(R.id.name);
        name.setText("Product : " + Database.currentIngredient);

        plusButton = (Button)getView().findViewById(R.id.plus_button);
        plusButton.setOnClickListener(this);

        minusButton = (Button)getView().findViewById(R.id.minus_button);
        minusButton.setOnClickListener(this);

        okButton = (Button)getView().findViewById(R.id.ok_button);
        okButton.setOnClickListener(this);

        finishButton = (Button)getView().findViewById(R.id.shop_finish);
        finishButton.setOnClickListener(this);

    }

    private String niceOutput(Date d){
        return (d.getDate() + 1) + "/" + (d.getMonth() + 1) + "/" + (1900 + d.getYear());
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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