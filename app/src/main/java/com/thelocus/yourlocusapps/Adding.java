package com.thelocus.yourlocusapps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Adding extends AppCompatActivity {


    private String Description, Stock, Price, Pname, CurrentDates, CurrentTimes;
    private Button AddNewProduct;
    private ImageView InputImage;
    private EditText InputName, InputDesc, InputPrice, InputStock;
    private static final int Picture = 1;
    private Uri ImageUri;
    private String productRandom, downloadimageurl;
    private StorageReference imagesRef;
    private DatabaseReference stockRef;
    private ProgressDialog loadingScreen;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding);

        imagesRef = FirebaseStorage.getInstance().getReference().child("Product Image");
        stockRef = FirebaseDatabase.getInstance().getReference().child("Product Stock");

        AddNewProduct = (Button) findViewById(R.id.addNew);
        InputImage = (ImageView) findViewById(R.id.addImage);
        InputName = (EditText) findViewById(R.id.productsname);
        InputDesc = (EditText) findViewById(R.id.productsdescription);
        InputPrice = (EditText) findViewById(R.id.productsprice);
        InputStock = (EditText) findViewById(R.id.productsstock);
        loadingScreen = new ProgressDialog(this);

        InputImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                GalleryOpen();
            }
        });

        AddNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ValidateProduct();
            }
        });



    }



    private void GalleryOpen()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Picture);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Picture && resultCode == RESULT_OK && data!= null)
        {
            ImageUri = data.getData();
            InputImage.setImageURI(ImageUri);
        }
    }

    private void ValidateProduct()
    {
        Description = InputDesc.getText().toString();
        Price = InputPrice.getText().toString();
        Pname = InputName.getText().toString();
        Stock = InputStock.getText().toString();

        if (ImageUri == null)
        {
            Toast.makeText(this, "Product image is null...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please write the Description....", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Price))
        {
            Toast.makeText(this, "Please write the Price....", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Pname))
        {
            Toast.makeText(this, "Please write the name....", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Stock))
        {
            Toast.makeText(this, "Please write the Stock....", Toast.LENGTH_SHORT).show();
        }
        else
        {
            InformationStock();
        }


    }

    private void InformationStock()
    {
        loadingScreen.setTitle("Add new Stock");
        loadingScreen.setMessage("Please wait, while we are adding the stock.");
        loadingScreen.setCanceledOnTouchOutside(false);
        loadingScreen.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        CurrentDates = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        CurrentTimes = currentTime.format(calendar.getTime());

        productRandom = CurrentDates + CurrentTimes;

        final StorageReference filepath = imagesRef.child(ImageUri.getLastPathSegment() + productRandom + ".jpg");

        final UploadTask uploadTask = filepath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) 
            {
                String message = e.toString();
                Toast.makeText(Adding.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingScreen.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(Adding.this, " Stock Image upload Sucessfully", Toast.LENGTH_SHORT).show();

                Task<Uri> urltask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw  task.getException();
                        }

                        downloadimageurl = filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if(task.isSuccessful())
                        {
                            downloadimageurl = task.getResult().toString();

                            Toast.makeText(Adding.this, "got image Url Sucessfully..", Toast.LENGTH_SHORT).show();

                            SaveProductInfo();
                        }
                    }
                });
            }
        });
    }

    private void SaveProductInfo()
    {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandom);
        productMap.put("date", CurrentDates);
        productMap.put("time", CurrentTimes);
        productMap.put("description", Description);
        productMap.put("image", downloadimageurl);
        productMap.put("price", Price);
        productMap.put("stock", Stock);
        productMap.put("pname", Pname);

        stockRef.child(productRandom).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task)
            {
                if (task.isSuccessful())
                {
                    Intent intent = new Intent(Adding.this, Home.class);
                    startActivity(intent);

                    loadingScreen.dismiss();
                    Toast.makeText(Adding.this, "Product is added sucessfully..", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingScreen.dismiss();
                    String message = task.getException().toString();
                    Toast.makeText(Adding.this, "Error " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


}