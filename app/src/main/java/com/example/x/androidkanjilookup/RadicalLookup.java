//import android.content.res.Resources;
package com.example.x.androidkanjilookup;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
            KanjiDic.initialize(c);
            for (Radical r : radicalsDb.values())
            {
                Collections.sort(r.relatedKanji, new KanjiComparator());
            }
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
        if (englishStrings.length == 1 && englishStrings[0].equals(""))
            return new ArrayList<String>();

        Set<String> a = getAllKanjiFromRadicalList(getRadicalsFromEnglishString(englishStrings[0].trim()));
        Set<String> b = null;

        for (int i = 1; i < englishStrings.length; i++)
        {
            output = new HashSet<String>();
            b = getAllKanjiFromRadicalList(getRadicalsFromEnglishString(englishStrings[i].trim()));

            for (String s : a)
                if (b.contains(s))
                    output.add(s);

            a = output;
        }

        if (output == null) {
            List<String> l = new ArrayList<String>(a);
            Collections.sort(l, new KanjiComparator());
            return l;
        }
        else {
            List<String> l = new ArrayList<String>(output);
            Collections.sort(l, new KanjiComparator());
            return l;
        }
    }
}