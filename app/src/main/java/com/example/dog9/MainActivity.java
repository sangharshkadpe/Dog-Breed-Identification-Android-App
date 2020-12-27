package com.example.dog9;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int mInputSize = 224;
    private String mModelPath = "model.tflite";
    private String mLabelPath = "labels.txt";
    private  Classifier classifier;
    private Bitmap bitmap;

    ImageView imageView;
    Uri imageuri;
    Button buclassify,buselect,bucapture;
    TextView classitext;
    private static final int PICK_IMAGE = 100;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView=(ImageView)findViewById(R.id.imageView2);
        buclassify=(Button)findViewById(R.id.button3);
        classitext=(TextView)findViewById(R.id.textView);
        buselect=(Button)findViewById(R.id.button2);
        bucapture=(Button)findViewById(R.id.button);

        buselect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        bucapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 13);
            }
        });

        try {
            classifier = new Classifier(getAssets(), mModelPath, mLabelPath, mInputSize);
        } catch (IOException e) {
            e.printStackTrace();
        }


        buclassify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



          try{
                List<Classifier.Recognition> result = classifier.recognizeImage(bitmap);
                classitext.setText(result.get(0).toString());

            }catch (Exception E){
              Toast.makeText(getApplicationContext(),"please SELECT or CAPTURE  image to PREDICT",Toast.LENGTH_SHORT).show();}}
        });


        }





    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, 12);
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==12 && resultCode==RESULT_OK && data!=null) {
            imageuri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == 13 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(bitmap);



        }
    }




}



