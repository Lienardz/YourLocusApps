package com.thelocus.yourlocusapps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.Calendar;
import java.util.HashMap;

public class Register extends AppCompatActivity {

    private Button CreateAccount;
    private EditText Name, Password, ReEnterPassword, Identify;
    private ProgressDialog loadingScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        CreateAccount = (Button) findViewById(R.id.GosignUp);
        Name = (EditText) findViewById(R.id.names);
        Password = (EditText) findViewById(R.id.passwordsignup);
        ReEnterPassword = (EditText) findViewById(R.id.reenterpassword);
        Identify = (EditText) findViewById(R.id.signupID);
        loadingScreen = new ProgressDialog(this);

        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });
    }

    private void CreateAccount() {
        String nama = Name.getText().toString();
        String Identified = Identify.getText().toString();
        String password = Password.getText().toString();
        String reenter = ReEnterPassword.getText().toString();

        if (TextUtils.isEmpty(nama))
        {
            Toast.makeText(this, "Write your name!", Toast.LENGTH_SHORT).show();
        }


        else if (TextUtils.isEmpty(Identified))
        {
            Toast.makeText(this, "Write your ID!", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Write your password!", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(reenter))
        {
            Toast.makeText(this, "Write your password!", Toast.LENGTH_SHORT).show();
        }

        else if(password.length()<6 || password.length()>20)
        {
            Toast.makeText(this, "Containt password must be 6-20 characters", Toast.LENGTH_SHORT).show();
        }

        else {
            loadingScreen.setTitle("Create Account");
            loadingScreen.setMessage("Please wait, while we are checking.");
            loadingScreen.setCanceledOnTouchOutside(false);
            loadingScreen.show();

            ValidateID(nama,Identified,password,reenter);
        }

    }

    private void ValidateID(final String nama,  final String Identified, final String password, String reenter)
    {
        final DatabaseReference Ref;
        Ref = FirebaseDatabase.getInstance().getReference();

        Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (!(snapshot.child("Users").child(Identified).exists()))
                {
                    HashMap<String, Object> userData = new HashMap<>();
                    userData.put("Name", nama);
                    userData.put("ID", Identified);
                    userData.put("Password", password);

                    Ref.child("Users").child(Identified).updateChildren(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful()){
                                StyleableToast.makeText(Register.this, "Your Account has been Created!", R.style.customToast).show();
                                loadingScreen.dismiss();

                                Intent intent = new Intent(Register.this, Login.class);
                                startActivity(intent);
                            }
                            
                            else{
                                loadingScreen.dismiss();
                                Toast.makeText(Register.this, "Network Error. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(Register.this, "This ID already exists.", Toast.LENGTH_SHORT).show();
                    loadingScreen.dismiss();
                    Toast.makeText(Register.this, "Please use another ID.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Register.this, LoginSignUp.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}