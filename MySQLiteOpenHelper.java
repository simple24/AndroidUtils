
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
	
	private static final String DBNAME = "persondb.db";
	private static final int VERSION = 1;

	public MySQLiteOpenHelper(Context context) {
		//context������   name ���ݿ���   factory �α깤��(null) version���ݿ�汾
		super(context, DBNAME, null, VERSION);
	}

	@Override  // ����   һ��������ִֻ��һ��
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table person(" +
				   "pid integer primary key autoincrement not null," +
				   "pname varchar(30) not null," +
				   "age integer not null," +
				   "score integer not null )");
	}

	@Override  // �汾����
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			db.execSQL("drop table if exists person");
			onCreate(db);
		}

	}

}
