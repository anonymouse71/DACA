package com.es.daca;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.ListView;

/**
 * Class ListActivity<br>
 * Activity that lists all borrowings and may delete or display the position of the borrowing
 *
 * @author  Carlos Martínez Wahnon
 */
public class ListActivity extends Activity {
	private CustomCursorAdapter customAdapter;
	private PersonDatabaseHelper databaseHelper;
	private ListView listView;
	boolean gmapvalue = false;
	boolean deletevalue = false;
	long id1 = 0;
	String person_name;         
	String person_object;
	double latitude;
	double longitude;
	private static final String TAG = ListActivity.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		databaseHelper = new PersonDatabaseHelper(this);
		listView = (ListView) findViewById(R.id.list_data);
		registerForContextMenu(listView);
		listView.setOnItemClickListener(new ListView.OnItemClickListener() {
			@Override            
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				person_name = databaseHelper.getName(id);         
				person_object = databaseHelper.getObject(id);
				latitude = databaseHelper.getLatitude(id);
				longitude = databaseHelper.getLongitude(id);
				id1=id;
				openContextMenu(view);
				Log.d(TAG, "clicked on item: " + position);
			}
		});
		new Handler().post(new Runnable() {
			@Override
			public void run() {				
				customAdapter = new CustomCursorAdapter(ListActivity.this, databaseHelper.getAllData());
				listView.setAdapter(customAdapter);
			}
		});
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.menu2context, menu);
		menu.setHeaderTitle("Selecciona una opción");
		menu.setHeaderIcon(R.drawable.ic_launcher2); 
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.menu_gmap:
			String label = person_object + " prestado a " + person_name + " aquí";
			String uriBegin = "geo:" + latitude + "," + longitude;
			String query = latitude + "," + longitude + "(" + label + ")";
			String encodedQuery = Uri.encode(query);
			String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
			Uri uri = Uri.parse(uriString);
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
			startActivity(intent);				
			finish();
			return true;
		case R.id.menu_delete:
			databaseHelper.deleteData(id1);
			Toast.makeText(ListActivity.this,"Se ha borrado correctamente!",Toast.LENGTH_SHORT ).show();
			finish();
			return true;
		}
		return super.onContextItemSelected(item);
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