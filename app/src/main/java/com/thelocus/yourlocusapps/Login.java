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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;
import com.thelocus.yourlocusapps.Model.Users;
import com.thelocus.yourlocusapps.Prevalent.Prevalent;


public class Login extends AppCompatActivity {

    private EditText Identify, Password;
    private Button LoginAccount;
    private ProgressDialog loadingScreen;

    private String DBname = "Users";
    private CheckBox RememberMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginAccount = (Button) findViewById(R.id.GosignIn);
        Password = (EditText) findViewById(R.id.loginpassword);
        Identify = (EditText) findViewById(R.id.loginID);
        loadingScreen = new ProgressDialog(this);



        RememberMe = (CheckBox) findViewById(R.id.ingatsaya);
        Paper.init(this);

        LoginAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                UserLogin();
            }
        });

    }

    private void UserLogin()
    {
        String Identified = Identify.getText().toString();
        String password = Password.getText().toString();

        if (TextUtils.isEmpty(Identified))
        {
            Toast.makeText(this, "Write your ID!", Toast.LENGTH_SHORT).show();
        }


        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Write your Password!", Toast.LENGTH_SHORT).show();
        }

        else {
            loadingScreen.setTitle("Login Account");
            loadingScreen.setMessage("Please wait, while we are checking.");
            loadingScreen.setCanceledOnTouchOutside(false);
            loadingScreen.show();

            AllowAccess(Identified, password);
        }

    }

    private void AllowAccess (final String identified, final String password)
    {
        if (RememberMe.isChecked())
        {
            Paper.book().write(Prevalent.UserIDKey, identified);
            Paper.book().write(Prevalent.UserPasswordKey, password);
        }


        final  DatabaseReference Ref;
        Ref = FirebaseDatabase.getInstance().getReference();

        Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (snapshot.child(DBname).child(identified).exists())
                {
                    Users datauser = snapshot.child(DBname).child(identified).getValue(Users.class);

                    if (datauser.getID().equals(identified))
                    {
                        if (datauser.getPassword().equals(password))
                        {
                            StyleableToast.makeText(Login.this, "Login was Sucessfull", R.style.customToast).show();
                            loadingScreen.dismiss();

                            Intent intent = new Intent(Login.this, Home.class);
                            startActivity(intent);
                        }
                        else
                        {
                            StyleableToast.makeText(Login.this, "Password was Incorrect", R.style.customToast).show();
                            loadingScreen.dismiss();
                        }
                    }
                }
                else
                {
                    Toast.makeText(Login.this, "Sorry, Not Available", Toast.LENGTH_SHORT).show();
                    loadingScreen.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}