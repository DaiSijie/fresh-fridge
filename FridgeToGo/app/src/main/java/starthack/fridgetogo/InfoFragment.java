package starthack.fridgetogo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class InfoFragment extends Fragment {

    public InfoFragment() {
        // Required empty public constructor
    }


    public static InfoFragment newInstance() {
        InfoFragment fragment = new InfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        TextView temperature = (TextView)view.findViewById(R.id.temperature);
        TextView humidity = (TextView)view.findViewById(R.id.humidity);
        Button state = (Button)view.findViewById(R.id.state);

        double temperatureInDegrees = Database.getTemperature()/1000.0;
        temperature.setText("Temperature : " + temperatureInDegrees + "°C");
        humidity.setText("Humidity : " + Database.getHumidity() + "%");

        int color;
        switch(getHappinness(temperatureInDegrees)){
            case 0:
                color = Integer.parseInt("F44336", 16);
                break;
            case 1:
                color = Integer.parseInt("FF9800", 16);
                break;
            case 2:
                color = Integer.parseInt("00E676", 16);
                break;
            default:
                color = Integer.parseInt("00BCD4", 16);
                break;
        }

        state.setBackgroundColor(color);


        return view;
    }

    public int getHappinness(double temperature){
        double tolerance = 5.0;

        //if every ingredient is in its zone then very happy
        boolean happy1 = true;
        boolean happy2 = true;
        for(String ing: Database.getCart().keySet()){
            happy1 = happy1 && temperature >= Database.getMinForIngredient(ing) && temperature <= Database.getMaxForIngredient(ing);
            happy2 = happy2 && temperature >= Database.getMinForIngredient(ing) - tolerance && temperature <= Database.getMaxForIngredient(ing) + tolerance;
        }

        return happy1? 2 : (happy2? 1 : 0);
    }
}
