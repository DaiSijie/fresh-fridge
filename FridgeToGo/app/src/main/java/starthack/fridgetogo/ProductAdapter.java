package starthack.fridgetogo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    public ProductAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ProductAdapter(Context context, int resource, List<Product> products) {
        super(context, resource, products);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.product_item,null);
        }

        Product p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.id);
            TextView tt2 = (TextView) v.findViewById(R.id.peremptionDate);

            if (tt1 != null) {
                tt1.setText(p.getIngredient().getName());
            }

            if (tt2 != null) {
                Format formatter = new SimpleDateFormat("yyyy-MM-dd");
                String date = formatter.format(p.getPeremptionDate());
                tt2.setText("will be rotten on " + date);
            }
        }

        return v;
    }
}