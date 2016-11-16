package com.team404.trackmyday;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.regions.Regions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    private SignInButton btnSignIn;
    private Button btnSignOut, btnRevokeAccess, btnViewLocations, btnViewReport, btnManualPing,btnViewMyLocation;
    private Button btnAddActivity, btnContact, btnActivateEmergency;
    private LinearLayout llProfileLayout;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail;
    private String tokenid;

    private EmergencyCoordinator emergencyCoordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignOut = (Button) findViewById(R.id.btn_sign_out);
        llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);
        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);

        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customizing G+ button
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());

        btnViewLocations = (Button) findViewById(R.id.btn_view_locations);
        btnViewReport = (Button) findViewById(R.id.btn_view_report);
        btnManualPing = (Button) findViewById(R.id.btn_manual_ping);
        btnAddActivity = (Button) findViewById(R.id.btn_input_activity);
        btnContact = (Button) findViewById(R.id.btn_contacts);
        btnActivateEmergency = (Button) findViewById(R.id.btn_activate_emergency);
        btnViewMyLocation = (Button) findViewById(R.id.btn_view_my_locations);


        btnViewMyLocation.setOnClickListener(this);
        btnViewLocations.setOnClickListener(this);
        btnViewReport.setOnClickListener(this);
        btnManualPing.setOnClickListener(this);
        btnAddActivity.setOnClickListener(this);
        btnContact.setOnClickListener(this);
        btnActivateEmergency.setOnClickListener(this);


    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String email = acct.getEmail();
            tokenid = acct.getIdToken();
            String idToken = acct.getIdToken();
            String personPhotoUrl;
            if (acct.getPhotoUrl() == null) {
                personPhotoUrl = "https://lh4.googleusercontent.com/-v0soe-ievYE/AAAAAAAAAAI/AAAAAAACyas/yR1_yhwBcBA/photo.jpg?sz=50";
            } else {
                personPhotoUrl = acct.getPhotoUrl().toString();
            }


            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);

            txtName.setText(personName);
            txtEmail.setText(email);
            Glide.with(getApplicationContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfilePic);

            //AWS Cognito
            // Initialize the Amazon Cognito credentials provider
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    getApplicationContext(),
//                "TrackMyDay",
                    "us-east-1:72e0d18f-ef52-4b2d-96e5-e7db7ce0e040",
//                "arn:aws:iam::471405578050:role/Cognito_TrackMyDayUnauth",
//                "arn:aws:iam::471405578050:role/Cognito_TrackMyDayAuth",
                    Regions.US_EAST_1);


            // Initialize the Cognito Sync client
            CognitoSyncManager syncClient = new CognitoSyncManager(
                    getApplicationContext(),
                    Regions.US_EAST_1, // Region
                    credentialsProvider);



            Map<String, String> logins = new HashMap<String, String>();
            logins.put("accounts.google.com", tokenid);
            credentialsProvider.setLogins(logins);

            // Create a record in a dataset and synchronize with the server
            Dataset dataset = syncClient.openOrCreateDataset("TrackMyDay");
            dataset.put("myKey", "myValue");
            dataset.synchronize(new DefaultSyncCallback() {
                @Override
                public void onSuccess(Dataset dataset, List newRecords) {
                    //Your handler code here
                }
            });

            emergencyCoordinator = new EmergencyCoordinator();

            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_sign_in:
                signIn();
                break;

            case R.id.btn_sign_out:
                signOut();
                break;

            case R.id.btn_view_locations:
                viewLocations();
                break;

            case R.id.btn_view_report:
                viewReport();
                break;

            case R.id.btn_manual_ping:
                triggerManualPing();
                break;

            case R.id.btn_input_activity:
                inputActivity();
                break;

            case R.id.btn_contacts:
                addContacts();
                break;

            case R.id.btn_activate_emergency:
                activateEmergency();
                break;
            case R.id.btn_view_my_locations:
                showMyLocation();
                break;

        }
    }

    private void showMyLocation() {

        Intent i = new Intent(MainActivity.this,GoogleMapsApiLocator.class);
        startActivity(i);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
            llProfileLayout.setVisibility(View.VISIBLE);
            btnViewMyLocation.setVisibility(View.VISIBLE);
            btnViewLocations.setEnabled(true);             //Make buttons usable when user is signed in
            btnViewReport.setEnabled(true);
            btnManualPing.setEnabled(true);
            btnAddActivity.setEnabled(true);
            btnContact.setEnabled(true);
            btnActivateEmergency.setEnabled(true);
        } else {
            btnViewMyLocation.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            llProfileLayout.setVisibility(View.GONE);
            btnViewLocations.setEnabled(false);             //Make buttons unusable until user is signed in
            btnViewReport.setEnabled(false);                //Comment out to test without having to sign in
            btnManualPing.setEnabled(false);
            btnAddActivity.setEnabled(false);
            btnContact.setEnabled(false);
            btnActivateEmergency.setEnabled(false);
        }
    }

    private void displayToast(String s)
    {
        Toast toast = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM,0,0);
        toast.show();
    }

    private void viewLocations(){
        displayToast("Locations Requested");
        displayToast("Display Locations");

        Intent i = new Intent(MainActivity.this,GoogleMapsApiLocator.class);
        startActivity(i);

    }

    private void viewReport(){
        displayToast("Report Requested");
        displayToast("Display Report");
    }

    private void inputActivity(){
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);

    }

    private void triggerManualPing()
    {
        Intent i = new Intent( MainActivity.this, Location.class);
        startActivity(i);
    }

    private void addContacts()
    {
        Intent i = new Intent (MainActivity.this, ContactInfo.class);
        startActivity(i);
    }

    private void activateEmergency()
    {
        emergencyCoordinator.activateEmergency(getApplicationContext());
    }

}
