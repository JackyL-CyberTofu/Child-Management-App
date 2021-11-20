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

import ca.sfu.cmpt276.be.parentapp.model.Child;

public class ImageManager {
    private static final String TAG = "ImageManager";
    public static final String PORTRAIT_FOLDER = "Portraits";

    private File getPhotoFilePath(Context context) {
        File filepath = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), PORTRAIT_FOLDER);
        if (!filepath.mkdirs()) {
            Log.e(TAG, "Photo directory could not be created");
        }
        return filepath;
    }

    public boolean doesPortraitExist(Context context, String imageName) {
        File filepath = getPhotoFilePath(context);
        File checkFile = new File(filepath, "/" + imageName + ".jpg");
        return checkFile.exists();
    }

    public Bitmap loadPortraitBitmap(Context context, String imageName) {
        File filepath = getPhotoFilePath(context);
        return BitmapFactory.decodeFile(filepath + "/" + imageName + ".jpg");
    }

    public void savePortraitBitmap(Context context, Uri result, String imageName) {
        try {
            Bitmap childImage = MediaStore.Images.Media.getBitmap(context.getContentResolver(), result);

            //TODO: replace this when UUID when that part is merged
            //TODO: fix for new child
            File file = new File(getPhotoFilePath(context), imageName + ".jpg");
            try {
                FileOutputStream fos = new FileOutputStream(file);
                childImage.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.flush();
                fos.close();
                Log.i(TAG, "Image saved to" + file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
