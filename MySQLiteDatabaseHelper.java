
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
	 * ��ѯ����1
	 * @param sql
	 * @param selectionArgs  sql����еĲ���
	 * @return  cursor�α�
	 */
	public Cursor selectCursor(String sql,String[] selectionArgs){
		return db.rawQuery(sql, selectionArgs);
	}
	
	/**
	 * ��ѯ����2
	 * @param sql
	 * @param selectionArgs
	 * @return  ���ݼ���
	 */
	public List<Map<String, Object>> selectList(String sql,String[] selectionArgs) {
		Cursor cursor = selectCursor(sql, selectionArgs);
		return cursorToList(cursor);
	}
	// ���α��װ��List
	private List<Map<String, Object>> cursorToList(Cursor cursor) {
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		while(cursor.moveToNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			// ������ǰ�У���¼����ÿһ��      cursor.getColumnCount(): ���ص�ǰ�е�����
			for (int i = 0; i < cursor.getColumnCount(); i++) {
				// key: ����   cursor.getColumnName(i): ����������ȡ����
				map.put(cursor.getColumnName(i), cursor.getString(i));
			}
			list.add(map);
		}
		return list;
	}
	
	/**
	 * ��ɾ�ĵķ���
	 * @param sql  ���磺"insert into student(sname,gender,score) values(?,?,?)"
	 * @param bindArgs  ���磺new Object[]{"����","female",88}
	 * @return  �Ƿ�ɹ�����
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
	 * �ر�SqliteDataBase����
	 */
	public void destroy() {
		if (db != null) {
			db.close();
		}
	}
	
}
