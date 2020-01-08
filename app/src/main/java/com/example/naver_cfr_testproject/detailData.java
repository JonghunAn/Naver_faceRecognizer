package com.example.naver_cfr_testproject;

import java.io.Serializable;

public class detailData implements Serializable{
        private String emotion;
        private String age;

        public String getEmotion() {
            return emotion;
        }

        public void setEmotion(String emotion) {
            this.emotion = emotion;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }
}