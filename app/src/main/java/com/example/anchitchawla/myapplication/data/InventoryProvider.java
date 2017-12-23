package com.example.anchitchawla.myapplication.data;
/**
 * Created by Anchit Chawla on 20-12-2017.
 */

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import static com.example.anchitchawla.myapplication.data.Contract.CONTENT_AUTHORITY;
import static com.example.anchitchawla.myapplication.data.Contract.PATH_Inventory;

public class InventoryProvider extends ContentProvider {
    public static final String LOG_TAG = InventoryProvider.class.getSimpleName();
    DBHelper helper;
    private static final int ITEMS = 100;
    private static final int ITEM_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_Inventory, ITEMS);
        sUriMatcher.addURI(CONTENT_AUTHORITY, PATH_Inventory + "/#", ITEM_ID);
    }

    @Override
    public boolean onCreate() {
        helper = new DBHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = helper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                cursor = database.query(Contract.Entry.Table_Name, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ITEM_ID:
                selection = Contract.Entry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(Contract.Entry.Table_Name,
                        projection
                        , selection
                        , selectionArgs
                        , null, null
                        , sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return Contract.Entry.CONTENT_LIST_TYPE;
            case ITEM_ID:
                return Contract.Entry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return insertPet(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertPet(Uri uri, ContentValues values) {
        String name = values.getAsString(Contract.Entry.Coloumn_Name);
        if (name == null) {
            throw new IllegalArgumentException("Enter valid name");
        }
        Integer quant = values.getAsInteger(Contract.Entry.Column_Quantity);
        if (quant == null && quant < 0) {
            throw new IllegalArgumentException("Enter valid quantity");
        }
        Integer price = values.getAsInteger(Contract.Entry.Column_Price);
        if (price == null && price < 0) {
            throw new IllegalArgumentException("Enter valid price");
        }
        String saname = values.getAsString(Contract.Entry.Column_SName);
        if (saname == null) {
            throw new IllegalArgumentException("Enter valid Seller's name");
        }
        String email = values.getAsString(Contract.Entry.Coloumn_SEmail);
        if (email == null) {
            throw new IllegalArgumentException("Enter valid email id");
        }
        String ph = values.getAsString(Contract.Entry.Coloumn_SPhone);
        if (ph == null) {
            throw new IllegalArgumentException("Enter valid Phone Number");
        }
        // TODO: Insert a new pet into the pets database table with the given ContentValues
        SQLiteDatabase database = helper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = database.insert(Contract.Entry.Table_Name, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = helper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(Contract.Entry.Table_Name, selection, selectionArgs);
                break;
            case ITEM_ID:
                // Delete a single row given by the ID in the URI
                selection = Contract.Entry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(Contract.Entry.Table_Name, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return updateItem(uri, values, selection, selectionArgs);
            case ITEM_ID:
                selection = Contract.Entry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = helper.getWritableDatabase();
        int rowsUpdated = database.update(Contract.Entry.Table_Name, values, selection, selectionArgs);
        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows updated
        return rowsUpdated;
    }
}

