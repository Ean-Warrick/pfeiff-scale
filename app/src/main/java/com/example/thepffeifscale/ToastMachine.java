package com.example.thepffeifscale;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thepffeifscale.infinitum.bind.HashBundle;
import com.example.thepffeifscale.infinitum.dev.Instance;

public class ToastMachine extends Instance {
    private static ToastMachine singleton;

    private ToastMachine() {
        Controller controller = Controller.getSingleton();
        this.connect(controller.toastMessage, "onToast", this::onToast);
    }

    private void onToast(HashBundle hashBundle) {
        String message = hashBundle.getString("MESSAGE");
        AppCompatActivity context = (AppCompatActivity) hashBundle.get("CONTEXT");
        context.runOnUiThread(() -> Toast.makeText(context, message, Toast.LENGTH_LONG).show());
    }

    public static ToastMachine getSingleton() {
        if (singleton == null) { singleton = new ToastMachine();}
        return singleton;
    }
}
