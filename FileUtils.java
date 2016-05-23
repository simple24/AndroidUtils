
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
 * 操作SDCARD文件的工具类
 * @author simplexu
 *
 */
public class FileUtils {
	
	private static final String CACHE_DIR = Environment.getExternalStorageDirectory() + "/images";
	
	/**
	 * SDCARD是否挂载
	 * @return
	 */
	public static boolean isMounted(){
		String state = Environment.getExternalStorageState();
		return state.equals(Environment.MEDIA_MOUNTED);
	}
	
	/**
	 * 获取SDCARD的文件的绝对物理路径 
	 * @return
	 */
	public static String getSDCARDDIR() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}
	
	/**
	 * 保存图片  方法1
	 * @param url
	 * @param data
	 * @throws IOException 
	 */
	public static void saveImage(String url,byte[] data) throws IOException {  // http:....../xxx.jpg
		//1. 判断是否有sdcard
		if (!isMounted()) {
			return;
		}
		//2. 判断是否有缓存的文件夹
		File dir = new File(CACHE_DIR); // 缓存目录的file对象
		
		Log.i("MainActivity", dir.getAbsolutePath());
		
		if (!dir.exists()) {
			dir.mkdirs();
		}
		//3. 把图片数据保存到sdcard
		File picFile = new File(dir, getFilename(url));
		FileOutputStream outputStream = new FileOutputStream(picFile);
		
		outputStream.write(data);
		
		outputStream.close();
	}
	
	/**
	 * 保存图片 方法2 
	 * @param url
	 * @param bitmap
	 * @throws FileNotFoundException 
	 */
	public static void saveImage(String url,Bitmap bitmap) throws FileNotFoundException {  // http:....../xxx.jpg
		//1. 判断是否有sdcard
		if (!isMounted()) {
			return;
		}
		//2. 判断是否有缓存的文件夹
		File dir = new File(CACHE_DIR); // 缓存目录的file对象
		if (!dir.exists()) {
			dir.mkdirs();
		}
		//3. 把图片数据保存到sdcard
		File picFile = new File(dir, getFilename(url));
		FileOutputStream outputStream = new FileOutputStream(picFile);
		
		//把bitmap保存到sdcard
		bitmap.compress(CompressFormat.JPEG, 100, outputStream);
	}
	
	/**
	 * 获取图片的文件名
	 * @param url
	 * @return
	 */
	public static String getFilename(String url) {
		return url.substring(url.lastIndexOf("/") + 1);
	}
	
	/**
	 * 读图片
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
	 * 清空缓存文件夹
	 */
	public void clearCaches(){
		File cache_dir = new File(CACHE_DIR);
		
		File[] allFiles = cache_dir.listFiles();
		for (File file : allFiles) {
			file.delete();
		}
	}
	
	/**
	 * 生成缩略图
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
