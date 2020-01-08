package com.example.naver_cfr_testproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class faceRecognition extends AppCompatActivity {
    private String imageData_me;
    private  String imageData_other;

    private File imgFile_me;
    private File imgFile_other;

    private ImageView imageView_other;
    private ImageView imageView_me;

    TextView meFaceCount;
    TextView otherFaceCount;
    TextView analysis_text;

    Button reselect_otherImg_btn;
    Button detail_compare_btn;

    private String json_me;
    private String json_other;

    Intent detailResult;

    int me_faceCount=0;
    int other_faceCount=0;
    boolean detailCompare = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_recognition);

        imageView_other = findViewById(R.id.img_other);
        imageView_me =findViewById(R.id.img_me);

        meFaceCount = findViewById(R.id.meFaceCount);
        otherFaceCount = findViewById(R.id.otherFaceCount);
        analysis_text = findViewById(R.id.analysis_result);

        reselect_otherImg_btn =findViewById(R.id.reselect_otherImg_btn);
        detail_compare_btn = findViewById(R.id.detail_compare_btn);

        Intent intentData = getIntent();
        imageData_me = intentData.getExtras().getString("ImgPath_me");
        imageData_other = intentData.getExtras().getString("ImgPath_other");


         detailResult = new Intent(this, DetailResult.class);

        imgFile_me = new File(imageData_me);
        imgFile_other = new File(imageData_other);

        if(imgFile_me.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile_me.getAbsolutePath());
            imageView_me.setImageBitmap(myBitmap);
        }
        if(imgFile_other.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile_other.getAbsolutePath());
            imageView_other.setImageBitmap(myBitmap);
        }

        faceRecognition.mainCfrFace makeCfrFace_other = new faceRecognition.mainCfrFace();
        makeCfrFace_other.setImgPath(imageData_other);
        makeCfrFace_other.execute();
        //비교할 이미지 분석
        faceRecognition.mainCfrFace makeCfrFace_me = new faceRecognition.mainCfrFace();
        makeCfrFace_me.setImgPath(imageData_me);
        makeCfrFace_me.execute();
        //내사진 분석

        detailResult.putExtra("ImgPath_me",imageData_me);
        detailResult.putExtra("ImgPath_other",imageData_other);
    }


    private class mainCfrFace extends AsyncTask<String, String, String> {
        private CFR_API cfrFace;
        private String imgPath;

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;

            Log.d("onClick", "image inserted " + this.imgPath);
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("onClick", "Face Recognition try to Start!");
            if(imgPath == imageData_me){
                json_me = cfrFace.faceRecog(imgPath);
            }
            else if(imgPath == imageData_other){
                json_other = cfrFace.faceRecog(imgPath);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                analysiable(json_me,json_other);        //분석가능여부 확인
            } catch (JSONException e) {
                e.printStackTrace();
            }
              clickButton(json_me,json_other);            //세부분석 OR 사진 재선택
        }
        private void analysiable(String json_me, String json_other) throws JSONException {
            if(json_me!=null) {
                JSONObject Me_json = new JSONObject(json_me);
                JSONObject Me_jsonInfo = (JSONObject)Me_json.get("info");
                me_faceCount = Integer.parseInt(Me_jsonInfo.getString("faceCount"));
                meFaceCount.setText("내사진에 인식된 사람 수:"+Me_jsonInfo.getString("faceCount"));
               // Log.e("내용 1",Me_jsonInfo.getString("faceCount"));
            }
            else if(json_other!=null) {
                JSONObject Other_json = new JSONObject(json_other);
                JSONObject Other_jsonInfo = (JSONObject)Other_json.get("info");
                other_faceCount = Integer.parseInt(Other_jsonInfo.getString("faceCount"));
                otherFaceCount.setText("비교대상에 인식된 사람 수:"+Other_jsonInfo.getString("faceCount"));
            }
                if(json_me!=null && json_other!=null){
                    String text = "비교가능 여부 : ";
                if(me_faceCount==0 && other_faceCount==0){
                 analysis_text.setText(text+"두 사진 모두 사람 얼굴이 인식되지 않았습니다.");
                }
                else if(me_faceCount==0){
                    analysis_text.setText(text+"내사진에서 사람 얼굴이 인식되지 않습니다.");
                }
                else if(other_faceCount==0){
                    analysis_text.setText(text+"비교할 사진에서 사람얼굴이 인식되지 않습니다.");
                }
                else if(me_faceCount<other_faceCount){
                    analysis_text.setText(text+"비교할 사진의 사람이 내사진보다 많습니다.");
                }
                else if(me_faceCount>other_faceCount){
                    analysis_text.setText(text+"내사진이 비교할 사진의 사람보다 많습니다.");
                }
                else{
                    analysis_text.setText(text+"분석을 비교할 준비가 되었습니다..");
                    detailCompare = true;
                }
            }
        }

    }

    private void clickButton(final String json_me, final String json_other) {
        if(detailCompare==true){
            reselect_otherImg_btn.setOnClickListener(new View.OnClickListener() {  //사진을 다시 선택할 경우
                @Override
                public void onClick(View v) {
                    Intent reSelectActivity = new Intent(getApplicationContext(),MainActivity.class);
                    // 스택에 남아있는 중간 액티비티 삭제
                    reSelectActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // 스택에 남아있는 액티비티라면, 재사용
                    reSelectActivity.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    // 액티비티를 실행한다
                    startActivity(reSelectActivity);
                    // 현재 액티비티를 종료한다.
                }
            });

        }
        else{
            Toast.makeText(this, "분석중입니다. 잠시만 기다려주세요.", Toast.LENGTH_SHORT).show();
        }
        detail_compare_btn.setOnClickListener(new View.OnClickListener() { //세부 분석을 원할경우
            @Override
            public void onClick(View v) {
                if(detailCompare==true){
                AnalysisDetail analysisDetail = new AnalysisDetail(json_me,json_other);

                detailData my_data =  analysisDetail.getMyDetailData();
                detailData other_data = analysisDetail.getOtherDetailData();

                detailResult.putExtra("mydata_age",my_data.getAge());
                detailResult.putExtra("mydata_emotion",my_data.getEmotion());
                detailResult.putExtra("otherdata_age",other_data.getAge());
                detailResult.putExtra("otherdata_emotion",other_data.getEmotion());
                startActivity(detailResult);
                }
                else{
                    Toast.makeText(getApplicationContext(), "사람의 수가 맞지않아요ㅠㅠ 다시 사진선택해주세요..", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}