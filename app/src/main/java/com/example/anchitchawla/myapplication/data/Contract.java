package com.example.anchitchawla.myapplication.data;
/**
 * Created by Anchit Chawla on 19-12-2017.
 */

import android.content.ContentResolver;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Contract {
    private Contract() {
    }

    public static final String CONTENT_AUTHORITY = "com.example.anchitchawla.myapplication";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_Inventory = "inventory";

    public static final class Entry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_Inventory);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_Inventory;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_Inventory;
        public final static String Table_Name = "inventory";
        public final static String _Id = BaseColumns._ID;
        public final static String Coloumn_Name = "Name";
        public final static String Column_Quantity = "Quantity";
        public final static String Column_Price = "Price";
        public final static String Column_SName = "SName";
        public final static String Coloumn_SEmail = "Email";
        public final static String Coloumn_SPhone = "Phone";
    }
}
