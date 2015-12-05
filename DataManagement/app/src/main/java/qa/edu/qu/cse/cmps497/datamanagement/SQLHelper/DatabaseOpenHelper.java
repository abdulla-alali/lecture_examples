package qa.edu.qu.cse.cmps497.datamanagement.SQLHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    public final static String TABLE_NAME = "scientists";
    public final static String SCIENTIST_NAME = "name";
    final static String _ID = "_id";
    public final static String[] columns = { _ID, SCIENTIST_NAME };

    final private static String CREATE_CMD =

            "CREATE TABLE scientists (" + _ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + SCIENTIST_NAME + " TEXT NOT NULL)";

    final private static String NAME = "scientists_db";
    final private static Integer VERSION = 1;
    final private Context mContext;

    public DatabaseOpenHelper(Context context) {
        super(context, NAME, null, VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CMD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // N/A
    }

    public void deleteDatabase() {
        mContext.deleteDatabase(NAME);
    }
}