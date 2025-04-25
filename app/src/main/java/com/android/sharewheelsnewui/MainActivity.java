package com.android.sharewheelsnewui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.sharewheelsnewui.AuthManager.FirebaseAuthCreds;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FrameLayout f1,f2;
    private PopUpDesign pop;
    private static final  int RC_SIGN_IN = 123;
    private FirebaseAuthCreds authCreds;
    private GoogleSignInClient signInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        f1 = findViewById(R.id.googleButton);
        f2 = findViewById(R.id.phoneButton);

        authCreds = new FirebaseAuthCreds(this);

        f1.setOnClickListener(v -> {
            signInClient = authCreds.getGoogleSignInClient();
            Intent signInIntent = signInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account != null){
                    Intent navigateToMain = new Intent(this,
                            HomePage.class);
                    String ImageStringUrl = Objects.requireNonNull(account.getPhotoUrl()).toString();
                    navigateToMain.putExtra("name",account.getDisplayName());
                    navigateToMain.putExtra("photo",ImageStringUrl);
                    pop = new PopUpDesign(MainActivity.this,
                            R.raw.success);
                    pop.showAndSwitchIntent(MainActivity.this,
                            navigateToMain,3000);
                }
            }
            catch (ApiException e){
                pop = new PopUpDesign(MainActivity.this,
                        R.raw.close);
                pop.showWithAutoDismiss(3000);
                Log.d("Google Sign In Error", ""+e.getStatusCode());
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            String ImageStringUrl = Objects.requireNonNull(account.getPhotoUrl()).toString();
            Intent navigate = new Intent(getApplicationContext(), HomePage.class);
            navigate.putExtra("name",account.getDisplayName());
            navigate.putExtra("photo", ImageStringUrl);
            startActivity(navigate);
        }
    }
}