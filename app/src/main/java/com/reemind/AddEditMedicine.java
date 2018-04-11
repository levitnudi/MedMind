package com.reemind;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.reemind.database.DatabaseHelper;
import com.reemind.models.MedData;
import com.reemind.models.TempData;
import com.reemind.receivers.AlarmReceiver;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddEditMedicine extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    @BindView(R.id.drug_name)
    EditText drugName;
    @BindView(R.id.drug_description)
    EditText drugDescription;
    @BindView(R.id.start_time)
    TextView start_time;
    @BindView(R.id.frequency_input)
    EditText freqInput;
    @BindView(R.id.time_interval_input)
    EditText intvlInput;
    @BindView(R.id.end_date)
    TextView end_date;
    @BindView(R.id.drug_icon)
    TextView drugIconText;
    @BindView(R.id.start_date)
    TextView startDate;
    @BindView(R.id.selected_icon)
    ImageView drugIcon;
    @BindView(R.id.horizontal_icon_view_holder)
    LinearLayout iconViewHolder;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    boolean isVisible = false;
    public int icon;
    public String colour;
    public Calendar calendar;
    public String dateToStart = "";
    public String dateToEnd = "";
    public String timeTostart = "";
    public int frequency;
    public int id;
    public int interval = 1;
    public DatabaseHelper db;
    public List<MedData> notesList = new ArrayList<>();
    boolean isUpdate = false;
    public int rowId = 0;
    public String mTitle;
    public String mTime;
    public String mDate;
    public String mRepeatNo;
    public String mRepeatType;
    public String mActive;
    public String mRepeat;
    public String[] mDateSplit;
    public String[] mTimeSplit;
    public int mReceivedID;
    public int mYear, mMonth, mHour, mMinute, mDay;
    public long mRepeatTime;
    public Calendar mCalendar;
    public AlarmReceiver mAlarmReceiver;
    public String DATE_TYPE = "start";


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_medicine);
        ButterKnife.bind(this);


        Bundle bundle = getIntent().getExtras();

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_cancel_white);
        if (getActionBar() != null) getActionBar().setDisplayHomeAsUpEnabled(true);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle(null);

        calendar = Calendar.getInstance();
        icon = R.drawable.ic_tablets;
        drugIcon.setTag(icon);

        db = new DatabaseHelper(this);

        if (bundle != null) {
            //get the database row id from itemClick
            getMedicationDetails(bundle.getInt("rowId"));
            isUpdate = true;
            rowId = bundle.getInt("rowId");
        }

        // Initialize default values
        mActive = "true";
        mRepeat = "true";
        mRepeatNo = Integer.toString(1);
        mRepeatType = "Hour";

        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DATE);

        mDate = mDay + "/" + mMonth + "/" + mYear;
        mTime = mHour + ":" + mMinute;

        // Setup Reminder Title EditText
        drugName.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitle = s.toString().trim();
                drugName.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Setup TextViews using reminder values
        startDate.setText(mDate);
        start_time.setText(mTime);
        //freqInput.setText(mRepeatNo);
        //intvlInput.setText(mRepeatType);
        //end_date.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");

    }


    // On clicking Time picker
    public void setTime(View v) {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.setThemeDark(false);
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    // On clicking Date picker
    public void setStartDate(View v) {
        DATE_TYPE = "start";
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    // On clicking Date picker
    public void setEndDate(View v) {
        DATE_TYPE = "end";
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    // Obtain time from time picker
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        mHour = hourOfDay;
        mMinute = minute;
        if (minute < 10) {
            mTime = hourOfDay + ":" + "0" + minute;
        } else {
            mTime = hourOfDay + ":" + minute;
        }
        start_time.setText("Starts at "+mTime);
    }


    // Obtain date from date picker
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear++;
        mDay = dayOfMonth;
        mMonth = monthOfYear;
        mYear = year;
        mDate = dayOfMonth + "/" + monthOfYear + "/" + year;
        if(DATE_TYPE.equals("start")){
            startDate.setText("Starts from "+mDate);
        }else if (DATE_TYPE.equals("end")){
            end_date.setText("Ends on "+mDate);
        }

    }


    public void getMedicationDetails(int id) {
        drugName.setText(db.getMedication(id).getColumnDrugName());
        drugDescription.setText(db.getMedication(id).getColumnDrugDesc());
        drugIcon.setImageResource(db.getMedication(id).getColumnIcon());
        startDate.setText(db.getMedication(id).getColumnStartDate());
        end_date.setText(db.getMedication(id).getColumnEndDate());
        start_time.setText(db.getMedication(id).getColumnStartTime());
        freqInput.setText(db.getMedication(id).getColumnFrequency());
        intvlInput.setText(db.getMedication(id).getColumnTimeInterval());
    }

    @OnClick(R.id.icon_select)
    public void iconSelector() {
        if (!isVisible) {
            iconViewHolder.setVisibility(View.VISIBLE);
            isVisible = true;
        } else {
            iconViewHolder.setVisibility(View.GONE);
            isVisible = false;
        }
    }


    //icon selection
    public void menuItemClick(View view) {
        drugIconText.setText(String.valueOf(view.getTag()));
        String iconName = String.valueOf(view.getTag());
        if (iconName.contains("Tablets")) {
            drugIcon.setImageResource(R.drawable.ic_tablets);
            drugIcon.setTag(R.drawable.ic_tablets);
        } else if (iconName.contains("Injection")) {
            drugIcon.setImageResource(R.drawable.ic_injection);
            drugIcon.setTag(R.drawable.ic_injection);
        } else if (iconName.contains("Liquid")) {
            drugIcon.setImageResource(R.drawable.ic_liquid);
            drugIcon.setTag(R.drawable.ic_liquid);
        } else if (iconName.contains("Spray")) {
            drugIcon.setImageResource(R.drawable.ic_spray);
            drugIcon.setTag(R.drawable.ic_spray);
        } else if (iconName.contains("Dropper")) {
            drugIcon.setImageResource(R.drawable.ic_dropper);
            drugIcon.setTag(R.drawable.ic_dropper);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.item_save:
                saveMedication();
                //saveReminder();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void saveMedication() {
        if (!isUpdate) {
          long ID = db.insertMedication(TempData.currentEmailAccount,
                    drugName.getText().toString(),
                    drugDescription.getText().toString(),
                    (int) drugIcon.getTag(),
                    startDate.getText().toString().replaceAll("Starts from ", ""),
                    end_date.getText().toString().replaceAll("Ends on", ""),
                    start_time.getText().toString(),
                    freqInput.getText().toString(),
                    intvlInput.getText().toString(),
                    TempData.REPEAT,
                    TempData.REPEAR_TYPE,
                    TempData.ACTIVATE_ALARM);

            // Set up calender for creating the notification
            mCalendar.set(Calendar.MONTH, --mMonth);
            mCalendar.set(Calendar.YEAR, mYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
            mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
            mCalendar.set(Calendar.MINUTE, mMinute);
            mCalendar.set(Calendar.SECOND, 0);

            // Add interval type (Hours)
            mRepeatTime = Integer.parseInt(mRepeatNo) * TempData.MIL_HOURS;

            new AlarmReceiver().setRepeatAlarm(getApplicationContext(), mCalendar, (int)ID, mRepeatTime);

        } else {
            updateNote(TempData.currentEmailAccount,
                    drugName.getText().toString(),
                    drugDescription.getText().toString(),
                    (int) drugIcon.getTag(),
                    startDate.getText().toString(),
                    end_date.getText().toString(),
                    start_time.getText().toString(),
                    freqInput.getText().toString(),
                    intvlInput.getText().toString(),
                    TempData.REPEAT,
                    TempData.REPEAR_TYPE,
                    TempData.ACTIVATE_ALARM);

            new AlarmReceiver().setRepeatAlarm(getApplicationContext(), mCalendar, rowId, mRepeatTime);
        }
        startActivity(new Intent(AddEditMedicine.this, MainActivity.class));
        showToast("Saved successfuly!");
        finish();
    }

    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }


    public void updateNote(String email, String drug, String desc, int icon, String start_date, String end_date, String start_time,
                            String freq, String interval, String repeat, String repeatType, String active) {
        MedData medData = new MedData();
        // updating note text
        medData.setId(rowId);
        medData.setColumnAccountEmail(email);
        medData.setColumnDrugName(drug);
        medData.setColumnDrugDesc(desc);
        medData.setColumnIcon(icon);
        medData.setColumnStartDate(start_date);
        medData.setColumnEndDate(end_date);
        medData.setColumnStartTime(start_time);
        medData.setColumnFrequency(freq);
        medData.setColumnTimeInterval(interval);
        medData.setColumnRepeat(repeat);
        medData.setColumnRepeatType(repeatType);
        medData.setColumnActive(active);
        // updating note in db
        db.updateMedication(medData);
    }

}