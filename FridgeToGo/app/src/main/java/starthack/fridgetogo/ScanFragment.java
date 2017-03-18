package starthack.fridgetogo;

import android.content.Intent;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import starthack.fridgetogo.com.google.zxing.integration.android.IntentIntegrator;
import starthack.fridgetogo.com.google.zxing.integration.android.IntentResult;

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
    private BarcodeMapping barcodeMapping;


    private Button scanBtn;
    private long content;

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
        barcodeMapping = ((FridgeToGo)getActivity()).getBarcodeMapping();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.scan_button){
            IntentIntegrator scanIntegrator = new IntentIntegrator(getActivity());
            scanIntegrator.initiateScan();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {
            String scanContent = scanningResult.getContents();
            content = Long.parseLong(scanContent);
            Ingredient ingredient = barcodeMapping.findBarcode(content);
            if(ingredient == null){
                

                ingredient = new Ingredient("Yog", 225, true, true, -1, -1, -1, -1);
                Product product = new Product(ingredient, 2001, 1, 1, 2000, 2, 2, 3.2);
                ((FridgeToGo)getActivity()).addMapping(content, ingredient);
                ((FridgeToGo)getActivity()).addProduct(product);
            } else{
                Date creationDate = null;
                Date peremptionDate = null;
                double price = 2.2;
                Product product = new Product(ingredient, 2001, 1, 1, 2000, 2, 2, price);
                ((FridgeToGo)getActivity()).addProduct(product);
            }
        }
        else{
            Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                    "No scan data received!", Toast.LENGTH_SHORT);
            toast.show();
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

        Button addInfoButton = (Button) getView().findViewById(R.id.add_info_button);
        addInfoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                alertDialog.setTitle("Add the ingredient");

                alertDialog.show();  //<-- See This!
            }

        });
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

    public long getContent() {
        return content;
    }
}
