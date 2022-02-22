package com.example.mysql;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    protected RecyclerView recyclerView;
    protected String email;
    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        recyclerView = findViewById(R.id.recyclerFavorites);
        toolbar = findViewById(R.id.toolbarFavorites);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FavoritesActivity.this.setTitle("Favorites");

        email = getIntent().getStringExtra("email");
        showFavorites(FavoritesActivity.this, email, new ArrayList<>(), recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list:
                Toast.makeText(this, "List animes", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ListActivity.class));
                return true;
            case R.id.person:
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.favorite:
                Toast.makeText(this, "Favorites", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, FavoritesActivity.class));
                return true;
            case android.R.id.home:
                Toast.makeText(this, "Back", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ListActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    static void showFavorites(Context context, String emailIntent, List<Anime> animeList, RecyclerView recyclerView) {

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.start();

        final String FINAL_URL = "https://joanseculi.com/edt69/animesfavorites.php?" + "email=" + emailIntent;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                FINAL_URL,
                null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("animesfavorites");

                            for (int i = 0 ; i < jsonArray.length() ; i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Anime animeOnJSONArray = new Anime();
                                animeOnJSONArray.setName(jsonObject.getString("name"));
                                animeOnJSONArray.setDescription(jsonObject.getString("description"));
                                animeOnJSONArray.setType(Type.valueOf(jsonObject.getString("type")));
                                animeOnJSONArray.setYear(Integer.parseInt(jsonObject.getString("year")));
                                animeOnJSONArray.setImage(jsonObject.getString("image"));

                                animeList.add(animeOnJSONArray);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                        AnimeAdapter myAnimeAdapter = new AnimeAdapter(context, animeList);
                        recyclerView.setAdapter(myAnimeAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        queue.add(jsonObjectRequest);
    }
}