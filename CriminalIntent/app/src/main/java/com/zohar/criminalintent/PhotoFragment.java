package com.zohar.criminalintent;

import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class PhotoFragment extends DialogFragment {

    private static final String ARGS_FILE_IMAGE = "args_file_photo";

    private ImageView mPhotoImageView;

    private File mPhotoFile;

    public static Fragment newInstance(File file){
        Bundle args = new Bundle();
        args.putSerializable(ARGS_FILE_IMAGE, file);
        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mPhotoFile = (File)getArguments().getSerializable(ARGS_FILE_IMAGE);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_image, null);

        mPhotoImageView = view.findViewById(R.id.photo_image_view);

        // 加载图片
        if (mPhotoFile == null){
            mPhotoImageView.setImageDrawable(null);
        }else{
            mPhotoImageView.setImageBitmap(PictureUtils.getBitmapScale(mPhotoFile.getPath(),getActivity()));
        }

        return new AlertDialog.Builder(getContext()).setView(view).create();

    }
}
