package ca.sfu.cmpt276.be.parentapp.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ca.sfu.cmpt276.be.parentapp.R;

/**
 * ImageManager handles the saving and loading of images for the portraits of children.
 */
public class ImageManager {
    private static final String TAG = "ImageManager";
    public static final String PORTRAIT_FOLDER = "Portraits";

    public Bitmap getPortrait(Context context, String childName) {
        if (doesPortraitExist(context, childName)) {
            return loadPortraitBitmap(context, childName);
        } else {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.sample_avatar);
        }
    }

    public void deletePortrait(Context context, String imageName) {
        if (doesPortraitExist(context, imageName)) {
            File filepath = getPhotoFilePath(context);
            File deleteFile = new File(filepath, "/" + imageName + ".jpg");
            if (!deleteFile.delete()) {
                Log.e(TAG, "Unable to delete file");
            }
        }
    }

    public void savePortrait(Context context, Uri result, String imageName) throws IOException {
        try {
            Bitmap childImage = MediaStore.Images.Media.getBitmap(context.getContentResolver(), result);

            File file = new File(getPhotoFilePath(context), imageName + ".jpg");
            try {
                FileOutputStream fos = new FileOutputStream(file);
                childImage.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.flush();
                fos.close();
                Log.d(TAG, "Image saved to" + file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new FileNotFoundException("Unable to save file");
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Unable to generate bitmap");
        }
    }

    private Bitmap loadPortraitBitmap(Context context, String imageName) {
        File filepath = getPhotoFilePath(context);
        return BitmapFactory.decodeFile(filepath + "/" + imageName + ".jpg");
    }

    private File getPhotoFilePath(Context context) {
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), PORTRAIT_FOLDER);
            if (!file.exists()) {
                if (file.mkdirs()) {
                    Log.e(TAG, "Directory unable to be made");
                }
            }

        return file;
    }

    private boolean doesPortraitExist(Context context, String imageName) {
        File filepath = getPhotoFilePath(context);
        File checkFile = new File(filepath, "/" + imageName + ".jpg");
        return checkFile.exists();
    }
}
