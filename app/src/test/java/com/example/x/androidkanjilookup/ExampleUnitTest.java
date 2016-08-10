package com.example.x.androidkanjilookup;

import org.junit.Test;

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
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void kanjiDic() throws Exception
    {

        assertEquals(KanjiDic.getSrokeCount("残"), 10);
    }

    @Test
    public void RadicalLookup() throws  Exception
    {

    }
}