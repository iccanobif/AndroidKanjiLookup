package com.example.x.androidkanjilookup;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

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
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final GridView filteredKanjiList = (GridView) findViewById(R.id.filteredKanjiList);
        final EditText radicalsInput = (EditText) findViewById(R.id.radicalsInput);
        final Button btnRadicalSearch = (Button) findViewById(R.id.btnRadicalSearch);
        final Button btnDictionarySearch = (Button) findViewById(R.id.btnDictionarySearch);
        final Button btnClearRadicalInput = (Button) findViewById(R.id.btnClearRadicalInput);
        final TextView txtTranslations = (TextView) findViewById(R.id.txtTranslations);
        final EditText txtTextInput = (EditText) findViewById(R.id.txtTextInput);
        final TextView lblOutput = (TextView) findViewById(R.id.lblOutput);
        final LinearLayout viewSplittedWords = (LinearLayout) findViewById(R.id.viewSplittedWords);
        //final TextView selectPreviousWord = (TextView) findViewById(R.id.selectPreviousWord);
        //final TextView selectNextWord = (TextView) findViewById(R.id.selectNextWord);

        txtTranslations.setMovementMethod(new ScrollingMovementMethod());

        //setSupportActionBar(toolbar);

        Typeface tf = Typeface.createFromAsset(getAssets(),"DroidSansJapanese.ttf");
        //lblOutput.setTypeface(tf);

        try {
            KanjiDic.initialize(this);
            Cedict.initialize(this);
        }
        catch (Exception e)
        {
            lblOutput.setText(e.getMessage());
        }

        radicalsInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Date start = new Date();
                try {
                    RadicalLookup rl = new RadicalLookup(getApplicationContext());
                    List<String> lista = rl.getKanjiFromEnglishStrings(radicalsInput.getText().toString().toLowerCase().split(","));
                    filteredKanjiList.setAdapter(new MerdaAdapter(getApplicationContext(), lista));
                    lblOutput.setText(Long.toString(((new Date()).getTime() - start.getTime())));
                } catch (Exception e) {
                    lblOutput.setText(e.getMessage());
                }
            }
        });

        txtTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    List<String> words = Cedict.splitSentence(txtTextInput.getText().toString());
                    setSplittedWords(words);
                    //showTranslation(txtTextInput.getText().toString());
                }
                catch (Exception e)
                {
                    txtTranslations.setText(e.getMessage());
                }
            }
        });

        filteredKanjiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedKanji = ((TextView)view).getText().toString();
                txtTextInput.append(selectedKanji);
            }
        });

        btnRadicalSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filteredKanjiList.setVisibility(View.VISIBLE);
                txtTranslations.setVisibility(View.GONE);
            }
        });

        btnDictionarySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filteredKanjiList.setVisibility(View.GONE);
                txtTranslations.setVisibility(View.VISIBLE);
            }
        });

        btnClearRadicalInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radicalsInput.setText("");
            }
        });

        /*selectPreviousWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < viewSplittedWords.getChildCount(); i++) {
                    if (!(viewSplittedWords.getChildAt(i) instanceof TextView))
                        continue;
                    TextView
                    ((TextView) viewSplittedWords.getChildAt(i)).setPaintFlags(
                            ((TextView) viewSplittedWords.getChildAt(i)).getPaintFlags() & ~TextPaint.UNDERLINE_TEXT_FLAG);
                }
            }
        });*/
    }

    private void showTranslation(String word)
    {
        final TextView txtTranslations = (TextView) findViewById(R.id.txtTranslations);

        txtTranslations.scrollTo(0, 0);
        List<String> translations = Cedict.getTranslations(word);
        if (translations == null)
            txtTranslations.setText("Nothing to see here....");
        else {
            StringBuilder output = new StringBuilder();
            for (String t : translations)
                output.append(t + "\n");
            txtTranslations.setText(output.toString());
        }
    }

    private void setSplittedWords(List<String> words)
    {
        final LinearLayout viewSplittedWords = (LinearLayout) findViewById(R.id.viewSplittedWords);
        viewSplittedWords.removeAllViews();
        for (String w: words)
        {
            TextView tmp = new TextView(this);
            tmp.setTextColor(android.graphics.Color.BLACK);
            tmp.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
            tmp.setText(w);
            tmp.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                           ViewGroup.LayoutParams.WRAP_CONTENT));
            tmp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView tv = (TextView)v;
                    showTranslation(tv.getText().toString());

                    //underline stuff
                    for (int i = 0; i < viewSplittedWords.getChildCount(); i++)
                        if (viewSplittedWords.getChildAt(i) instanceof TextView)
                            ((TextView)viewSplittedWords.getChildAt(i)).setPaintFlags(
                                    ((TextView)viewSplittedWords.getChildAt(i)).getPaintFlags() & ~TextPaint.UNDERLINE_TEXT_FLAG);
                    tv.setPaintFlags(TextPaint.UNDERLINE_TEXT_FLAG);
                }
            });
            viewSplittedWords.addView(tmp);
            Space space = new Space(this);
            space.setMinimumWidth(10);
            viewSplittedWords.addView(space);
        }

    }

}
