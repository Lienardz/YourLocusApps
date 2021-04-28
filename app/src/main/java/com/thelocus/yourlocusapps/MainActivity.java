package com.thelocus.yourlocusapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.thelocus.yourlocusapps.Model.Users;
import com.thelocus.yourlocusapps.Prevalent.Prevalent;

public class MainActivity extends AppCompatActivity {

    private ImageButton arrowButton;
    private ProgressDialog loadingScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingScreen = new ProgressDialog(this);

        arrowButton = (ImageButton) findViewById(R.id.arrow);

        Paper.init(this);

        arrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginSignUp.class);
                startActivity(intent);
            }
        });

        String UserIDKey = Paper.book().read(Prevalent.UserIDKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if (UserIDKey != "" && UserPasswordKey != "")
        {
            if (!TextUtils.isEmpty(UserIDKey) && !TextUtils.isEmpty(UserPasswordKey))
            {
                AllowAccess(UserIDKey, UserPasswordKey);

                loadingScreen.setTitle("Already Logged in");
                loadingScreen.setMessage("Please wait......");
                loadingScreen.setCanceledOnTouchOutside(false);
                loadingScreen.show();
            }
        }
    }

    private void AllowAccess(final String identified, final String password)
    {
        final DatabaseReference Ref;
        Ref = FirebaseDatabase.getInstance().getReference();

        Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.child("Users").child(identified).exists())
                {
                    Users datauser = snapshot.child("Users").child(identified).getValue(Users.class);

                    if (datauser.getID().equals(identified))
                    {
                        if (datauser.getPassword().equals(password))
                        {
                            StyleableToast.makeText(MainActivity.this, "Login was Sucessfull", R.style.customToast).show();
                            loadingScreen.dismiss();

                            Intent intent = new Intent(MainActivity.this, Home.class);
                            startActivity(intent);
                        }
                        else
                        {
                            StyleableToast.makeText(MainActivity.this, "Password was Incorrect", R.style.customToast).show();
                            loadingScreen.dismiss();
                        }
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Sorry, Not Available", Toast.LENGTH_SHORT).show();
                    loadingScreen.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}