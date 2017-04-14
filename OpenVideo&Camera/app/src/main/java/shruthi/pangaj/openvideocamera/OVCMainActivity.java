package shruthi.pangaj.openvideocamera;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.security.acl.Permission;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class OVCMainActivity extends AppCompatActivity {
    Button btnCamera;
    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ovc_activity_main);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPermission();

                }
            }
        });
    }

    private void checkPermission() {
        Boolean camera = false, externalStorage = false;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            openCamera();
            return;
        }
        if (checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED) {
            camera = true;
        }
        if (checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            externalStorage = true;
        }
        if (camera && externalStorage) {
            openCamera();
        }
        requestPermissions(new String[]{
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.ACCESS_FINE_LOCATION},
                ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ASK_MULTIPLE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }
        }
    }

    private void openCamera() {
        Intent takePictureOrVideo = new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        takePictureOrVideo.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 120); //120 secs = 2 mins
        takePictureOrVideo.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1048576); //10 * 1024 * 1024 = 10MB
        startActivityForResult(takePictureOrVideo, 0);
    }
}