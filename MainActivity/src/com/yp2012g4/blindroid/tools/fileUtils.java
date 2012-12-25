package com.yp2012g4.blindroid.tools;

import java.io.File;
import java.util.Locale;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class fileUtils {
  private static final String LOG_TAG = "fileUtil";
  
  /* Checks if external storage is available for read and write */
  public static boolean isExternalStorageWritable() {
    String state = Environment.getExternalStorageState();
    if (Environment.MEDIA_MOUNTED.equals(state))
      return true;
    return false;
  }
  
  /* Checks if external storage is available to at least read */
  public static boolean isExternalStorageReadable() {
    String state = Environment.getExternalStorageState();
    if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
      return true;
    return false;
  }
  
  public static File getStorageDir(Context context, String folder) {
    // Get the directory for the app's private pictures directory.
    File file = new File(context.getExternalFilesDir(null), folder);
    if (!file.mkdirs())
      Log.e(LOG_TAG, "Directory not created");
    return file;
  }
  
  public static TYPES getFileExtension(String fileName) {
    // TODO ADD unit testing here.
    if (fileName == null)
      return TYPES.NO_TYPE;
    try {
      String filenameArray[] = fileName.split("\\.");
      String extension = filenameArray[filenameArray.length - 1].toUpperCase(Locale.US);
      if (extension.length() > 0)
        if (extension.equals("MP3"))
          return TYPES.MP3;
        else if (extension.equals("3GPP"))
          return TYPES.THREE_GPP;
        else
          return TYPES.OTHER;
      return TYPES.NO_TYPE;
    } catch (Exception e) {
      return TYPES.NO_TYPE;
    }
  }
  
  public static String getFileNameOnly(String fileName) {
    // TODO ADD unit testing here.
    if (fileName == null)
      return null;
    try {
      String filenameArray[] = fileName.split("\\.");
      return filenameArray[0];
    } catch (Exception e) {
      return null;
    }
  }
  
  public enum TYPES {
    MP3, THREE_GPP, OTHER, NO_TYPE
  }
}
