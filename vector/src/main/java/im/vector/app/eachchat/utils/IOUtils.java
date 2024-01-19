package im.vector.app.eachchat.utils;

import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by chengww on 2019-10-21.
 */
public class IOUtils {
    public static <T> void checkNotNull(T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] toByteArray(Object input) {
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(input);
            oos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(oos);
            IOUtils.closeQuietly(baos);
        }
        return null;
    }

    public static byte[] toByteArray(InputStream input) {
        ByteArrayOutputStream output = null;
        try {
            output = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[4096];
            while ((len = input.read(buffer)) != -1) output.write(buffer, 0, len);
            return output.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(output);
        }

        return null;
    }


    public static Object toObject(byte[] input) {
        if (input == null) return null;
        ByteArrayInputStream bais = null;
        ObjectInputStream ois = null;
        try {
            bais = new ByteArrayInputStream(input);
            ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(ois);
            IOUtils.closeQuietly(bais);
        }
        return null;
    }

    /**
     * Create a folder, If the folder exists is not created.
     *
     * @param folderPath folder path.
     * @return True: success, or false: failure.
     */
    public static boolean createFolder(String folderPath) {
        if (!TextUtils.isEmpty(folderPath)) {
            File folder = new File(folderPath);
            return createFolder(folder);
        }
        return false;
    }

    /**
     * Create a folder, If the folder exists is not created.
     *
     * @param targetFolder folder path.
     * @return True: success, or false: failure.
     */
    public static boolean createFolder(File targetFolder) {
        if (targetFolder.exists()) {
            if (targetFolder.isDirectory()) return true;
            //noinspection ResultOfMethodCallIgnored
            targetFolder.delete();
        }
        return targetFolder.mkdirs();
    }

    /**
     * Delete file or folder.
     *
     * @param path path.
     * @return is succeed.
     * @see #delFileOrFolder(File)
     */
    public static boolean delFileOrFolder(String path) {
        if (TextUtils.isEmpty(path)) return false;
        return delFileOrFolder(new File(path));
    }

    /**
     * Delete file or folder.
     *
     * @param file file.
     * @return is succeed.
     * @see #delFileOrFolder(String)
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static boolean delFileOrFolder(File file) {
        if (file == null || !file.exists()) {
            // do nothing
        } else if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File sonFile : files) {
                    delFileOrFolder(sonFile);
                }
            }
            file.delete();
        }
        return true;
    }

    public static String getFileName(String objectKey) {
        String fileName = "";
        if (!TextUtils.isEmpty(objectKey)) {
            int index = objectKey.lastIndexOf("/");
            if (index > -1) {
                fileName = objectKey.substring(index + 1);
            } else {
                fileName = objectKey;
            }
        }
        return fileName;
    }

}

