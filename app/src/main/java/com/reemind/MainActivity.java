// Copyright 2016 Google Inc.
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//      http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.reemind;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.reemind.adapters.MedicationsAdapter;
import com.reemind.database.DatabaseHelper;
import com.reemind.models.MedData;
import com.reemind.models.TempData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.reemind.models.TempData.SHARED_PREF_NAME;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MedicationsAdapter.RecyclerListener,
        GoogleApiClient.OnConnectionFailedListener{

    //private RecyclerView.LayoutManager mItemsLayoutManager;
    private MedicationsAdapter mAdapter;
    private List<MedData> notesList = new ArrayList<>();
    private RecyclerView recyclerView;
    AppCompatButton mSelectMonthButton;
    Context context;
    TextView name_view;
    TextView email_view;
    ImageView imageView;
    TextView noText;
    private DatabaseHelper db;
    SharedPreferences shared;
    SharedPreferences.Editor edit;
    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;
    protected GoogleApiClient mGoogleApiClient;

    @BindView(R.id.fab_button) FloatingActionButton floatingActionButton;

    private boolean fabIsHidden = false;

    private RecyclerView getItemsRecycleView() {
        return (RecyclerView) findViewById(R.id.items_list);
    }

    private NavigationView getNavigationView() {
        return (NavigationView) findViewById(R.id.nav_view);
    }

    private Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    private DrawerLayout getDrawerLayout() {
        return (DrawerLayout) findViewById(R.id.drawer_layout);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shared = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        edit = shared.edit();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        Toolbar toolbar = getToolbar();
        setSupportActionBar(toolbar);

        DrawerLayout drawer = getDrawerLayout();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = getNavigationView();
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        name_view = (TextView) headerView.findViewById(R.id.name_view);
        View subheaderView = navigationView.getHeaderView(0);
        email_view = (TextView) subheaderView.findViewById(R.id.email_view);
        View imageheaderView = navigationView.getHeaderView(0);
        imageView = (ImageView) imageheaderView.findViewById(R.id.image_view);
        noText = (TextView)findViewById(R.id.no_items_text);

        name_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, EditProfile.class));
                finish();
            }
        });

         getAccountDetails();

        context = getApplicationContext();

        db = new DatabaseHelper(context);

        notesList.addAll(db.getAllMedications());

        recyclerView = (RecyclerView) findViewById(R.id.items_list);

        mAdapter = new MedicationsAdapter(this, notesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        if(notesList.isEmpty()){
            noText.setVisibility(View.VISIBLE);
        }else{
            noText.setVisibility(View.GONE);
        }


        //recyclerView.

        floatingActionButton = (FloatingActionButton)findViewById(R.id.fab_button);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddEditMedicine.class));
                finish();
            }
        });


        mSelectMonthButton = (AppCompatButton)findViewById(R.id.month_button);

        mSelectMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(MainActivity.this, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_months, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        onMonthSelected(item);
                        return true;
                    }
                });
                popup.show();
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {


        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.

            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {

                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // When we get here in an automanager activity the error is likely not
        // resolvable - meaning Google Sign In and other Google APIs will be
        // unavailable.
        //Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void onMonthSelected(MenuItem item) {
        final String month = item.getTitle().toString();
        AsyncTask<Void, Void, Void> save = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSelectMonthButton.setText(month);
                    }
                });
                return null;
            }
        }.execute();

    }


    public void getAccountDetails(){
        GoogleSignInAccount acct  = GoogleSignIn.getLastSignedInAccount(this);

        if (acct != null) {

            name_view.setText(shared.getString("user-name-"+TempData.currentEmailAccount, null));

            email_view.setText(acct.getEmail());
            TempData.currentEmailAccount = acct.getEmail();

            if(shared.getString("user-photo-"+acct.getEmail(), null).equals(TempData.PHOTOTYPE)) {

                Picasso.with(this).load(acct.getPhotoUrl()).placeholder(R.drawable.ic_account_face)// Place holder image from drawable folder
             /*.error(R.drawable.ic_error_load)*/
                        .resize(110, 110).centerCrop()
                        //.networkPolicy(NetworkPolicy.OFFLINE)
                        .into(imageView);

            }else{
                String base = shared.getString("user-photo-"+acct.getEmail(), null);
                byte[] imageAsBytes = Base64.decode(base.getBytes(), Base64.DEFAULT);
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length) );
            }
        }

       /* edit.putString("user-name-"+acct.getEmail(), acct.getDisplayName());
        edit.putString("user-photo-"+acct.getEmail(), acct.getPhotoUrl().toString());
        edit.commit();*/
    }

    public void logoutAction(){
        // This will clear the default account in order to allow the user
        // to potentially choose a different account from the
        // account chooser.
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        // Hide the sign out buttons, show the sign in button.
                        Intent preferenceIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(preferenceIntent);
                        finish();
                    }
                });
    }



    public void showSnackbar(String text) {
        Snackbar.make(getToolbar(), text, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
    @OnClick(R.id.fab_button)
    public void fabClicked() {
        //Intent intent = new Intent(this, AddEditMedicine.class);
        //startActivity(intent);

    }

    @Override
    public void hideFab() {
        floatingActionButton.hide();
        fabIsHidden = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fabIsHidden) {
            floatingActionButton.show();
            fabIsHidden = false;
        }
    }





    /**
     * Deleting note from SQLite and removing the
     * item from the list by its position
     */
    public void deleteMedData(int position) {
        // deleting the note from db

        db.deleteMedication(notesList.get(position));

        // removing the note from the list
        //notesList.remove(position);
        mAdapter.notifyItemRemoved(position);


       // toggleEmptyNotes();
    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = getDrawerLayout();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_share: {
                Intent sendIntent = new Intent();
                Uri url = Uri.parse("https://github.com/levitnudi/MedMind");
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Hi!, I'm using "+getString(R.string.app_name)+" to manage my medication. " +
                        "Find out more here "+url);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.notification_manager: {
                //CartActivity.startActivity(this);
                Intent preferenceIntent = new Intent(this, NotificationActivity.class);
                startActivity(preferenceIntent);
                break;
            }

            case R.id.app_about: {
                Intent preferenceIntent = new Intent(this, AboutActivity.class);
                startActivity(preferenceIntent);
                break;
            }

            case R.id.app_logout: {
                logoutAction();
                break;
            }

            case R.id.app_rate_us:
                Uri gitLink = Uri.parse("https://github.com/levitnudi/MedMind");
                Intent gitIntent = new Intent(Intent.ACTION_VIEW, gitLink);
                try{
                    startActivity(gitIntent);
                }catch (ActivityNotFoundException e){}
                break;
        }

        DrawerLayout drawer = getDrawerLayout();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}
