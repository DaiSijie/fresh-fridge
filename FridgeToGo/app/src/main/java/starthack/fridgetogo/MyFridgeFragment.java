package starthack.fridgetogo;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MyFridgeFragment extends ListFragment {

    public static MyFridgeFragment newInstance() {
        MyFridgeFragment fragment = new MyFridgeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_fridge, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<Product> products = Database.getFridgeAsProductList();


        ProductAdapter adapter = new ProductAdapter(getContext(), R.layout.product_item, products);
        setListAdapter(adapter);
        //getListView().setOnItemClickListener(this);
    }

    /*@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
    }*/
}
