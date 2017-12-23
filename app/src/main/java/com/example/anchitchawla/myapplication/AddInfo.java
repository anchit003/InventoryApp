package com.example.anchitchawla.myapplication;

import android.content.Loader;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.example.anchitchawla.myapplication.data.Contract.Entry;
import com.example.anchitchawla.myapplication.data.DBHelper;

import android.content.ContentValues;
import android.widget.Toast;
import android.content.Intent;
import android.database.Cursor;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.MotionEvent;

public class AddInfo extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private EditText name;
    private EditText quantity;
    private EditText price;
    private EditText sname;
    private EditText semail;
    private EditText sno;
    private Button inc;
    private Button dec;
    private Button del;
    private Button contact;
    private Uri mCurrentItemUri;
    private static final int EXISTING_Item_LOADER = 0;

    private boolean mItemHasChanged = false;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mItemHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();

        setContentView(R.layout.activity_add_info);
        name = (EditText) findViewById(R.id.pname);
        quantity = (EditText) findViewById(R.id.quantity);
        price = (EditText) findViewById(R.id.price);
        sname = (EditText) findViewById(R.id.sname);
        semail = (EditText) findViewById(R.id.semail);
        sno = (EditText) findViewById(R.id.sno);
        inc = (Button) findViewById(R.id.inc);
        dec = (Button) findViewById(R.id.dec);
        del = (Button) findViewById(R.id.del);
        contact = (Button) findViewById(R.id.contact);
        if (mCurrentItemUri == null) {
            setTitle("Add an Item");
            invalidateOptionsMenu();
            del.setVisibility(View.INVISIBLE);
            contact.setVisibility(View.INVISIBLE);
            inc.setVisibility(View.INVISIBLE);
            dec.setVisibility(View.INVISIBLE);
        } else {
            setTitle("Edit Item");
            getLoaderManager().initLoader(EXISTING_Item_LOADER, null, this);
        }
        inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increase();
            }
        });
        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decrease();
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog();
            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order();
            }
        });
        name.setOnTouchListener(mTouchListener);
        quantity.setOnTouchListener(mTouchListener);
        price.setOnTouchListener(mTouchListener);
        sname.setOnTouchListener(mTouchListener);
        semail.setOnTouchListener(mTouchListener);
        sno.setOnTouchListener(mTouchListener);
        inc.setOnTouchListener(mTouchListener);
        dec.setOnTouchListener(mTouchListener);
    }

    private void decrease() {
        String previousValueString = quantity.getText().toString();
        int previousValue;
        if (previousValueString.isEmpty()) {
            return;
        } else if (previousValueString.equals("0")) {
            return;
        } else {
            previousValue = Integer.parseInt(previousValueString);
            quantity.setText(String.valueOf(previousValue - 1));
        }
    }

    private void increase() {
        String previousValueString = quantity.getText().toString();
        int previousValue;
        if (previousValueString.isEmpty()) {
            previousValue = 0;
        } else {
            previousValue = Integer.parseInt(previousValueString);
        }
        quantity.setText(String.valueOf(previousValue + 1));
    }

    void save() {
        String nname = name.getText().toString().trim();
        String stquant = quantity.getText().toString().trim();
        String spric = price.getText().toString().trim();
        String snamee = sname.getText().toString().trim();
        String semaile = semail.getText().toString().trim();
        String snoo = sno.getText().toString().trim();
        if (mCurrentItemUri == null &&
                TextUtils.isEmpty(nname) || TextUtils.isEmpty(stquant) ||
                TextUtils.isEmpty(spric) || TextUtils.isEmpty(snamee) ||
                TextUtils.isEmpty(semaile) || TextUtils.isEmpty(snoo)) {
            Toast.makeText(this, "Please fill all the entries.", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(Entry.Coloumn_Name, nname);
        int quant = 0;
        if (!TextUtils.isEmpty(stquant)) {
            quant = Integer.parseInt(stquant);
        }
        values.put(Entry.Column_Quantity, quant);
        int pric = 0;
        if (!TextUtils.isEmpty(spric)) {
            pric = Integer.parseInt(spric);
        }
        values.put(Entry.Column_Price, pric);
        values.put(Entry.Column_SName, snamee);
        values.put(Entry.Coloumn_SEmail, semaile);
        values.put(Entry.Coloumn_SPhone, snoo);
        if (mCurrentItemUri == null) {
            Uri newUri = getContentResolver().insert(Entry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentItemUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_Item_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_update_Item_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addinfo, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentItemUri == null) {
            MenuItem menuItem = menu.findItem(R.id.deletee);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!mItemHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deletePet();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deletePet() {
        if (mCurrentItemUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentItemUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_item_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_item_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                save();
                return true;
            case R.id.order:
                order();
                return true;
            case R.id.deletee:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mItemHasChanged) {
                    NavUtils.navigateUpFromSameTask(AddInfo.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(AddInfo.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
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
                mCurrentItemUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int colname = cursor.getColumnIndex(Entry.Coloumn_Name);
            int colquan = cursor.getColumnIndex(Entry.Column_Quantity);
            int colprice = cursor.getColumnIndex(Entry.Column_Price);
            int colsname = cursor.getColumnIndex(Entry.Column_SName);
            int colsemail = cursor.getColumnIndex(Entry.Coloumn_SEmail);
            int colsphone = cursor.getColumnIndex(Entry.Coloumn_SPhone);
            String nname = cursor.getString(colname);
            int quant = cursor.getInt(colquan);
            int pric = cursor.getInt(colprice);
            String ssname = cursor.getString(colsname);
            String ssemail = cursor.getString(colsemail);
            int phone = cursor.getInt(colsphone);
            name.setText(nname);
            quantity.setText(Integer.toString(quant));
            price.setText(Integer.toString(pric));
            sname.setText(ssname);
            semail.setText(ssemail);
            sno.setText(Integer.toString(phone));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
       /* name.setText("");
        quantity.setText(0);
        price.setSelection(0);
        sname.setText("");
        semail.setText("");
        sno.setText("");*/
    }

    public void order() {
        String nname = name.getText().toString().trim();
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Ordering more of " + nname);
        intent.putExtra(Intent.EXTRA_TEXT, "Hello");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
