package com.sam.photoeditor.customtoast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sam.photoeditor.R;
import com.sam.photoeditor.shakedetector.ShakeOri;

public class CustomToast extends Toast {
    private static long SHORT = 4000;
    private static long LONG = 7000;

    public CustomToast(Context context) {
        super(context);
    }

    public static Toast makeText(Context context, String message, int duration, ShakeOri type) {

        Toast toast = new Toast(context);
        toast.setDuration(duration);
        View layout = LayoutInflater.from(context).inflate(R.layout.customtoast_layout, null, false);
        TextView toastTv = (TextView) layout.findViewById(R.id.customtoast_text);
        LinearLayout linearLayout = (LinearLayout) layout.findViewById(R.id.customtoast_bg);
        ImageView img = (ImageView) layout.findViewById(R.id.customtoast_icon);

        toastTv.setText(message);
        if (type == ShakeOri.LEFT) {
            toast.setGravity(Gravity.LEFT, 65, 0);
            toastTv.setText(message);
            linearLayout.setBackgroundResource(R.drawable.customtoat_bg);
            img.setImageResource(R.drawable.mate_icon_undo);
        }

        else if (type == ShakeOri.RIGHT) {
            toast.setGravity(Gravity.RIGHT, 65, 0);
            toastTv.setText(message);
            linearLayout.setBackgroundResource(R.drawable.customtoat_bg);
            img.setImageResource(R.drawable.mate_icon_redo);
        }
        toast.setView(layout);
        return toast;
    }

}
