package br.bonilha.buscaescola;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Debug;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ImageProvider {
    private final String TAG = this.getClass().getCanonicalName();
    private static final ImageProvider ourInstance = new ImageProvider();
    public static final long SAFETY_MEMORY_BUFFER = 10;//MB

    public static ImageProvider getInstance() {
        return ourInstance;
    }

    private ImageProvider() {
    }

    public File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }

    public Uri getUriFromImage(Context inContext, Bitmap inImage, int quality) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, quality, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        try {
            bytes.close();
            return Uri.parse(path);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            inImage.recycle();
        }
    }


    /**
     * Get the path to the full image for a given thumbnail.
     */
    private Uri uriToFullImage(Cursor thumbnailsCursor, Context context) {
        String imageId = thumbnailsCursor.getString(thumbnailsCursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID));

        // Request image related to this thumbnail
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor imagesCursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, filePathColumn, MediaStore.Images.Media._ID + "=?", new String[]{imageId}, null);

        if (imagesCursor != null && imagesCursor.moveToFirst()) {
            int columnIndex = imagesCursor.getColumnIndex(filePathColumn[0]);
            String filePath = imagesCursor.getString(columnIndex);
            imagesCursor.close();
            return Uri.parse(filePath);
        } else {
            imagesCursor.close();
            return Uri.parse("");
        }
    }

    public Bitmap resizeRotatePicture(String path) {
        // Get the dimensions of the View
        int targetW = 400;
        int targetH = 400;
        int rotation = detectRotation(path);

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        if (photoH >= targetH || photoW >= targetW) {
            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
            bmOptions.inSampleSize = scaleFactor;
        }

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 45, out);

            if (canBitmapFitInMemory(bmOptions)) {
                return rotatedBitmap;
            } else {
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "[EXCEPTION]", e);
            return null;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "[EXCEPTION]", e);
            }
        }
    }

    public Integer detectRotation(String filePath) {
        try {
            ExifInterface ei = new ExifInterface(filePath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    return 0;
            }
        } catch (IOException e) {
            Log.e(TAG, "[EXCEPTION]", e);
            return 0;
        }
    }

    public Uri getImageUri(Context inContext, String absolutePath) {
        Bitmap inImage = ImageProvider.getInstance().resizeRotatePicture(absolutePath);

        if (inImage != null) {
            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
            inImage.recycle();
            return Uri.parse(path);
        }

        return null;
    }

    public BitmapFactory.Options getBitmapOptionsWithoutDecoding(String url) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, opts);
        return opts;
    }

    //ref:http://stackoverflow.com/questions/6073744/android-how-to-check-how-much-memory-is-remaining
    public double availableMemoryMB() {
        double max = Runtime.getRuntime().maxMemory() / 1024;
        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
        Debug.getMemoryInfo(memoryInfo);
        return (max - memoryInfo.getTotalPss()) / 1024;
    }

    public boolean canBitmapFitInMemory(BitmapFactory.Options options) {
        long size = options.outHeight * options.outWidth * 32 / (1024 * 1024 * 8);
        Log.d(TAG, "image MB:" + size);
        return size <= availableMemoryMB() - SAFETY_MEMORY_BUFFER;
    }
}
