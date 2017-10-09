package com.example.remote;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.ConsumerIrManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ConsumerIrTest";
    TextView mFreqsText;
    ConsumerIrManager mCIR;

    /**
     * Initialization of the Activity after it is first created.  Must at least
     * call {@link android.app.Activity#setContentView setContentView()} to
     * describe what is to be displayed in the screen.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Be sure to call the super class.
        super.onCreate(savedInstanceState);



        // Get a reference to the ConsumerIrManager
        mCIR = (ConsumerIrManager)getSystemService(Context.CONSUMER_IR_SERVICE);

        // See assets/res/any/layout/consumer_ir.xml for this
        // view layout definition, which is being set here as
        // the content of our screen.
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        //getWindow().getDecorView().setBackgroundColor(Color.parseColor("#415370"));

        // Set the OnClickListener for the button so we see when it's pressed.
        findViewById(R.id.red_button).setOnClickListener(mSendClickListener);
        findViewById(R.id.black_button_info).setOnClickListener(toastListener);


        //findViewById(R.id.send_button).setOnClickListener(mSendClickListener);
        //findViewById(R.id.get_freqs_button).setOnClickListener(mGetFreqsClickListener);
        //mFreqsText = (TextView) findViewById(R.id.freqs_text);
    }

    View.OnClickListener mSendClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (!mCIR.hasIrEmitter()) {
                Log.e(TAG, "No IR Emitter found\n");
                return;
            }

            // A pattern of alternating series of carrier on and off periods measured in
            // microseconds.
            int[] pattern = {1901, 4453, 625, 1614, 625, 1588, 625, 1614, 625, 442, 625, 442, 625,
                    468, 625, 442, 625, 494, 572, 1614, 625, 1588, 625, 1614, 625, 494, 572, 442, 651,
                    442, 625, 442, 625, 442, 625, 1614, 625, 1588, 651, 1588, 625, 442, 625, 494, 598,
                    442, 625, 442, 625, 520, 572, 442, 625, 442, 625, 442, 651, 1588, 625, 1614, 625,
                    1588, 625, 1614, 625, 1588, 625, 48958};

            // transmit the pattern at 38.4KHz
            mCIR.transmit(38400, pattern);
        }
    };

    View.OnClickListener toastListener = new View.OnClickListener() {
        public void onClick(View v) {
            Context context = getApplicationContext();
            CharSequence text = "I denne menyen kan du velge Kanaler med knappene. Du kan skru av/På Roboten ved å trykke på den røde AV/På knappen. Du kan også endre til Kommandomodus helt øverst ";
            int duration = Toast.LENGTH_LONG;

            for (int i=0; i < 3; i++)
            {
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        }
    };







    View.OnClickListener mGetFreqsClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            StringBuilder b = new StringBuilder();

            if (!mCIR.hasIrEmitter()) {
                mFreqsText.setText("No IR Emitter found!");
                Log.e(TAG, "No IR Emitter found!\n");
                return;
            }

            // Get the available carrier frequency ranges
            ConsumerIrManager.CarrierFrequencyRange[] freqs = mCIR.getCarrierFrequencies();
            b.append("IR Carrier Frequencies:\n");
            for (ConsumerIrManager.CarrierFrequencyRange range : freqs) {
                b.append(String.format("    %d - %d\n", range.getMinFrequency(),
                        range.getMaxFrequency()));
            }
            mFreqsText.setText(b.toString());
        }
    };

    public class CustomWindowTitle extends Activity {
        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

            setContentView(R.layout.activity_main);

            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        }
    }



}
