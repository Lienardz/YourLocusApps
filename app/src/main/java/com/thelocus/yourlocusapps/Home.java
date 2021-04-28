package com.thelocus.yourlocusapps;

import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Home extends AppCompatActivity {

    private ImageButton Logoutbtn, Addbtn, displaybtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Logoutbtn = (ImageButton) findViewById(R.id.logout);
        Addbtn = (ImageButton) findViewById(R.id.add);
        displaybtn = (ImageButton) findViewById(R.id.display);

        Logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Paper.book().destroy();

                Intent intent = new Intent(Home.this, Login.class);
                startActivity(intent);
            }
        });

        Addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Adding.class);
                startActivity(intent);
            }
        });

        displaybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, Display.class);
                startActivity(intent);
            }
        });
    }
}