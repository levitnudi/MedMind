package com.reemind.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.reemind.database.DatabaseHelper;
import com.reemind.models.MedData;
import com.reemind.models.TempData;

import java.util.Calendar;
import java.util.List;


public class BootBroadcastReceiver extends BroadcastReceiver {

    public String drugName;
    public String startTime;
    public String mDate;
    public String frequency;
    public String repeatType;
    public String active;
    public String repeat;
    public String[] mDateSplit;
    public String[] startTimeSplit;
    public int mYear, mMonth, mHour, mMinute, mDay, colId;
    public long repeatTime;

    public Calendar mCalendar;
    public AlarmReceiver mAlarmReceiver;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            DatabaseHelper db = new DatabaseHelper(context);
            mCalendar = Calendar.getInstance();
            mAlarmReceiver = new AlarmReceiver();

            List<MedData> medDatas = db.getAllMedications();

            for (MedData medData : medDatas) {
                colId = medData.getId();
                repeat = medData.getColumnRepeat();
                frequency = medData.getColumnFrequency();
                repeatType = medData.getColumnRepeatType();
                active = medData.getColumnActive();
                mDate = medData.getColumnStartDate();
                startTime = medData.getColumnStartTime();

                mDateSplit = mDate.split("/");
                startTimeSplit = startTime.split(":");

                mDay = Integer.parseInt(mDateSplit[0]);
                mMonth = Integer.parseInt(mDateSplit[1]);
                mYear = Integer.parseInt(mDateSplit[2]);
                mHour = Integer.parseInt(startTimeSplit[0]);
                mMinute = Integer.parseInt(startTimeSplit[1]);

                mCalendar.set(Calendar.MONTH, --mMonth);
                mCalendar.set(Calendar.YEAR, mYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
                mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
                mCalendar.set(Calendar.MINUTE, mMinute);
                mCalendar.set(Calendar.SECOND, 0);

                    repeatTime = Integer.parseInt(frequency) * TempData.MIL_HOURS;

                        mAlarmReceiver.setRepeatAlarm(context, mCalendar, colId, repeatTime);
                 
                
            }
        }
    }
}