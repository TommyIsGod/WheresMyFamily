package dk.projekt.bachelor.wheresmyfamily.Controller;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;

public class PictUtil {

    public static File getSavePath() {
        File path;
        if (hasSDCard()) { // SD card
            path = new File(getSDCardPath() + "/WMF/");
            path.mkdir();
        } else {
            path = Environment.getDataDirectory();
        }
        return path;
    }
    public static String getCacheFilename(String filename) {
        File f = getSavePath();
        return f.getAbsolutePath() + filename + ".png";
    }

    public static Bitmap loadFromFile(String filename) {
        try {
            File f = new File(filename);
            if (!f.exists()) { return null; }
            Bitmap tmp = BitmapFactory.decodeFile(filename);
            return tmp;
        } catch (Exception e) {
            return null;
        }
    }
    public static Bitmap loadFromCacheFile(String filename) {
        return loadFromFile(getCacheFilename(filename));
    }
    public static void saveToCacheFile(Bitmap bmp, String filename) {
        saveToFile(getCacheFilename(filename),bmp);
    }
    public static void saveToFile(String filename,Bitmap bmp) {
        try {
            FileOutputStream out = new FileOutputStream(filename);
            bmp.compress(CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch(Exception e) {}
    }

    public static boolean hasSDCard() { // SD????????
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }
    public static String getSDCardPath() {
        File path = Environment.getExternalStorageDirectory();
        return path.getAbsolutePath();
    }

}
