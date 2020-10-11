package com.burhanrashid52.photoeditor;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 1234;
    private static final int CAMERA_REQUEST = 53;
    private static final int PICK_REQUEST = 54;

    private final String SAMPLE_CROPPED_IMG_NAME = "SampleCropImg";

    private boolean mPermissions;
    private ImageView btnCapHome;
    private ImageView btnLibHome;
    private ImageView imageView;
    private Uri mUri;
    private ContentValues values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        btnCapHome = findViewById(R.id.btn_cap_home);
        btnLibHome = findViewById(R.id.btn_lib_home);
        imageView = findViewById(R.id.iv_home);

        if (mPermissions) {
            btnCapHome.setOnClickListener(this);
            btnLibHome.setOnClickListener(this);

        } else {
            verifyPermissions();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cap_home:
                takePictureByCamera();
                break;
            case R.id.btn_lib_home:
                takePictureByLibrary();
                break;
            default:
                break;
        }
    }

    private void takePictureByCamera() {
        if (checkCameraHardware(this)) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        } else {
            showSnackBar("You need a camera to use this application", Snackbar.LENGTH_INDEFINITE);
        }
    }

    private void takePictureByLibrary() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_REQUEST);
    }


    public void verifyPermissions() {
        Log.d(TAG, "verifyPermissions: asking user for permissions.");
        String[] permissions = {
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};
        if (ContextCompat.checkSelfPermission(this,
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                permissions[1]) == PackageManager.PERMISSION_GRANTED) {
            mPermissions = true;
            init();
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    REQUEST_CODE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (mPermissions) {
                init();
            } else {
                verifyPermissions();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
//            imageView.setImageBitmap(imageBitmap);
            Uri imageUri = getImageUri(getApplicationContext(), imageBitmap);
            if (imageUri != null) {
                startCrop(imageUri);
            }
        } else if (requestCode == PICK_REQUEST && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                startCrop(imageUri);
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            showSnackBar("Da vao crop", Snackbar.LENGTH_SHORT);
            Uri imageUriResultCrop = UCrop.getOutput(data);
            if (imageUriResultCrop != null) {
//                imageView.setImageURI(imageUriResultCrop);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUriResultCrop);
                    imageView.setImageBitmap(bitmap);
                    openEditActivityAndSendData(bitmap, new EditImageActivity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    //Get image Uri from bitmap
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 70, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void startCrop(Uri uri) {
        String destinationFileName = SAMPLE_CROPPED_IMG_NAME;
        destinationFileName += ".jpg";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));

//        uCrop.withAspectRatio(1,1);
//        uCrop.withAspectRatio(3,4);
        uCrop.useSourceImageAspectRatio();

        uCrop.withMaxResultSize(450, 450);
        uCrop.withOptions(getCropOptions());
        uCrop.start(MainActivity.this);
    }

    private UCrop.Options getCropOptions() {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(80);

//        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
//        options.setCompressionFormat(Bitmap.CompressFormat.PNG);

        //UI Setting
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(false);

        //Color
        options.setStatusBarColor(getResources().getColor(R.color.drarker_template));
        options.setToolbarColor(getResources().getColor(R.color.drark_template));
        options.setToolbarTitle("Crop & Rotate");

        return options;
    }


    void openEditActivityAndSendData(Bitmap bitmap, Activity activity) {
        //Convert to byte array
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Intent in1 = new Intent(this, activity.getClass());
        in1.putExtra("image", byteArray);
        startActivity(in1);
    }

    /**
     * Check if this device has a camera
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private void showSnackBar(final String text, final int length) {
        View view = this.findViewById(android.R.id.content).getRootView();
        Snackbar.make(view, text, length).show();
    }
}