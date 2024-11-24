package com.example.fae;


import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.annotations.SerializedName;

import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

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
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity{
    private TextView tempText;
    private TextView idText;
    private TextView mainCloud;
    private Switch switch4;
    private Switch switch5;
    private ImageView imageView;
    private ImageView imageView3;
    lampMenuPOST lampMenuPOST;


    private static final String BASE_URL = "http://192.168.1.7:5000";

    public interface OpenWeatherMapApi {
        @GET("weather?q=Depok,ID&units=metric&APPID=158c42e67ece5e48473de917f4c1093d")
        Call<WeatherResponse> getWeatherData();
    }
    public class WeatherResponse {
        @SerializedName("main")
        public MainWeather mainWeather;
        @SerializedName("weather")
        public Weather[] weather;
        public String name;
    }

    public interface lampMenuPOST {
        @POST("/lampMenuPOST")
        Call<ResponseBody> sendData(@Body RequestBody body);
    }


    public class MainWeather {
        public float temp;
        public int humidity;
        public float pressure;
    }

    public class Weather {
        public int id;
        public String main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switch4 = findViewById(R.id.switch4);
        switch5 = findViewById(R.id.switch5);
        tempText = findViewById(R.id.temp_Text);
        idText = findViewById(R.id.id_Text);
        mainCloud = findViewById(R.id.main_Cloud);
        imageView = findViewById(R.id.imageView);
        imageView3 = findViewById(R.id.imageView3);


        // ----------------------------------------------------



        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, firstActivity.class);
                startActivity(intent);
            }
        });

        Button btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, secondActivity.class);
                startActivity(intent);
            }
        });

        Button btn3 = findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, thirdActivity.class);
                startActivity(intent);
            }
        });

        // ----------------------------------------------------

        Retrofit retrofitPost = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenWeatherMapApi openWeatherMapApi = retrofit.create(OpenWeatherMapApi.class);
        lampMenuPOST = retrofitPost.create(lampMenuPOST.class);

        Call<WeatherResponse> call = openWeatherMapApi.getWeatherData();
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful()) {
                    WeatherResponse weatherResponse = response.body();
                    int temperature = Math.round(weatherResponse.mainWeather.temp);
                    tempText.setText(String.valueOf(temperature));

                    int idWeather = weatherResponse.weather[0].id;
                    idText.setText(String.valueOf(idWeather));

                    String mCloud = weatherResponse.weather[0].main;
                    mainCloud.setText(mCloud);
                    if (mCloud == "??"){

                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                // Log the error message
                Log.e("error", t.getMessage());
                // Show an error message to the user
                tempText.setText("Error getting temperature data");
                idText.setText("Error ID");
            }
        });


        switch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("R", "255");
                        jsonObject.put("G", "255");
                        jsonObject.put("B", "255");
                        jsonObject.put("Intensity", "4");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
                    Call<ResponseBody> call = lampMenuPOST.sendData(body);
                    Log.d("POST", "Data sent: " + jsonObject.toString());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.d("POST", "onResponse: " + response.body());
                            imageView3.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("POST", "onFailure: " + t.getMessage());
                        }
                    });
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("R", "0");
                        jsonObject.put("G", "0");
                        jsonObject.put("B", "0");
                        jsonObject.put("Intensity", "0");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
                    Call<ResponseBody> call = lampMenuPOST.sendData(body);
                    Log.d("POST", "Data sent: " + jsonObject.toString());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.d("POST", "onResponse: " + response.body());
                            imageView3.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("POST", "onFailure: " + t.getMessage());
                        }
                    });
                }
            }
        });

        switch5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("R", "255");
                        jsonObject.put("G", "255");
                        jsonObject.put("B", "255");
                        jsonObject.put("Intensity", "4");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
                    Call<ResponseBody> call = lampMenuPOST.sendData(body);
                    Log.d("POST", "Data sent: " + jsonObject.toString());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.d("POST", "onResponse: " + response.body());
                            imageView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("POST", "onFailure: " + t.getMessage());
                        }
                    });
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("R", "0");
                        jsonObject.put("G", "0");
                        jsonObject.put("B", "0");
                        jsonObject.put("Intensity", "0");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
                    Call<ResponseBody> call = lampMenuPOST.sendData(body);
                    Log.d("POST", "Data sent: " + jsonObject.toString());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.d("POST", "onResponse: " + response.body());
                            imageView.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("POST", "onFailure: " + t.getMessage());
                        }
                    });
                }
            }
        });
    }
}