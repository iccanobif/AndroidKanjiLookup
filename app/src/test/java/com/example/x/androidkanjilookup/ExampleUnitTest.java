package com.example.x.androidkanjilookup;

import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    public ExampleUnitTest() throws Exception
    {
        KanjiDic.initialize(null);
        RadicalLookup.initialize(null);
        Cedict.initialize(null);
    }

    @Test
    public void kanjiDic() throws Exception
    {
        assertEquals(KanjiDic.getSrokeCount("残"), 10);
    }

    @Test
    public void RadicalLookupEmpty() throws  Exception
    {
        List<String> l = RadicalLookup.getKanjiFromEnglishStrings(new String[] {""});
        assert(l.size() == 0);
    }

    @Test
    public void RadicalLookup() throws  Exception
    {
        List<String> l = RadicalLookup.getKanjiFromEnglishStrings(new String[] {"action", "power"});
        assert(l.contains("務"));
    }

    @Test
    public void fdafdsa() throws Exception
    {
        List<RadicalLookup.Radical> action = RadicalLookup.getRadicalsFromEnglishString("action");
        List<RadicalLookup.Radical> power = RadicalLookup.getRadicalsFromEnglishString("power");
        assert(RadicalLookup.getAllKanjiFromRadicalList(action).contains("務")
                && RadicalLookup.getAllKanjiFromRadicalList(power).contains("務"));
    }

    @Test
    public void CedictByKanji() throws Exception
    {
        String s = new String();
        for (String t : Cedict.getTranslations("女"))
            s += t;
        assert(s.contains("woman"));
    }

    @Test
    public void CedictByReadingWithTones() throws Exception
    {

        String s = new String();
        for (String t : Cedict.getTranslations("ni3hao3"))
            s += t;
        assert(s.contains("Hello"));
    }

    @Test
    public void CedictByReadingWithoutTones() throws Exception
    {

        String s = new String();
        for (String t : Cedict.getTranslations("nihao"))
            s += t;
        assert(s.contains("Hello"));
    }
}