package com.yp2012g4.vision.tools.test;

import junit.framework.TestCase;

import com.yp2012g4.vision.tools.TTS;

@SuppressWarnings("static-method")
public class TTSTest extends TestCase {
  public TTSTest(String name) {
    super(name);
  }
  
  @Override protected void setUp() throws Exception {
    super.setUp();
  }
  
  @Override protected void tearDown() throws Exception {
    super.tearDown();
  }
  
  public void testisPureEnglish() {
    assertTrue(TTS.isPureEnglish("Abcdefghijklmnopqrstuvwxyz"));
    assertTrue(TTS.isPureEnglish("Aabnhdkkmoolelma;oerffen,cmnieio89249fkjfh';adf,zvn"));
    assertTrue(TTS.isPureEnglish(""));
    assertTrue(TTS.isPureEnglish("111111"));
    assertTrue(TTS.isPureEnglish(".,"));
    assertFalse(TTS.isPureEnglish("�"));
    assertFalse(TTS.isPureEnglish("�Aabnhdkkmool����elma;oerffen,cmnieio89249fkjfh';adf,zvn"));
    assertFalse(TTS.isPureEnglish("kkkkkk�"));
  }
  
  public void testSpell() {
    assertEquals("a b c", TTS.spell("abc"));
    assertEquals("", TTS.spell(""));
    assertEquals("a b c d e f g 1 5 6 e", TTS.spell("abcdefg156e"));
  }
}
