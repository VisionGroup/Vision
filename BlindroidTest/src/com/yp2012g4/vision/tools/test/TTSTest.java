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
    public void testIsPureEnglise() {
	assertTrue(TTS.isPureEnglise("Abcdefghijklmnopqrstuvwxyz"));
	assertTrue(TTS
		.isPureEnglise("Aabnhdkkmoolelma;oerffen,cmnieio89249fkjfh';adf,zvn"));
	assertTrue(TTS.isPureEnglise(""));
	assertTrue(TTS.isPureEnglise("111111"));
	assertTrue(TTS.isPureEnglise(".,"));
	assertFalse(TTS.isPureEnglise("ò"));
	assertFalse(TTS
		.isPureEnglise("òAabnhdkkmoolîéúòelma;oerffen,cmnieio89249fkjfh';adf,zvn"));

	assertFalse(TTS.isPureEnglise("kkkkkkò"));
    }

}
