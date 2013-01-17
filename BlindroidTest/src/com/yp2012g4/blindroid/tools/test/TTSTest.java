package com.yp2012g4.blindroid.tools.test;

import junit.framework.TestCase;

import com.yp2012g4.blindroid.tools.TTS;

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

    public void testTTS() {
	fail("Not yet implemented");
    }

    public void testIsRuning() {
	fail("Not yet implemented");
    }

    public void testSetQueueMode() {
	fail("Not yet implemented");
    }

    public void testSetLanguage() {
	fail("Not yet implemented");
    }

    public void testSpeak() {
	fail("Not yet implemented");
    }

    public void testStop() {
	fail("Not yet implemented");
    }

    public void testShutdown() {
	fail("Not yet implemented");
    }

    public void testIsSpeaking() {
	fail("Not yet implemented");
    }

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
