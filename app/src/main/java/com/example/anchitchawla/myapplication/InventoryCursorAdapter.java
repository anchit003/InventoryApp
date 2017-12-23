package com.example.anchitchawla.myapplication;

/**
 * Created by Anchit Chawla on 21-12-2017.
 */

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anchitchawla.myapplication.data.Contract.Entry;

import org.w3c.dom.Text;

public class InventoryCursorAdapter extends CursorAdapter {
    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView view1 = (TextView) view.findViewById(R.id.iname);
        TextView view2 = (TextView) view.findViewById(R.id.iprice);
        TextView view3 = (TextView) view.findViewById(R.id.iqn);
        int idd = cursor.getColumnIndex(Entry._ID);
        final long id = cursor.getLong(idd);
        int nameColumnIndex = cursor.getColumnIndex(Entry.Coloumn_Name);
        int priceColumnIndex = cursor.getColumnIndex(Entry.Column_Price);
        int quanColumnIndex = cursor.getColumnIndex(Entry.Column_Quantity);

        String name = cursor.getString(nameColumnIndex);
        final int price = cursor.getInt(priceColumnIndex);
        final int quantity = cursor.getInt(quanColumnIndex);

        ImageView sellButton = (ImageView) view.findViewById(R.id.sale);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity > 0) {
                    ContentValues values = new ContentValues();
                    int newAvailableQuantity = quantity - 1;
                    Log.v("new quantity", "after update" + newAvailableQuantity);
                    values.put(Entry.Column_Quantity, newAvailableQuantity);
                    Uri uri = ContentUris.withAppendedId(Entry.CONTENT_URI, id);
                    view.getContext().getContentResolver().update(uri, values, null, null);
                } else {
                    Toast.makeText(view.getContext(), "No more Items", Toast.LENGTH_SHORT).show();
                }
            }
        });
        String pp = "Price: " + price;
        String qq = "Quantity: " + quantity;
        view1.setText(name);
        view2.setText(pp);
        view3.setText(qq);
    }
}
