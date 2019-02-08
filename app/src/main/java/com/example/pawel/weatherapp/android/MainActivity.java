package com.example.pawel.weatherapp.android;

import android.os.Bundle;

import com.example.pawel.weatherapp.R;
import com.example.pawel.weatherapp.databinding.ActivityMainBinding;
import com.example.weatherlibwithcityphotos.MainLib;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableBoolean;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import static com.example.pawel.weatherapp.android.MyApplication.GOOGLE_API_KEY;
import static com.example.pawel.weatherapp.android.MyApplication.WEATHER_API_KEY;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private ObservableBoolean showFAB = new ObservableBoolean(true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setFABcontroller(showFAB);

        setupNavigation();

        MainLib.setup(this, WEATHER_API_KEY, GOOGLE_API_KEY);
    }

    private void setupNavigation() {
        NavController controller = Navigation.findNavController(this, R.id.main_activity_fragment);
        NavigationUI.setupWithNavController(findViewById(R.id.ctb_main_collapse), findViewById(R.id.tb_main_toolbar), controller);

        controller.addOnDestinationChangedListener((controller1, destination, arguments) -> {
            try {
                CharSequence destName = controller1.getCurrentDestination().getLabel();
                if (destName.equals(getResources().getString(R.string.app_name))) {
                    showFAB.set(true);
                } else if (destName.equals("{cityId}")) {
                    showFAB.set(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}

