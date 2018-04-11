package com.reemind;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.reemind.models.TempData;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.reemind.models.TempData.SHARED_PREF_NAME;

public class EditProfile extends AppCompatActivity {
    @BindView(R.id.userphoto) ImageView userPhoto;
    @BindView(R.id.useremail)EditText userEmail;
    @BindView(R.id.username)EditText userName;
    @BindView(R.id.toolbar)Toolbar toolbar;
    private static final int IMAGE_PICK = 1;
    private static final int IMAGE_CAPTURE = 2;
    SharedPreferences shared;
    SharedPreferences.Editor edit;
    ArrayList<String> permissions=new ArrayList<>();
    private  int CAMERA_PERMISSION_CODE = 0;
    private  int STORAGE_PERMISSION_CODE = 0;

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        ButterKnife.bind(this);

        //check camera and external storage permissions
        if(!isCameraPermissionGranted()) requestAccessCameraPermissions();
        if(!isStoragePermissionGranted()) requestReadStoragePermissions();


        //setSupportActionBar(toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_cancel_white);
        if (getActionBar() != null) getActionBar().setDisplayHomeAsUpEnabled(true);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("User Profile");

        shared = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        edit = shared.edit();

        prepareForm();

    }

    void getFromGallery(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Photo"), IMAGE_PICK);
    }

    void getFromCamera(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, IMAGE_CAPTURE);
    }

    void restoreDefault(View view){
        GoogleSignInAccount acct  = GoogleSignIn.getLastSignedInAccount(this);
       if (acct != null) {
           edit.putString("user-name-"+TempData.currentEmailAccount, acct.getDisplayName());
           edit.putString("user-photo-"+TempData.currentEmailAccount, TempData.PHOTOTYPE);
           edit.commit();
            Picasso.with(this).load(acct.getPhotoUrl()).placeholder(R.drawable.ic_account_face)// Place holder image from drawable folder
             /*.error(R.drawable.ic_error_load)*/
                    .resize(110, 110).centerCrop()
                    //.networkPolicy(NetworkPolicy.OFFLINE)
                    .into(userPhoto);
           userName.setText(acct.getDisplayName());
           showToast("Restored!");
        }else{
           showToast("Error, try again later...");
       }

    }



  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_menu, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) { 
                case IMAGE_PICK: this.imageFromGallery(resultCode, data); 
                    break; 
                case IMAGE_CAPTURE: this.imageFromCamera(resultCode, data); 
                    break; 
                default: 
                    break; } 

        }
    }
    private void imageFromCamera(int resultCode, Intent data) {
        this.userPhoto.setImageBitmap((Bitmap) data.getExtras().get("data")); }

    private void imageFromGallery(int resultCode, Intent data) {
        Uri selectedImage = data.getData();
        String [] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        this.userPhoto.setImageBitmap(BitmapFactory.decodeFile(filePath));
    }

    private void prepareForm() {

        EditText etUserName=(EditText)findViewById(R.id.username);
        EditText etUserEmail=(EditText)findViewById(R.id.useremail);
        ImageView ivUserPhoto=(ImageView) findViewById(R.id.userphoto);
        GoogleSignInAccount acct  = GoogleSignIn.getLastSignedInAccount(this);

        etUserName.setText(shared.getString("user-name-"+ TempData.currentEmailAccount, null));
        etUserEmail.setText(TempData.currentEmailAccount);
        String img_str= shared.getString("user-photo-"+ TempData.currentEmailAccount, null);
        if (!img_str.equals("") && !img_str.equals(TempData.PHOTOTYPE)){
            //decode string to image
            String base = img_str;
            byte[] imageAsBytes = Base64.decode(base.getBytes(), Base64.DEFAULT);
            ivUserPhoto.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length) );
            //ivUserSavedPhoto.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length) );
        }else if (acct != null) {
                    Picasso.with(this).load(acct.getPhotoUrl()).placeholder(R.drawable.ic_account_face)// Place holder image from drawable folder
             /*.error(R.drawable.ic_error_load)*/
                            .resize(110, 110).centerCrop()
                            //.networkPolicy(NetworkPolicy.OFFLINE)
                            .into(ivUserPhoto);
        }
    }



    public void setProfilePhoto(View view){
        ImageView ivphoto = (ImageView)findViewById(R.id.userphoto);

        //code image to string
        ivphoto.buildDrawingCache();
        Bitmap bitmap = ivphoto.getDrawingCache();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image=stream.toByteArray();
        //System.out.println("byte array:"+image);
        //final String img_str = "data:image/png;base64,"+ Base64.encodeToString(image, 0);
        //System.out.println("string:"+img_str);
        String img_str = Base64.encodeToString(image, 0);

        //decode string to image
        String base=img_str;

        //save in sharedpreferences
        edit.putString("user-name-"+TempData.currentEmailAccount, userName.getText().toString());
        edit.putString("user-photo-"+TempData.currentEmailAccount, img_str);
        edit.commit();

        showToast("Saved successfully!");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }


    /**
     * Check if we have CAMERA AND STORAGE permissions
     */
    public boolean isCameraPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean isStoragePermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Request runtime CAMERA AND STORAGE permissions
     */
    private void requestAccessCameraPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
    }

    private void requestReadStoragePermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    public void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
