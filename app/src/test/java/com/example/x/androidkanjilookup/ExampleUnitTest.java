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
        for (String s : l)
            System.out.println(s);
        assert(l.contains("務"));
    }

    @Test
    public void fdafdsa() throws Exception
    {
        //Fuck reflection
        /*Class c = Class.forName("RadicalLookup");
        Method m = c.getDeclaredMethod("dsfdsaf");*/
        List<RadicalLookup.Radical> action = RadicalLookup.getRadicalsFromEnglishString("action");
        List<RadicalLookup.Radical> power = RadicalLookup.getRadicalsFromEnglishString("power");
        Set<String> set = RadicalLookup.getAllKanjiFromRadicalList(action);
        assert(set.contains("務"));
        //assert(RadicalLookup.getAllKanjiFromRadicalList(power).contains("務"));

    }
}