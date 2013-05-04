package com.yp2012g4.vision.tools.test;

import junit.framework.TestCase;

import com.yp2012g4.vision.tools.TTS;

public class TTSTest extends TestCase {

    public TTSTest(String name) {
	super(name);
    }

    @Override
    protected void setUp() throws Exception {
	super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
	super.tearDown();
    }

    @SuppressWarnings("static-method")
    public void testisPureEnglish() {
	assertTrue(TTS.isPureEnglish("Abcdefghijklmnopqrstuvwxyz"));
	assertTrue(TTS
		.isPureEnglish("Aabnhdkkmoolelma;oerffen,cmnieio89249fkjfh';adf,zvn"));
	assertTrue(TTS.isPureEnglish(""));
	assertTrue(TTS.isPureEnglish("111111"));
	assertTrue(TTS.isPureEnglish(".,"));
	assertFalse(TTS.isPureEnglish("ò"));
	assertFalse(TTS
		.isPureEnglish("òAabnhdkkmoolîéúòelma;oerffen,cmnieio89249fkjfh';adf,zvn"));

	assertFalse(TTS.isPureEnglish("kkkkkkò"));
    }

}
