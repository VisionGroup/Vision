package com.yp2012g4.vision.apps.settings;

import java.util.ArrayList;
import java.util.Locale;

public class Language {
  private static final ArrayList<Locale> _availableLocale = new ArrayList<Locale>() {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    {
      add(Locale.ENGLISH);
      add(new Locale("iw")); // TODO Change to HE including the res library.
    }
  };
  private static final Locale _defaultLocal = Locale.US;
  
  /**
   * 
   * @param l
   * @return true if this language is supported by the application (has
   *         translation files for it)
   */
  public static boolean isAvailable(final Locale l) {
    return _availableLocale.contains(l);
  }
  
  /**
   * 
   * @return a list of available Locals which are supported by the application.
   */
  public static ArrayList<Locale> availableLocals() {
    return _availableLocale;
  }
  
  /**
   * 
   * @return The applications default Locale.
   */
  public static Locale getDefaultLocale() {
    return _defaultLocal;
  }
}
