package fransis.org.ar.radiustool.store;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import fransis.org.ar.radiustool.model.TestCase;

/**
 * Created by francisco on 9/26/16.
 */
public class TestCaseDbHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE testcase (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name" + TEXT_TYPE + COMMA_SEP +
                    "address" + TEXT_TYPE + COMMA_SEP +
                    "auth_port INTEGER " + COMMA_SEP +
                    "secret" + TEXT_TYPE + COMMA_SEP +
                    "user_name" + TEXT_TYPE + COMMA_SEP +
                    "user_password" + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS testcase";


    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "TestCase.db";
    private Context mContext = null;


    public TestCaseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public TestCase insert(String name, String address, int authPort, String secret, String userName, String userPassword){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("address", address);
        values.put("auth_port", authPort);
        values.put("secret", secret);
        values.put("user_name", userName);
        values.put("user_password", userPassword);
        db.beginTransaction();
        long newRowId = db.insert("testcase", null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();

        return new TestCase(newRowId, name, address, authPort, secret, userName, userPassword);
    }

    public List<TestCase> getAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.beginTransaction();
        String[] projection = {"id", "name", "address", "auth_port", "secret", "user_name", "user_password"};
        Cursor c = db.query(
                "testcase",
                projection,
                null,
                null,
                null,
                null,
                null
        );
        List<TestCase> list = new ArrayList<>();

        for(c.moveToFirst(); c.moveToNext();){
            TestCase tc = new TestCase(
                    c.getLong(0),
                    c.getString(1),
                    c.getString(2),
                    c.getInt(3),
                    c.getString(4),
                    c.getString(5),
                    c.getString(6)
            );
            list.add(tc);
        }
        c.close();
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return list;
    }

    public int remove(TestCase tc) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        int testcase = db.delete("testcase", "id = " + tc.getId(), null);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return testcase;
    }

    public void removeAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        db.delete("testcase", "1 = 1", null);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }

}
