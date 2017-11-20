package com.synopsis;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import io.fabric.sdk.android.Fabric;

/*import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;*/

public class SignInPage extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    EditText edtemail, edtpassword;
    TextView txtskip, fpassword, error;
    ImageView imgmenu;
    RelativeLayout rl_login, rlfb_login, rlgplush_login, rltwitter_login, idaccountsignin;
    String Password;
    String emailPattern = "[a-zA-Z0-9.]+@[a-z]+\\.+[a-z]+";
    String Message, UserId;
    JSONArray responce = null;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    LoginButton btnMainUserInfo;
    private ProgressDialog pdia;
    String UserName = "", Fname = "", Mname = "", Lname = "", Birthday = "", Email = "", Id = "", address = "", profilePicUrl = "";
    private UiLifecycleHelper uiHelper;
    String access_tocken;
    Button btnsend, btncancel;
    String message;
    RelativeLayout rlheader;

    ////////////twitter
    private static final String TWITTER_KEY = "Dq8gu32s4u6lz6uTdD9gEalIV";
    private static final String TWITTER_SECRET = "Qg1tFNv98gBqptevWGeeE6Mhey1bRU1IEawawEmcusastwA6Wh";

    Long userid;
    TwitterSession session;
    private TwitterLoginButton twitterloginButton;
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

    DBHelper mydb;
    boolean internet = false;
    String NewsId = "", NewsTitle = "", NewsSummary = "", NewsVideo = "", NewsImage = "", NewsDate = "", News_source = "", news_like, News_url, Share_url, published_by = "", cattype;
    JSONArray news = null;
    Boolean like_status;
    View skipview;
    String Insert_DateTime;

    Cursor c, c_background;
    byte[] imagebitmap, imagebitmap_background;
    String NewsId_background, NewsImage_background;
    Convertimage convert_image;
    int newslength_background;
    InputStream input;
    Thread mythread;
    boolean GoInstruction = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getKeyHash();
        uiHelper = new UiLifecycleHelper(SignInPage.this, statusCallback);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.sign_in_page);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor0 = sharedpreferences.edit();
        editor0.putBoolean("firstscreen", true);
        editor0.putBoolean("second", true);
        editor0.commit();
        edtemail = (EditText) findViewById(R.id.edtemail);
        edtpassword = (EditText) findViewById(R.id.edtpassword);
        txtskip = (TextView) findViewById(R.id.txtskip);
        fpassword = (TextView) findViewById(R.id.fpassword);
        imgmenu = (ImageView) findViewById(R.id.imgmenu);
        rl_login = (RelativeLayout) findViewById(R.id.rllogin);
        idaccountsignin = (RelativeLayout) findViewById(R.id.idaccountsignin);
        rlfb_login = (RelativeLayout) findViewById(R.id.rlfb);
        rlgplush_login = (RelativeLayout) findViewById(R.id.rlgplush);
        rltwitter_login = (RelativeLayout) findViewById(R.id.rltwitter);
        skipview = (View) findViewById(R.id.idview);
        rl_login.setOnClickListener(this);
        rlfb_login.setOnClickListener(this);
        rlgplush_login.setOnClickListener(this);
        rltwitter_login.setOnClickListener(this);
        btnMainUserInfo = (LoginButton) findViewById(R.id.btnfb);
        twitterloginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        rlheader = (RelativeLayout) findViewById(R.id.rlheader);
        rlheader.setBackgroundColor(Color.argb(200, 255, 255, 255));
//        btnMainUserInfo.setFragment(this);
        btnMainUserInfo.setReadPermissions(Arrays.asList("email,public_profile,user_birthday"));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        if (!cd.isConnectingToInternet()) {
            internet = false;
        } else {
            internet = true;
        }

        mythread = new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                convert_image = new Convertimage();
                convert_image.execute();
            }
        });
        mythread.start();
        txtskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("CatId", "0");
                editor.putString("CateName", "All News");
                editor.putBoolean("firstscreen", true);
                editor.putBoolean("second", true);
                editor.putBoolean("skip", true);
                editor.commit();
                if (convert_image != null) {
                    convert_image.cancel(true);
                }
                GoInstruction = true;

                Intent in = new Intent(SignInPage.this, InstructionsPage.class);
                startActivity(in);
                SignInPage.this.finish();
            }
        });
        fpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idaccountsignin.setBackgroundResource(R.color.xyz);
                LayoutInflater inflaterr = SignInPage.this.getLayoutInflater();
                View popupVieww = inflaterr.inflate(R.layout.popup, null);
                final PopupWindow popupWindoww = new PopupWindow(popupVieww, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                btncancel = (Button) popupVieww.findViewById(R.id.btncancel);
                btnsend = (Button) popupVieww.findViewById(R.id.btnsend);
                edtemail = (EditText) popupVieww.findViewById(R.id.edtemail);
                NumberKeyListener emailinput = new NumberKeyListener() {

                    public int getInputType() {
                        return InputType.TYPE_MASK_VARIATION;
                    }

                    @Override
                    protected char[] getAcceptedChars() {
                        return new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '@', '.', '_'};
                    }
                };
                edtemail.setKeyListener(emailinput);
                error = (TextView) popupVieww.findViewById(R.id.txterror);
                btncancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        //					rltop.setAlpha(1F);
                        idaccountsignin.setBackgroundResource(R.color.white);
                        popupWindoww.dismiss();
                    }
                });
                btnsend.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Email = edtemail.getText().toString();
                        if (Email.trim().length() < 1) {
                            //						edtemail.requestFocus();
                            //						edtemail.setText("");
                            //						edtemail.setError("Enter Email Address");
                            error.setText("Please fill email address");
                            error.setTextColor(Color.RED);
                            error.setVisibility(View.VISIBLE);
                        } else {

                            //						popupWindow.dismiss();
                            new ForgotProgress().execute();
                            idaccountsignin.setAlpha(1F);
                            idaccountsignin.setBackgroundResource(R.color.white);
                            popupWindoww.dismiss();

                        }
                        //					rltop.setAlpha(1F);
                        //					rltop.setBackgroundResource(R.color.white);
                        //					popupWindoww.dismiss();
                    }
                });
                popupWindoww.showAtLocation(popupVieww, ViewGroup.LayoutParams.WRAP_CONTENT, 0, 180);
                popupWindoww.setFocusable(true);
                popupWindoww.update();

            }
        });
        rlheader.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(SignInPage.this, CategoryandSetting.class);
                startActivity(intent);
                return false;
            }
        });
        NumberKeyListener emailinput = new NumberKeyListener() {

            public int getInputType() {
                return InputType.TYPE_MASK_VARIATION;
            }

            @Override
            protected char[] getAcceptedChars() {
                return new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '@', '.', '_'};
            }
        };
        edtemail.setKeyListener(emailinput);

        edtemail.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edtemail.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        edtpassword.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edtpassword.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        twitterloginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                Log.d("Twitter ", "Login sucessfull");
                session = result.data;
                UserName = session.getUserName();
                userid = session.getUserId();

                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;
                // TODO: Remove toast and use the TwitterSession's userID
                Twitter.getApiClient(session).getAccountService()
                        .verifyCredentials(true, false, new Callback<User>() {

                            @Override
                            public void failure(TwitterException e) {

                            }

                            @Override
                            public void success(Result<User> userResult) {

                                User user = userResult.data;
                                profilePicUrl = user.profileImageUrl;
                                Email = user.email;
                                if (UserName.equalsIgnoreCase("")) {
                                    UserName = user.name;
                                }
                                new TwitterProgress().execute();
                            }

                        });
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
    }

    //----------------------------G_PLUSH===========================


    //====================
    private void getKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("Keyhash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));//XRt1Im/ckYIrqWSc2Ev4kap5I14=

            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    private Session.StatusCallback statusCallback = new Session.StatusCallback() {

        @Override

        public void call(Session session, SessionState state,

                         Exception exception) {

            if (state.isOpened()) {
                //Session session = Session.getActiveSession();
                if (session.isOpened()) {
                    //Toast.makeText(MainActivity2.this, session.getAccessToken(), Toast.LENGTH_LONG).show();


                    access_tocken = session.getAccessToken();
                    new FBProgressBar().execute();
                    btnMainUserInfo.performClick();


                }
                Log.d("MainActivity", "Facebook session opened.");

            } else if (state.isClosed()) {

                Log.d("MainActivity", "Facebook session closed.");
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                int fblogin = sharedpreferences.getInt("fblogin", 0);
//				if(fblogin==0)
//					.fMainActivity2.thisinish();

            }
        }

    };


    class FBProgressBar extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(SignInPage.this);
            pdia.setMessage("Loading...");
            pdia.show();
            pdia.setCancelable(false);
            // Log.e("pre","execute");
        }

        @Override
        protected String doInBackground(String... f_url) {
            //Log.e("do","execute");
            //makeGetRequest();


            JSONParser jParser = new JSONParser();
            // getting JSON string from URL
            JSONObject json = jParser.getJSONFromUrl("https://graph.facebook.com/me?fields=id,email,name,first_name,last_name,middle_name,birthday,gender,picture.type(large)&access_token=" + access_tocken);
            try {
                // Getting Array of Employee
                //								payment = json.getJSONArray("payment");
                int length = json.length();
                // looping of List
                for (int i = 0; i < 1; i++) {
                    //									JSONObject c = payment.getJSONObject(i);

                    // Storing each json item in variable
                    if (json.has("name")) {
                        UserName = json.getString("name");
                    }
                    if (json.has("first_name")) {
                        Fname = json.getString("first_name");
                    }
                    if (json.has("middle_name")) {
                        Mname = json.getString("middle_name");
                    }
                    if (json.has("last_name")) {
                        Lname = json.getString("last_name");
                    }

                    if (json.has("email")) {
                        Email = json.getString("email");
                    }
                    if (json.has("birthday")) {
                        Birthday = json.getString("birthday");
                    }
                    if (json.has("address")) {
                        address = json.getString("address");
                    }
                    if (json.has("id")) {
                        Id = json.getString("id");
                    }
                    if (json.has("picture")) {
                        profilePicUrl = json.getJSONObject("picture").getJSONObject("data").getString("url");

                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {

//			pdia.dismiss();
//			pdia = null;
            //fblogin=1;

            new FbProgress().execute();


        }
    }

    public class FbProgress extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            // getting JSON string from URL//fname,lname,email,pwd,pwd2,zipcode,month,day,subscriber
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "register.php?account_type=facebook&first_name=" + URLEncoder.encode(Fname) + "&last_name=" + URLEncoder.encode(Lname) + "&email=" + URLEncoder.encode(Email) + "&location=" + Splash_Page.country_name);
            try {
                // Getting Array of Employee
                responce = json.getJSONArray("success");
                int length = responce.length();
                // looping of List
                for (int i = 0; i < length; i++) {
                    JSONObject c = responce.getJSONObject(i);
                    UserId = c.getString("user_id");
                    Message = c.getString("message");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pdia.dismiss();
            pdia = null;
            SharedPreferences.Editor editor = sharedpreferences.edit();
            if (UserId.length() > 5) {
                editor.putInt("LoginId", 1);
            } else {
                editor.putInt("LoginId", Integer.parseInt(UserId));
            }
            editor.putString("email", UserName);
            editor.putString("CatId", "0");
            editor.putString("CateName", "All News");
            editor.putBoolean("firstscreen", true);
            editor.putBoolean("second", true);
            editor.putString("LoginType", "facebook");
            editor.putString("profile_image", profilePicUrl);
            editor.commit();
            if (convert_image != null) {
                convert_image.cancel(true);
            }
            SignInPage.this.finish();
            Intent in = new Intent(SignInPage.this, InstructionsPage.class);
            startActivity(in);
        }
    }

    public class TwitterProgress extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            // getting JSON string from URL//fname,lname,email,pwd,pwd2,zipcode,month,day,subscriber
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "register.php?account_type=twitter&first_name=" + URLEncoder.encode(UserName) + "&last_name=" + URLEncoder.encode(UserName) + "&email=" + URLEncoder.encode("@" + UserName) + "&location=" + Splash_Page.country_name);
            try {
                // Getting Array of Employee
                responce = json.getJSONArray("success");
                int length = responce.length();
                // looping of List
                for (int i = 0; i < length; i++) {
                    JSONObject c = responce.getJSONObject(i);
                    UserId = c.getString("user_id");
                    Message = c.getString("message");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            pdia.dismiss();
//            pdia = null;
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt("LoginId", Integer.parseInt(UserId));
            editor.putString("email", "@" + UserName);
            editor.putString("CatId", "0");
            editor.putString("CateName", "All News");
            editor.putBoolean("firstscreen", true);
            editor.putBoolean("second", true);
            editor.putString("LoginType", "twitter");
            editor.putString("profile_image", profilePicUrl);
            editor.commit();
            if (convert_image != null) {
                convert_image.cancel(true);
            }
            SignInPage.this.finish();
            Intent in = new Intent(SignInPage.this, InstructionsPage.class);
            startActivity(in);
        }
    }

    public class GplushProgress extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            // getting JSON string from URL//fname,lname,email,pwd,pwd2,zipcode,month,day,subscriber
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "register.php?account_type=google_plus&first_name=" + URLEncoder.encode(Fname) + "&last_name=" + URLEncoder.encode(Lname) + "&email=" + URLEncoder.encode(Email) + "&location=" + Splash_Page.country_name);
            try {
                // Getting Array of Employee
                responce = json.getJSONArray("success");
                int length = responce.length();
                // looping of List
                for (int i = 0; i < length; i++) {
                    JSONObject c = responce.getJSONObject(i);
                    UserId = c.getString("user_id");
                    Message = c.getString("message");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            pdia.dismiss();
//            pdia = null;
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putInt("LoginId", Integer.parseInt(UserId));
            editor.putString("email", UserName);
            editor.putString("CatId", "0");
            editor.putString("CateName", "All News");
            editor.putBoolean("firstscreen", true);
            editor.putBoolean("second", true);
            editor.putString("LoginType", "gplush");
            editor.putString("profile_image", profilePicUrl);
            editor.commit();
            signOut();
            if (convert_image != null) {
                convert_image.cancel(true);
            }
            SignInPage.this.finish();
            Intent in = new Intent(SignInPage.this, InstructionsPage.class);
            startActivity(in);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uiHelper.onActivityResult(requestCode, resultCode, data);
        twitterloginButton.onActivityResult(requestCode, resultCode, data);
        //Log.i(TAG, "OnActivityResult...");

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            UserName = acct.getDisplayName();
//                Fname = acct.getFamilyName();
//                Lname = acct.getGivenName();
            Email = acct.getEmail();
            Uri uri = acct.getPhotoUrl();
            if (uri != null)
                profilePicUrl = uri.toString();
//            tv_username.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            new GplushProgress().execute();


        } else {
            // Signed out, show unauthenticated UI.
            // updateUI(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rllogin:
                GoInstruction = true;
                Email = edtemail.getText().toString();
                Password = edtpassword.getText().toString();
                if (Email.equalsIgnoreCase("")) {
                    edtemail.requestFocus();
                    edtemail.setText("");
                    edtemail.setError("Please fill email");
                } else {
                    if (!Email.matches(emailPattern)) {
                        edtemail.requestFocus();
                        edtemail.setText("");
                        edtemail.setError("Invalid email id.");
                    } else {
                        if (Password.equalsIgnoreCase("")) {
                            edtpassword.requestFocus();
                            edtpassword.setText("");
                            edtpassword.setError("Please fill password");
                        } else {
                            ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
                            if (!cd.isConnectingToInternet()) {
                                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
                            } else {
                                new LoginProgress().execute();
                            }

                        }
                    }
                }

                break;
            case R.id.rlfb:
                GoInstruction = true;
                ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
                if (!cd.isConnectingToInternet()) {
                    Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
                } else {
                    btnMainUserInfo.performClick();
                }

                break;
            case R.id.rlgplush:
                GoInstruction = true;
                ConnectionDetector cd1 = new ConnectionDetector(getApplicationContext());
                if (!cd1.isConnectingToInternet()) {
                    Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
                } else {
                    signIn();
                }
                break;
            case R.id.rltwitter:
                GoInstruction = true;
                ConnectionDetector cd2 = new ConnectionDetector(getApplicationContext());
                if (!cd2.isConnectingToInternet()) {
                    Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
                } else {
                    PackageManager pkManager = getPackageManager();
                    try {
                        PackageInfo pkgInfo = pkManager.getPackageInfo("com.twitter.android", 0);
                        String getPkgInfo = pkgInfo.toString();

                        if (getPkgInfo.contains("com.twitter.android")) {
                            twitterloginButton.performClick();
                        } else {
                            Toast.makeText(SignInPage.this, "Please,firstly insttal the twitter app on your device.", Toast.LENGTH_LONG).show();
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(SignInPage.this, "Please,firstly insttal the twitter app on your device.", Toast.LENGTH_LONG).show();
                        // APP NOT INSTALLED

                    }
                }

                break;
            case R.id.sign_in_button:
                GoInstruction = true;
                ConnectionDetector cd3 = new ConnectionDetector(getApplicationContext());
                if (!cd3.isConnectingToInternet()) {
                    Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
                } else {
                    signIn();
                }


                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
//                        tv_username.setText("");
                    }
                });
    }

    public class LoginProgress extends AsyncTask<String, String, String> {
        ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(SignInPage.this);
            pdia.setMessage("Login authentication.");
            pdia.setCancelable(false);
            pdia.show();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONParser jParser = new JSONParser();
            // getting JSON string from URL//http://www.vantagewebtech.com/synopsis/api/register.php?email=vantage@gmail.com&password=ashok&account_type=register_via_app
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "register.php?email=" + URLEncoder.encode(Email) + "&password=" + Password + "&account_type=register_via_app" + "&location=" + Splash_Page.country_name);
            try {
                // Getting Array of Employee
                responce = json.getJSONArray("success");
                int length = responce.length();
                // looping of List
                for (int i = 0; i < length; i++) {
                    JSONObject c = responce.getJSONObject(i);
                    UserId = c.getString("user_id");
                    Message = c.getString("message");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pdia.dismiss();
            pdia = null;
            if (Integer.parseInt(UserId) == 0) {

                AlertDialog alertDialog = new AlertDialog.Builder(SignInPage.this, AlertDialog.THEME_HOLO_LIGHT).create();
                alertDialog.setTitle(Message);

                alertDialog.setIcon(R.drawable.launchicon);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.show();
            } else {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt("LoginId", Integer.parseInt(UserId));
                editor.putString("email", Email);
                editor.putString("CatId", "0");
                editor.putString("CateName", "All News");
                editor.putBoolean("firstscreen", true);
                editor.putBoolean("second", true);
                editor.putString("LoginType", "app");
                editor.commit();

                Toast.makeText(SignInPage.this, Message, Toast.LENGTH_LONG).show();
                SignInPage.this.finish();
                if (convert_image != null) {
                    convert_image.cancel(true);
                }
                Intent in = new Intent(SignInPage.this, InstructionsPage.class);
                startActivity(in);

            }
        }
    }

    public class ForgotProgress extends AsyncTask<String, String, String> {
        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pdia = new ProgressDialog(SignInPage.this);
            pdia.setMessage("Loading...");
            pdia.show();
            pdia.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            JSONParser jParser = new JSONParser();
            // getting JSON string from URL//checkuserforgotpwd=1,user_login=emailid
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "forgot-password.php?email=" + URLEncoder.encode(Email));
            try {
                // Getting Array of Employee
                responce = json.getJSONArray("success");
                int length = responce.length();
                // looping of List
                for (int i = 0; i < length; i++) {
                    JSONObject c = responce.getJSONObject(i);
                    message = c.getString("message");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pdia.dismiss();
            pdia = null;

            AlertDialog alertDialog = new AlertDialog.Builder(SignInPage.this, AlertDialog.THEME_HOLO_LIGHT).create();
            alertDialog.setTitle(message);

            alertDialog.setIcon(R.drawable.launchicon);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();
        }
    }


    public class Convertimage extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mydb = new DBHelper(SignInPage.this);
            c_background = mydb.getallnewsData();
            newslength_background = c_background.getCount();
        }

        @Override
        protected String doInBackground(String... params) {
            if (c_background != null) {
                c_background.moveToFirst();
            }
            if (newslength_background != 0) {
                do {

                    NewsId_background = c_background.getString(c_background.getColumnIndex("newsid"));
                    NewsImage_background = c_background.getString(c_background.getColumnIndex("newsimageurl"));
                    imagebitmap_background = c_background.getBlob(c_background.getColumnIndex("newsimage"));
                    try {
                        if (NewsImage_background.equalsIgnoreCase("")) {

                        } else {
                            if (NewsImage_background.contains("base64")) {
//                                String imageDataBytes = NewsImage_background.substring(NewsImage_background.indexOf(",") + 1);
//
//                                InputStream stream = new ByteArrayInputStream(Base64.decode(imageDataBytes.getBytes(), Base64.DEFAULT));
//                                BitmapFactory.Options opts = new BitmapFactory.Options();
//                                // opts.inJustDecodeBounds = true;
//                                opts.inSampleSize = 1;
//                                myBitmap = BitmapFactory.decodeStream(stream, null, opts);
                            } else if (NewsImage_background.contains("http://images")) {

                            } else {
                                if (imagebitmap_background == null) {

                                    ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
                                    if (cd.isConnectingToInternet()) {


                                        URL url = new URL(NewsImage_background);
                                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                        connection.setDoInput(true);
                                        connection.setInstanceFollowRedirects(false);
                                        connection.setRequestMethod("GET");
                                        connection.connect();

                                        if (connection.getResponseCode() == 200) {
                                            input = connection.getInputStream();

                                            BitmapFactory.Options opts = new BitmapFactory.Options();
                                            // opts.inJustDecodeBounds = true;
                                            opts.inSampleSize = 1;
                                            Bitmap bitmap = null;
                                            bitmap = BitmapFactory.decodeStream(input, null, opts);

                                            mydb.updateBitmap(NewsId_background, "All News", bitmap,"true");

                                            connection.disconnect();
                                        }
                                    }
                                }
                            }
                        }

                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    if (GoInstruction) {
                        break;
                    }

                } while (c_background.moveToNext());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

}
