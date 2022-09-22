package com.qrdevelopers.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.qrdevelopers.app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding binding;
    private DBHandler dbHandler;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        dbHandler = new DBHandler(MainActivity.this);

        binding.btnAdd.setOnClickListener(this);
        binding.btnDelete.setOnClickListener(this);

        adapter = new UserAdapter(MainActivity.this, dbHandler.getUserList());
        binding.rvUersList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        binding.rvUersList.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                addUser();
                break;
            case R.id.btn_delete:
                deleteTableData();
                break;
        }
    }

    private void addUser() {

        String name = binding.etUserName.getText().toString();
        String mobile = binding.etUserMobile.getText().toString();

        if (TextUtils.isEmpty(name)) {
            binding.etUserName.setError("Please enter user name");
            return;
        }
        if (TextUtils.isEmpty(mobile)) {
            binding.etUserName.setError("Please enter mobile number");
            return;
        }

        if (dbHandler != null) dbHandler.addUser(name, mobile);
    }

    private void deleteTableData() {
        if (dbHandler != null && dbHandler.getCount() > 0) {
            dbHandler.deleteData();
            Toast.makeText(MainActivity.this, "Clear!",Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(MainActivity.this, "Empty!",Toast.LENGTH_SHORT).show();

    }
}