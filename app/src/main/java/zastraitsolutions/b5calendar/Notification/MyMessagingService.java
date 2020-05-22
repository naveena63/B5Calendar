package zastraitsolutions.b5calendar.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import zastraitsolutions.b5calendar.R;
import zastraitsolutions.b5calendar.SplashActivity;

public class MyMessagingService extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "4565";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotificationMessage(remoteMessage.getData().get("title"),remoteMessage.getData().get("message"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channcel_desc);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            NotificationManager notificationManager = (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            FirebaseMessaging.getInstance().subscribeToTopic("weather")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = getString(R.string.msg_subscribed);
                            if (!task.isSuccessful()) {
                                msg = getString(R.string.msg_subscribe_failed);
                            }
                            Log.d("app", msg);
                            Toast.makeText(MyMessagingService.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    public void showNotificationMessage(String title, String message) {
        Intent intent=new Intent(MyMessagingService.this, SplashActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.b5logo)
                .setContentText(message)
                .setContentTitle(title)
                .setChannelId(CHANNEL_ID)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(notificationManager!=null)
            notificationManager.notify(999, notificationBuilder.build());

    }
//
}