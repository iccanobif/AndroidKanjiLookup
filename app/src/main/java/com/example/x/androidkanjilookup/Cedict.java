package com.example.x.androidkanjilookup;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.LinkedList;
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

            //readingWithoutTones = re.sub("\d", "", readingWithTones)
            StringBuilder readingWithoutTones = new StringBuilder(readingWithTones.length());
            for (int i = 0; i < readingWithTones.length(); i++)
                if ((int)readingWithTones.charAt(i) < (int)'0'
                        && (int)readingWithTones.charAt(i) > (int)'9')
                    readingWithoutTones.append(readingWithTones.charAt(i));

            addToDictionary(readingWithoutTones.toString(), line);

            /*translations = line[line.find("/"):].lower().replace("/", " ")
            for w in translations.split(" "):
            self.__addToDictionary(w, line)*/


            /*
            String[] split = line.split(" ");
            for (String s : split)
                if (s.charAt(0) == 'S') {
                    int strokeCount = Integer.parseInt(s.substring(1));
                    strokeNumberDictionary.put(split[0], strokeCount);
                    break;
                }*/
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
            LinkedList<String> l = new LinkedList<>();
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

}
