package com.example.android.itsover9000.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import com.example.android.itsover9000.R;



public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_visualizer);

        //reference to share preferences
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();

        //reference to the preference screen
        PreferenceScreen prefScreen = getPreferenceScreen();
        //getting a count of the total number of preferences
        int count = prefScreen.getPreferenceCount();

        //iterating through the share preferences
        for(int i = 0; i<count; i++){
            Preference p = prefScreen.getPreference(i);

            //Checking if it's a checkbox preference.
            if(!(p instanceof CheckBoxPreference)){
                //sending the value and the preference to the setPreference method
                setPreferenceSummary(p,sharedPreferences.getString(p.getKey(),""));
            }

        }
    }

    //method that sets the correct preference summary
    private void setPreferenceSummary(Preference preference, String value){
        //Checking if the preference is an instance of List Preference
        if(preference instanceof ListPreference){

            //Casting it
            ListPreference listPreference = (ListPreference) preference;
            //index of the current preference
            int prefIndex = listPreference.findIndexOfValue(value);

            //If the index is valid
            if(prefIndex >= 0){
                //setting the summary
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }

        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Preference preference = findPreference(s);
        if(null != preference){
            if(!(preference instanceof CheckBoxPreference)){
                setPreferenceSummary(preference,sharedPreferences.getString(preference.getKey(),""));
            }
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
