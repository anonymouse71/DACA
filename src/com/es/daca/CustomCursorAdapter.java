package com.es.daca;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Class CustomCursorAdapter<br>
 * CustomCursorAdapter manages the cursor on the listview
 *
 * @author  Carlos Martínez Wahnon
 */
public class CustomCursorAdapter extends CursorAdapter {

	@SuppressWarnings("deprecation")
	public CustomCursorAdapter(Context context, Cursor c) {
		super(context, c);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View retView = inflater.inflate(R.layout.single_row_item, parent, false); 
		return retView;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView textViewPersonName = (TextView) view.findViewById(R.id.tv_person_name);
		textViewPersonName.setText(" Contacto: " + cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
		TextView textViewPersonObject = (TextView) view.findViewById(R.id.tv_person_object);
		textViewPersonObject.setText(" Objeto: " + cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))));        
		TextView textViewDates = (TextView) view.findViewById(R.id.tv_dates);  
		textViewDates.setText(" Del " + 
				cursor.getString(cursor.getColumnIndex(cursor.getColumnName(5))) + "/" + 
				cursor.getString(cursor.getColumnIndex(cursor.getColumnName(4))) + "/" +
				cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3))) + " hasta el " +
				cursor.getString(cursor.getColumnIndex(cursor.getColumnName(8))) + "/" +
				cursor.getString(cursor.getColumnIndex(cursor.getColumnName(7))) + "/" +
				cursor.getString(cursor.getColumnIndex(cursor.getColumnName(6)))  
				);
	}
}