package com.reemind.models;

/**
 * Created by Levit Nudi on 20/03/18.
 */

public class MedData {
    public static final String TABLE_NAME = "medmind";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ACCOUNT_EMAIL = "email";
    public static final String COLUMN_DRUG_NAME = "drug";
    public static final String COLUMN_DRUG_DESC = "description";
    public static final String COLUMN_ICON = "icon";
    public static final String COLUMN_START_DATE = "start_date";
    public static final String COLUMN_END_DATE = "end_date";
    public static final String COLUMN_START_TIME = "start_time";
    public static final String COLUMN_FREQUENCY = "frequency";
    public static final String COLUMN_TIME_INTERVAL = "interval";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_REPEAT = "repeat";
    public static final String COLUMN_REPEAT_TYPE = "repeat_type";
    public static final String COLUMN_ACTIVE = "active";

    public int id;
    public String email;
    public String drug;
    public String description;
    public int icon;
    public String start_date;
    public String end_date;
    public String start_time;
    public String frequency;
    public String interval;
    public String timestamp;
    public String repeat;
    public String repeatType;
    public String active;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_ACCOUNT_EMAIL + " TEXT,"
                    + COLUMN_DRUG_NAME + " TEXT,"
                    + COLUMN_DRUG_DESC + " TEXT,"
                    + COLUMN_ICON + " TEXT,"
                    + COLUMN_START_DATE + " TEXT,"
                    + COLUMN_END_DATE + " TEXT,"
                    + COLUMN_START_TIME + " INTEGER,"
                    + COLUMN_FREQUENCY + " INTEGER,"
                    + COLUMN_TIME_INTERVAL + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + COLUMN_REPEAT + " BOOLEAN,"
                    + COLUMN_REPEAT_TYPE + " TEXT,"
                    + COLUMN_ACTIVE + " BOOLEAN"
                    + ")";

    public MedData() {
    }

    public MedData(int id, String email, String drug, String description, int icon, String start_date,
                   String end_date, String start_time, String frequency, String interval, String timestamp,
                   String repeat, String repeatType, String active) {
        this.id = id;
        this.email = email;
        this.drug = drug;
        this.description = description;
        this.icon = icon;
        this.start_date = start_date;
        this.end_date = end_date;
        this.start_time = start_time;
        this.frequency = frequency;
        this.interval = interval;
        this.timestamp = timestamp;
        this.repeat = repeat;
        this.repeatType = repeatType;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public String getColumnDrugName() {
        return drug;
    }

    public String getColumnAccountEmail() {
        return email;
    }

    public String getColumnDrugDesc() {
        return description;
    }

    public int getColumnIcon() {
        return icon;
    }

    public String getColumnStartDate() {
        return start_date;
    }

    public String getColumnEndDate() {
        return end_date;
    }

    public String getColumnStartTime() {
        return start_time;
    }

    public String getColumnFrequency() {
        return frequency;
    }

    public String getColumnTimeInterval() {
        return interval;
    }

    public String getColumnTimestamp() {
        return timestamp;
    }

    public String getColumnActive() {
        return active;
    }

    public String getColumnRepeat() {
        return repeat;
    }

    public String getColumnRepeatType() {
        return repeatType;
    }


   public void setId(int id) {
        this.id = id;
    }

    public void setColumnAccountEmail(String email) {
        this.email = email;
    }

    public void setColumnDrugName(String drug) {
        this.drug = drug;
    }

    public void setColumnDrugDesc(String desc) {
        this.description = desc;
    }

    public void setColumnIcon(int icon) {
        this.icon = icon;
    }

    public void setColumnStartDate(String start) {
        this.start_date = start;
    }

    public void setColumnEndDate(String end) {
        this.end_date = end;
    }

    public void setColumnStartTime(String time) {
        this.start_time = time;
    }

    public void setColumnFrequency(String freq) {
        this.frequency = freq;
    }

    public void setColumnTimeInterval(String interval) {
        this.interval = interval;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setColumnRepeatType(String repeatType) {
        repeatType = repeatType;
    }

    public void setColumnRepeat(String repeat) {
        repeat = repeat;
    }

    public void setColumnActive(String active) {
        active = active;
    }
}
