package com.example.anchitchawla.myapplication;

import com.example.anchitchawla.myapplication.data.Contract;
import com.example.anchitchawla.myapplication.data.Contract.Entry;
import com.example.anchitchawla.myapplication.data.DBHelper;

import android.app.LoaderManager;
import android.content.Loader;
import android.net.Uri;
import android.sax.EndTextElementListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.content.ContentValues;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.ListView;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static int InvLoader = 0;
    InventoryCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddInfo.class);
                startActivity(intent);
            }
        });
        ListView ListView = (ListView) findViewById(R.id.mylist);
        View emptyView = findViewById(R.id.empty_view);
        ListView.setEmptyView(emptyView);
        cursorAdapter = new InventoryCursorAdapter(this, null);
        ListView.setAdapter(cursorAdapter);
        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, AddInfo.class);
                Uri currentUri = ContentUris.withAppendedId(Entry.CONTENT_URI, id);
                intent.setData(currentUri);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(InvLoader, null, this);
    }

    void insert() {
        ContentValues values = new ContentValues();
        values.put(Entry.Coloumn_Name, "Sample");
        values.put(Entry.Column_Quantity, "10");
        values.put(Entry.Column_Price, "10");
        values.put(Entry.Column_SName, "hjlkh");
        values.put(Entry.Coloumn_SEmail, "anchit787@gmail.com");
        values.put(Entry.Coloumn_SPhone, "9876023619");
        getContentResolver().insert(Entry.CONTENT_URI, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.catalog, menu);
        return true;
    }

    private void deleteAllItems() {
        int rowsDeleted = getContentResolver().delete(Entry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dummy:
                insert();
                return true;
            case R.id.delete:
                deleteAllItems();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                Entry._Id,
                Entry.Coloumn_Name,
                Entry.Column_Quantity,
                Entry.Column_Price,
                Entry.Column_SName,
                Entry.Coloumn_SEmail,
                Entry.Coloumn_SPhone};
        return new CursorLoader(this,
                Contract.Entry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}
