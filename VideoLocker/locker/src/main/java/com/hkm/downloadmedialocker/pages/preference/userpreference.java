package com.hkm.downloadmedialocker.pages.preference;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hkm.downloadmedialocker.R;
import com.hkm.downloadmedialocker.life.EBus;
import com.hkm.downloadmedialocker.life.HBUtil;
import com.hkm.downloadmedialocker.life.RenderTrigger;
import com.hypebeast.sdk.clients.HBEditorialClient;

/**
 * Created by hesk on 12/1/16.
 */
public class userpreference extends PreferenceFragment {
    private ListPreference mListPreference;
    private HBEditorialClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = HBEditorialClient.getInstance(getActivity().getApplicationContext());
        addPreferencesFromResource(R.xml.setup_option_list);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mListPreference = (ListPreference) getPreferenceManager().findPreference("PREF_LIST_LANG");
        mListPreference.setOnPreferenceChangeListener(check_preference);
        return inflater.inflate(R.layout.content_setting_page, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(shared_preference_changes);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(shared_preference_changes);
    }

    private Preference.OnPreferenceChangeListener check_preference = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference.getKey().equalsIgnoreCase("PREF_LIST_LANG") && newValue instanceof String) {
                setSwitchExistingClientFromPreferencePanel((String) newValue);
                return true;
            }
            return false;
        }
    };

    private SharedPreferences.OnSharedPreferenceChangeListener shared_preference_changes = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        }
    };

    public void setSwitchExistingClientFromPreferencePanel(String mLanguage) {

        if (mLanguage.equals("en")) {
            client.setLanguageBase(HBEditorialClient.BASE_EN);
        } else if (mLanguage.equals("zh_CN")) {
            client.setLanguageBase(HBEditorialClient.BASE_CN);
        } else if (mLanguage.equals("ja")) {
            client.setLanguageBase(HBEditorialClient.BASE_JP);
        } else if (mLanguage.equals("zh_TW")) {
            client.setLanguageBase(HBEditorialClient.BASE_CN);
        }
        HBUtil.setApplicationLanguageBase(mLanguage, getActivity().getApplication(), client);
        EBus.getInstance().post(new RenderTrigger());
    }


}
