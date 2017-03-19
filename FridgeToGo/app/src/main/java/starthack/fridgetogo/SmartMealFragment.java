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

public class SmartMealFragment extends Fragment {

    public static SmartMealFragment newInstance() {
        SmartMealFragment fragment = new SmartMealFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ArrayList<Map.Entry<String, Double>> bests = Database.s.proposeDishes();
        int size = bests.size();

        View view = inflater.inflate(R.layout.fragment_smart_meal, container, false);


        TextView text1 = (TextView)view.findViewById(R.id.textView);
        Button button11 = (Button)view.findViewById(R.id.button11);
        Button button12 = (Button)view.findViewById(R.id.button12);

        TextView text2 = (TextView)view.findViewById(R.id.textView2);
        Button button21 = (Button)view.findViewById(R.id.button21);
        Button button22 = (Button)view.findViewById(R.id.button22);

        TextView text3 = (TextView)view.findViewById(R.id.textView3);
        Button button31 = (Button)view.findViewById(R.id.button31);
        Button button32 = (Button)view.findViewById(R.id.button32);

        if(size > 0) {
            final String name1 = bests.get(0).getKey();
            text1.setText("Meal suggestion 1 : " + name1);
            button11.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Database.s.provideHint(name1, true);
                }
            });
            button12.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Database.s.provideHint(name1, false);
                }
            });
        } else{
            text1.setVisibility(View.GONE);
            button11.setVisibility(View.GONE);
            button12.setVisibility(View.GONE);

        }
        if(size > 1) {
            final String name2 = bests.get(1).getKey();
            text2.setText("Meal suggestion 2 : " + name2);
            button21.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Database.s.provideHint(name2, true);
                }
            });
            button22.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Database.s.provideHint(name2, false);
                }
            });
        }else {
            text2.setVisibility(View.GONE);
            button21.setVisibility(View.GONE);
            button22.setVisibility(View.GONE);
        }

        if(size > 2) {
            final String name3 = bests.get(2).getKey();
            text3.setText("Meal suggestion 3 : " + name3);
            button31.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Database.s.provideHint(name3, true);
                }
            });
            button32.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Database.s.provideHint(name3, false);
                }
            });
        } else {
            text3.setVisibility(View.GONE);
            button31.setVisibility(View.GONE);
            button32.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}
