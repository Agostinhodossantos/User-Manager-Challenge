package user.app.com.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import user.app.com.R;
import user.app.com.adapters.UserListAdapter;
import user.app.com.databinding.ActivityMainBinding;
import user.app.com.models.User;
import user.app.com.network.DataProvider;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        setupUsersRv();

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterUserActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupUsersRv() {
        DataProvider provider = new DataProvider();
        provider.getAllUsers(new DataProvider.ResponseListener() {
            @Override
            public void onSuccess(Object response) {
                UserListAdapter adapter = new UserListAdapter(getApplicationContext(), (List<User>) response);
                StaggeredGridLayoutManager layoutManager =
                        new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
                binding.rvUsers.setLayoutManager(layoutManager);
                binding.rvUsers.setAdapter(adapter);
                binding.progressCircular.setVisibility(View.GONE); // hide progressBar
            }

            @Override
            public void onFailure(String message) {
                binding.progressCircular.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            DataProvider provider = new DataProvider();
            provider.removeAll(new DataProvider.ResponseListener() {
                @Override
                public void onSuccess(Object response) {
                    Toast.makeText(MainActivity.this, response.toString(),
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String message) {

                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}