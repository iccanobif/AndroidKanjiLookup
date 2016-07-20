//import android.content.res.Resources;
package com.example.x.androidkanjilookup;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class RadicalLookup {
    /*
    Example: from "pers" will return the "person" radicals, that is 人, 𠆢 and 亻
     */

    public class Radical
    {
        public String character;
        public String description;

        public Radical(String character, String description)
        {
            this.character = character;
            this.description = description;
        }
    }

    private ArrayList<Radical> radicals = null;

    public RadicalLookup(Context c) throws Exception
    {
        radicals = new ArrayList<Radical>();
        InputStreamReader f = new InputStreamReader(c.getAssets().open("radicals.txt"), "UTF8");
        BufferedReader r = new BufferedReader(f);
        String line;
        while ((line = r.readLine()) != null)
        {
            String[] split = line.split("\t");
            radicals.add(new Radical(split[0], split[1]));
        }
        r.close();
        f.close();
    }

    List<Radical> getRadicalsFromEnglishString(String englishString) throws Exception
    {
        List<Radical> output = new ArrayList<Radical>();
        for (Radical r : this.radicals)
        {
            if (r.description.contains(englishString))
                output.add(r);
        }

        return output;
    }

    /*public static void main(String args[]) throws Exception
    {
        RadicalLookup rl = new RadicalLookup();

        for (Radical rad : rl.getRadicalsFromEnglishString("croo")) {
            //System.out.println(rad);
            PrintStream out = new PrintStream(System.out, true, "UTF8");
            out.print(rad.character);
        }
    }*/

}

/*
For reading resource files:

    try {
        Resources res = getResources();
        InputStream in_s = res.openRawResource(R.raw.help);

        byte[] b = new byte[in_s.available()];
        in_s.read(b);
        txtHelp.setText(new String(b));
    } catch (Exception e) {
        // e.printStackTrace();
        txtHelp.setText("Error: can't show help.");
    }

 */