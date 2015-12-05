package qa.edu.qu.cse.cmps497.datamanagement;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import qa.edu.qu.cse.cmps497.datamanagement.SQLHelper.DatabaseOpenHelper;

public class SQLDatabaseActivity extends AppCompatActivity {

    private DatabaseOpenHelper mDbHelper;
    private SimpleCursorAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sql_database);

        ListView listView = (ListView) findViewById(R.id.list_view);

        // Create a new DatabaseHelper
        mDbHelper = new DatabaseOpenHelper(this);

        // start with an empty database
        clearAll();

        // Insert records
        insertScientists();

        // Create a cursor
        Cursor c = readScientists();
        mAdapter = new SimpleCursorAdapter(this, R.layout.list_layout, c,
                DatabaseOpenHelper.columns, new int[] { R.id._id, R.id.name },
                0);

        listView.setAdapter(mAdapter);

        Button fixButton = (Button) findViewById(R.id.fix_button);
        fixButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // execute database operations
                fix();

                // Redisplay data
                Cursor c = readScientists();
                mAdapter.changeCursor(c);
                mAdapter.notifyDataSetChanged();
            }
        });

    }

    // Insert several scientists records
    private void insertScientists() {

        ContentValues values = new ContentValues();

        values.put(DatabaseOpenHelper.SCIENTIST_NAME, "Omar Al-Khayyam");
        mDbHelper.getWritableDatabase().insert(DatabaseOpenHelper.TABLE_NAME, null, values);

        values.put(DatabaseOpenHelper.SCIENTIST_NAME, "Al-Farabi");
        mDbHelper.getWritableDatabase().insert(DatabaseOpenHelper.TABLE_NAME, null, values);

        values.put(DatabaseOpenHelper.SCIENTIST_NAME, "Ibn Rushd");
        mDbHelper.getWritableDatabase().insert(DatabaseOpenHelper.TABLE_NAME, null, values);

        values.put(DatabaseOpenHelper.SCIENTIST_NAME, "Shakira");
        mDbHelper.getWritableDatabase().insert(DatabaseOpenHelper.TABLE_NAME, null, values);

        values.put(DatabaseOpenHelper.SCIENTIST_NAME, "Inb Khaldun");
        mDbHelper.getWritableDatabase().insert(DatabaseOpenHelper.TABLE_NAME, null, values);

        values.put(DatabaseOpenHelper.SCIENTIST_NAME, "Muhammad Al-Khwarizmi");
        mDbHelper.getWritableDatabase().insert(DatabaseOpenHelper.TABLE_NAME, null, values);

    }

    // Returns all scientist records in the database
    private Cursor readScientists() {
        return mDbHelper.getWritableDatabase().query(DatabaseOpenHelper.TABLE_NAME,
                DatabaseOpenHelper.columns, null, new String[] {}, null, null,
                null);
    }

    // Modify the contents of the database
    private void fix() {

        // Bye Shakira :-(
        mDbHelper.getWritableDatabase().delete(DatabaseOpenHelper.TABLE_NAME,
                DatabaseOpenHelper.SCIENTIST_NAME + "=?",
                new String[] { "Shakira" });

        // fix Ibn Khaldun
        ContentValues values = new ContentValues();
        values.put(DatabaseOpenHelper.SCIENTIST_NAME, "Ibn Khaldun");

        mDbHelper.getWritableDatabase().update(DatabaseOpenHelper.TABLE_NAME, values,
                DatabaseOpenHelper.SCIENTIST_NAME + "=?",
                new String[] { "Inb Khaldun" });

    }

    // Delete all records
    private void clearAll() {

        mDbHelper.getWritableDatabase().delete(DatabaseOpenHelper.TABLE_NAME, null, null);

    }

    // Close database
    @Override
    protected void onDestroy() {

        mDbHelper.getWritableDatabase().close();
        mDbHelper.deleteDatabase();

        super.onDestroy();

    }
}
