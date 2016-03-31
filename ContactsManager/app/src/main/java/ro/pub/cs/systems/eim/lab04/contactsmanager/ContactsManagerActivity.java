package ro.pub.cs.systems.eim.lab04.contactsmanager;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsManagerActivity extends AppCompatActivity {

    final public static String PHONE_NUMBER_KEY = "ro.pub.cs.systems.eim.lab04.contactsmanager.PHONE_NUMBER_KEY";
    final public static int CONTACTS_MANAGER_REQUEST_CODE = 1;

    private EditText nameEditText = null;
    private EditText phoneEditText = null;
    private EditText emailEditText = null;
    private EditText addressEditText = null;
    private EditText jobTitleEditText = null;
    private EditText companyEditText = null;
    private EditText websiteEditText = null;
    private EditText imEditText = null;

    private LinearLayout additionalFieldsContainer = null;

    private Button showHideAdditionalFieldsButton = null,
                    saveButton = null,
                    cancelButton = null;

    private ShowHideAdditionalFieldsButtonOnClickListener showHideAdditionalFieldsButtonOnClickListener = new ShowHideAdditionalFieldsButtonOnClickListener();
    private class ShowHideAdditionalFieldsButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (showHideAdditionalFieldsButton.getText().toString().equals(getResources().getString(R.string.show_additional_fields))) {
                showHideAdditionalFieldsButton.setText(getResources().getString(R.string.hide_additional_fields));
                additionalFieldsContainer.setVisibility(View.VISIBLE);
            } else if (showHideAdditionalFieldsButton.getText().toString().equals(getResources().getString(R.string.hide_additional_fields))) {
                showHideAdditionalFieldsButton.setText(getResources().getString(R.string.show_additional_fields));
                additionalFieldsContainer.setVisibility(View.INVISIBLE);
            }
        }
    }

    private SaveButtonOnClickListener saveButtonOnClickListener = new SaveButtonOnClickListener();
    private class SaveButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String name = nameEditText.getText().toString();
            String phone = phoneEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String address = addressEditText.getText().toString();
            String jobTitle = jobTitleEditText.getText().toString();
            String company = companyEditText.getText().toString();
            String website = websiteEditText.getText().toString();
            String im = imEditText.getText().toString();

            Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
            intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
            if (name != null) {
                intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
            }
            if (phone != null) {
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
            }
            if (email != null) {
                intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
            }
            if (address != null) {
                intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
            }
            if (jobTitle != null) {
                intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, jobTitle);
            }
            if (company != null) {
                intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company);
            }
            ArrayList<ContentValues> contactData = new ArrayList<ContentValues>();
            if (website != null) {
                ContentValues websiteRow = new ContentValues();
                websiteRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
                websiteRow.put(ContactsContract.CommonDataKinds.Website.URL, website);
                contactData.add(websiteRow);
            }
            if (im != null) {
                ContentValues imRow = new ContentValues();
                imRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
                imRow.put(ContactsContract.CommonDataKinds.Im.DATA, im);
                contactData.add(imRow);
            }
            intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);
            startActivityForResult(intent, CONTACTS_MANAGER_REQUEST_CODE);
        }
    }

    private CancelButtonOnClickListener cancelButtonOnClickListener = new CancelButtonOnClickListener();
    private class CancelButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            setResult(Activity.RESULT_CANCELED, new Intent());
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_manager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        phoneEditText = (EditText) findViewById(R.id.phone_edit_text);
        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        addressEditText = (EditText) findViewById(R.id.address_edit_text);
        jobTitleEditText = (EditText) findViewById(R.id.job_title_edit_text);
        companyEditText = (EditText) findViewById(R.id.company_edit_text);
        websiteEditText = (EditText) findViewById(R.id.website_edit_text);
        imEditText = (EditText) findViewById(R.id.im_edit_text);

        showHideAdditionalFieldsButton = (Button) findViewById(R.id.show_hide_additional_fields);
        showHideAdditionalFieldsButton.setOnClickListener(showHideAdditionalFieldsButtonOnClickListener);
        saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(saveButtonOnClickListener);
        cancelButton = (Button) findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(cancelButtonOnClickListener);

        additionalFieldsContainer = (LinearLayout) findViewById(R.id.additional_fields_container);

        Intent intent = getIntent();
        if (intent != null) {
            String phoneNumber = intent.getStringExtra(PHONE_NUMBER_KEY);
            if (phoneNumber != null) {
                phoneEditText.setText(phoneNumber);
            } else {
                Toast.makeText(this, getResources().getString(R.string.phone_number_error), Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch(requestCode) {
            case CONTACTS_MANAGER_REQUEST_CODE:
                setResult(resultCode, new Intent());
                finish();
                break;
        }
    }
}
