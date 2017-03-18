package starthack.fridgetogo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ProductAdapterSmall extends ArrayAdapter<String> {

    public ProductAdapterSmall(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ProductAdapterSmall(Context context, int resource, List<String> products) {
        super(context, resource, products);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.product_item_small,null);
        }

        String s = getItem(position);

        if (s != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.product_item_small);

            if (tt1 != null) {
                tt1.setText(s);
            }
        }

        return v;
    }
}