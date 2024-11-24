package com.example.fae;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import java.io.IOException;


import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Tag;

import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class secondActivity extends AppCompatActivity {
    private TextView temp;
    private TextView water;
    apiTemp apiTemp;
    apiWater apiWater;

    private static final String BASE_URL = "http://192.168.1.7:5000";

    public interface apiTemp {
        @GET("/tempGET")
        Call<ResponseBody> sendData();
    }

    public interface apiWater {
        @GET("/waterGET")
        Call<ResponseBody> sendData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

        temp = findViewById(R.id.textView16);
        water = findViewById(R.id.textView15);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiTemp = retrofit.create(apiTemp.class);
        apiWater = retrofit.create(apiWater.class);

        apiTemp.sendData().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String receivedData = response.body().string();
                        JSONObject json = new JSONObject(receivedData);
                        JSONArray temp_C_values = json.getJSONArray("temp_C");

                        temp.setText(temp_C_values.getString(0));
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("API", "Unsuccessful response: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API", "Error: " + t.getMessage());
            }
        });

        apiWater.sendData().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String receivedData = response.body().string();
                        JSONObject json = new JSONObject(receivedData);
                        JSONArray water_value = json.getJSONArray("water_Value ");

                        water.setText(water_value.getString(0));
                        Log.e("API", "Response : " + receivedData);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("API", "Unsuccessful response: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("API", "Error: " + t.getMessage());
            }
        });
    }
}
