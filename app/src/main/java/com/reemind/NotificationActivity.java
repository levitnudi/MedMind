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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.reemind.models.TempData.NO_NOTIFICATION;

public class NotificationActivity extends AppCompatActivity {

    @BindView(R.id.fab) FloatingActionButton fab;
    SharedPreferences shared;
    SharedPreferences.Editor edit;
    @BindView(R.id.notification_text) TextView notificationText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        shared = getSharedPreferences("REEMIND", MODE_PRIVATE);
        edit = shared.edit();


        if(shared.getString("notification", null)==null){
            edit.putString("notification", NO_NOTIFICATION);
            edit.commit();
        }


        notificationText.setText(shared.getString("notification", null));


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Snackbar.make(view, "Confirming notification...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AsyncTask<Void, Void, Void> _ = new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... voids) {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        fab.setVisibility(View.GONE);
                                        Snackbar.make(
                                                view, "No pending notification!", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                edit.putString("notification", NO_NOTIFICATION);
                                                edit.commit();
                                                startHomeActivity();
                                            }
                                        }, TimeUnit.SECONDS.toMillis(1));
                                    }
                                });
                                return null;
                            }
                        }.execute();
                    }
                }, TimeUnit.SECONDS.toMillis(2)); // Delay for 2 seconds
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    public void startHomeActivity(){
        startActivity(new Intent(NotificationActivity.this, MainActivity.class));
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                startHomeActivity();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
