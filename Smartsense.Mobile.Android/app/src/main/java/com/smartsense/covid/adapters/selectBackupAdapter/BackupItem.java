package com.smartsense.covid.adapters.selectBackupAdapter;

import androidx.annotation.Keep;

@Keep
public class BackupItem {
    private String backupName;
    private String path;

    public String getBackupName() {
        return backupName;
    }

    public void setBackupName(String backupName) {
        this.backupName = backupName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
