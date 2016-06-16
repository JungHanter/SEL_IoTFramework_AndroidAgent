package net.infidea.cma.setting;

import net.infidea.cma.LoginActivity;
import net.infidea.cma.MainActivity;
import net.infidea.cma.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.View;

public class SettingFragment extends PreferenceFragment {
	
	private PreferenceManager preferenceManager = null;

	private Preference logoutBt = null;

	private EditTextPreference sensingDurationEt = null;
	private EditTextPreference transmissionPeriodEt = null;
	private SharedPreferences session = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.setting);

		preferenceManager = getPreferenceManager();

		session = preferenceManager.getDefaultSharedPreferences(getActivity());

		logoutBt = (Preference) preferenceManager.findPreference("logout");
		
		sensingDurationEt = (EditTextPreference) preferenceManager.findPreference("sensingDuration");
		transmissionPeriodEt = (EditTextPreference) preferenceManager.findPreference("transferPeriod");
		
		sensingDurationEt.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
		transmissionPeriodEt.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);

		logoutBt.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				SharedPreferences.Editor editor = session.edit();
				editor.remove("serverAddr");
				editor.remove("connection");
				editor.commit();
				Intent intent = new Intent(SettingFragment.this.getActivity(), LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				return true;
			}
		});

		sensingDurationEt.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO Auto-generated method stub
				try {
					int val = Integer.parseInt((String)newValue);
					if(val >= 0) {
						sensingDurationEt.setText(""+val);
					} else {
						sensingDurationEt.setText("0");
					}
				} catch (Exception e) {
					// TODO: handle exception
					sensingDurationEt.setText("0");
				}
				return false;
			}
		});
		
		transmissionPeriodEt.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				// TODO Auto-generated method stub
				try {
					double val = Double.parseDouble((String)newValue);
					if(val >= 0.1) {
						transmissionPeriodEt.setText(""+val);
					} else {
						transmissionPeriodEt.setText("0.1");
					}
				} catch (Exception e) {
					// TODO: handle exception
					transmissionPeriodEt.setText("0");
				}
				return false;
			}
		});
	}
}
