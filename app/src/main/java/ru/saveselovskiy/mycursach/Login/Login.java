package ru.saveselovskiy.mycursach.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import ru.saveselovskiy.mycursach.MainActivity;
import ru.saveselovskiy.mycursach.R;

/**
 * Created by Admin on 27.03.2015.
 */
public class Login extends Activity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        Button btn = (Button) findViewById(R.id.LoginButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),MainActivity.class);
                startActivity(intent);
                int i = 0;
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplication(),MainActivity.class);
        startActivity(intent);
    }
}
