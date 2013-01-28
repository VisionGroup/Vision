package com.yp2012g4.vision.test;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;
import com.yp2012g4.vision.R;
import com.yp2012g4.vision.contacts.ContactsActivity;
import com.yp2012g4.vision.customUI.TalkingButton;
import com.yp2012g4.vision.managers.ContactManager;
import com.yp2012g4.vision.managers.ContactType;
import com.yp2012g4.vision.sms.QuickSMSActivity;

public class ContactsActivityTest extends
		ActivityInstrumentationTestCase2<ContactsActivity> {

	private Solo solo;
	private Activity activity;

	public ContactsActivityTest() {
		super("com.yp2012g4.vision.contacts", ContactsActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		Intent i = new Intent();
		i.putExtra(ContactsActivity.LIST_TYPE, "test");
		setActivityIntent(i);
		activity = getActivity();
		solo = new Solo(getInstrumentation(), activity);

	}

	/**
	 * Test the Contacts activity functionality
	 */
	public void testContactActivity() {
		solo.assertCurrentActivity("wrong activity", ContactsActivity.class);
		// check intent
		final Bundle extras = solo.getCurrentActivity().getIntent().getExtras();
		String listType = "";
		if (extras != null)
			try {
				listType = extras.getString(ContactsActivity.LIST_TYPE);
			} catch (final Exception e) {
				listType = "";
			}
		assertEquals("test", listType);

		// check info
		TalkingButton name = (TalkingButton) solo.getView(R.id.contact_name);
		TalkingButton phone = (TalkingButton) solo.getView(R.id.contact_phone);

		ArrayList<ContactType> ac = ContactManager.getTestContacts();

		assertEquals(ac.get(0).getContactName(), name.getText());
		assertEquals(ac.get(0).getPhone(), phone.getText());

		solo.clickOnView(solo.getView(R.id.contact_next));
		assertEquals(ac.get(1).getContactName(), name.getText());
		assertEquals(ac.get(1).getPhone(), phone.getText());

		solo.clickOnView(solo.getView(R.id.contact_next));
		assertEquals(ac.get(1).getContactName(), name.getText());
		assertEquals(ac.get(1).getPhone(), phone.getText());

		solo.clickOnView(solo.getView(R.id.contact_prev));
		assertEquals(ac.get(0).getContactName(), name.getText());
		assertEquals(ac.get(0).getPhone(), phone.getText());

		solo.clickOnView(solo.getView(R.id.contact_prev));
		assertEquals(ac.get(0).getContactName(), name.getText());
		assertEquals(ac.get(0).getPhone(), phone.getText());

		solo.clickOnView(solo.getView(R.id.contact_next));
		solo.clickOnView(solo.getView(R.id.contacts_sms));

		// test passing correct number to Quick SMS activity
		solo.assertCurrentActivity("wrong activity", QuickSMSActivity.class);
		final Bundle extras2 = solo.getCurrentActivity().getIntent()
				.getExtras();
		String smsExtra = "";
		if (extras2 != null)
			try {
				smsExtra = extras2.getString(QuickSMSActivity.NUMBER_KEY);
			} catch (final Exception e) {
				smsExtra = "";
			}
		assertEquals(ac.get(1).getPhone(), smsExtra);
	}

	
	/**
	 * Test contact manager 
	 */
	public void testManager() {
		ContactManager cm = new ContactManager(activity.getApplicationContext());

		// should not contain empty contacts or phone numbers.
		ArrayList<ContactType> allContacts = cm.getAllContacts();
		for (ContactType contactType : allContacts) {
			assertTrue(contactType.getContactName() != "");
			assertEquals(contactType.getContactName(),
					cm.getNameFromPhone(contactType.getPhone()));
			assertTrue(contactType.getPhone() != "");
			assertEquals(contactType.getPhone(),
					cm.lookupPhoneNumbers(contactType.getLookUpKey()));

		}

		// should not contain empty contacts or phone numbers.
		ArrayList<ContactType> favoritContacts = cm.getFavoriteContacts();
		for (ContactType contactType : favoritContacts) {
			assertTrue(contactType.getContactName() != "");
			assertEquals(contactType.getContactName(),
					cm.getNameFromPhone(contactType.getPhone()));
			assertTrue(contactType.getPhone() != "");
			assertEquals(contactType.getPhone(),
					cm.lookupPhoneNumbers(contactType.getLookUpKey()));
		}

	}

}
