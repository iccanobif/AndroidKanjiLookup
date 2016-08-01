package com.example.x.androidkanjilookup;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    protected class MerdaAdapter extends BaseAdapter
    {
        List<String> strings;
        Context context;

        public MerdaAdapter(Context c, List<String> strings)
        {
            context = c;
            this.strings = strings;
        }

        @Override
        public int getCount() {
            return strings.size();
        }

        @Override
        public Object getItem(int position) {
            return strings;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView != null)
            {
                ((TextView)convertView).setText(strings.get(position));
                return convertView;
            }
            else {
                TextView output = new TextView(context);
                output.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
                output.setTextColor(android.graphics.Color.BLACK);
                output.setText(strings.get(position));
                return output;
            }
        }
    }


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

        //inzializza roba

        GridView filteredKanjiList = (GridView) findViewById(R.id.filteredKanjiList);

        //carica dati

        try {
            RadicalLookup rl = new RadicalLookup(getApplicationContext());
            StringBuilder out = new StringBuilder();
            filteredKanjiList.setAdapter(new MerdaAdapter(this, rl.getKanjiFromEnglishStrings( new String[]{"woman", "roof"})));
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
                GridView filteredKanjiList = (GridView) findViewById(R.id.filteredKanjiList);
                //lblOutput.setText(radicalsInput.getText());

                Date start = new Date();

                try {
                    RadicalLookup rl = new RadicalLookup(getApplicationContext());

                    //String merda = radicalsInput.getText().toString().split(",");

                    StringBuilder out = new StringBuilder();
                    //for (RadicalLookup.Radical r : rl.getRadicalsFromEnglishStringList(Arrays.asList(merda.split(","))))

                    List<String> lista = rl.getKanjiFromEnglishStrings(radicalsInput.getText().toString().toLowerCase().split(","));
                    filteredKanjiList.setAdapter(new MerdaAdapter(getApplicationContext(), lista));

                    out.append(Long.toString(((new Date()).getTime() - start.getTime())));
                    /*out.append(" ");

                    for (int i = 0; i < 100 && i < lista.size(); i++)
                    {
                        out.append(lista.get(i));
                        out.append("/");
                    }

                    if (lista.size() > 100)
                        out.append("...");*/

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
