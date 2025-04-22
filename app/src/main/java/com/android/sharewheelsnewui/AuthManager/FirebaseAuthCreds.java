package com.android.sharewheelsnewui.AuthManager;

import android.content.Context;

import com.android.sharewheelsnewui.BuildConfig;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseAuthCreds {

    private final FirebaseAuth mAuth;
    private final GoogleSignInClient signInClient;

    public FirebaseAuthCreds(Context context){
        mAuth =FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(
                GoogleSignInOptions.DEFAULT_SIGN_IN
        ).requestIdToken(BuildConfig.WEB_CLIENT)
                .requestEmail()
                .build();

        signInClient = GoogleSignIn.getClient(context, googleSignInOptions);
    }

    public GoogleSignInClient getGoogleSignInClient() {
        return signInClient;
    }

    public FirebaseAuth getFirebaseAuth() {
        return mAuth;
    }

}
