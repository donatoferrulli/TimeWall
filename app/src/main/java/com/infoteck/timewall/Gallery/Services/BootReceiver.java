package com.infoteck.timewall.Gallery.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by donato.ferrulli on 14/07/2017.
 */

public class BootReceiver extends BroadcastReceiver
{

    public void onReceive(Context context, Intent intent)
    {
        //START ASSISTANT SERVICE
        context.startService(new Intent(context,serviceAssistant.class));
    }
}
