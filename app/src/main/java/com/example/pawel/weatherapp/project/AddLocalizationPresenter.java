package com.example.pawel.weatherapp.project;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.weatherlibwithcityphotos.MainLib;


public class AddLocalizationPresenter {
    
    private Context context;
    
    public AddLocalizationPresenter(Context context) {
        this.context = context;
    }
    
    public AlertDialog.Builder buildAlertDialog() {
        
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Enter city name");
        
        final EditText input = buildEditText();
        input.setHint("City name");
        
        alertDialogBuilder.setView(input)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    final String cityName = input.getText()
                            .toString().trim();
                    MainLib.downloadNewForecastFor(cityName);
                })
                .setNegativeButton("cancel", (dialog, which) -> dialog.cancel());
        
        return alertDialogBuilder;
    }
    
    private EditText buildEditText() {
        EditText input = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setFocusable(true);
        input.setSelection(input.getText()
                .length());
        return input;
    }
    
}
