
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

/**
 * ����SDCARD�ļ��Ĺ�����
 * @author simplexu
 *
 */
public class FileUtils {
	
	private static final String CACHE_DIR = Environment.getExternalStorageDirectory() + "/images";
	
	/**
	 * SDCARD�Ƿ����
	 * @return
	 */
	public static boolean isMounted(){
		String state = Environment.getExternalStorageState();
		return state.equals(Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * ��ȡSDCARD���ļ��ľ�������·�� 
	 * @return
	 */
	public static String getSDCARDDIR() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}
	
	/**
	 * ����ͼƬ  ����1
	 * @param url
	 * @param data
	 * @throws IOException 
	 */
	public static void saveImage(String url,byte[] data) throws IOException {  // http:....../xxx.jpg
		//1. �ж��Ƿ���sdcard
		if (!isMounted()) {
			return;
		}
		//2. �ж��Ƿ��л�����ļ���
		File dir = new File(CACHE_DIR); // ����Ŀ¼��file����
		
		Log.i("MainActivity", dir.getAbsolutePath());
		
		if (!dir.exists()) {
			dir.mkdirs();
		}
		//3. ��ͼƬ���ݱ��浽sdcard
		File picFile = new File(dir, getFilename(url));
		FileOutputStream outputStream = new FileOutputStream(picFile);
		
		outputStream.write(data);
		
		outputStream.close();
	}
	
	/**
	 * ����ͼƬ ����2 
	 * @param url
	 * @param bitmap
	 * @throws FileNotFoundException 
	 */
	public static void saveImage(String url,Bitmap bitmap) throws FileNotFoundException {  // http:....../xxx.jpg
		//1. �ж��Ƿ���sdcard
		if (!isMounted()) {
			return;
		}
		//2. �ж��Ƿ��л�����ļ���
		File dir = new File(CACHE_DIR); // ����Ŀ¼��file����
		if (!dir.exists()) {
			dir.mkdirs();
		}
		//3. ��ͼƬ���ݱ��浽sdcard
		File picFile = new File(dir, getFilename(url));
		FileOutputStream outputStream = new FileOutputStream(picFile);
		
		//��bitmap���浽sdcard
		bitmap.compress(CompressFormat.JPEG, 100, outputStream);
	}
	
	/**
	 * ��ȡͼƬ���ļ���
	 * @param url
	 * @return
	 */
	public static String getFilename(String url) {
		return url.substring(url.lastIndexOf("/") + 1);
	}
	
	/**
	 * ��ͼƬ
	 * @param url
	 * @return
	 */
	public static Bitmap readImage(String url) {
		if (!isMounted()) {
			return null;
		}
		File file = new File(CACHE_DIR, getFilename(url));
		if (file.exists()) {
			return BitmapFactory.decodeFile(file.getAbsolutePath());
		}
		return null;
	}
	
	/**
	 * ��ջ����ļ���
	 */
	public void clearCaches(){
		File cache_dir = new File(CACHE_DIR);
		
		File[] allFiles = cache_dir.listFiles();
		for (File file : allFiles) {
			file.delete();
		}
	}
	
	/**
	 * ��������ͼ
	 * @param filePath
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap createThumbnail(String filePath, int newWidth, int newHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		
		BitmapFactory.decodeFile(filePath, options);
		
		int originalWidth = options.outWidth;
		int originalHeight = options.outHeight;

		int ratioWidth = originalWidth / newWidth;
		int ratioHeight = originalHeight / newHeight;

		options.inSampleSize = ratioHeight > ratioWidth ? ratioHeight
				: ratioWidth;
		
		options.inJustDecodeBounds = false;
		
		return BitmapFactory.decodeFile(filePath, options);
	}
	
}
