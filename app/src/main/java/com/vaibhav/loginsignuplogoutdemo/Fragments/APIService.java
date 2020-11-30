package com.vaibhav.loginsignuplogoutdemo.Fragments;

import com.vaibhav.loginsignuplogoutdemo.Notifications.MyResponse;
import com.vaibhav.loginsignuplogoutdemo.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAACvMQDIg:APA91bGQSW5P3rQF87yzrnrttfomsXNa76T32moTv-0AqpXZfniarmJjfqANVBeGW8Eh3z9vqORrgjP1j5jgKSdhLFWOFot1GZST9r1K-JWgGovW7QeTD9L457U9X8_aW4AbTDvfgn4e"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body Sender body);

}

