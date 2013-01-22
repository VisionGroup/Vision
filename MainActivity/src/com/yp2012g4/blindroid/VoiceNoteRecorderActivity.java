package com.yp2012g4.blindroid;

import java.io.File;
import java.util.HashSet;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.yp2012g4.blindroid.tools.BlindroidActivity;
import com.yp2012g4.blindroid.tools.fileUtils;
import com.yp2012g4.blindroid.tools.fileUtils.TYPES;

public class VoiceNoteRecorderActivity extends BlindroidActivity {
  private final android.media.MediaRecorder _recorder = new android.media.MediaRecorder();
  @SuppressWarnings("unused") private final android.media.MediaPlayer _player = new android.media.MediaPlayer();
  @SuppressWarnings("unused") private final int _currentNote = 0;
  @SuppressWarnings("unused") private final int _numberOfNotes = 0;
  private boolean _usingExternal = true;
  private final String LOG_TAG = "VNR";
  private final String DIR_NAME = "notes";
  private File _notesDir;
  
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_voice_note_recorder);
    _setAudio();
  }
  
  private void _setAudio() {
    _recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    // TODO: Check if 3GPP is the best format.
    _recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
    _recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
    if ((fileUtils.isExternalStorageReadable()) && (fileUtils.isExternalStorageWritable())) // TODO:
                                                                                            // Allow
                                                                                            // read
      // without write.
      _usingExternal = true;
    else
      _usingExternal = false;
  }
  
  @Override public boolean onSingleTapUp(MotionEvent e) {
    super.onSingleTapUp(e);
    switch (curr_view.getId()) {
      case R.id.button_prevNote:
        _prevNote();
        break;
      case R.id.button_nextNote:
        _nextNote();
        break;
      case R.id.button_playStop:
        _playStop();
        break;
      case R.id.button_recordStop:
        _recordStop();
        break;
      default:
        break;
    }
    return false;
  }
  
  @SuppressWarnings("unused") private void _loadNoteList() {
    if (_usingExternal) {
      _notesDir = fileUtils.getStorageDir(this, DIR_NAME);
      if (!_notesDir.isDirectory()) {
        Log.e(LOG_TAG, "Directory was not found and couldn't be created");
        // TODO add better error handling!
        return;
      }
      final File[] files = _notesDir.listFiles();
      if (files == null) {
        Log.e(LOG_TAG, "Unable to list files in directory");
        // TODO add better error handling!
        return;
      }
      final HashSet<Integer> notesAvailable = new HashSet<Integer>();
      for (final File file : files)
        if (file.getName().length() > 0)
          if (fileUtils.getFileExtension(file.getName()) == TYPES.THREE_GPP) {
            final String fileName = fileUtils.getFileNameOnly(file.getName());
            if (fileName != null)
              try {
                notesAvailable.add(Integer.valueOf(fileName));
              } catch (final Exception e) {
                Log.e(LOG_TAG, "Failed to convert filename to int");
              }
          }
    }
  }
  
  private void _recordStop() {
    // TODO Auto-generated method stub
  }
  
  private void _nextNote() {
    // TODO Auto-generated method stub
  }
  
  private void _prevNote() {
    // TODO Auto-generated method stub
  }
  
  private void _playStop() {
    // TODO Auto-generated method stub
  }
  
  @Override public int getViewId() {
    return R.id.VoiceNoteRecorderActivity;
  }
}
