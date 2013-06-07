package com.yp2012g4.vision.apps.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.yp2012g4.vision.R;
import com.yp2012g4.vision.managers.ContactManager;
import com.yp2012g4.vision.managers.ContactType;
import com.yp2012g4.vision.tools.VisionActivity;

/**
 * A class for an sending SMS
 * 
 * @author Amir B
 * @version 2.0
 */
public class AddContactActivity extends VisionActivity {
  private static final int TEXT = 0;
  private static final int PHONE = 1;
  String contactDisplayName = null;
  String contactPhone = null;
  boolean createNewContact = true;
  
  @Override public void onCreate(final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_contact);
    init(0, getString(R.string.add_contact_screen), getString(R.string.add_contact_screen));
    final Bundle extras = getIntent().getExtras();
    if (extras != null)
      try {
        contactDisplayName = extras.getString(ContactsActivity.CONTACT_NAME_FLAG);
      } catch (final Exception e) {
        contactDisplayName = null;
        createNewContact = true;
      }
    if (contactDisplayName != null) {
      createNewContact = false;
      fillFieldWithContact();
    }
  }
  
  private void fillFieldWithContact() {
    final EditText _etPhone = (EditText) findViewById(R.id.phoneNumber);
    final EditText _etName = (EditText) findViewById(R.id.contact_name);
    _etName.setText(contactDisplayName);
    moveCursor(_etName, contactDisplayName);
    final ContactType ct = ContactManager.getContactFromName(contactDisplayName, this);
    contactPhone = ct.getPhone();
    _etPhone.setText(contactPhone);
    moveCursor(_etPhone, contactPhone);
  }
  
  @Override public int getViewId() {
    return R.id.AddContactActivity;
  }
  
  @Override public boolean onSingleTapUp(final MotionEvent e) {
    if (super.onSingleTapUp(e))
      return true;
    final View _editText = getButtonByMode();
    final EditText _etPhone = (EditText) findViewById(R.id.phoneNumber);
    final EditText _etName = (EditText) findViewById(R.id.contact_name);
    CharSequence _s;
    switch (_editText.getId()) {
      case R.id.contact_name:
        _s = getText(_etName, TEXT);
        moveCursor(_etName, _s);
        break;
      case R.id.phoneNumber:
        _s = getText(_etPhone, PHONE);
        moveCursor(_etPhone, _s);
        break;
      case R.id.confirmation_button:
        confirmationButtonPressed(_etPhone, _etName);
        break;
      default:
        break;
    }
    return false;
  }
  
  /**
   * User pressed the confirmation button so we confirm the data and respond
   * accordingly
   * 
   * @param _etPhone
   * @param _etName
   */
  private void confirmationButtonPressed(final EditText _etPhone, final EditText _etName) {
    final Intent returnIntent = new Intent();
    final CharSequence num = _etPhone.getText();
    final CharSequence name = getText(_etName, TEXT);
    if (num.length() <= 0 || name.equals(getString(R.string.enter_a_name))) {
      speakOutSync(getString(R.string.required_fields));
      return;
    }
    if (!createNewContact) {
      ContactManager.deleteContact(contactPhone, contactDisplayName, this);
      ContactManager.addContactToPhone(this, name.toString(), num.toString());
      speakOutSync(getString(R.string.edit_contact_success));
      setResult(RESULT_OK, returnIntent);
    } else if (ContactManager.getContactFromName(name.toString(), this) == null) {
      ContactManager.addContactToPhone(this, name.toString(), num.toString());
      speakOutSync(getString(R.string.add_contact_success));
      setResult(RESULT_OK, returnIntent);
    } else {
      speakOutSync(getString(R.string.contact_exist));
      setResult(RESULT_CANCELED, returnIntent);
    }
    finish();
  }
  
  /**
   * Retrieving the text shown in a given EditText view
   * 
   * @param editable
   *          The EditText view
   * @param type
   *          The type of view (can be either the phone number or the name
   *          content view)
   * @return the text shown in the given EditText view
   */
  private CharSequence getText(final EditText editable, final int type) {
    CharSequence _res = editable.getText();
    switch (type) {
      case TEXT:
        _res = _res.length() == 0 ? getString(R.string.enter_a_name) : _res;
        break;
      case PHONE:
        _res = _res.length() == 0 ? getString(R.string.enter_a_phone_number) : _res;
        break;
      default:
        break;
    }
    return _res;
  }
  
  /**
   * Moves cursor between EditText views
   * 
   * @param et
   *          The EditText view to which the cursor will be moved
   * @param cs
   *          The string to be spoken when moving to the view
   */
  static private void moveCursor(final EditText et, final CharSequence cs) {
    Selection.setSelection(et.getText(), et.getSelectionStart());
    et.requestFocus();
    speakOutAsync(cs.toString());
  }
}
