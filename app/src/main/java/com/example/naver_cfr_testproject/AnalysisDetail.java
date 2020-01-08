package com.example.naver_cfr_testproject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AnalysisDetail {
    private String json_me;
    private  String json_other;
    private JSONObject Me_json;
    private JSONObject Other_json;

    public detailData my_data = new detailData();
    public detailData other_data = new detailData();

    public AnalysisDetail(String json_me, String json_other) {
        this.json_me = json_me;
        this.json_other = json_other;
        try {
            Me_json = new JSONObject(json_other);
            JSONArray myDataArray = Me_json.getJSONArray("faces");

            for (int i = 0; i <myDataArray.length() ; i++) {
                JSONObject faceObject = (JSONObject) myDataArray.get(i);
                JSONObject emotionData = (JSONObject) faceObject.get("emotion");
                JSONObject ageData = (JSONObject) faceObject.get("age");

                /*detailData my_data = new detailData();*/

                my_data.setEmotion(emotionData.get("value").toString());
                my_data.setAge(ageData.get("value").toString());
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Other_json = new JSONObject(json_me);
            JSONArray otherDataArray = Other_json.getJSONArray("faces");

            for (int i = 0; i <otherDataArray.length() ; i++) {
                JSONObject faceObject = (JSONObject) otherDataArray.get(i);
                JSONObject emotionData = (JSONObject) faceObject.get("emotion");
                JSONObject ageData = (JSONObject) faceObject.get("age");

                /*detailData my_data = new detailData();*/

                other_data.setEmotion(emotionData.get("value").toString());
                other_data.setAge(ageData.get("value").toString());

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public detailData getMyDetailData(){
        return my_data;
    }

    public detailData getOtherDetailData(){
        return other_data;
    }

}
