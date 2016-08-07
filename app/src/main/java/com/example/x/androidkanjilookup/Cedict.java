package com.example.x.androidkanjilookup;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by x on 07/08/2016.
 */
public class Cedict {

    public static Hashtable<String, List<String>> dictionary;

    public static void initialize(Context c) throws Exception
    {
        if (dictionary != null)
            return;

        dictionary = new Hashtable<>();

        InputStreamReader f = new InputStreamReader(c.getAssets().open("cedict.txt"), "UTF8");
        BufferedReader r = new BufferedReader(f);
        String line;
        while ((line = r.readLine()) != null)
        {
            if (line.charAt(0) == '#')
                continue;
            int traditionalSimplifiedSeparator = line.indexOf(' ');
            //traditional = line[0:i]
            String traditional = line.substring(0, traditionalSimplifiedSeparator);
            //simplified = line[i+1:line.find(" ", i+1)]
            String simplified = line.substring(traditionalSimplifiedSeparator+1, line.indexOf(' ', traditionalSimplifiedSeparator+1));
            //reading = line[line.find("[") + 1:line.find("]")].lower().replace("u:", "v")
            String reading = line.substring(line.indexOf("[")+1, line.indexOf("]")).toLowerCase().replace("u:", "v");
            //readingWithLinks = " ".join(["<a href='{0}'>{0}</a>".format(x) for x in reading.split(" ")])
            //line = line[0:line.find("[")+1] + readingWithLinks + line[line.find("]"):]

            //self.__addToDictionary(traditional, line)
            addToDictionary(traditional, line);

            if (traditional.compareTo(simplified) != 0)
                addToDictionary(simplified, line);

            String readingWithTones = reading.replace(" ", "");
            addToDictionary(readingWithTones, line);

            StringBuilder readingWithoutTones = new StringBuilder(readingWithTones.length());
            for (int i = 0; i < readingWithTones.length(); i++)
                if ((int)readingWithTones.charAt(i) < (int)'0'
                        || (int)readingWithTones.charAt(i) > (int)'9')
                    readingWithoutTones.append(readingWithTones.charAt(i));

            addToDictionary(readingWithoutTones.toString(), line);

            //translations (too memory hungry, as it is...)
            /*for (String w : line.substring(line.indexOf("/")).toLowerCase().replace('/', ' ').split(" "))
                addToDictionary(w, line);*/
        }
        r.close();
        f.close(); //TODO: is this really the right way to close the streams?
    }

    private static void addToDictionary(String key, String content)
    {
        if (dictionary.containsKey(key))
            dictionary.get(key).add(content);
        else
        {
            ArrayList<String> l = new ArrayList<>();
            l.add(content);
            dictionary.put(key, l);
        }
    }

    public static List<String> getTranslations(String text)
    {
        if (text == null || text.isEmpty())
            return null;
        return dictionary.get(text.toLowerCase());
    }

    public static List<String> splitSentence(String text)
    {
        List<String> output = new ArrayList<>();
        if (text.length() == 0)
            return output;
        if (text.length() == 1)
        {
            output.add(text);
            return output;
        }
        for (int length = text.length(); length > 0; length--)
        {
            for (int i = 0; i < text.length() - length + 1; i++)
            {
                String t = text.substring(i, i+length);
                if (getTranslations(t) != null)
                {
                    output.addAll(splitSentence(text.substring(0, i)));
                    output.add(t);
                    output.addAll(splitSentence(text.substring(i+length)));
                    return output;
                }
            }
        }

        output.add(text);
        return output;
    }

}
