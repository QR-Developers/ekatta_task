package com.qrdevelopers.app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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


    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter = new UserAdapter(MainActivity.this, dbHandler.getUserList());
        binding.rvUersList.setLayoutManager(new GridLayoutManager(MainActivity.this, 1));
        binding.rvUersList.setAdapter(adapter);
        listVisibility();

    }

    private void listVisibility() {
        if (adapter.getItemCount() > 0) {
            binding.tvNoRecord.setVisibility(View.GONE);
            binding.rvUersList.setVisibility(View.VISIBLE);
        } else {
            binding.tvNoRecord.setVisibility(View.VISIBLE);
            binding.rvUersList.setVisibility(View.GONE);
        }
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
            binding.etUserName.requestFocus();
            binding.etUserName.setError("Please enter user name");
            return;
        }
        if (TextUtils.isEmpty(mobile)) {
            binding.etUserMobile.requestFocus();
            binding.etUserMobile.setError("Please enter mobile number");
            return;
        }

        if (dbHandler != null) {
            dbHandler.addUser(name, mobile);
            binding.etUserName.setText("");
            binding.etUserMobile.setText("");
            adapter.setList(dbHandler.getUserList());
            adapter.notifyDataSetChanged();
            listVisibility();
            showNotification();
        }
    }

    private void showNotification() {
        NotificationManager notificationManager;
        NotificationChannel notificationChannel;
        Notification.Builder builder;
        String channelId = "ekatta";
        String description = "Test notification";

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

//        Intent intent = new Intent(this, MainActivity.class);

//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);

            builder = new Notification.Builder(this, channelId)
                    .setContentTitle("Data added")
                    .setSmallIcon(R.drawable.ic_launcher_foreground);
//                    .setContentIntent(pendingIntent);
        } else {

            builder = new Notification.Builder(this)
                    .setContentTitle("Data added")
                    .setSmallIcon(R.drawable.ic_launcher_foreground);
//                    .setContentIntent(pendingIntent);
        }
        notificationManager.notify(1234, builder.build());
    }


    private void deleteTableData() {

        dbHandler.getUserList().forEach(user -> {
            Log.i("UserList", "User Name: " + user.getName());
            Log.i("UserList", "User Mobile: " + user.getMobile());
        });

        if (dbHandler != null && adapter.getItemCount() > 0) {
            dbHandler.deleteData();
            adapter.setList(dbHandler.getUserList());
            adapter.notifyDataSetChanged();
            listVisibility();
            Toast.makeText(MainActivity.this, "Clear!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Empty!", Toast.LENGTH_SHORT).show();
        }
    }
}