package com.eskeptor.openTextViewer.license;

import android.content.Context;

import com.eskeptor.openTextViewer.R;

import de.psdev.licensesdialog.licenses.License;

/**
 * Created by Esk on 2018-03-06.
 *
 */

public class PassCodeViewLicense extends License {
    @Override
    public String getName() {
        return "PassCodeView License";
    }

    @Override
    public String readSummaryTextFromResources(Context context) {
        return getContent(context, R.raw.asl_20_summary);
    }

    @Override
    public String readFullTextFromResources(Context context) {
        return getContent(context, R.raw.asl_20_full);
    }

    @Override
    public String getVersion() {
        return "1.2.1";
    }

    @Override
    public String getUrl() {
        return "https://github.com/Arjun-sna/android-passcodeview";
    }
}
