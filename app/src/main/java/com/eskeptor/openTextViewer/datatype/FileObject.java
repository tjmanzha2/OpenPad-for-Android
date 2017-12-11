package com.eskeptor.openTextViewer.datatype;

import com.eskeptor.openTextViewer.Constant;

import java.io.File;

/**
 * Created by eskeptor on 17. 2. 14.
 * Copyright (C) 2017 Eskeptor(Jeon Ye Chan)
 */

// 파일 목록을 리스트에 표현하기 위한 클래스
public class FileObject {
    // get, set을 이용하면 좋지만 직접접근이 덜 부하를 준다.
    public String mFilePath;
    public String mFileName;
    public Constant.BrowserIconType mIconType;
    public long mFileSize;  // KB 단위

    public FileObject(final File _file) {
        mFilePath = _file.getPath();
        mFileName = _file.getName();

        if (_file.isDirectory()) {
            mIconType = Constant.BrowserIconType.Folder;
        } else if (_file.isFile()) {
            if (_file.length() >= Constant.KILOBYTE * Constant.SAFE_LOAD_CAPACITY &&
                    _file.length() <= Constant.MEGABYTE) {
                mIconType = Constant.BrowserIconType.Over1;
            } else if (_file.length() >= Constant.MEGABYTE) {
                mIconType = Constant.BrowserIconType.Over2;
            } else {
                mIconType = Constant.BrowserIconType.Normal;
            }
        }

        if (_file.length() >= Constant.KILOBYTE) {
            mFileSize = _file.length() / Constant.KILOBYTE;
        } else {
            mFileSize = 1L;
        }
    }
}