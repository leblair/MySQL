package com.example.mysql;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class UserActivity extends AppCompatActivity {

    protected Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        toolbar = findViewById(R.id.toolbarFavorites);

        setSupportActionBar(toolbar);

        UserActivity.this.setTitle("Profile details");

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
                Toast.makeText(this, "User", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.favorite:
                Toast.makeText(this, "Favorites", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, FavoritesActivity.class));
                return true;
            case android.R.id.home:
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ListActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}