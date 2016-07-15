
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

public class MySQLiteDatabaseHelper {

	private final static String DBPATH = Environment
			.getExternalStorageDirectory()
			+ File.separator
			+ "studentdb.db";
	
	private SQLiteDatabase db = null;
	
	public MySQLiteDatabaseHelper() {
		db = SQLiteDatabase.openDatabase(DBPATH, null, SQLiteDatabase.OPEN_READWRITE);
	}
	
	/**
	 * 查询方法1
	 * @param sql
	 * @param selectionArgs  sql语句中的参数
	 * @return  cursor游标
	 */
	public Cursor selectCursor(String sql,String[] selectionArgs){
		return db.rawQuery(sql, selectionArgs);
	}
	
	/**
	 * 查询方法2
	 * @param sql
	 * @param selectionArgs
	 * @return  数据集合
	 */
	public List<Map<String, Object>> selectList(String sql,String[] selectionArgs) {
		Cursor cursor = selectCursor(sql, selectionArgs);
		return cursorToList(cursor);
	}
	// 把游标封装成List
	private List<Map<String, Object>> cursorToList(Cursor cursor) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		while(cursor.moveToNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 遍历当前行（记录）的每一列      cursor.getColumnCount(): 返回当前行的列数
			for (int i = 0; i < cursor.getColumnCount(); i++) {
				// key: 列名   cursor.getColumnName(i): 根据索引获取列名
				map.put(cursor.getColumnName(i), cursor.getString(i));
			}
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 增删改的方法
	 * @param sql  例如："insert into student(sname,gender,score) values(?,?,?)"
	 * @param bindArgs  例如：new Object[]{"张三","female",88}
	 * @return  是否成功操作
	 */
	public boolean execData(String sql,Object[] bindArgs) {
		try {
			if (bindArgs == null) {
				db.execSQL(sql);
			} else {
				db.execSQL(sql, bindArgs);
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 关闭SqliteDataBase对象
	 */
	public void destroy() {
		if (db != null) {
			db.close();
		}
	}
	
}
