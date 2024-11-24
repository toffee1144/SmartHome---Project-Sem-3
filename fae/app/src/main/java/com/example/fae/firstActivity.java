package com.example.fae;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.os.Bundle;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.POST;

import java.util.List;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.SeekBar;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.json.JSONException;
import org.json.JSONObject;

public class firstActivity extends AppCompatActivity {

    private Switch switch1;
    private Chip blue_Chip;
    private Chip red_Chip;
    private Chip violet_Chip;
    private Chip orange_Chip;
    private SeekBar seek_Bar;

    private static final String BASE_URL = "http://192.168.1.7:5000";
    bedRoomPOST bedRoomPOST;

    public interface bedRoomPOST {
        @POST("/lampMenuPOST")
        Call<ResponseBody> sendData(@Body RequestBody body);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);

        blue_Chip = findViewById(R.id.blueChip);
        red_Chip = findViewById(R.id.redChip);
        violet_Chip = findViewById(R.id.violetChip);
        orange_Chip = findViewById(R.id.orangeChip);
        seek_Bar = findViewById(R.id.seekBar);
        switch1 = findViewById(R.id.switch1);

        Retrofit retrofitPost = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        bedRoomPOST = retrofitPost.create(bedRoomPOST.class);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("R", "255");
                        jsonObject.put("G", "255");
                        jsonObject.put("B", "255");
                        jsonObject.put("Intensity", "5");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
                    Call<ResponseBody> call = bedRoomPOST.sendData(body);
                    Log.d("POST", "Data sent: " + jsonObject.toString());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.d("POST", "onResponse: " + response.body());
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
                        jsonObject.put("Intensity", "5");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
                    Call<ResponseBody> call = bedRoomPOST.sendData(body);
                    Log.d("POST", "Data sent: " + jsonObject.toString());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            Log.d("POST", "onResponse: " + response.body());
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e("POST", "onFailure: " + t.getMessage());
                        }
                    });
                }
            }
        });

        seek_Bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("Intensity", seek_Bar.getProgress());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
                Call<ResponseBody> call = bedRoomPOST.sendData(body);
                Log.d("POST", "Data sent: " + jsonObject.toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("POST", "onResponse: " + response.body());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("POST", "onFailure: " + t.getMessage());
                    }
                });
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }
        });


        blue_Chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform actions when the chip is clicked
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("R", 0);
                    jsonObject.put("G", 0);
                    jsonObject.put("B", 255);
                    jsonObject.put("Intensity", seek_Bar.getProgress());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
                Call<ResponseBody> call = bedRoomPOST.sendData(body);
                Log.d("POST", "Data sent: " + jsonObject.toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("POST", "onResponse: " + response.body());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("POST", "onFailure: " + t.getMessage());
                    }
                });

            }

        });

        red_Chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform actions when the chip is clicked
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("R", 255);
                    jsonObject.put("G", 0);
                    jsonObject.put("B", 0);
                    jsonObject.put("Intensity", seek_Bar.getProgress());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
                Call<ResponseBody> call = bedRoomPOST.sendData(body);
                Log.d("POST", "Data sent: " + jsonObject.toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("POST", "onResponse: " + response.body());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("POST", "onFailure: " + t.getMessage());
                    }
                });
            }
        });

        violet_Chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform actions when the chip is clicked
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("R", 103);
                    jsonObject.put("G", 58);
                    jsonObject.put("B", 183);
                    jsonObject.put("Intensity", seek_Bar.getProgress());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
                Call<ResponseBody> call = bedRoomPOST.sendData(body);
                Log.d("POST", "Data sent: " + jsonObject.toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("POST", "onResponse: " + response.body());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("POST", "onFailure: " + t.getMessage());
                    }
                });
            }
        });

        orange_Chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform actions when the chip is clicked
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("R", 255);
                    jsonObject.put("G", 87);
                    jsonObject.put("B", 34);
                    jsonObject.put("Intensity", seek_Bar.getProgress());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());
                Call<ResponseBody> call = bedRoomPOST.sendData(body);
                Log.d("POST", "Data sent: " + jsonObject.toString());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("POST", "onResponse: " + response.body());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("POST", "onFailure: " + t.getMessage());
                    }
                });
            }
        });

    }
}