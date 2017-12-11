package com.eskeptor.openTextViewer.datatype;

import android.content.Context;
import com.eskeptor.openTextViewer.Constant;
import com.eskeptor.openTextViewer.R;

import java.io.File;

/**
 * Created by eskeptor on 17. 2. 2.
 * Copyright (C) 2017 Eskeptor(Jeon Ye Chan)
 */

// 폴더를 리스트에 표현하기 위한 클래스
public class FolderObject {
    // get, set을 이용하면 좋지만 직접접근이 덜 부하를 준다.
    public String mFolderName;
    public String mFolderPath;
    public int mFileCountInFolder;
    public int mFolderType;

    public FolderObject(final String _name, final int _count, final int _type, final Context _context) {
        if (!_name.equals(Constant.FOLDER_DEFAULT_NAME) && !_name.equals(Constant.FOLDER_WIDGET_NAME)) {
            this.mFolderName = _name;
        } else {
            if (_name.equals(Constant.FOLDER_WIDGET_NAME))
                this.mFolderName = _context.getResources().getString(R.string.folder_widget);
            else
                this.mFolderName = _context.getResources().getString(R.string.folder_default);
        }

        this.mFileCountInFolder = _count;
        this.mFolderType = _type;

        if (_context != null) {
            mFolderPath = Constant.APP_INTERNAL_URL + File.separator + _name;
        }
    }
}