package com.example.zebli.restauranttipper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.DecimalFormat;

// TODO: DESIGN APP BETTER

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Spinner dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"Waiter", "Bartender", "Hair Stylist", "Taxi Driver", "Massage Therapist"}; // items for dropdown
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        final ToggleButton goodservice = (ToggleButton)findViewById(R.id.goodService);
        final ToggleButton badservice = (ToggleButton)findViewById(R.id.badService);
        final ToggleButton okayservice = (ToggleButton)findViewById(R.id.okayService);

        /****GOOD SERVICE BUTTON FUNC***/
        goodservice.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(goodservice.isChecked())
                {
                    badservice.setChecked(false);
                    okayservice.setChecked(false);
                }
            }
        });
        /****BAD SERVICE BUTTON FUNC***/
        badservice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(badservice.isChecked())
                {
                    goodservice.setChecked(false);
                    okayservice.setChecked(false);
                }
            }
        });
        /****OKAY SERVICE BUTTON FUNC***/
        okayservice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(okayservice.isChecked())
                {
                    goodservice.setChecked(false);
                    badservice.setChecked(false);
                }
            }
        });

        final EditText billinput = (EditText)findViewById(R.id.billinput);
        final TextView errortext = (TextView)findViewById(R.id.errortext);
        final TextView finaloutput = (TextView)findViewById(R.id.finaloutput);
        final SeekBar tipseekbar = (SeekBar)findViewById(R.id.tipseekbar);
        final DecimalFormat df2 = new DecimalFormat(".##");


        Button submit = (Button)findViewById(R.id.submitButton); // submit button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* ERROR CHECKING */
                if(billinput.getText().length() == 0)
                {
                    errortext.setText(R.string.erroramount);
                }
                else if(!buttonsChecked(okayservice, badservice, goodservice))
                {
                    errortext.setText(R.string.errorrating);
                }
                else // all errors checked
                {
                    String buttonSelected = buttonSelected(goodservice, okayservice, badservice);
                    // return tip
                    double bill = Double.parseDouble(billinput.getText().toString()); // holds bill amount
                    double percentage = percentageTip(dropdown.getSelectedItem().toString(), buttonSelected);
                    finaloutput.setText("$" + df2.format(bill * percentage));
                    tipseekbar.setVisibility(View.VISIBLE);
                    int progressPercent = (int)(percentage * 100);
                    tipseekbar.setProgress(progressPercent);
                }
            }
        });

        final TextView textView = (TextView)findViewById(R.id.textview);

        tipseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                double percentage = (double)progress / 100;
                double bill = Double.parseDouble(billinput.getText().toString());
                // Updates Tip Amount when slider moves
                finaloutput.setText("$" + df2.format(percentage * bill));


                // Update Text showing progress
                int val = (progress * (seekBar.getWidth() - 2 * seekBar.getThumbOffset())) / seekBar.getMax();
                textView.setText("" + progress + "%");
                textView.setX(seekBar.getX() + val + seekBar.getThumbOffset() / 2);
                //textView.setY(100); just added a value set this properly using screen with height aspect ratio , if you do not set it by default it will be there below seek bar

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });



    }

    // Returns string of chosen button
    private String buttonSelected(ToggleButton goodservice, ToggleButton okayservice, ToggleButton badservice) {
        if(goodservice.isChecked()){return "Good";}
        if(okayservice.isChecked()){return "Okay";}
        else{return "Bad";}
    }

    // Returns tip percentage as an int
    private double percentageTip(String type, String quality) {

        if(quality.equals("Good"))
        {
            switch (type){
                case "Waiter":
                    return .20;
                case "Bartender":
                    return .15;
                case "Taxi Driver":
                    return .17;
                case "Hair Stylist":
                    return .18;
                case "Massage Therapist":
                    return .2;
            }
        }
        if(quality.equals("Okay"))
        {
            switch (type){
                case "Waiter":
                    return .15;
                case "Bartender":
                    return .12;
                case "Taxi Driver":
                    return .14;
                case "Hair Stylist":
                    return .15;
                case "Massage Therapist":
                    return .15;
            }
        }
        else // bad quality
        {
            switch (type)
            {
                case "Waiter":
                    return .10;
                case "Bartender":
                    return .10;
                case "Taxi Driver":
                    return .10;
                case "Hair Stylist":
                    return .10;
                case "Massage Therapist":
                    return .1;
            }
        }
        return 0; // never going to happen, just to appease the compiler
    }

    // Returns bool value depending if 1 togglebutton is checked
    boolean buttonsChecked(ToggleButton okay, ToggleButton bad, ToggleButton good)
    {
        if(okay.isChecked())
        {
            if(bad.isChecked() || good.isChecked())
            {
                return false; // more than 1 button checked
            }
            else
            {
                return true;
            }
        }
        else if(bad.isChecked()){
            // Now we know okay isn't checked
            if(good.isChecked())
            {
                return false; // more than 1 button checked
            }
            else
            {
                return true;
            }
        }
        else if(good.isChecked())
        {
            // Only button checked
            return true;
        }
        else{
            // no buttons checked
            return false;
        }

    }

}
