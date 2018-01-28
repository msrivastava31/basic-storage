package edu.uw.medhas.basicStorage;

import android.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class MediaFragment extends Fragment implements View.OnClickListener {
    private Button mPictureButton;
    private ImageView mImageView;
    private Bitmap mBitmap;

    private final int IMAGE_PICKER_REQUEST_CODE = 40;
    private final String IMAGE_DIR = "images";
    private final String IMAGE_NAME = "picture.jpg";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.media_layout, container, false);

        mPictureButton = view.findViewById(R.id.picture_button);
        mImageView = view.findViewById(R.id.picture_view);

        mPictureButton.setOnClickListener(this);

        if(loadImageFromInternalStorage()) {
            setupImageView();
        }

        return view;
    }

    private File getFileForInternalStorage() {
        ContextWrapper contextWrapper = new ContextWrapper(getActivity().getApplication());
        File directory = contextWrapper.getDir(IMAGE_DIR, Context.MODE_PRIVATE);
        return new File(directory, IMAGE_NAME);
    }

    private boolean loadImageFromInternalStorage() {
        final File picture = getFileForInternalStorage();

        if (!picture.exists()) {
            return false;
        }

        try(final FileInputStream fis = new FileInputStream(picture)) {
            mBitmap = BitmapFactory.decodeStream(fis);
        } catch (IOException ioex) {
            ioex.printStackTrace();
            return false;
        }
        return true;
    }

    private void saveImageInternaly() {
        final File picture = getFileForInternalStorage();
        try (final FileOutputStream fos = new FileOutputStream(picture)) {
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }

    private void setupImageView() {
        mImageView.setImageBitmap(mBitmap);
    }

    private void loadImageFromExternalStorage(Uri imageUri) {
        try {
            mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
        } catch (IOException ioex) {
            ioex.printStackTrace();
        }
    }

    private void imagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pick an image"), IMAGE_PICKER_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (IMAGE_PICKER_REQUEST_CODE == requestCode
                && resultCode == RESULT_OK
                && data != null) {
            final Uri imageUri = data.getData();
            loadImageFromExternalStorage(imageUri);
            setupImageView();
            saveImageInternaly();
        }
    }

    @Override
    public void onClick(View view) {
        if (mPictureButton == view) {
            Log.d("MediaFragment", "Reached inside button click");
            imagePicker();
        }
    }
}
