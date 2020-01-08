package com.example.naver_cfr_testproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE1 = 0;
    private static final int REQUEST_CODE2 = 1;
    ImageView img_file1;
    ImageView img_file2;

    Button Start_button;
    Button getImageBtn1;
    Button getImageBtn2;

    private String imgPath_other = null;    //비교할사진 파일 경로
    private String imgPath_me = null;       //내사진 파일 경로

    Intent intent_faceRecognition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Start_button = findViewById(R.id.btn_start);
        img_file1 = findViewById(R.id.img_file1);//비교할 사진
        img_file2 = findViewById(R.id.img_file2);//내사진
        getImageBtn1 = findViewById(R.id.getImageBtn);  //비교할사진 가져오기
        getImageBtn2 = findViewById(R.id.getImageBtn2); //내사진 가져오기

        intent_faceRecognition = new Intent(getApplicationContext(),faceRecognition.class);

        setOnClickToButton();
    }

    private void setOnClickToButton() {

        getImageBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE1);
            }
        });
        getImageBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE2);
            }
        });
        Start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(intent_faceRecognition);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE1) {         //비교할사진 세팅
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();

                    img_file1.setImageBitmap(img);

                    String tempFileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                    imgPath_other = saveBitmapToJpeg(this, img, tempFileName);


                    intent_faceRecognition.putExtra("ImgPath_other", imgPath_other);

                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == REQUEST_CODE2) {             //내사진 세팅
            if (resultCode == RESULT_OK) {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();

                    img_file2.setImageBitmap(img);

                    String tempFileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                    imgPath_me = saveBitmapToJpeg(this, img, tempFileName);


                    intent_faceRecognition.putExtra("ImgPath_me", imgPath_me);

                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }

    public static String saveBitmapToJpeg(Context context, Bitmap bitmap, String name){
        File storage = context.getCacheDir(); // 이 부분이 임시파일 저장 경로
        String fileName = name + ".jpg";  // 파일이름은 마음대로!
        File tempFile = new File(storage, fileName);

        try{
            tempFile.createNewFile();  // 파일을 생성해주고
            FileOutputStream out = new FileOutputStream(tempFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90 , out);  // 넘거 받은 bitmap을 jpeg(손실압축)으로 저장해줌
            out.close(); // 마무리로 닫아줍니다.

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFile.getAbsolutePath();   // 임시파일 저장경로를 리턴해주면 끝!
    }
}
