package com.example.memeshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    Button sharebtn;
    Button nextbtn;
    ImageView memeImage;
    ProgressBar progressBar;
    String memeurl = "i";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharebtn = findViewById(R.id.sharebtn);
        nextbtn = findViewById(R.id.nextbtn);
        memeImage = findViewById(R.id.memeImage);
        progressBar = findViewById(R.id.progressBar);
        loadmeme();
    }

    public void loadmeme() {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        String url = "https://meme-api.herokuapp.com/gimme";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    memeurl = response.getString("url");
                    Glide.with(MainActivity.this).load(memeurl).into(memeImage);
                    progressBar.setVisibility(ProgressBar.INVISIBLE);

                } catch (JSONException e) {
                    loadmeme();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error Ocurred", Toast.LENGTH_SHORT).show();
                loadmeme();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    public void Share(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, memeurl);
        startActivity(intent);
    }

    public void Next(View view) {
        loadmeme();
    }


}