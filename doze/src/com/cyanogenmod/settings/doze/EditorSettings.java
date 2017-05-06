package com.cyanogenmod.settings.doze;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.util.KeyValueListParser;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.provider.Settings;
import android.util.Log;
import android.provider.Settings.SettingNotFoundException;

public class EditorSettings extends PreferenceActivity {

    // Key names stored in the Global Setting
    private static final String KEY_INACTIVE_TIMEOUT = "inactive_to";
    private static final String KEY_SENSING_TIMEOUT = "sensing_to";
    private static final String KEY_LOCATING_TIMEOUT = "locating_to";
    private static final String KEY_LOCATION_ACCURACY = "location_accuracy";
    private static final String KEY_MOTION_INACTIVE_TIMEOUT = "motion_inactive_to";
    private static final String KEY_IDLE_AFTER_INACTIVE_TIMEOUT = "idle_after_inactive_to";
    private static final String KEY_IDLE_PENDING_TIMEOUT = "idle_pending_to";
    private static final String KEY_MAX_IDLE_PENDING_TIMEOUT = "max_idle_pending_to";
    private static final String KEY_IDLE_PENDING_FACTOR = "idle_pending_factor";
    private static final String KEY_IDLE_TIMEOUT = "idle_to";
    private static final String KEY_MAX_IDLE_TIMEOUT = "max_idle_to";
    private static final String KEY_IDLE_FACTOR = "idle_factor";
    private static final String KEY_MIN_TIME_TO_ALARM = "min_time_to_alarm";
    private static final String KEY_MAX_TEMP_APP_WHITELIST_DURATION =
            "max_temp_app_whitelist_duration";
    private static final String KEY_MMS_TEMP_APP_WHITELIST_DURATION =
            "mms_temp_app_whitelist_duration";
    private static final String KEY_SMS_TEMP_APP_WHITELIST_DURATION =
            "sms_temp_app_whitelist_duration";

    final long INACTIVE_TIMEOUT = 30 * 60 * 1000L;
    final long SENSING_TIMEOUT = 4 * 60 * 1000L;
    final long LOCATING_TIMEOUT = 30 * 1000L;
    final long LOCATION_ACCURACY = 20;
    final long MOTION_INACTIVE_TIMEOUT = 10 * 60 * 1000L;
    final long IDLE_AFTER_INACTIVE_TIMEOUT = 30 * 60 * 1000L;
    final long IDLE_PENDING_TIMEOUT = 5 * 60 * 1000L;
    final long MAX_IDLE_PENDING_TIMEOUT = 10 * 60 * 1000L;
    final long IDLE_PENDING_FACTOR = 2;
    final long IDLE_TIMEOUT = 60 * 60 * 1000L;
    final long MAX_IDLE_TIMEOUT = 6 * 60 * 60 * 1000L;
    final long IDLE_FACTOR = 2;
    final long MIN_TIME_TO_ALARM = 60 * 60 * 1000L;
    final long MAX_TEMP_APP_WHITELIST_DURATION = 5 * 60 * 1000L;
    final long MMS_TEMP_APP_WHITELIST_DURATION = 60 * 1000L;
    final long SMS_TEMP_APP_WHITELIST_DURATION = 20 * 1000L;

    private EditTextPreference et_inactive_to;
    private EditTextPreference et_sensing_to;
    private EditTextPreference et_locating_to;
    private EditTextPreference et_location_accuracy;
    private EditTextPreference et_motion_inactive_to;
    private EditTextPreference et_idle_after_inactive_to;
    private EditTextPreference et_idle_pending_to;
    private EditTextPreference et_max_idle_pending_to;
    private EditTextPreference et_idle_pending_factor;
    private EditTextPreference et_idle_to;
    private EditTextPreference et_max_idle_to;
    private EditTextPreference et_idle_factor;
    private EditTextPreference et_min_time_to_alarm;
    private EditTextPreference et_max_temp_app_whitelist_duration;
    private EditTextPreference et_mms_temp_app_whitelist_duration;
    private EditTextPreference et_sms_temp_app_whitelist_duration;

    int millisecondsInOneSecond = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_editor);
        // Get EditTextPreference from pref_editor.xml
        // The name of edit text is "et_" + key of corresponding doze value
        et_inactive_to = (EditTextPreference) findPreference("et_" + KEY_INACTIVE_TIMEOUT);
        et_sensing_to = (EditTextPreference) findPreference("et_" + KEY_SENSING_TIMEOUT);
        et_locating_to = (EditTextPreference) findPreference("et_" + KEY_LOCATING_TIMEOUT);
        et_location_accuracy = (EditTextPreference) findPreference("et_" + KEY_LOCATION_ACCURACY);
        et_motion_inactive_to = (EditTextPreference) findPreference("et_" + KEY_MOTION_INACTIVE_TIMEOUT);
        et_idle_after_inactive_to = (EditTextPreference) findPreference("et_" + KEY_IDLE_AFTER_INACTIVE_TIMEOUT);
        et_idle_pending_to = (EditTextPreference) findPreference("et_" + KEY_IDLE_PENDING_TIMEOUT);
        et_max_idle_pending_to = (EditTextPreference) findPreference("et_" + KEY_MAX_IDLE_PENDING_TIMEOUT);
        et_idle_pending_factor = (EditTextPreference) findPreference("et_" + KEY_IDLE_PENDING_FACTOR);
        et_idle_to = (EditTextPreference) findPreference("et_" + KEY_IDLE_TIMEOUT);
        et_max_idle_to = (EditTextPreference) findPreference("et_" + KEY_MAX_IDLE_TIMEOUT);
        et_idle_factor = (EditTextPreference) findPreference("et_" + KEY_IDLE_FACTOR);
        et_min_time_to_alarm = (EditTextPreference) findPreference("et_" + KEY_MIN_TIME_TO_ALARM);
        et_max_temp_app_whitelist_duration = (EditTextPreference) findPreference("et_" + KEY_MAX_TEMP_APP_WHITELIST_DURATION);
        et_mms_temp_app_whitelist_duration = (EditTextPreference) findPreference("et_" + KEY_MMS_TEMP_APP_WHITELIST_DURATION);
        et_sms_temp_app_whitelist_duration = (EditTextPreference) findPreference("et_" + KEY_SMS_TEMP_APP_WHITELIST_DURATION);

        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        try {
            // Get current values and set these on relative EditTextPreference
            updateEditText(getCurrentValues());
        }catch (SettingNotFoundException e){
            Log.e("EditorSettings", e.getLocalizedMessage());
            Toast.makeText(getApplicationContext(), "Error to get values", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case android.R.id.home:
                startActivity(new Intent(this, DozeSettings.class));
                break;
            case R.id.action_save:
                try{
                    Toast.makeText(getApplicationContext(), "Saving in progres..", Toast.LENGTH_SHORT).show();
                    showToastMessage(saveValues());
                }catch (SettingNotFoundException e){
                    Log.e("EditorSettings", e.getLocalizedMessage());
                    Toast.makeText(getApplicationContext(), "Error to saveValues value", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.action_restore_default:
                try{
                    Toast.makeText(getApplicationContext(), "Restoring in progres..", Toast.LENGTH_SHORT).show();
                    updateEditText(restoreValues());
                    showToastMessage(saveValues());
                }catch (SettingNotFoundException e){
                    Log.e("EditorSettings", e.getLocalizedMessage());
                    Toast.makeText(getApplicationContext(), "Error to restore values", Toast.LENGTH_LONG).show();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateEditText(KeyValueListParser mParser){

        long inactiveTimeout = mParser.getLong(KEY_INACTIVE_TIMEOUT, INACTIVE_TIMEOUT);
        long maxIdlePendingTimeout = mParser.getLong(KEY_MAX_IDLE_PENDING_TIMEOUT, MAX_IDLE_PENDING_TIMEOUT);
        long locatingTimeout = mParser.getLong(KEY_LOCATING_TIMEOUT, LOCATING_TIMEOUT);
        long sensingTimeout =  mParser.getLong(KEY_SENSING_TIMEOUT, SENSING_TIMEOUT);
        long locationAccuracy = mParser.getLong(KEY_LOCATION_ACCURACY, LOCATION_ACCURACY);
        long motionInactiveTimeout =  mParser.getLong(KEY_MOTION_INACTIVE_TIMEOUT, MOTION_INACTIVE_TIMEOUT);
        long idleAfterInactiveTimeout =  mParser.getLong(KEY_IDLE_AFTER_INACTIVE_TIMEOUT, IDLE_AFTER_INACTIVE_TIMEOUT);
        long idlePendingTimeout =  mParser.getLong(KEY_IDLE_PENDING_TIMEOUT, IDLE_PENDING_TIMEOUT);
        long idlePendingFactor = mParser.getLong(KEY_IDLE_PENDING_FACTOR, IDLE_PENDING_TIMEOUT);
        long idleTimeout =  mParser.getLong(KEY_IDLE_TIMEOUT, IDLE_TIMEOUT);
        long maxIdleTimeout = mParser.getLong(KEY_MAX_IDLE_TIMEOUT, MAX_IDLE_TIMEOUT);
        long idleFactor =  mParser.getLong(KEY_IDLE_FACTOR, IDLE_FACTOR);
        long minTimeToAlarm =   mParser.getLong(KEY_MIN_TIME_TO_ALARM, MIN_TIME_TO_ALARM);
        long maxTempAppWhitelistDuration = mParser.getLong(KEY_MAX_TEMP_APP_WHITELIST_DURATION, MAX_TEMP_APP_WHITELIST_DURATION);
        long mmsTempAppWhitelistDuration = mParser.getLong(KEY_MMS_TEMP_APP_WHITELIST_DURATION, MMS_TEMP_APP_WHITELIST_DURATION);
        long smsTempAppWhitelistDuration = mParser.getLong(KEY_SMS_TEMP_APP_WHITELIST_DURATION, SMS_TEMP_APP_WHITELIST_DURATION);

        et_inactive_to.setText(String.valueOf(inactiveTimeout / millisecondsInOneSecond));
        et_sensing_to.setText(String.valueOf(sensingTimeout / millisecondsInOneSecond));
        et_locating_to.setText(String.valueOf(locatingTimeout / millisecondsInOneSecond));
        et_location_accuracy.setText(String.valueOf(locationAccuracy));
        et_motion_inactive_to.setText(String.valueOf(motionInactiveTimeout / millisecondsInOneSecond));
        et_idle_after_inactive_to.setText(String.valueOf(idleAfterInactiveTimeout / millisecondsInOneSecond));
        et_idle_pending_to.setText(String.valueOf(idlePendingTimeout / millisecondsInOneSecond));
        et_max_idle_pending_to.setText(String.valueOf(maxIdlePendingTimeout / millisecondsInOneSecond));
        et_idle_pending_factor.setText(String.valueOf(idlePendingFactor));
        et_idle_to.setText(String.valueOf(idleTimeout / millisecondsInOneSecond));
        et_max_idle_to.setText(String.valueOf(maxIdleTimeout / millisecondsInOneSecond));
        et_idle_factor.setText(String.valueOf(idleFactor));
        et_min_time_to_alarm.setText(String.valueOf(minTimeToAlarm / millisecondsInOneSecond));
        et_max_temp_app_whitelist_duration.setText(String.valueOf(maxTempAppWhitelistDuration / millisecondsInOneSecond));
        et_mms_temp_app_whitelist_duration.setText(String.valueOf(mmsTempAppWhitelistDuration / millisecondsInOneSecond));
        et_sms_temp_app_whitelist_duration.setText(String.valueOf(smsTempAppWhitelistDuration / millisecondsInOneSecond));
    }


    private boolean saveValues() throws SettingNotFoundException{
        StringBuilder sb = new StringBuilder();
        // Get new values from EditTex
        sb.append(KEY_INACTIVE_TIMEOUT + "=" + Long.valueOf(et_inactive_to.getText()) * millisecondsInOneSecond + ",");
        sb.append(KEY_SENSING_TIMEOUT + "=" + Long.valueOf(et_sensing_to.getText()) * millisecondsInOneSecond + ",");
        sb.append(KEY_LOCATING_TIMEOUT + "=" + Long.valueOf(et_locating_to.getText()) * millisecondsInOneSecond + ",");
        sb.append(KEY_LOCATION_ACCURACY + "=" + Long.valueOf(et_location_accuracy.getText()) + ",");
        sb.append(KEY_MOTION_INACTIVE_TIMEOUT + "=" + Long.valueOf(et_motion_inactive_to.getText()) * millisecondsInOneSecond + ",");
        sb.append(KEY_IDLE_AFTER_INACTIVE_TIMEOUT + "=" + Long.valueOf(et_idle_after_inactive_to.getText()) * millisecondsInOneSecond + ",");
        sb.append(KEY_IDLE_PENDING_TIMEOUT + "=" + Long.valueOf(et_idle_pending_to.getText()) * millisecondsInOneSecond + ",");
        sb.append(KEY_MAX_IDLE_PENDING_TIMEOUT + "=" + Long.valueOf(et_max_idle_pending_to.getText()) * millisecondsInOneSecond + ",");
        sb.append(KEY_IDLE_PENDING_FACTOR + "=" + Long.valueOf(et_idle_pending_factor.getText()) + ",");
        sb.append(KEY_IDLE_TIMEOUT + "=" + Long.valueOf(et_idle_to.getText()) * millisecondsInOneSecond + ",");
        sb.append(KEY_MAX_IDLE_TIMEOUT + "=" + Long.valueOf(et_max_idle_to.getText()) * millisecondsInOneSecond + ",");
        sb.append(KEY_IDLE_FACTOR + "=" + Long.valueOf(et_idle_factor.getText()) + ",");
        sb.append(KEY_MIN_TIME_TO_ALARM + "=" + Long.valueOf(et_min_time_to_alarm.getText()) * millisecondsInOneSecond + ",");
        sb.append(KEY_MAX_TEMP_APP_WHITELIST_DURATION + "=" + Long.valueOf(et_max_temp_app_whitelist_duration.getText()) * millisecondsInOneSecond + ",");
        sb.append(KEY_MMS_TEMP_APP_WHITELIST_DURATION + "=" + Long.valueOf(et_mms_temp_app_whitelist_duration.getText()) * millisecondsInOneSecond + ",");
        sb.append(KEY_SMS_TEMP_APP_WHITELIST_DURATION + "=" + Long.valueOf(et_sms_temp_app_whitelist_duration.getText()) * millisecondsInOneSecond);

        // Save new values into Setting
        return Settings.Global.putString(getContentResolver(), Settings.Global.DEVICE_IDLE_CONSTANTS, sb.toString());
    }

    private KeyValueListParser getCurrentValues() throws SettingNotFoundException {
        KeyValueListParser mParser = new KeyValueListParser(',');
        mParser.setString(Settings.Global.getString(getContentResolver(), Settings.Global.DEVICE_IDLE_CONSTANTS));
        return mParser;
    }

    private KeyValueListParser restoreValues() throws Settings.SettingNotFoundException{
        StringBuilder sb = new StringBuilder();
        // Restore old values
        sb.append(KEY_INACTIVE_TIMEOUT + "=" + INACTIVE_TIMEOUT + ",");
        sb.append(KEY_SENSING_TIMEOUT + "=" + SENSING_TIMEOUT + ",");
        sb.append(KEY_LOCATING_TIMEOUT + "=" + LOCATING_TIMEOUT + ",");
        sb.append(KEY_LOCATION_ACCURACY + "=" + LOCATION_ACCURACY + ",");
        sb.append(KEY_MOTION_INACTIVE_TIMEOUT + "=" + MOTION_INACTIVE_TIMEOUT + ",");
        sb.append(KEY_IDLE_AFTER_INACTIVE_TIMEOUT + "=" + IDLE_AFTER_INACTIVE_TIMEOUT + ",");
        sb.append(KEY_IDLE_PENDING_TIMEOUT + "=" + IDLE_PENDING_TIMEOUT + ",");
        sb.append(KEY_MAX_IDLE_PENDING_TIMEOUT + "=" + MAX_IDLE_PENDING_TIMEOUT + ",");
        sb.append(KEY_IDLE_PENDING_FACTOR + "=" + IDLE_PENDING_FACTOR + ",");
        sb.append(KEY_IDLE_TIMEOUT + "=" + IDLE_TIMEOUT + ",");
        sb.append(KEY_MAX_IDLE_TIMEOUT + "=" + MAX_IDLE_TIMEOUT + ",");
        sb.append(KEY_IDLE_FACTOR + "=" + IDLE_FACTOR + ",");
        sb.append(KEY_MIN_TIME_TO_ALARM + "=" + MIN_TIME_TO_ALARM + ",");
        sb.append(KEY_MAX_TEMP_APP_WHITELIST_DURATION + "=" + MAX_TEMP_APP_WHITELIST_DURATION + ",");
        sb.append(KEY_MMS_TEMP_APP_WHITELIST_DURATION + "=" + MMS_TEMP_APP_WHITELIST_DURATION + ",");
        sb.append(KEY_SMS_TEMP_APP_WHITELIST_DURATION + "=" + SMS_TEMP_APP_WHITELIST_DURATION);

        KeyValueListParser mParser =  new KeyValueListParser(',');
        mParser.setString(sb.toString());
        return mParser;
    }

    private void showToastMessage(boolean successfull){
        if(successfull){
            Toast.makeText(getApplicationContext(), "Values update", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), "Error to update values", Toast.LENGTH_LONG).show();
        }
    }


}
