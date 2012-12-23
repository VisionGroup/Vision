package com.yp2012g4.blindroid;

import java.io.File;
import java.util.HashSet;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.yp2012g4.blindroid.customUI.TalkingImageButton;
import com.yp2012g4.blindroid.tools.fileUtils;
import com.yp2012g4.blindroid.tools.fileUtils.TYPES;

public class VoiceNoteRecorderActivity extends onTouchEventClass implements
	OnClickListener {
    private final android.media.MediaRecorder _recorder = new android.media.MediaRecorder();
    private final android.media.MediaPlayer _player = new android.media.MediaPlayer();
    private final int _currentNote = 0;
    private final int _numberOfNotes = 0;
    private boolean _usingExternal = true;
    private final String LOG_TAG = "VNR";
    private final String DIR_NAME = "notes";
    private File _notesDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_voice_note_recorder);
	tts = new TextToSpeech(this, this);
	_setAudio();

	TalkingImageButton b = (TalkingImageButton) findViewById(R.id.button_prevNote);

	b.setOnClickListener(this);
	b.setOnTouchListener(this);

	b = (TalkingImageButton) findViewById(R.id.button_nextNote);
	b.setOnClickListener(this);
	b.setOnTouchListener(this);

	b = (TalkingImageButton) findViewById(R.id.button_playStop);
	b.setOnClickListener(this);
	b.setOnTouchListener(this);

	b = (TalkingImageButton) findViewById(R.id.button_recordStop);
	b.setOnClickListener(this);
	b.setOnTouchListener(this);

	TalkingImageButton ib = (TalkingImageButton) findViewById(R.id.next_button);
	ib.setOnClickListener(this);
	ib.setOnTouchListener(this);

	ib = (TalkingImageButton) findViewById(R.id.settings_button);
	ib.setOnClickListener(this);
	ib.setOnTouchListener(this);

	ib = (TalkingImageButton) findViewById(R.id.back_button);
	ib.setOnClickListener(this);
	ib.setOnTouchListener(this);
    }

    private void _setAudio() {
	_recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	// TODO: Check if 3GPP is the best format.
	_recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	_recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
	if ((fileUtils.isExternalStorageReadable())
		&& (fileUtils.isExternalStorageWritable())) // TODO: Allow read
							    // without write.
	    _usingExternal = true;
	else
	    _usingExternal = false;
    }

    @Override
    public void onClick(View v) {
	if (v instanceof TalkingImageButton)
	    // speakOut("Dialing to" + ((TalkingButton)
	    // v).getText().toString());
	    switch (v.getId()) {

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

	    case R.id.next_button:
		speakOut("Next");
		break;
	    case R.id.settings_button:
		speakOut("Settings");
		break;
	    case R.id.back_button:
		speakOut("Back");
		finish();
		break;
	    default:
		break;
	    }

    }

    private void _loadNoteList() {
	if (_usingExternal) {
	    _notesDir = fileUtils.getStorageDir(this, DIR_NAME);
	    if (!_notesDir.isDirectory()) {
		Log.e(LOG_TAG,
			"Directory was not found and couldn't be created");
		// TODO add better error handling!
		return;
	    }

	    File[] files = _notesDir.listFiles();
	    if (files == null) {
		Log.e(LOG_TAG, "Unable to list files in directory");
		// TODO add better error handling!
		return;
	    }
	    HashSet<Integer> notesAvailable = new HashSet<Integer>();
	    for (File file : files)
		if (file.getName().length() > 0)
		    if (fileUtils.getFileExtension(file.getName()) == TYPES.THREE_GPP) {
			String fileName = fileUtils.getFileNameOnly(file
				.getName());
			if (fileName != null)
			    try {
				notesAvailable.add(Integer.valueOf(fileName));
			    } catch (Exception e) {
				Log.e(LOG_TAG,
					"Failed to convert filename to int");
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

    }

}
