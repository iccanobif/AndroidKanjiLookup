//import android.content.res.Resources;
package com.example.x.androidkanjilookup;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
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

    public static class Radical
    {
        public String character;
        public String description;
        public ArrayList<String> relatedKanji;

        public Radical(String character, String description)
        {
            this.character = character;
            this.description = description;
            this.relatedKanji = new ArrayList<>();
        }
    }

    private static HashMap<String, Radical> radicalsDb = null;

    public static void initialize(Context c) throws Exception
    {
        if (radicalsDb == null) {
            radicalsDb = new HashMap<>();
            loadRadicalsDb(c);
            loadKradFile(c);
            completeConnections();
            KanjiDic.initialize(c);
            for (Radical r : radicalsDb.values())
            {
                Collections.sort(r.relatedKanji, new KanjiComparator());
            }
        }
    }

    private static void loadRadicalsDb(Context c) throws Exception
    {
        InputStreamReader f;
        if (c != null)
            f = new InputStreamReader(c.getAssets().open("radicals.txt"), "UTF8");
        else
            f = new InputStreamReader(new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\assets\\radicals.txt"), "UTF8");

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

    private static void loadKradFile(Context c) throws Exception
    {
        //丼 : ｜ ノ 二 丶 廾 井
        InputStreamReader f;
        if (c != null)
            f = new InputStreamReader(c.getAssets().open("kradfile-u.txt"), "UTF8");
        else
            f = new InputStreamReader(new FileInputStream(System.getProperty("user.dir") + "\\src\\main\\assets\\kradfile-u.txt"), "UTF8");

        BufferedReader r = new BufferedReader(f);
        String line;
        while ((line = r.readLine()) != null)
        {
            if (line.charAt(0) == '#')
                continue;
            if (line.charAt(0) == '務')
            {
                line = line;
            }
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

    public static void completeConnections()
    {
        for (Radical r : new ArrayList<>(radicalsDb.values()))
            for (String k: new ArrayList<>(r.relatedKanji))
                if (radicalsDb.keySet().contains(k))
                    for (String n: new ArrayList<>(radicalsDb.get(k).relatedKanji))
                        if (!radicalsDb.get(r.character).relatedKanji.contains(n))
                            radicalsDb.get(r.character).relatedKanji.add(n);
    }

    public static List<Radical> getRadicalsFromEnglishString(String englishString) throws Exception
    {
        List<Radical> output = new ArrayList<Radical>();
        for (Radical r : radicalsDb.values())
        {
            if (r.description.contains(englishString))
                output.add(r);
        }

        return output;
    }

    public static Set<String> getAllKanjiFromRadicalList(List<Radical> radicalList)
    {
        HashSet<String> output = new HashSet<>();

        for (Radical r : radicalList)
            output.addAll(r.relatedKanji);

        return output;
    }

    //This kinda sucks... there must be a better way of doing an intersection of Lists...
    public static List<String> getKanjiFromEnglishStrings(String[] englishStrings) throws Exception
    {
        Set<String> output = null;

        if (englishStrings.length == 0)
            return new ArrayList<>();
        if (englishStrings.length == 1 && englishStrings[0].equals(""))
            return new ArrayList<>();

        Set<String> a = getAllKanjiFromRadicalList(getRadicalsFromEnglishString(englishStrings[0].trim()));
        Set<String> b = null;

        for (int i = 1; i < englishStrings.length; i++)
        {
            output = new HashSet<>();
            b = getAllKanjiFromRadicalList(getRadicalsFromEnglishString(englishStrings[i].trim()));

            for (String s : a)
                if (b.contains(s))
                    output.add(s);

            a = output;
        }

        if (output == null) {
            List<String> l = new ArrayList<>(a);
            Collections.sort(l, new KanjiComparator());
            return l;
        }
        else {
            List<String> l = new ArrayList<>(output);
            Collections.sort(l, new KanjiComparator());
            return l;
        }
    }
}