
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
	
	private static final String DBNAME = "persondb.db";
	private static final int VERSION = 1;

	public MySQLiteOpenHelper(Context context) {
		//context上下文   name 数据库名   factory 游标工厂(null) version数据库版本
		super(context, DBNAME, null, VERSION);
	}

	@Override  // 建表   一般来讲，只执行一次
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table person(" +
				   "pid integer primary key autoincrement not null," +
				   "pname varchar(30) not null," +
				   "age integer not null," +
				   "score integer not null )");
	}

	@Override  // 版本升级
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			db.execSQL("drop table if exists person");
			onCreate(db);
		}

	}

}
