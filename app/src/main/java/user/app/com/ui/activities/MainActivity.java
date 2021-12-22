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

import android.view.Menu;
import android.view.MenuItem;

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
        UserListAdapter adapter = new UserListAdapter(getApplicationContext(), getUsers());
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        binding.rvUsers.setLayoutManager(layoutManager);
        binding.rvUsers.setAdapter(adapter);
    }

    private List<User> getUsers() {
        List<User> userList = new ArrayList<>();
        userList.add(new User("a", "Agostinho", "", "my bio"));
        userList.add(new User("a", "Santos", "", "a"));
        userList.add(new User("a", "Mario", "", "a"));
        userList.add(new User("a", "Helio", "", "a"));
        return userList;
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}