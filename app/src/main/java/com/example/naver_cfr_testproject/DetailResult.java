package com.example.naver_cfr_testproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.io.File;

public class DetailResult extends AppCompatActivity {

    private String imageData_me;
    private  String imageData_other;
    private String my_age;
    private String my_emotion;
    private String other_age;
    private String other_emotion;
    private String resultText;

    private File imgFile_me;
    private File imgFile_other;

    private ImageView imageView_other;
    private ImageView imageView_me;

    TextView TextView_my_age;
    TextView  TextView_my_emotion;
    TextView  TextView_other_age;
    TextView  TextView_other_emotion;
    TextView TextView_total_result;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailresult);

        imageView_other = findViewById(R.id.img_other);
        imageView_me =findViewById(R.id.img_me);

        TextView_my_age = findViewById(R.id.my_age);
        TextView_my_emotion = findViewById(R.id.my_emotion);
        TextView_other_age = findViewById(R.id.other_age);
        TextView_other_emotion = findViewById(R.id.other_emotion);
        TextView_total_result = findViewById(R.id.total_result);

        Intent intentData = getIntent();

        imageData_me = intentData.getExtras().getString("ImgPath_me");
        imageData_other = intentData.getExtras().getString("ImgPath_other");

        imgFile_me = new File(imageData_me);
        imgFile_other = new File(imageData_other);



        if(imgFile_me.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile_me.getAbsolutePath());
           /* imageView_me.setImageBitmap(myBitmap);*/
        }
        if(imgFile_other.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile_other.getAbsolutePath());
           /* imageView_other.setImageBitmap(myBitmap);*/
        }

        my_age = intentData.getExtras().getString("mydata_age");
        my_emotion = intentData.getExtras().getString("mydata_emotion");
        other_age = intentData.getExtras().getString("otherdata_age");
        other_emotion = intentData.getExtras().getString("otherdata_emotion");

        if(other_emotion == my_emotion){
            resultText = "비슷한 표정을 짓고있네요";
        }
        else{
            resultText = "조금 다른 표정을 짓고있어요 비슷하게 다시 따라해보세요 ^^";
        }

        TextView_total_result.setText(resultText);
        TextView_my_age.setText(my_age);
        TextView_my_emotion.setText(my_emotion);
        TextView_other_age.setText(other_age);
        TextView_other_emotion.setText(other_emotion);
    }
}
