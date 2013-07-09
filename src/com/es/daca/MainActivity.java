package com.es.daca;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * Class MainActivity<br>
 * MainActivity is a principal Activity of this APP
 *
 * @author  Carlos Martínez Wahnon
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);        
		Button button1 = (Button) findViewById(R.id.main_activity_button1);        
		button1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {			
				Intent i = new Intent();				
				i.setClass(getApplicationContext(), ListActivity.class);
				startActivity(i);				
			}				
		});		
		Button button2 = (Button) findViewById(R.id.main_activity_button2);
		button2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {			
				Intent i = new Intent();
				i.setClass(getApplicationContext(), com.es.daca.EnterDataActivity.class);
				startActivity(i);				
			}				
		});				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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