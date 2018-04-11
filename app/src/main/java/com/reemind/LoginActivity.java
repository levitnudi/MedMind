package com.reemind;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.reemind.models.TempData;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.reemind.models.TempData.SHARED_PREF_NAME;

public class LoginActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 0;
    SharedPreferences shared;
    SharedPreferences.Editor edit;
    @BindView(R.id.mGoogleLogin)
    SignInButton mGoogleLogin;
    public static GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        shared = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        edit = shared.edit();

        ButterKnife.bind(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            GoogleSignInAccount acct  = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                startActivity(intent);
                finish();
           }



        mGoogleLogin.setOnClickListener(v -> googleSignIn());
    }


    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //callbackManager.onActivityResult(requestCode, resultCode, data);
        //showToast("Status code "+resultCode);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        //Timber.d("handleSignInResult:" + result.isSuccess() + " " + result.getStatus());
        //showToast(result.getStatus()+"");
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.

            GoogleSignInAccount acct = result.getSignInAccount();
            assert acct != null;

            String name = acct.getDisplayName();
            String email = acct.getEmail();
            String imageUrl = String.valueOf(acct.getPhotoUrl());


            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("user-name", name);
            intent.putExtra("user-email", email);
            intent.putExtra("user-photo", imageUrl);

            edit.putString("user-name-"+email, name);
            edit.putString("user-photo-"+email, TempData.PHOTOTYPE);
            edit.commit();

            startActivity(intent);
            finish();

        } else {
            // Signed out, show unauthenticated UI.

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
   }
}

