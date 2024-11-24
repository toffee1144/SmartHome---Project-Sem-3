package com.example.fae;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public class thirdActivity extends AppCompatActivity {

    private ToggleButton door;
    doorPOST doorPOST;

    private static final String BASE_URL = "http://192.168.1.7:5000";

    public interface doorPOST {
        @POST("/doorPOST")
        Call<ResponseBody> sendData(@Body RequestBody body);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_activity);
        door = findViewById(R.id.toggleButton2);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        doorPOST = retrofit.create(doorPOST.class);

        door.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("Door", "1");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
                    Call<ResponseBody> call = doorPOST.sendData(body);
                    Log.d("POST", "Data sent: " + jsonObject.toString());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Log.d("POST", "Data sent successfully");
                            } else {
                                Log.d("POST", "Data sent failed: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("POST", "Error sending data: " + t.getMessage());
                        }
                    });
                }
                else
                {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("Door", "0");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
                    Call<ResponseBody> call = doorPOST.sendData(body);
                    Log.d("POST", "Data sent: " + jsonObject.toString());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Log.d("POST", "Data sent successfully");
                            } else {
                                Log.d("POST", "Data sent failed: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("POST", "Error sending data: " + t.getMessage());
                        }
                    });

                }

            }
        });

    }
}
