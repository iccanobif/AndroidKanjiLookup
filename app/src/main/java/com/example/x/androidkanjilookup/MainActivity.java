package com.example.x.androidkanjilookup;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView lblOutput = (TextView) findViewById(R.id.lblOutput);
        Typeface tf = Typeface.createFromAsset(getAssets(),"DroidSansJapanese.ttf");
        //lblOutput.setTypeface(tf);

       /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        try {
            RadicalLookup rl = new RadicalLookup(getApplicationContext());
            StringBuilder out = new StringBuilder();
            for (String k : rl.getKanjiFromEnglishStrings( new String[]{"woman", "roof"})) {
                out.append(k);
                out.append("/");
            }
            lblOutput.setText(out);

        }
        catch (Exception e)
        {
            lblOutput.setText(e.getMessage());
        }


        EditText radicalsInput = (EditText) findViewById(R.id.radicalsInput);

        radicalsInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                EditText radicalsInput = (EditText) findViewById(R.id.radicalsInput);
                TextView lblOutput = (TextView) findViewById(R.id.lblOutput);
                //lblOutput.setText(radicalsInput.getText());

                Date start = new Date();

                try {
                    RadicalLookup rl = new RadicalLookup(getApplicationContext());

                    //String merda = radicalsInput.getText().toString().split(",");

                    StringBuilder out = new StringBuilder();
                    //for (RadicalLookup.Radical r : rl.getRadicalsFromEnglishStringList(Arrays.asList(merda.split(","))))

                    List<String> lista = rl.getKanjiFromEnglishStrings(radicalsInput.getText().toString().toLowerCase().split(","));

                    out.append(Long.toString(((new Date()).getTime() - start.getTime())));
                    out.append(" ");

                    for (int i = 0; i < 100 && i < lista.size(); i++)
                    {
                        out.append(lista.get(i));
                        out.append("/");
                    }

                    if (lista.size() > 100)
                        out.append("...");

                    lblOutput.setText(out.toString());
                } catch (Exception e) {
                    lblOutput.setText(e.getMessage());
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
