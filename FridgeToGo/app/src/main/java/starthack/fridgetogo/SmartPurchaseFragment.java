package starthack.fridgetogo;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

public class SmartPurchaseFragment extends Fragment {

    public static SmartPurchaseFragment newInstance() {
        SmartPurchaseFragment fragment = new SmartPurchaseFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ArrayList<Map.Entry<String, ArrayList<Double>>> bests = Database.s.proposeShopping();
        int size = bests.size();

        View view = inflater.inflate(R.layout.fragment_smart_purchase, container, false);

        TextView text1 = (TextView)view.findViewById(R.id.textView);
        TextView text2 = (TextView)view.findViewById(R.id.textView2);
        TextView text3 = (TextView)view.findViewById(R.id.textView3);
        text1.setText(size > 0 ? "Shopping suggestion 1: " + bests.get(0).getKey() : "No suggestions yet.");
        text2.setText(size > 1 ? "Shopping suggestion 2: " + bests.get(1).getKey() : "");
        text3.setText(size > 2 ? "Shopping suggestion 3: " + bests.get(2).getKey() : "");

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
