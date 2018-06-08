package com.eskeptor.openTextViewer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.eskeptor.openTextViewer.license.ACCLicense;
import com.eskeptor.openTextViewer.license.BmJUALicense;
import com.eskeptor.openTextViewer.license.FloatingActionButtonLicense;
import com.eskeptor.openTextViewer.license.GlideLicense;
import com.eskeptor.openTextViewer.license.KOPUBLicense;
import com.eskeptor.openTextViewer.license.LicensesDialogLicense;
import com.eskeptor.openTextViewer.license.OpenpadLicense;
import com.eskeptor.openTextViewer.license.PassCodeViewLicense;
import com.eskeptor.openTextViewer.textManager.LogManager;
import com.tsengvn.typekit.Typekit;
import com.tsengvn.typekit.TypekitContextWrapper;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;
import util.TestLog;


/*
 * Created by eskeptor on 17. 2. 8.
 * Copyright (C) 2017 Eskeptor(Jeon Ye Chan)
 */

/**
 * 설정 페이지
 */
public class SettingsActivity extends AppCompatPreferenceActivity {
    public enum ActiveScreenType {
        Main, Font, Help, Security
    }

    private static ActiveScreenType mActiveScene;
    private static SharedPreferences mSharedPref;
    private static SharedPreferences.Editor mSharedPrefEditor;
    private static Help mHelpAct;
    private static FontSettings mFontSettingAct;
    private static Settings mSettingsAct;
    private static Security mSecurityAct;
    private static ActionBar mActionBar;
    private static int mFontStyle;
    private static int mPrevFontStyle;
    private static View mSettingsView;

    public static class Help extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_settings_help);

            mActionBar.setTitle(R.string.settings_information_help_title);

            Preference mainList = findPreference("settings_key_main_list");
            Preference mainMemoCreate = findPreference("settings_key_main_memocreate");
            Preference mainDelete = findPreference("settings_key_main_delete");
            Preference mainOpenFolder = findPreference("settings_key_main_openfolder");
            Preference folderList = findPreference("settings_key_folder_list");
            Preference folderDefault = findPreference("settings_key_folder_default");
            Preference folderCreate = findPreference("settings_key_folder_create");
            Preference folderDelete = findPreference("settings_key_folder_delete");
            Preference folderExternal = findPreference("settings_key_foler_external");
            Preference memoSaveLength = findPreference("settings_key_memo_savelengthlimited");
            Preference memoFontColor = findPreference("settings_key_memo_fontcolor");
            Preference memoEdit = findPreference("settings_key_memo_edit");
            Preference etcAboutHelp = findPreference("settings_key_etc_abouthelp");
            Preference etcBackup = findPreference("settings_key_etc_backup");
            Preference etcFileColor = findPreference("settings_key_etc_filecolor");
            Preference etcPermission = findPreference("settings_key_etc_permission");
            Preference etcAd = findPreference("settings_key_etc_ad");
            Preference etcFontBroken = findPreference("settings_key_etc_fontbroken");
            Preference etcWidget = findPreference("settings_key_etc_widget");

            Preference.OnPreferenceClickListener clickListener = new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.setClass(getActivity(), HelpContentsActivity.class);
                    intent.putExtra(Constant.INTENT_EXTRA_HELP_INDEX, preference.getKey());
                    startActivity(intent);
                    getActivity().overridePendingTransition(0, 0);
                    return false;
                }
            };

            mainList.setOnPreferenceClickListener(clickListener);
            mainMemoCreate.setOnPreferenceClickListener(clickListener);
            mainDelete.setOnPreferenceClickListener(clickListener);
            mainOpenFolder.setOnPreferenceClickListener(clickListener);
            folderExternal.setOnPreferenceClickListener(clickListener);
            folderDelete.setOnPreferenceClickListener(clickListener);
            folderCreate.setOnPreferenceClickListener(clickListener);
            folderDefault.setOnPreferenceClickListener(clickListener);
            folderList.setOnPreferenceClickListener(clickListener);
            memoFontColor.setOnPreferenceClickListener(clickListener);
            memoSaveLength.setOnPreferenceClickListener(clickListener);
            memoEdit.setOnPreferenceClickListener(clickListener);
            etcAboutHelp.setOnPreferenceClickListener(clickListener);
            etcBackup.setOnPreferenceClickListener(clickListener);
            etcFileColor.setOnPreferenceClickListener(clickListener);
            etcPermission.setOnPreferenceClickListener(clickListener);
            etcAd.setOnPreferenceClickListener(clickListener);
            etcFontBroken.setOnPreferenceClickListener(clickListener);
            etcWidget.setOnPreferenceClickListener(clickListener);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mActionBar.setTitle(R.string.settings_title);
        }
    }

    public static class FontSettings extends PreferenceFragment {
        private Preference mTextSize;
        private CheckBoxPreference mFontBasic;
        private CheckBoxPreference mFontBDJua;
        private CheckBoxPreference mFontKPDotum;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_settings_font);

            mTextSize = findPreference("settings_key_font_fontsize");
            mTextSize.setSummary(Float.toString(mSharedPref.getFloat("FontSize", Constant.SETTINGS_DEFAULT_VALUE_TEXT_SIZE)));
            mTextSize.setDefaultValue(Float.toString(mSharedPref.getFloat("FontSize", Constant.SETTINGS_DEFAULT_VALUE_TEXT_SIZE)));
            Preference.OnPreferenceClickListener clickListener = new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (preference.getKey().equals("settings_key_font_fontsize")) {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setTitle(R.string.settings_font_fontsize);
                        dialog.setMessage(R.string.settings_dialog_font_context);
                        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fontsize, null);
                        final TextView sizePreview = layout.findViewById(R.id.dialog_font_preview);
                        NumberPicker picker = layout.findViewById(R.id.dialog_font_picker);
                        picker.setMaxValue(30);
                        picker.setMinValue(8);
                        picker.setValue((int) mSharedPref.getFloat("FontSize", Constant.SETTINGS_DEFAULT_VALUE_TEXT_SIZE));
                        sizePreview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, picker.getValue());
                        picker.setOnScrollListener(new NumberPicker.OnScrollListener() {
                            @Override
                            public void onScrollStateChange(NumberPicker picker, int scrollState) {
                                sizePreview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, picker.getValue());
                            }
                        });
                        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                            @Override
                            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                if (oldVal != newVal) {
                                    if (mSharedPrefEditor == null)
                                        mSharedPrefEditor = mSharedPref.edit();
                                    mSharedPrefEditor.putFloat("FontSize", newVal);
                                    mSharedPrefEditor.apply();
                                    sizePreview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, picker.getValue());
                                    mTextSize.setSummary(Integer.toString(newVal));
                                }
                            }
                        });
                        dialog.setView(layout);
                        dialog.setPositiveButton(R.string.settings_dialog_info_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case AlertDialog.BUTTON_POSITIVE:
                                        break;
                                }
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                    return false;
                }
            };
            mTextSize.setOnPreferenceClickListener(clickListener);

            Preference.OnPreferenceChangeListener changeListener = new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String key = preference.getKey();
                    switch (key) {
                        case "settings_key_font_basic":
                            mFontBasic.setChecked(true);
                            mFontBDJua.setChecked(false);
                            mFontKPDotum.setChecked(false);
                            mFontStyle = Constant.FontType.Default.getValue();
                            break;
                        case "settings_key_font_baedaljua":
                            mFontBasic.setChecked(false);
                            mFontBDJua.setChecked(true);
                            mFontKPDotum.setChecked(false);
                            mFontStyle = Constant.FontType.BaeDal_JUA.getValue();
                            break;
                        case "settings_key_font_kopubdot":
                            mFontBasic.setChecked(false);
                            mFontBDJua.setChecked(false);
                            mFontKPDotum.setChecked(true);
                            mFontStyle = Constant.FontType.KOPUB_Dotum.getValue();
                            break;
                    }
                    return false;
                }
            };

            mFontBasic = (CheckBoxPreference)findPreference("settings_key_font_basic");
            mFontBasic.setOnPreferenceChangeListener(changeListener);
            mFontBDJua = (CheckBoxPreference)findPreference("settings_key_font_baedaljua");
            mFontBDJua.setOnPreferenceChangeListener(changeListener);
            mFontKPDotum = (CheckBoxPreference)findPreference("settings_key_font_kopubdot");
            mFontKPDotum.setOnPreferenceChangeListener(changeListener);

            if (mFontStyle == Constant.FontType.BaeDal_JUA.getValue()) {
                mFontBasic.setChecked(false);
                mFontBDJua.setChecked(true);
                mFontKPDotum.setChecked(false);
            } else if (mFontStyle == Constant.FontType.KOPUB_Dotum.getValue()) {
                mFontBasic.setChecked(false);
                mFontBDJua.setChecked(false);
                mFontKPDotum.setChecked(true);
            } else {
                mFontBasic.setChecked(true);
                mFontBDJua.setChecked(false);
                mFontKPDotum.setChecked(false);
            }
            setRetainInstance(true);
        }

        @Override
        public void onPause() {
            super.onPause();
            if (mSharedPrefEditor == null)
                mSharedPrefEditor = mSharedPref.edit();
            mSharedPrefEditor.putInt(Constant.APP_FONT, mFontStyle);
            mSharedPrefEditor.apply();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mTextSize = null;
            mFontBasic = null;
            mFontBDJua = null;
            mFontKPDotum = null;
        }
    }

    public static class Security extends PreferenceFragment {
        // todo 비밀번호 암호화하여 처리하기 구현

        private CheckBoxPreference mSetSecurity;
        private CheckBoxPreference mSetPasswordApp;
        private CheckBoxPreference mSetPasswordFile;
        private Preference mResetPassword;
        private Preference mBackupKey;

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case Constant.REQUEST_CODE_PASSWORD: {
                        mSetPasswordApp.setEnabled(true);
                        mSetPasswordFile.setEnabled(true);
                        mResetPassword.setEnabled(true);
                        break;
                    }
                }
            }
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_settings_security);

            mSetSecurity = (CheckBoxPreference)findPreference("settings_key_password_enabled");
            mSetPasswordApp = (CheckBoxPreference)findPreference("settings_key_password_app");
            mSetPasswordFile = (CheckBoxPreference)findPreference("settings_key_password_file");
            mResetPassword = findPreference("settings_key_password_reset");
            mBackupKey = findPreference("settings_key_password_key_backup");

            TestLog.Tag("Security").Logging(TestLog.LogType.ERROR, "APP_PASSWORD_FILE_SET: " + mSharedPref.getBoolean(Constant.APP_PASSWORD_SET, false));
            if (mSharedPref.getBoolean(Constant.APP_PASSWORD_SET, false)) {
                mSetPasswordApp.setEnabled(true);
                mSetPasswordFile.setEnabled(true);
                mResetPassword.setEnabled(true);
            } else {
                mSetPasswordApp.setEnabled(false);
                mSetPasswordFile.setEnabled(false);
                mResetPassword.setEnabled(false);
            }

            Preference.OnPreferenceClickListener clickListener = new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String key = preference.getKey();
                    switch (key)
                    {
                        case "settings_key_password_enabled": {
                            if (mSharedPref.getBoolean(Constant.APP_PASSWORD_SET, false)) {
                                mSetPasswordApp.setEnabled(false);
                                mSetPasswordFile.setEnabled(false);
                                mResetPassword.setEnabled(false);
                                mSharedPrefEditor.putBoolean(Constant.APP_PASSWORD_SET, false);
                                mSharedPrefEditor.apply();
                            } else {
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), PasswordActivity.class);
                                intent.putExtra(Constant.INTENT_EXTRA_PASSWORD, Constant.PasswordIntentType.Set.getValue());
                                startActivityForResult(intent, Constant.REQUEST_CODE_PASSWORD);
                                getActivity().overridePendingTransition(0, 0);
                            }
                            break;
                        }
                        case "settings_key_password_app": {
                            mSharedPrefEditor.putBoolean(Constant.APP_PASSWORD_SET_APP, mSetPasswordApp.isChecked());
                            mSharedPrefEditor.apply();
                            break;
                        }
                        case "settings_key_password_file": {
                            mSharedPrefEditor.putBoolean(Constant.APP_PASSWORD_SET_FILE, mSetPasswordFile.isChecked());
                            mSharedPrefEditor.apply();
                            break;
                        }
                        case "settings_key_password_reset": {
                            Intent intent = new Intent();
                            intent.setClass(getActivity(), PasswordActivity.class);
                            intent.putExtra(Constant.INTENT_EXTRA_PASSWORD, Constant.PasswordIntentType.Reset.getValue());
                            startActivity(intent);
                            getActivity().overridePendingTransition(0, 0);
                            break;
                        }
                        case "settings_key_password_key_backup": {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                            dialog.setTitle(R.string.settings_expfunc_security_password_key_backup_title);
                            dialog.setMessage(R.string.settings_expfunc_security_password_key_backup_dialog);
                            dialog.setPositiveButton(R.string.paint_dialog_yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String key = mSharedPref.getString(Constant.APP_PASSWORD_KEY, Constant.APP_PASSWORD_KEY_DEFAULT);
                                    if (LogManager.saveKey(key)) {
                                        Snackbar.make(mSettingsView, R.string.settings_expfunc_security_password_key_backup_success, Snackbar.LENGTH_LONG).show();
                                    } else {
                                        Snackbar.make(mSettingsView, R.string.settings_expfunc_security_password_key_backup_fail, Snackbar.LENGTH_LONG).show();
                                    }

                                    dialog.dismiss();
                                }
                            });
                            dialog.setNegativeButton(R.string.paint_dialog_no, null);
                            dialog.show();
                            break;
                        }
                    }
                    return false;
                }
            };
            mSetSecurity.setOnPreferenceClickListener(clickListener);
            mSetPasswordApp.setOnPreferenceClickListener(clickListener);
            mSetPasswordFile.setOnPreferenceClickListener(clickListener);
            mResetPassword.setOnPreferenceClickListener(clickListener);
            mBackupKey.setOnPreferenceClickListener(clickListener);

            if (!BuildConfig.VERSION_NAME.equals("Dev_Build")) {
                mSetPasswordFile.setEnabled(false);
                mSetPasswordFile.setSummary(R.string.test_preparing);
            }
        }

    }

    public static class Settings extends PreferenceFragment {
        private CheckBoxPreference mAdMob;
        private CheckBoxPreference mViewImage;
        private Preference mEnhanceIOLine;
        private CheckBoxPreference mSwipeDelete;

        private long mPressTime = 0;

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case Constant.REQUEST_CODE_PASSWORD: {
                        getFragmentManager().beginTransaction().replace(android.R.id.content, mSecurityAct).commit();
                        mActiveScene = ActiveScreenType.Security;
                        break;
                    }
                }
            }
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_settings);

            final Preference info = findPreference("settings_key_info");
            Preference version = findPreference("settings_key_version");
            Preference font = findPreference("settings_key_font");
            Preference bugReport = findPreference("settings_key_bugreport");
            Preference help = findPreference("settings_key_help");
            Preference updateList = findPreference("settings_key_updatelist");
            mAdMob = (CheckBoxPreference) findPreference("settings_key_admob");
            mEnhanceIOLine = findPreference("settings_key_enhanceIO_Line");
            mViewImage = (CheckBoxPreference) findPreference("settings_key_viewimage");
            Preference license = findPreference("settings_key_license");
            Preference security = findPreference("settings_key_security");
            mSwipeDelete = (CheckBoxPreference)findPreference("settings_key_swipe_delete");

            Preference.OnPreferenceClickListener clickListener = new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String key = preference.getKey();
                    switch (key) {
                        case "settings_key_info": {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                            dialog.setTitle(getResources().getString(R.string.settings_dialog_info_title));
                            String copyright = getResources().getString(R.string.settings_dialog_info_copyright);
                            String license = getResources().getString(R.string.settings_dialog_info_license);
                            String icons = getResources().getString(R.string.settings_dialog_info_icon);
                            String homepage = getResources().getString(R.string.settings_dialog_info_homepage);
                            String line = "\n";

                            dialog.setMessage(copyright + line + license + line + icons + line + homepage);
                            dialog.setPositiveButton(R.string.settings_dialog_info_ok, null);
                            dialog.show();
                            break;
                        }
                        case "settings_key_font": {
                            getFragmentManager().beginTransaction().replace(android.R.id.content, mFontSettingAct).commit();
                            mActiveScene = ActiveScreenType.Font;
                            break;
                        }
                        case "settings_key_bugreport": {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            String[] address = {Constant.APP_DEV_MAILADDRESS};
                            intent.setType("message/rfc822");
                            intent.putExtra(Intent.EXTRA_EMAIL, address);
                            intent.putExtra(Intent.EXTRA_SUBJECT, "");
                            intent.putExtra(Intent.EXTRA_TEXT, "");
                            startActivity(Intent.createChooser(intent, getString(R.string.settings_information_bug_email_choose)));
                            break;
                        }
                        case "settings_key_help": {
                            getFragmentManager().beginTransaction().replace(android.R.id.content, mHelpAct).commit();
                            mActiveScene = ActiveScreenType.Help;
                            break;
                        }
                        case "settings_key_updatelist": {
                            Intent intent = new Intent();
                            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.setClass(getActivity(), UpdateListActivity.class);
                            startActivity(intent);
                            getActivity().overridePendingTransition(0, 0);
                            break;
                        }
                        case "settings_key_version": {
                            if (System.currentTimeMillis() > mPressTime + 1000) {
                                mPressTime = System.currentTimeMillis();
                                return false;
                            }
                            if (System.currentTimeMillis() <= mPressTime + 1000) {
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), HiddenActivity.class);
                                startActivity(intent);
                                getActivity().overridePendingTransition(0, 0);
                            }
                            break;
                        }
                        case "settings_key_enhanceIO_Line": {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                            dialog.setTitle(getResources().getString(R.string.settings_expfunc_enhanceIO_Line_title));
                            dialog.setMessage(getResources().getString(R.string.settings_dialog_expfunc_enhanceIO_Line));
                            NumberPicker picker = new NumberPicker(getActivity());
                            picker.setMaxValue(500);
                            picker.setMinValue(10);
                            picker.setValue(mSharedPref.getInt(Constant.APP_TEXT_LINES, Constant.SETTINGS_DEFAULT_VALUE_TEXT_LINES));
                            picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                                @Override
                                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                                    if (oldVal != newVal) {
                                        if (mSharedPrefEditor == null)
                                            mSharedPrefEditor = mSharedPref.edit();
                                        mSharedPrefEditor.putInt(Constant.APP_TEXT_LINES, newVal);
                                        mSharedPrefEditor.apply();
                                    }
                                }
                            });
                            dialog.setView(picker);
                            dialog.setPositiveButton(getResources().getString(R.string.settings_dialog_info_ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case AlertDialog.BUTTON_POSITIVE:
                                            break;
                                    }
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                            break;
                        }
                        case "settings_key_license": {
                            final Notices notices = new Notices();
                            notices.addNotice(new Notice("OpenPad", "http://skyvvv624.blog.me", "OpenPad License", new OpenpadLicense()));
                            notices.addNotice(new Notice("Glide 4", "https://github.com/bumptech/glide", "Glide 4 License", new GlideLicense()));
                            notices.addNotice(new Notice("BM-JUA", "http://www.woowahan.com", "BM-JUA License", new BmJUALicense()));
                            notices.addNotice(new Notice("KOPUB Dotum", "http://www.kopus.org", "KOPUB Dotum License", new KOPUBLicense()));
                            notices.addNotice(new Notice("FloatingActionButton", "https://github.com/PSDev/LicensesDialog", "FloatingActionButton License", new FloatingActionButtonLicense()));
                            notices.addNotice(new Notice("Licenses Dialog", "https://github.com/PSDev/LicensesDialog", "Licenses Dialog License", new LicensesDialogLicense()));
                            notices.addNotice(new Notice("Apache Commons Codec", "http://commons.apache.org/", "Apache Commons Codec License", new ACCLicense()));
                            notices.addNotice(new Notice("PassCodeView", "https://github.com/Arjun-sna/android-passcodeview", "PassCodeView License", new PassCodeViewLicense()));
                            new LicensesDialog.Builder(getActivity())
                                    .setNotices(notices)
                                    .setShowFullLicenseText(false)
                                    .setIncludeOwnLicense(false)
                                    .build()
                                    .show();
                            break;
                        }
                        case "settings_key_security": {
                            if (mSharedPref.getBoolean(Constant.APP_PASSWORD_SET, false)) {
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), PasswordActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.putExtra(Constant.INTENT_EXTRA_PASSWORD, Constant.PasswordIntentType.Execute.getValue());
                                startActivityForResult(intent, Constant.REQUEST_CODE_PASSWORD);
                            }
                            getFragmentManager().beginTransaction().replace(android.R.id.content, mSecurityAct).commit();
                            mActiveScene = ActiveScreenType.Security;
                            break;
                        }
                    }
                    return false;
                }
            };

            CheckBoxPreference.OnPreferenceClickListener checkClickListener = new CheckBoxPreference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    String key = preference.getKey();
                    switch (key) {
                        case "settings_key_admob": {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                            dialog.setTitle(R.string.settings_general_admob);
                            dialog.setMessage(R.string.settings_general_admob_dialog_context);
                            dialog.setPositiveButton(R.string.settings_dialog_info_ok, null);
                            dialog.show();
                            break;
                        }
                    }
                    return false;
                }
            };

            info.setOnPreferenceClickListener(clickListener);
            font.setOnPreferenceClickListener(clickListener);
            bugReport.setOnPreferenceClickListener(clickListener);
            help.setOnPreferenceClickListener(clickListener);
            updateList.setOnPreferenceClickListener(clickListener);
            version.setOnPreferenceClickListener(clickListener);
            version.setSummary(BuildConfig.VERSION_NAME);
            mEnhanceIOLine.setOnPreferenceClickListener(clickListener);
            license.setOnPreferenceClickListener(clickListener);
            mAdMob.setOnPreferenceClickListener(checkClickListener);
            security.setOnPreferenceClickListener(clickListener);

            setRetainInstance(true);
        }

        @Override
        public void onResume() {
            super.onResume();
            mFontStyle = mSharedPref.getInt(Constant.APP_FONT, Constant.FontType.Default.getValue());

            if (mPrevFontStyle != mFontStyle) {
                mPrevFontStyle = mFontStyle;
            }
            mAdMob.setChecked(mSharedPref.getBoolean(Constant.APP_ADMOB_VISIBLE, true));
            mSwipeDelete.setChecked(mSharedPref.getBoolean(Constant.APP_SWIPE_DELETE_PREFERENCE, true));
        }

        @Override
        public void onPause() {
            super.onPause();
            if (mSharedPrefEditor == null)
                mSharedPrefEditor = mSharedPref.edit();
            mSharedPrefEditor.putBoolean(Constant.APP_ADMOB_VISIBLE, mAdMob.isChecked());
            mSharedPrefEditor.putBoolean(Constant.APP_VIEW_IMAGE, mViewImage.isChecked());
            mSharedPrefEditor.putBoolean(Constant.APP_SWIPE_DELETE_PREFERENCE, mSwipeDelete.isChecked());
            mSharedPrefEditor.apply();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mAdMob = null;
            mEnhanceIOLine = null;
            mViewImage = null;
        }
    }

    private void setupActionBar() {
        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayShowHomeEnabled(true);
            mActionBar.setTitle(R.string.settings_title);
        }
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || FontSettings.class.getName().equals(fragmentName)
                || Settings.class.getName().equals(fragmentName)
                || Help.class.getName().equals(fragmentName)
                || Security.class.getName().equals(fragmentName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();

        mSettingsView = SettingsActivity.this.getListView();
        mHelpAct = new Help();
        mFontSettingAct = new FontSettings();
        mSettingsAct = new Settings();
        mSecurityAct = new Security();

        getFragmentManager().beginTransaction().replace(android.R.id.content, mSettingsAct).commit();
        mActiveScene = ActiveScreenType.Main;

        mSharedPref = getSharedPreferences(Constant.APP_SETTINGS_PREFERENCE, MODE_PRIVATE);

        mFontStyle = mSharedPref.getInt(Constant.APP_FONT, Constant.FontType.Default.getValue());
        mPrevFontStyle = mFontStyle;
        Context contextThis = getApplicationContext();
        int font = mSharedPref.getInt(Constant.APP_FONT, Constant.FontType.Default.getValue());
        if (font == Constant.FontType.BaeDal_JUA.getValue()) {
            Typekit.getInstance().addNormal(Typekit.createFromAsset(contextThis, "fonts/bmjua.ttf"))
                    .addBold(Typekit.createFromAsset(contextThis, "fonts/bmjua.ttf"));
        } else if (font == Constant.FontType.KOPUB_Dotum.getValue()) {
            Typekit.getInstance().addNormal(Typekit.createFromAsset(contextThis, "fonts/kopub_dotum_medium.ttf"))
                    .addBold(Typekit.createFromAsset(contextThis, "fonts/kopub_dotum_medium.ttf"));
        } else {
            Typekit.getInstance().addNormal(Typeface.DEFAULT).addBold(Typeface.DEFAULT_BOLD);
        }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        switch (mActiveScene) {
            case Main:
                super.onBackPressed();
                overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
                break;
            default:
                getFragmentManager().beginTransaction().replace(android.R.id.content, new Settings()).commit();
                mActiveScene = ActiveScreenType.Main;
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHelpAct = null;
        mFontSettingAct = null;
        mSettingsAct = null;
        mSharedPref = null;
        mSharedPrefEditor = null;
        mActionBar = null;
        mSettingsView = null;
    }
}
