package com.airbnb.deeplinkdispatch;

import android.content.Context;
import android.util.Log;

import com.airbnb.deeplinkdispatch.base.MatchIndex;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {

    private static final String TAG = "Utils";

    public static byte[] readMatchIndex(Context context, String moduleName) {
        byte[] array = null;
        try {
            BufferedInputStream is = new BufferedInputStream(
                context.getAssets().open(MatchIndex.getMatchIdxFileName(moduleName)));
            array = getBytes(is);
        } catch (IOException e) {
            Log.e(TAG, "Error reading match index.", e);
        }
        return array;
    }

    private static byte[] getBytes(InputStream is) throws IOException {

        int read;
        int bufferSize = 8192; // Default buffer size of underlying impelmentation
        byte[] buffer = new byte[bufferSize];

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((read = is.read(buffer, 0, bufferSize)) != -1) {
            bos.write(buffer, 0, read);
        }

        return bos.toByteArray();
    }

}
