package com.example.brochure.util;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

import org.nuxeo.onedrive.client.OneDriveAPI;
import org.nuxeo.onedrive.client.OneDriveAPIException;
import org.nuxeo.onedrive.client.OneDriveBasicAPI;
import org.nuxeo.onedrive.client.OneDriveBusinessAPI;
import org.nuxeo.onedrive.client.OneDriveFolder;

import static android.content.Context.DOWNLOAD_SERVICE;

public class DownloadConfigurationFile {

    public static void execute () {
        OneDriveAPI api = new OneDriveBusinessAPI("http://aibtaus-my.sharepoint.com/:u:/g/personal/app_developer_aibtglobal_edu_au/EbyLVmc7D3BCmfqH_CD_fEsBucrx3AQVdgCNsRSunadYzA?e=qsa8CO", "a76ec95b-c018-4d8d-9075-dae7a7b1cee5");
        OneDriveFolder folder = OneDriveFolder.getRoot(api);
        try {
            OneDriveFolder.Metadata folderMetadata = folder.getMetadata();
        } catch (OneDriveAPIException e) {
            e.printStackTrace();
        }


    }
}
