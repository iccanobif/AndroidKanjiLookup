package com.example.x.androidkanjilookup;

import java.util.Comparator;

public class KanjiComparator implements Comparator<String> {
    public int compare(String k1, String k2)
    {
        int strokeCount1 = KanjiDic.getSrokeCount(k1);
        int strokeCount2 = KanjiDic.getSrokeCount(k2);
        if (strokeCount1 < strokeCount2) {
            return -1;
        }
        else {
            if (strokeCount1 > strokeCount2) {
                return 1;
            }
            else {
                return k1.compareTo(k2);
            }
        }
    }
}
