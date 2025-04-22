package com.android.sharewheelsnewui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class PopUpDesign {

    private Dialog dialog;

    public PopUpDesign(Context context, Object gifSource){
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_design);
        dialog.setCancelable(true);

        ImageView gifView = dialog.findViewById(R.id.gifView);

        if(gifSource instanceof String){
            Glide.with(context).asGif().load((String) gifSource).into(gifView);
        }
        else if(gifSource instanceof Integer){
            Glide.with(context).asGif().load((Integer) gifSource).into(gifView);
        }
    }

    public void show(){
        if(dialog != null && !dialog.isShowing()){
            dialog.show();
        }
    }

    public void dismiss(){
        if(dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    public void showWithAutoDismiss(int milliseconds) {
        show();
        new Handler().postDelayed(this::dismiss, milliseconds);
    }

    public void showAndSwitchIntent(Context context, Intent intent, int delayMs) {
        show();
        new Handler().postDelayed(() -> {
            dismiss();
            context.startActivity(intent);
        }, delayMs);
    }

    public boolean isShowing() {
        return dialog != null && dialog.isShowing();
    }
}