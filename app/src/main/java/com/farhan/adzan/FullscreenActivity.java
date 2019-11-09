package com.farhan.adzan;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import at.favre.lib.dali.Dali;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class FullscreenActivity extends AppCompatActivity {
    ImageView imageView;
    CardView cv;
    View rootView;
    Button btn1, btn2, btn3;
    TextInputEditText tv1, tv2, tv3;
    int randomNumber;
    public static final String NOTIFICATION_ID = "4655";
    private static final String TAG = "FullscreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        //async task
        StrictMode.ThreadPolicy policy= new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //blurimage
        imageView = findViewById(R.id.img1);

        tv1 = findViewById(R.id.nama);
        tv2 = findViewById(R.id.nomor);
        tv3 = findViewById(R.id.kode);

        btn1 = findViewById(R.id.dummy_button1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //construct
                    String apiKey = "apikey=" + "djApWIfYATY-9HqEngDdQac01gvMyHmkYgLaSRIZUx";
                    Random random = new Random();
                    randomNumber = random.nextInt(999999);
                    Log.d(TAG, "onClick: " + randomNumber);
                    String message = "&message=" + "Hey "+tv1.getText().toString()+ "Your OTP IS "+randomNumber;
                    String sender = "&sender=" + "ADZAN";
                    String numbers = "&numbers=" +tv2.getText().toString();

                    //send data
                    HttpURLConnection conn = (HttpURLConnection) new URL("https://api.txtlocal.com/send/?").openConnection();
                    String data = apiKey + numbers + message + sender;
                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
                    conn.getOutputStream().write(data.getBytes("UTF-8"));
                    final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    final StringBuffer stringBuffer = new StringBuffer();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                    rd.close();
                    Toast.makeText(getApplicationContext(), "OTP SEND SUCCESSFULLY", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    System.out.println("Error SMS "+e);
                    Toast.makeText(getApplicationContext(), "ERROR SMS "+e, Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "ERROR "+e, Toast.LENGTH_LONG).show();
                }
            }
        });
        btn2 = findViewById(R.id.dummy_button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(randomNumber==Integer.valueOf(tv3.getText().toString())){
                    Toast.makeText(getApplicationContext(), "You are logined successfully", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(FullscreenActivity.this, MainActivity.class);
                    startActivity(i);
                }else{
                    Toast.makeText(getApplicationContext(), "WRONG OTP", Toast.LENGTH_LONG).show();
                }
            }
        });

        btn3 = findViewById(R.id.notif);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "CLICKED", Toast.LENGTH_LONG).show();
                Context ctx = getApplicationContext();
                NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    String CHANNEL_ID = "my_channel_01";


                    Intent intent = new Intent(ctx, FullscreenActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    PendingIntent pendingIntent = PendingIntent.getActivity(ctx, 0, intent, 0);
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_launcher_foreground)
                            .setContentTitle("My notification")
                            .setContentText("Hello World!")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            // Set the intent that will fire when the user taps the notification
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);

                    Random random = new Random();
                    int notificationId = random.nextInt(999999);
                    NotificationManagerCompat nM = NotificationManagerCompat.from(ctx);
                    nM.notify(notificationId, builder.build());
//
//
//                    CharSequence name = "my_channel";
//                    String Description = "This is my channel";
//                    int importance = NotificationManager.IMPORTANCE_HIGH;
//                    NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
//                    mChannel.setDescription(Description);
//                    mChannel.enableLights(true);
//                    mChannel.setLightColor(Color.RED);
//                    mChannel.enableVibration(true);
//                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
//                    notificationManager.createNotificationChannel(mChannel);
//                    notificationManager.notify(CHANNEL_ID, builder.build());

                }

                NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, NOTIFICATION_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Title")
                        .setContentText("Message");

                Intent resultIntent = new Intent(ctx, FullscreenActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(ctx);
                stackBuilder.addParentStack(FullscreenActivity.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                builder.setContentIntent(resultPendingIntent);

            }
        });

    }
}
