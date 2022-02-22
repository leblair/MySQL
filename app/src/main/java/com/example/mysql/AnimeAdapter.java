package com.example.mysql;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder> {

    public Context context;
    public List<Anime> animes;
    public static String INSERTFAVORITE = "https://www.joanseculi.com/edt69/insertfavorite.php";

    public AnimeAdapter(Context context, List<Anime> animes) {
        this.context = context;
        this.animes = animes;
    }

    @NonNull
    @Override
    public AnimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.anime_row, parent, false);
        return new AnimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeAdapter.AnimeViewHolder holder, int position) {
        Picasso.get().load("https://joanseculi.com/" + animes.get(position).getImage()).fit().centerCrop().into(holder.animeImage);

        holder.animeName.setText(animes.get(position).getName());
        holder.description.setText(animes.get(position).getDescription());
        holder.tv.setText(String.valueOf(animes.get(holder.getAdapterPosition()).getType()));
        holder.year.setText(animes.get(position).getYear() + "");
        holder.like.setImageResource(R.drawable.ic_favorite2);

        if (context instanceof FavoritesActivity) {
            holder.like.setImageResource(R.drawable.ic_favorite);
        }

        List<Anime> allAnimes = new ArrayList<>();

        showAnimes(context, ListActivity.email, allAnimes, null);

        holder.like.setOnClickListener(view -> {
            favorite(context, animes.get(holder.getAdapterPosition()), holder.like, ListActivity.email);
        });

        if(holder.like.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.ic_favorite2).getConstantState()) {
            deleteFavorite(context, animes.get(holder.getAdapterPosition()), holder.like, ListActivity.email);
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

    static void favorite(Context context, Anime anime, ImageView heartImg, String email) {

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.start();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                INSERTFAVORITE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("favorite inserted")) {
                            if(heartImg.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.ic_favorite).getConstantState()) {
                                heartImg.setImageResource(R.drawable.ic_favorite2);
                                Toast.makeText(context, anime.getName() + " added to favorites.", Toast.LENGTH_SHORT).show();
                            } else {
                                heartImg.setImageResource(R.drawable.ic_favorite);
                                Toast.makeText(context, anime.getName() + " deleted from favorites.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            heartImg.setImageResource(R.drawable.ic_favorite2);
                            Toast.makeText(context, anime.getName() + " is already in favorites.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error adding " + anime.getName() + " to favorites.", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("anime", anime.getName());
                return params;
            }
        };
        queue.add(stringRequest);
    }

    static void deleteFavorite(Context context, Anime anime, ImageView heartImg, String email) {

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.start();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                INSERTFAVORITE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("favorite deleted")) {
                            if(heartImg.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.ic_favorite).getConstantState()) {
                                heartImg.setImageResource(R.drawable.ic_favorite2);
                                Toast.makeText(context, anime.getName() + " deleted from favorites.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error deleting " + anime.getName() + " from favorites.", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("anime", anime.getName());
                return params;
            }
        };
        queue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return animes.size();
    }

    public class AnimeViewHolder extends RecyclerView.ViewHolder {

        public TextView animeName;
        public TextView description;
        public TextView year;
        public TextView tv;
        public ImageView animeImage;
        public ImageView like;

        public AnimeViewHolder(@NonNull View itemView) {
            super(itemView);
            animeName = itemView.findViewById(R.id.animeName);
            description = itemView.findViewById(R.id.description);
            year = itemView.findViewById(R.id.year);
            tv = itemView.findViewById(R.id.tv);
            animeImage = itemView.findViewById(R.id.animeImage);
            like = itemView.findViewById(R.id.like);
        }
    }
}
