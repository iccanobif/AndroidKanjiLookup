//import android.content.res.Resources;
package com.example.x.androidkanjilookup;

import android.content.Context;
import android.support.v7.util.SortedList;
import android.util.ArraySet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class RadicalLookup {
    /*
    Example: from "pers" will return the "person" radicalsDb, that is 人, ? and 亻
     */

    public class Radical
    {
        public String character;
        public String description;
        public ArrayList<String> relatedKanji;

        public Radical(String character, String description)
        {
            this.character = character;
            this.description = description;
            this.relatedKanji = new ArrayList<String>();
        }
    }

    private static HashMap<String, Radical> radicalsDb = null;

    public RadicalLookup(Context c) throws Exception
    {
        if (radicalsDb == null) {
            radicalsDb = new HashMap<String, Radical>();
            loadRadicalsDb(c);
            loadKradFile(c);
        }
    }

    private void loadRadicalsDb(Context c) throws Exception
    {
        InputStreamReader f = new InputStreamReader(c.getAssets().open("radicals.txt"), "UTF8");
        BufferedReader r = new BufferedReader(f);
        String line;
        while ((line = r.readLine()) != null)
        {
            if (line.charAt(0) == '#')
                continue;
            String[] split = line.split("\t");
            radicalsDb.put(split[0],  new Radical(split[0], split[1]));
        }
        r.close();
        f.close(); //TODO: is this really the right way to close the streams?
    }

    private void loadKradFile(Context c) throws Exception
    {
        //丼 : ｜ ノ 二 丶 廾 井
        InputStreamReader f = new InputStreamReader(c.getAssets().open("kradfile-u.txt"), "UTF8");
        BufferedReader r = new BufferedReader(f);
        String line;
        while ((line = r.readLine()) != null)
        {
            if (line.charAt(0) == '#')
                continue;
            String[] split = line.split(" ");
            for (int i = 2; i < split.length; i++)
            {
                Radical rad = radicalsDb.get(split[i]);
                rad.relatedKanji.add(split[0]);
            }
        }
        r.close();
        f.close();
    }

    List<Radical> getRadicalsFromEnglishString(String englishString) throws Exception
    {
        List<Radical> output = new ArrayList<Radical>();
        for (Radical r : this.radicalsDb.values())
        {
            if (r.description.contains(englishString))
                output.add(r);
        }

        return output;
    }

    Set<String> getAllKanjiFromRadicalList(List<Radical> radicalList)
    {
        HashSet<String> output = new HashSet<String>();

        for (Radical r : radicalList)
            output.addAll(r.relatedKanji);

        return output;
    }

    //This kinda sucks... there must be a better way of doing an intersection of Lists...
    List<String> getKanjiFromEnglishStrings(String[] englishStrings) throws Exception
    {
        Set<String> output = null;

        if (englishStrings.length == 0)
            return new ArrayList<String>();

        Set<String> a = getAllKanjiFromRadicalList(getRadicalsFromEnglishString(englishStrings[0]));
        Set<String> b = null;

        for (int i = 1; i < englishStrings.length; i++)
        {
            output = new HashSet<String>();
            b = getAllKanjiFromRadicalList(getRadicalsFromEnglishString(englishStrings[i]));

            for (String s : a)
                if (b.contains(s))
                    output.add(s);

            a = output;
        }

        if (output == null)
            return new ArrayList<String>(a);
        else
            return new ArrayList<String>(output);
    }

    List<String> getKanjiFromRadicals(List<Radical> radicals)
    {
        return null;
    }

    //This is probably useless...
    /*List<Radical> getRadicalsFromEnglishStringList(List<String> englishStrings) throws Exception
    {
        List<Radical> output = new ArrayList<Radical>();
        Hashtable<String, Radical> h = new Hashtable<String, Radical>();

        for (String s : englishStrings)
            for (Radical r : getRadicalsFromEnglishString(s))
                if (!h.containsKey(r.character))
                    h.put(r.character, r);

        for (Radical r : h.values())
            output.add(r);

        return output;
    }*/

    List<String> getStuff()
    {
        return radicalsDb.get("女").relatedKanji;
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