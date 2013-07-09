package com.es.daca;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import java.util.Calendar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.TextView;

/**
 * Class EnterData2Activity <br>
 * EnterData2Activity manage the entry of all borrowing data
 *
 * @author  Carlos Martínez Wahnon
 */
public class EnterDataActivity extends Activity {

	private Uri uriContact;
	private static final String TAG = ListActivity.class.getSimpleName();
	private PersonDatabaseHelper databaseHelper;
	private static final int REQUEST_CODE_PICK_CONTACTS = 1;
	private String contactName = null;
	private String personObject = null;
	private Button button;
	static final int DATE_DIALOG_ID = 100;
	private int year0;
	private int month0;
	private int day0;
	private int year;
	private int month;
	private int day; 
	private double latitude = 0.0;
	private double longitude = 0.0;
	private TextView text_date;
	private TextView text_contact;
	private TextView text_location;
	boolean myalarm = false;
	boolean datevalue = false;
	boolean gpsvalue = false;
	EditText editTextPersonObject;
	CheckBox cb;
	Button btnShowLocation;      
	GPSLocator gps;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.enter_data);
		editTextPersonObject = (EditText) findViewById(R.id.person_object);
		setCurrentDate();
		addButtonListener();
		cb = (CheckBox) findViewById(R.id.checkBox1);
		checkmybox(cb);		
		btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
		btnShowLocation.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {     
				gps = new GPSLocator(EnterDataActivity.this);				
				text_location = (TextView) findViewById(R.id.text_location);

				if(gps.canGetLocation()){
					latitude = gps.getLatitude();
					longitude = gps.getLongitude();
					text_location.setText(new StringBuilder().append(" GPS-> Latitud: ").append(latitude).append(", Longitud: ").append(longitude).append(" "));
					gpsvalue = true;
				}else{
					gps.showSettingsAlert();
					Toast.makeText(getApplicationContext(), "No es posible obtener la locaclización GPS", Toast.LENGTH_LONG).show();
					gpsvalue = true;
				}
			}
		});
	}

	public void checkmybox (CheckBox cBox) {
		cBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() 
		{
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				myalarm = false;
				if (isChecked) {
					myalarm = true;					
				}
			}
		}); 
	}

	public void setCurrentDate() {
		text_date = (TextView) findViewById(R.id.text_date);
		final Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH);
		day = calendar.get(Calendar.DAY_OF_MONTH);
		year0 = calendar.get(Calendar.YEAR);
		month0 = calendar.get(Calendar.MONTH);
		day0 = calendar.get(Calendar.DAY_OF_MONTH);
	}

	public void addButtonListener() {

		button = (Button) findViewById(R.id.buttonDate1);
		button.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, datePickerListener, year, month,day);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			text_date.setText(new StringBuilder().append(" Fecha de Devolución: ").append(day).append("/").append(month + 1).append("/").append(year).append(" "));
			datevalue = true;
		}
	};


	public void onClickAdd (View btnAdd) {
		personObject = editTextPersonObject.getText().toString();		
		databaseHelper = new PersonDatabaseHelper(this);
		if ( contactName != null && personObject.isEmpty() == false && datevalue == true && gpsvalue == true) {
			if (myalarm == true) {
				insertCalendarAlarm(day,month,year);
				myalarm = false;
			}
			datevalue = false;
			month++;
			month0++;
			databaseHelper.insertData(contactName,personObject,year0,month0,day0,year,month,day,latitude,longitude);
			Toast.makeText(getApplicationContext(), "Se ha añadido correctamente!",Toast.LENGTH_SHORT ).show();
			finish();
		}else{
			Toast.makeText(getApplicationContext(), "Es necesario rellenar todos los datos!", Toast.LENGTH_SHORT).show();
		}
	}

	public void onClickSelectContact(View btnSelectContact) {
		startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);        
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
			Log.d(TAG, "Response: " + data.toString());
			uriContact = data.getData(); 
			retrieveContactName();
		}
	}

	private void retrieveContactName() {
		Cursor cursor = getContentResolver().query(uriContact, null, null, null, null); 
		if (cursor.moveToFirst()) {
			contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
		}
		cursor.close(); 
		Log.d(TAG, "Contact Name: " + contactName);    
		text_contact = (TextView) findViewById(R.id.text_Contact);
		text_contact.setText(new StringBuilder().append("Contacto: ").append(contactName));
	}

	private void insertCalendarAlarm(int day, int month, int year) { 
		Calendar beginTime = Calendar.getInstance();
		beginTime.set(year, month, day);
		Calendar endTime = Calendar.getInstance();
		endTime.set(year, month, day);
		Intent intent = new Intent(Intent.ACTION_INSERT)
		.setData(Events.CONTENT_URI)
		.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
		.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
		.putExtra(Events.TITLE, "Ha finalizado el préstamo --> " + personObject + ", prestado a --> " + contactName)
		.putExtra(Events.ALL_DAY, true)
		.putExtra(Events.ACCESS_LEVEL, Events.ACCESS_PRIVATE)
		.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_FREE)
		.putExtra(Events.DESCRIPTION, "DACA: Recordatorio de Prestamos");
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu); 
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Toast.makeText(this, "Carlos Martinez Wahnon - Junio 2013",
					Toast.LENGTH_LONG).show();
			break;
		}
		return true;
	}
}