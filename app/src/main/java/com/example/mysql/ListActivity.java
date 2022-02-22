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

public class ListActivity extends AppCompatActivity {

    Toolbar myToolbar;
    RecyclerView recyclerView;
    static String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        myToolbar = (Toolbar) findViewById(R.id.myToolbar);
        setSupportActionBar(myToolbar);

        recyclerView = findViewById(R.id.recyclerView);
        email = getIntent().getStringExtra("email");

        showAnimes(ListActivity.this, email, new ArrayList<>(), recyclerView);

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
                startActivity(new Intent(this, ListActivity.class).putExtra("email", email));
                return true;
            case R.id.person:
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, UserActivity.class));
                return true;
            case R.id.favorite:
                Toast.makeText(this, "Favorites", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, FavoritesActivity.class).putExtra("email", email));
                return true;
            case android.R.id.home:
                Toast.makeText(this, "Back", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class).putExtra("email", email));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    static void showAnimes(Context context, String emailIntent, List<Anime> animeList, RecyclerView recyclerView) {
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.start();

        final String FINAL_URL = "https://www.joanseculi.com/edt69/animes2.php?" + "email=" + emailIntent;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                FINAL_URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("animes");

                            for (int i = 0 ; i < jsonArray.length() ; i++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Anime animeOnJSONArray = new Anime();
                                animeOnJSONArray.setName(jsonObject.getString("name"));
                                animeOnJSONArray.setDescription(jsonObject.getString("description"));
                                animeOnJSONArray.setType(Type.valueOf(jsonObject.getString("type")));
                                animeOnJSONArray.setYear(Integer.parseInt(jsonObject.getString("year")));
                                animeOnJSONArray.setImage(jsonObject.getString("image"));
                                animeOnJSONArray.setFavorite(jsonObject.getString("favorite"));

                                animeList.add(animeOnJSONArray);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(recyclerView!=null) {
                            recyclerView.setLayoutManager(new LinearLayoutManager(context));
                            AnimeAdapter myAnimeAdapter = new AnimeAdapter(context, animeList);
                            recyclerView.setAdapter(myAnimeAdapter);
                        }
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

enum Type {

    TV("TV"),
    Movie("Movie"),
    Special("Special"),
    OVA("OVA");

    public String type;

    Type(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

}