package com.example.x.androidkanjilookup;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class KanjiDic {
    public static HashMap<String, Integer> strokeNumberDictionary;

    public static void initialize(Context c) throws Exception
    {
        if (strokeNumberDictionary != null)
            return;

        strokeNumberDictionary = new HashMap<>();

        InputStreamReader f = new InputStreamReader(c.getAssets().open("kanjidic.txt"), "UTF8");
        BufferedReader r = new BufferedReader(f);
        String line;
        while ((line = r.readLine()) != null)
        {
            if (line.charAt(0) == '#')
                continue;
            String[] split = line.split(" ");
            for (String s : split)
                if (s.charAt(0) == 'S') {
                    int strokeCount = Integer.parseInt(s.substring(1));
                    strokeNumberDictionary.put(split[0], strokeCount);
                    break;
                }
        }
        r.close();
        f.close(); //TODO: is this really the right way to close the streams?
    }

    public static int getSrokeCount(String kanji)
    {
        Integer output = strokeNumberDictionary.get(kanji);

        if (output == null)
            return 999;
        return output;
    }


}
