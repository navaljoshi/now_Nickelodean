package com.example.facebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.RemoteException;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.RawContacts.Data;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.facebook.imagefromurl.ImageMenu;
import com.example.facebook.imagefromurl.PhoneContactAdapter;
import com.example.horizontalscroll.HorizontalListView;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.com.dina.oauth.instagram.InstagramApp;
import br.com.dina.oauth.instagram.InstagramApp.OAuthAuthenticationListener;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


public class MainActivity extends Activity implements OnItemClickListener {
	private CallbackManager callbackManager;

	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");

	private ImageView shareBtn;
	public Button mail;
    public Button twittr;
    public Button whatsapp;
    public Button inst;
    public Button dismis;
	public Button home;
	public Button skip;
    public Button button1= null;
	String ip ="";
	public Dialog dialog;
    public int i ;
    public ImageView myImage;
	public  int totalImageCount= 0;
	public int notFirstTime = 0;
    public int shareImg = 0;
	public int flag = 0;
	public int imageShare = 0;

	//ShareDialog shareDialog;
	String acccessToken="";

	Socket socket ;




	public static String   image_url[] = {"http://www.livefoods.co.uk/images/01%20small_locust.jpg","http://www.gettyimages.ca/gi-resources/images/Homepage/Category-Creative/UK/UK_Creative_462809583.jpg","http://www.quicksprout.com/images/foggygoldengatebridge.jpg","http://www.menucool.com/slider/prod/image-slider-2.jpg","http://i765.photobucket.com/albums/xx291/just-meller/national%20geografic/Birds-national-geographic-6873734-1600-1200.jpg"};

	PhoneContactAdapter adapter;
	public ArrayList<ImageMenu> imageListUri=new ArrayList<ImageMenu>();
	private HorizontalListView listview;
	public ImageView imageSelected;
	Bitmap bitmap;
	String imagePathSelected;
	private int screenLogin=1;
	private int screenAdapter=2;
	private int screenShare=3;
	private int currentScreen=0;

	public  ArrayList<SaveData> bitmapList = new ArrayList<SaveData>() ;
	public  ArrayList<SaveData> arrayListForAdapter = new ArrayList<SaveData>() ;
	public  ArrayList<SaveData> arrayListForAdapterNew = new ArrayList<SaveData>() ;

	private Context context = this;

	private RelativeLayout relativelayoutShare;
	private RelativeLayout relativelayoutImages;
	private RelativeLayout relativelayoutThank;
	private RelativeLayout relativelayoutMain;
	private ImageView login_thanks,logout_thanks;
	private LoginButton loginBtn;



	// for twitter integration


	/* Shared preference keys */
	private static final String PREF_NAME = "sample_twitter_pref";
	private static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	private static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	private static final String PREF_KEY_TWITTER_LOGIN = "is_twitter_loggedin";
	private static final String PREF_USER_NAME = "twitter_user_name";

	/* Any number for uniquely distinguish your request */
	public static final int WEBVIEW_REQUEST_CODE = 100;

	private ProgressDialog pDialog;

	private  Twitter twitter;
	private  RequestToken requestToken;

	private static SharedPreferences mSharedPreferences;

	private String consumerKey = null;
	private String consumerSecret = null;
	private String callbackUrl = null;
	private String oAuthVerifier = null;

	//ShareDialog shareDialog;



	public static String CONTACTS_NUMBER = "number";
	public static String CONTACTS_NAME = "name";

	public static int scrWidth;
	public static int scrHeight;


	private ProgressDialog pdForFacebook;
	private ProgressDialog pdForTwitter;

    public ImageView cnt;



	//public  Bitmap bitmap1,bitmap2,bitmap3,bitmap4;

	private Timer timerCreate;
	private TextView txtPlease;
	private Timer timerForSyncData;
	ArrayList<Integer> arayListIndexOld=new ArrayList<Integer>();
	ArrayList<Integer> arayListIndexNew=new ArrayList<Integer>();

	// If no touch be at main screen
    int j =0;
	CountDownTimer countDownTimer = new CountDownTimer(80000, 1000) { // 5 minutes timer

		public void onTick(long millisUntilFinished) {
			//TODO: Do something every second
			j++;
			Log.d("naval","second ->"+j);
		}

		public void onFinish() {
			Log.d("naval","timer Finished ");
             if(relativelayoutShare.getVisibility() ==  View.VISIBLE) {
				 home();
			 }

		}
	}.start();


	@Override
	public boolean onTouchEvent(MotionEvent e) {
		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			countDownTimer.cancel();
			countDownTimer.start();
		}
		Log.d("naval","TABLET touch");
		return super.onTouchEvent(e);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


		/*  call Pop up dialog for filling IP*/


		dialog = new Dialog(this);
		FacebookSdk.sdkInitialize(getApplicationContext());
		initTwitterConfigs();

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);


		setContentView(R.layout.activity_main);
		context=MainActivity.this;
		//showHashKey(context);
		currentScreen=screenLogin;
		callbackManager = CallbackManager.Factory.create();
		mSharedPreferences = getSharedPreferences(PREF_NAME, 0);

		File files = new File(android.os.Environment.getExternalStorageDirectory(), "nikalodean");

		if (files.isDirectory()) {
			File[] listFile1 = files.listFiles();

			if (listFile1 != null) {
				int j;
				for (j = 0; j < listFile1.length; j++) {
					System.out.println(listFile1[j].getAbsolutePath());
					System.out.println(listFile1[j].delete());
				}
			}

			//send trigger to anant

		}

		//shareBtn = (ImageView)findViewById(R.id.fb_share_button);
        /* MAIL button*/
		mail= (Button) findViewById(R.id.image_email);
		mail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mail1();
				countDownTimer.cancel();
				countDownTimer.start();
			}
		});
        /*TWITTER Button*/
        twittr= (Button) findViewById(R.id.image_twitter);
        twittr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tweet();
				countDownTimer.cancel();
				countDownTimer.start();
            }
        });
        /*Instagram Button*/
       /* inst= (Button) findViewById(R.id.image_instagram);
        inst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insta();
            }
        });*/
        /*Whatsapp Button*/
        whatsapp = (Button) findViewById(R.id.image_whatsapp);
        whatsapp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				whats();
				countDownTimer.cancel();
				countDownTimer.start();
			}
		});

		home =(Button) findViewById(R.id.home);
		home.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				home();
			}
		});




 		listview=(HorizontalListView) findViewById(R.id.listview);
		//imageSelected=(ImageView)findViewById(R.id.image_view);
		relativelayoutShare=(RelativeLayout) findViewById(R.id.layout_second);
        relativelayoutImages=(RelativeLayout) findViewById(R.id.layout_images);
		relativelayoutThank=(RelativeLayout) findViewById(R.id.layout_thankscreen);
		relativelayoutMain=(RelativeLayout) findViewById(R.id.layout_main);

		login_thanks = (ImageView)findViewById(R.id.image);
		logout_thanks = (ImageView)findViewById(R.id.image_signout);
		loginBtn= (LoginButton) findViewById(R.id.image_facebook);
		txtPlease= (TextView) findViewById(R.id.txtTitlePlease);
		relativelayoutThank.setVisibility(View.GONE);
		relativelayoutShare.setVisibility(View.GONE);
		relativelayoutImages.setVisibility(View.VISIBLE);


		if(timerForSyncData==null)
		{
			timerForSyncData=new Timer();
		}






		timerForSyncData.schedule(new TimerTask() {


			@Override
			public void run() {
				// TODO Auto-generated method stub

				try
				{
					runOnUiThread(new Runnable() {

						@Override
						public void run() {

							try
							{
								DownloadFileFromURL downFile = new DownloadFileFromURL();

								downFile.execute();
							}
							catch(Exception e)
							{

							}
						}



					});
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}, 100, 2000);

		DownloadFileFromURL downFile = new DownloadFileFromURL();

		downFile.execute();

		adapterActivity();
	}

	public String getIp()
	{
		File sdcard = Environment.getExternalStorageDirectory();

//Get the text file
		File file = new File(sdcard,"mytextfile.txt");

//Read text from file
		StringBuilder text = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;

			while ((line = br.readLine()) != null) {
				text.append(line);
				text.append('\n');
			}
			br.close();
		}
		catch (IOException e) {
			//You'll need to add proper error handling here
		}

		return text.toString();
	}
	public void sendToPort() throws IOException {
		Socket socket = null;
		OutputStreamWriter osw;
		String str = "click";
		String ip = getIp();
		Log.d("naval","ip read :"+ip);
		try {
			socket = new Socket(ip,4014);
			osw =new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
			osw.write(str, 0, str.length());
		} catch (IOException e) {
			System.err.print(e);
		}  finally {
			socket.close();
		}

	}
public void  home() {
	relativelayoutThank.setVisibility(View.GONE);
	relativelayoutShare.setVisibility(View.GONE);
	home.setVisibility(View.INVISIBLE);
	relativelayoutImages.setBackground(getResources().getDrawable(R.drawable.preloader));
	relativelayoutImages.setVisibility(View.VISIBLE);
	Log.d("naval", "Home button clicked -- sending string to anant");
	try {
		sendToPort();
	} catch (Exception e) {
		e.printStackTrace();
	}

	// deleting all files if more then 1 when here

	File files = new File(android.os.Environment.getExternalStorageDirectory(), "nikalodean");

	if (files.isDirectory()) {
		File[] listFile1 = files.listFiles();

		if (listFile1 != null) {
			int j;
			for (j = 0; j < listFile1.length; j++) {
				System.out.println(listFile1[j].getAbsolutePath());
				System.out.println(listFile1[j].delete());
			}
		}

		//send trigger to anant

	}
}



	private void initTwitterConfigs() {
		consumerKey = getString(R.string.twitter_consumer_key);
		consumerSecret = getString(R.string.twitter_consumer_secret);
		callbackUrl = getString(R.string.twitter_callback);
		oAuthVerifier = getString(R.string.twitter_oauth_verifier);
	}


	public void shareClick(View view)
	{
		Log.d("naval", "share click function called");
		relativelayoutImages.setVisibility(View.GONE);
		relativelayoutThank.setVisibility(View.GONE);
		//home.setBackground(R.drawable.home);
		home.setVisibility(View.VISIBLE);
		relativelayoutShare.setVisibility(View.VISIBLE);
		//countDownTimer.start();

		currentScreen=screenShare;

		// strting a count down timer so to move to main screen if no touch event
	}

	public void facebook()
	{
		loginBtn.setPublishPermissions(PERMISSIONS);
		countDownTimer.cancel();
		countDownTimer.start();

		loginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
			@Override
			public void onSuccess(LoginResult loginResult) {
			//	Toast.makeText(MainActivity.this, "Login Successful!" +loginResult.getAccessToken(), Toast.LENGTH_LONG).show();

				try
				{
					publishImage(bitmap);
				}
				catch(OutOfMemoryError e)
				{

				}

			}

			@Override
			public void onCancel() {
				Toast.makeText(MainActivity.this, "Login cancelled by user!", Toast.LENGTH_LONG).show();
				System.out.println("Facebook Login failed!!");

			}

			@Override
			public void onError(FacebookException e) {
				Toast.makeText(MainActivity.this, "Login unsuccessful!", Toast.LENGTH_LONG).show();
				System.out.println("Facebook Login failed!!");
			}
		});

	}


	public void mail1()
	{
		File file=new File(imagePathSelected);

		Uri u = null;
		u = Uri.fromFile(file);

		Intent emailIntent = new Intent(Intent.ACTION_SEND);
		emailIntent.setType("image/*");
		emailIntent.putExtra(Intent.EXTRA_SUBJECT, "#NickelodeanFunTravels");
		// + "\n\r" + "\n\r" +
		// feed.get(Selectedposition).DETAIL_OBJECT.IMG_URL
		emailIntent.putExtra(Intent.EXTRA_TEXT, "#NickelodeanFunTravels " );
		emailIntent.putExtra(Intent.EXTRA_STREAM, u);
		PackageManager pm = getPackageManager();
		final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
		ResolveInfo best = null;
		for (final ResolveInfo info : matches) {
			if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail")) {
				best = info;
				break;
			}
		}
		if (best != null) {
			emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
		}
		startActivity(emailIntent);


	}

    public void tweet()
    {
        Log.d("naval9","tweet fn enter");
        pdForTwitter = new ProgressDialog(MainActivity.this);
        pdForTwitter.setCancelable(false);
        pdForTwitter.setMessage("Login...");
        pdForTwitter.show();
        boolean isLoggedIn = mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
        if (isLoggedIn) {
            //logOutOfTwitter();
			Log.d("naval10","logged in - shd not be here");

        } else {


            Uri uri = getIntent().getData();
			Log.d("naval11","tweet fn enter");

			if( uri == null)
			{
				Log.d("naval12","URI in tweet is NULL");
			}

            if (uri != null && uri.toString().startsWith(callbackUrl)) {
				Log.d("naval13","Enter the main tweet leg");
                String verifier = uri.getQueryParameter(oAuthVerifier);

                try {

						/* Getting oAuth authentication token */
                    AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

						/* Getting user id form access token */
                    long userID = accessToken.getUserId();
                    final User user = twitter.showUser(userID);
                    final String username = user.getName();

						/* save updated token */
                    saveTwitterInfo(accessToken);



                } catch (Exception e) {
                    Log.e("FailedtologinTwitter!!", e.getMessage());
                }
            }Log.d("naval14","Login to twitter last fn call");
            loginToTwitter();

        }


    }

    public void insta()
    {
        if (isPackageInstalled("com.instagram.android", this))
        {
            alertDialogForInstgramLogout();
        }
        else
        {
            Toast.makeText(MainActivity.this, "Please install Instagram...", Toast.LENGTH_LONG).show();
        }

    }

    public void whats()
    {
        addContactDialog(this);
    }

	private InstagramApp mApp;
	public void iconClick(View view)
	{
		switch (view.getId()) {
			case R.id.image_facebook:

				loginBtn.setPublishPermissions(PERMISSIONS);

				loginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
					@Override
					public void onSuccess(LoginResult loginResult) {
						Toast.makeText(MainActivity.this, "Login Successful!" +loginResult.getAccessToken(), Toast.LENGTH_LONG).show();

						try
						{
							publishImage(bitmap);
						}
						catch(OutOfMemoryError e)
						{

						}

					}

					@Override
					public void onCancel() {
						Toast.makeText(MainActivity.this, "Login cancelled by user!", Toast.LENGTH_LONG).show();
						System.out.println("Facebook Login failed!!");

					}

					@Override
					public void onError(FacebookException e) {
						Toast.makeText(MainActivity.this, "Login unsuccessful!", Toast.LENGTH_LONG).show();
						System.out.println("Facebook Login failed!!");
					}
				});

				break;
		/*	case R.id.image_instagram:
				if (isPackageInstalled("com.instagram.android", this))
				{
					alertDialogForInstgramLogout();
				}
				else
				{
					Toast.makeText(MainActivity.this, "Please install Instagram...", Toast.LENGTH_LONG).show();
				}



				break;*/
			case R.id.image_twitter:

				pdForTwitter = new ProgressDialog(MainActivity.this);
				pdForTwitter.setCancelable(false);
				pdForTwitter.setMessage("Login...");
				pdForTwitter.show();
				boolean isLoggedIn = mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
				if (isLoggedIn) {
					//logOutOfTwitter();

				} else {


					Uri uri = getIntent().getData();

					if (uri != null && uri.toString().startsWith(callbackUrl)) {

						String verifier = uri.getQueryParameter(oAuthVerifier);

						try {

						/* Getting oAuth authentication token */
							AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

						/* Getting user id form access token */
							long userID = accessToken.getUserId();
							final User user = twitter.showUser(userID);
							final String username = user.getName();

						/* save updated token */
							saveTwitterInfo(accessToken);



						} catch (Exception e) {
							Log.e("FailedtologinTwitter!!", e.getMessage());
						}
					}
					loginToTwitter();

				}





				break;
			case R.id.image_whatsapp:
				addContactDialog(this);

				break;
			case R.id.image_email:
				File file=new File(imagePathSelected);

				Uri u = null;
				u = Uri.fromFile(file);

				Intent emailIntent = new Intent(Intent.ACTION_SEND);
				emailIntent.setType("image/*");
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "#NickelodeanFunTravels ");
				// + "\n\r" + "\n\r" +
				// feed.get(Selectedposition).DETAIL_OBJECT.IMG_URL
				emailIntent.putExtra(Intent.EXTRA_TEXT, "#NickelodeanFunTravels");
				emailIntent.putExtra(Intent.EXTRA_STREAM, u);
				PackageManager pm = getPackageManager();
				final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
				ResolveInfo best = null;
				for (final ResolveInfo info : matches) {
					if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail")) {
						best = info;
						break;
					}
				}
				if (best != null) {
					emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
				}
				startActivity(emailIntent);

				break;
			case R.id.image_signout:
				//System.out.println("sign out" +);

				boolean isLoggedInn = mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
				if(isLoggedInn)
				{
					logOutOfTwitter();
				}
				relativelayoutThank.setVisibility(View.GONE);
				relativelayoutShare.setVisibility(View.VISIBLE);
				home.setVisibility(View.VISIBLE);

				break;

			default:
				break;
		}
	}



	OAuthAuthenticationListener listener = new OAuthAuthenticationListener() {

		@Override
		public void onSuccess() {
			Toast.makeText(MainActivity.this, "Connected ", Toast.LENGTH_SHORT).show();
			if(mApp.hasAccessToken())
			{
				File file =new File(imagePathSelected);
				createInstagramIntent(file);
			}
		}

		@Override
		public void onFail(String error) {
			Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
		}
	};



	public boolean isPackageInstalled(String packagename, Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	public void logOutOfTwitter(){

		Editor e = mSharedPreferences.edit();

		e.putString(PREF_KEY_OAUTH_TOKEN, null);
		e.putString(PREF_KEY_OAUTH_SECRET, null);
		e.putBoolean(PREF_KEY_TWITTER_LOGIN, false);
		e.putString(PREF_USER_NAME, null);
		e.commit();

		requestToken=null;



	}


	private void loginToTwitter() {
		boolean isLoggedIn = mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);

		if (!isLoggedIn) {
			final ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setOAuthConsumerKey(consumerKey);
			builder.setOAuthConsumerSecret(consumerSecret);

			final Configuration configuration = builder.build();
			final TwitterFactory factory = new TwitterFactory(configuration);
			twitter = factory.getInstance();

			try {
				requestToken = twitter.getOAuthRequestToken(callbackUrl);

				/**
				 *  Loading twitter login page on webview for authorization
				 *  Once authorized, results are received at onActivityResult
				 *  */

				if(pdForTwitter!=null && pdForTwitter.isShowing())
				{
					pdForTwitter.dismiss();
					pdForTwitter=null;
				}
				final Intent intent = new Intent(this, WebViewActivity.class);
				intent.putExtra(WebViewActivity.EXTRA_URL, requestToken.getAuthenticationURL());
				startActivityForResult(intent, 500);

			} catch (TwitterException e) {
				e.printStackTrace();
			}
		} else {


			if(pdForTwitter!=null && pdForTwitter.isShowing())
			{
				pdForTwitter.dismiss();
				pdForTwitter=null;
			}

		}
	}



	private void saveTwitterInfo(AccessToken accessToken) {

		long userID = accessToken.getUserId();

		User user;
		try {
			user = twitter.showUser(userID);

			String username = user.getName();

			/* Storing oAuth tokens to shared preferences */
			Editor e = mSharedPreferences.edit();
			e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
			e.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
			e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
			e.putString(PREF_USER_NAME, username);
			e.commit();

		} catch (TwitterException e1) {
			e1.printStackTrace();
		}
	}


	public void adapterActivity()
	{
		currentScreen=screenAdapter;
		relativelayoutThank.setVisibility(View.GONE);
		relativelayoutShare.setVisibility(View.GONE);
		home.setVisibility(View.GONE);
		relativelayoutImages.setVisibility(View.VISIBLE);

		listview.setOnItemClickListener(this);


	}



	private void selectImage()
	{
		if(Build.VERSION.SDK_INT >20)
		{
			Intent intent2 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
			intent2.addCategory(Intent.CATEGORY_OPENABLE);
			intent2.setType("image/*");
			startActivityForResult(intent2, 1);
		}
		else if (Build.VERSION.SDK_INT <19){
			Intent intent1 = new Intent();
			intent1.setType("image/*");
			intent1.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(intent1,
					"Select Picture"), 100);

		}
	}

	boolean isRunning=false;

	private  String getPath(Uri uri)
	{
		// just some safety built in
		if( uri == null )
		{
			// TODO perform some logging or show user feedback
			return null;
		}
		// try to retrieve the image from the media store first
		// this will only work for images selected from gallery
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if( cursor != null ){
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		return uri.getPath();
	}


	ShareDialog shareDialog;

	private void publishImage(Bitmap bitmap){
		SharePhotoContent content = null;
		pdForFacebook = new ProgressDialog(MainActivity.this);
		pdForFacebook.setCancelable(false);
		pdForFacebook.setMessage("Please Wait..");
		pdForFacebook.show();
		isRunning=false;


		SharePhoto photo = new SharePhoto.Builder()
				.setBitmap(bitmap)

				.build();



		content = new SharePhotoContent.Builder()
				.addPhoto(photo)
				.build();



		ShareApi.share(content, new FacebookCallback<Sharer.Result>() {
			@Override
			public void onSuccess(Sharer.Result result)
			{
				if(pdForFacebook!=null && pdForFacebook.isShowing())
				{
					pdForFacebook.dismiss();
					pdForFacebook=null;
				}
				// Toast.makeText(getApplicationContext(),"Showing picture posted on following account.", Toast.LENGTH_SHORT).show();
				// loginBtn.
				relativelayoutShare.setVisibility(View.GONE);
				home.setVisibility(View.GONE);
				relativelayoutImages.setVisibility(View.GONE);
				login_thanks.setImageResource(R.drawable.facebook_login);
				logout_thanks.setImageResource(R.drawable.facebook_logout);
				logout_thanks.setVisibility(View.GONE);
				txtPlease.setVisibility(View.GONE);
				relativelayoutThank.setVisibility(View.VISIBLE);


				if(timerCreate==null)
				{
					timerCreate=new Timer();
				}


				timerCreate.schedule(new TimerTask() {


					@Override
					public void run() {
						// TODO Auto-generated method stub

						try
						{


							runOnUiThread(new Runnable() {

								@Override
								public void run() {

									try
									{
										if(!isRunning)
										{
											relativelayoutThank.setVisibility(View.GONE);
											relativelayoutShare.setVisibility(View.VISIBLE);
											home.setVisibility(View.VISIBLE);
											isRunning=true;

										}
										else
										{
											timerCreate.cancel();
											timerCreate=null;

										}
									}
									catch(Exception e)
									{

									}
								}



							});
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}, 1000, 500);



			}

			@Override
			public void onCancel()
			{
				Log.v("FACEBOOK_TEST", "share api cancel");
				if(pdForFacebook!=null && pdForFacebook.isShowing())
				{
					pdForFacebook.dismiss();
					pdForFacebook=null;
				}
			}

			@Override
			public void onError(FacebookException e)
			{
				Log.v("FACEBOOK_TEST", "share api error " + e);
				if(pdForFacebook!=null && pdForFacebook.isShowing())
				{
					pdForFacebook.dismiss();
					pdForFacebook=null;
				}
				Toast.makeText(getApplicationContext(),"Error in share", Toast.LENGTH_SHORT).show();
			}
		});


	}

	@Override
	protected void onActivityResult(int reqCode, int resCode, Intent data) {

		System.out.println(" in activty result" +resCode);
		if(callbackManager!=null)
		{
			callbackManager.onActivityResult(reqCode, resCode, data);
		}



		switch(reqCode)
		{
			case 500:
				try {
					String verifier = data.getExtras().getString(oAuthVerifier);
					AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

					long userID = accessToken.getUserId();
					final User user = twitter.showUser(userID);
					String username = user.getName();

					saveTwitterInfo(accessToken);
					//String text = "I love Jabong ramp. <a href=\"http://www.google.com\">#jabongLFW#lakmefashionweek</a>.";

					//String text="I love Jabong ramp"+"<a href='http://www.pintelapp.com/content/androidtos.html'>#jabongLFW#lakmefashionweek</a>.";

					new updateTwitterStatus().execute("#NickelodeanFunTravels");



				} catch (Exception e) {
					//Log.e("Twitter Login Failed", e.getMessage());
				}
				break;

			case 1000:
				thanksWhatsappDialog();


				break;
			case 5000:



				break;
			case 6000:


				break;
		}


	}

	private void alertDialogForInstgramLogout()
	{
		AlertDialog.Builder builderInst = new AlertDialog.Builder(this);
		builderInst.setTitle("NOTE");
		builderInst.setMessage("Please logout of instagram once your images are posted.");
		builderInst.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();

						if(imagePathSelected!=null)
						{
							File file =new File(imagePathSelected);
							createInstagramIntent(file);
						}
						else
						{
							Toast.makeText(MainActivity.this, "You have not Selected any Image..", Toast.LENGTH_SHORT).show();
						}


					}
				});


		// create alert dialog
		AlertDialog alertDialogIns = builderInst.create();
		// show it
		alertDialogIns.show();

	}


	class updateTwitterStatus extends AsyncTask<String, String, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			try
			{
				pDialog = new ProgressDialog(MainActivity.this);
				pDialog.setMessage("Posting to twitter...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				if(pDialog!=null)
				{
					pDialog.show();
				}
			}
			catch(Exception e)
			{

			}
		}

		protected Void doInBackground(String... args) {


			String status = args[0];
			try {
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(consumerKey);
				builder.setOAuthConsumerSecret(consumerSecret);

				// Access Token
				String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
				// Access Token Secret
				String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");

				AccessToken accessToken = new AccessToken(access_token, access_token_secret);
				Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

				// Update status
				StatusUpdate statusUpdate = new StatusUpdate(status);


				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
				byte[] bitmapdata = bos.toByteArray();
				ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);

				statusUpdate.setMedia("test.jpg", bs);

				twitter4j.Status response = twitter.updateStatus(statusUpdate);

				Log.d("Status", response.getText());

			}
			catch(OutOfMemoryError e)
			{

			}
			catch (TwitterException e) {
				Log.d("Failed to post!", e.getMessage());
			}
			catch(Exception e)
			{

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			/* Dismiss the progress dialog after sharing */
			try
			{
				pDialog.dismiss();
			}
			catch(Exception e)
			{

			}

			Toast.makeText(MainActivity.this, "Posted to Twitter!", Toast.LENGTH_SHORT).show();

			// Clearing EditText field


			relativelayoutShare.setVisibility(View.GONE);
			home.setVisibility(View.GONE);
			relativelayoutImages.setVisibility(View.GONE);
			logout_thanks.setVisibility(View.VISIBLE);
			txtPlease.setVisibility(View.VISIBLE);
			login_thanks.setImageResource(R.drawable.twitter_login);
			logout_thanks.setImageResource(R.drawable.twitter_logout);
			relativelayoutThank.setVisibility(View.VISIBLE);


		}

	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		// TODO Auto-generated method stub


		try
		{
			//imageSelected.setVisibility(View.VISIBLE);
		   //shareBtn.setVisibility(View.VISIBLE);
			bitmap=arrayListForAdapter.get(position).getBitmap();
			imagePathSelected=arrayListForAdapter.get(position).getImagePath();
            Log.d("naval", "got file -> Image path :" + imagePathSelected);
			//imageSelected.setImageBitmap(bitmap);
			currentScreen=screenShare;

            final Dialog dialog1 = new Dialog(this);
			dialog1.setContentView(R.layout.coach_mark1);

            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog1.getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
			dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog1.setCanceledOnTouchOutside(false);
			dialog1.setCancelable(false);

			dialog1.setOnCancelListener(
					new DialogInterface.OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							//When you touch outside of dialog bounds,
							//the dialog gets canceled and this method executes..
							home();
						}
					}
			);
          //for dismissing anywhere you touch



			myImage = (ImageView) dialog1.findViewById(R.id.coach_marks_image1);
            if(myImage == null) {
                Log.d("naval", "Bitmap NULL");
            }
			myImage.setImageBitmap(bitmap);
            dialog1.show();

            dismis = (Button) dialog1.findViewById(R.id.share);



            dismis.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					dialog1.dismiss();
					listview.setVisibility(View.INVISIBLE);
					relativelayoutImages.setBackground(getResources().getDrawable(R.drawable.preloader));
					button1.setEnabled(true);
					shareClick(view);
					Log.d("naval", "Inside Share Button function");

				}
			});


          //  overlay2(imagePathSelected);
		}
		catch(OutOfMemoryError e)
		{

		}

	}

	class DownloadFileFromURL extends AsyncTask<String, String, String>
	{
		//ProgressDialog mProgressDialog;
		File[] listFile;

		int  fileLength;
		String fileSubstring;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// mProgressDialog = ProgressDialog.show(MainActivity.this, "", "Downloading Images...\nPlease wait...", false);
			// mProgressDialog.setCancelable(false);
		}

		@Override
		protected String doInBackground(String... URL)
		{

			if(bitmapList.size()>0)
			{
				bitmapList.clear();
			}

			File file= new File(android.os.Environment.getExternalStorageDirectory(),"nikalodean");

			if (file.isDirectory())
			{
				listFile = file.listFiles();
				if(notFirstTime == 0)
				{
					totalImageCount = listFile.length;
					notFirstTime = 1;
				}

				if(listFile.length == 0)
				{
					imageShare = 0; // if files are 0 no sharing or UI dialog to open
					Log.d("naval", "Image share set as 0");
				}
				//Log.d("naval", "File number " +listFile.length);
				if((0 != listFile.length)&& imageShare == 0 )
				{
					Log.d("naval", "flag value A " +flag);
					// set 2 flags here indicating we have an update , but we will skip this and in next turn wen count == lenth we will trigger.
					// then afterwards back to same.. smart logic here
                   if((flag == 1)) { // skipping first time
					   runOnUiThread(new Runnable() {
						   public void run() {
							   flag = 0;
							   imageShare  = 1;
							   Log.d("naval", "Image Share set as 1 and Flag as 0 ");

							   Log.d("naval", "Array Adapter list size :" + arrayListForAdapter.size());
							   Log.d("naval", "File List Size :" + listFile.length);

							   try {
								   imagePathSelected = listFile[listFile.length - 1].getAbsolutePath();

								   bitmap = arrayListForAdapter.get(0).getBitmap();
								   imagePathSelected = arrayListForAdapter.get(0).getImagePath();


								   Log.d("naval", "Image path :" + imagePathSelected);
								   //imageSelected.setImageBitmap(bitmap);
								   currentScreen = screenShare;

								   final Dialog dialog1 = new Dialog(MainActivity.this);

								   dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
								   dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

								   dialog1.setCanceledOnTouchOutside(false);
								   //for dismissing anywhere you touch
								   View masterView = dialog1.findViewById(R.id.coach_mark_master_view);
								   dialog1.setContentView(R.layout.coach_mark1);

								   myImage = (ImageView) dialog1.findViewById(R.id.coach_marks_image1);
								   if (myImage == null) {

								   }
								   myImage.setImageBitmap(bitmap);


								   dialog1.show();

								   dismis = (Button) dialog1.findViewById(R.id.share);
								   shareImg = 0;// resetting for next time so we can have update adapter

								   dismis.setOnClickListener(new View.OnClickListener() {
									   @Override
									   public void onClick(View view) {
										   dialog1.dismiss();
										   listview.setVisibility(View.INVISIBLE);
										   home.setVisibility(View.VISIBLE);
										   relativelayoutImages.setBackground(getResources().getDrawable(R.drawable.sharepic));
										   shareClick(view);

									   }
								   });

								   skip = (Button) dialog1.findViewById(R.id.skip);
								   skip.setOnClickListener(new View.OnClickListener() {
									   @Override
									   public void onClick(View view) {
										   dialog1.dismiss();
										   home();
										   Log.d("naval", "Inside Skip Button function");

									   }
								   });

							   } catch (OutOfMemoryError e) {

							   }

						   }
					   });
				   }else if((flag == 0 ))
					{flag = 1;
						totalImageCount = listFile.length;
				   Log.d("naval","flag  set -> Now dialog Shown -> flag value" + flag );}

				}



				for (int i = 0; i < listFile.length; i++)
				{
                    //h = listFile.length;
					SaveData saveData=new SaveData();
					saveData.setImagePath(listFile[i].getAbsolutePath());


					//saveData.setBitmap(BitmapFactory.decodeFile(listFile[i].getAbsolutePath()));

					bitmapList.add(saveData);

				}
			}

			return null;


		}

		/**
		 * Updating progress bar
		 * */
		protected void onProgressUpdate(String... progress) {
			// setting progress percentage
			//pDialog.setProgress(Integer.parseInt(progress[0]));
		}

		/**
		 * After completing background task
		 * Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String file_url) {

			if(arayListIndexOld.size()>0)
			{
				arayListIndexOld.clear();
			}


			for(int i=0;i<bitmapList.size();i++)
			{


				String filename=listFile[i].getAbsolutePath().substring(listFile[i].getAbsolutePath().lastIndexOf("/")+1);
				if(filename.trim().contains("nik.jpg"))
				{
					int  fileLength=filename.indexOf("nik.jpg");

					fileSubstring= filename.substring(0, fileLength);

				}
				arayListIndexOld.add(Integer.parseInt(fileSubstring));

			}
			if(arayListIndexNew.size()>0)
			{
				List<Integer> c = new ArrayList<Integer> (arayListIndexNew.size() >arayListIndexOld.size() ?arayListIndexNew.size():arayListIndexOld.size());
				c.addAll(arayListIndexNew);
				c.retainAll(arayListIndexOld);
				if(c.size()>0)
				{


					arayListIndexNew=arayListIndexOld;


					Collections.sort(arayListIndexNew);
					Collections.reverse(arayListIndexNew);
					ArrayList<String> arraPth=new ArrayList<String>();
					for(int i=0;i<1;i++)
					{
						for(int j=0;j<bitmapList.size();j++)
						{
							String filename=bitmapList.get(j).getImagePath().substring(bitmapList.get(j).getImagePath().lastIndexOf("/")+1);

							if(filename.trim().contains((arayListIndexNew.get(i)+"").trim()))
							{

								arraPth.add(bitmapList.get(j).getImagePath());

							}
						}
					}

					ArrayList<String>	 arrayPathNew=removeDupliciacy(arraPth);
					if(arrayListForAdapter.size()>0)
					{
						arrayListForAdapter.clear();
					}
					for(int i=0;i<arrayPathNew.size();i++)
					{

						try
						{
							SaveData saveData=new SaveData();
							saveData.setImagePath(arrayPathNew.get(i));

							saveData.setBitmap(BitmapFactory.decodeFile(arrayPathNew.get(i)));
							arrayListForAdapter.add(saveData);
							//Log.d("naval", "Array List Adapter updated for new image --> Path:" + saveData.getImagePath());

						}
						catch(OutOfMemoryError e)
						{

						}

					}



					try
					{
						if(arrayListForAdapter.size()>0)
						{
							adapter=new PhoneContactAdapter(context, arrayListForAdapter);
							listview.setAdapter(adapter);
							imageSelected.setImageURI(Uri.parse(arrayListForAdapter.get(0).getImagePath()));
							bitmap=arrayListForAdapter.get(0).getBitmap();
							imagePathSelected=arrayListForAdapter.get(0).getImagePath();
							adapter.notifyDataSetChanged();
							Log.d("naval", "adapter notify called" );

						}
					}

					catch(OutOfMemoryError e)
					{

					}catch(Exception e)
					{

					}


				}
				else
				{

				}
			}
			else
			{

				arayListIndexNew=arayListIndexOld;

				Collections.sort(arayListIndexNew);
				Collections.reverse(arayListIndexNew);

				ArrayList<String> arraPth=new ArrayList<String>();
				for(int i=0;i<1;i++)
				{
					for(int j=0;j<bitmapList.size();j++)
					{
						String filename=bitmapList.get(j).getImagePath().substring(bitmapList.get(j).getImagePath().lastIndexOf("/")+1);

						if(filename.trim().contains((arayListIndexNew.get(i)+"").trim()))
						{

							arraPth.add(bitmapList.get(j).getImagePath());


						}
					}
				}


				ArrayList<String>	 arrayPathNew=removeDupliciacy(arraPth);
				if(arrayListForAdapter.size()>0)
				{
					arrayListForAdapter.clear();
				}
				try
				{
					for(int i=0;i<arrayPathNew.size();i++)
					{

						SaveData saveData=new SaveData();
						saveData.setImagePath(arrayPathNew.get(i));
						saveData.setBitmap(BitmapFactory.decodeFile(arrayPathNew.get(i)));
						arrayListForAdapter.add(saveData);


					}





					if(arrayListForAdapter.size()>0)
					{
						adapter=new PhoneContactAdapter(context, arrayListForAdapter);
						listview.setAdapter(adapter);
						imageSelected.setImageURI(Uri.parse(arrayListForAdapter.get(0).getImagePath()));
						bitmap=arrayListForAdapter.get(0).getBitmap();
						imagePathSelected=arrayListForAdapter.get(0).getImagePath();
						adapter.notifyDataSetChanged();

					}
				}
				catch(OutOfMemoryError e)
				{

				}catch(Exception e)
				{

				}
			}

		}




	}


	private ArrayList<String> removeDupliciacy(
			ArrayList<String> hashStreetAddress) {
		// TODO Auto-generated method stub
		ArrayList<String> uniqueList = new ArrayList<String>();
		if (hashStreetAddress.size() > 0) {
			String[] array = new String[hashStreetAddress.size()];
			for (int i = 0; i < hashStreetAddress.size(); i++)
				array[i] = hashStreetAddress.get(i);
			uniqueList.add(array[0]);
			for (int i = 0; i < array.length; i++) {
				String items = array[i];
				boolean add = false;
				for (int j = 0; j < uniqueList.size(); j++) {
					if (items.equalsIgnoreCase(uniqueList.get(j)))
						add = true;
				}
				if (!add)
					uniqueList.add(items);
			}
		}
		return uniqueList;
	}



	//Bitmap bitmapDownloaded;
	private SaveData bitmapStoreInSDCard(Bitmap bmp,String path,
										 String nameOfImageAlongWithPath) throws Exception {
		System.out.println("path" +nameOfImageAlongWithPath +path);
		SaveData savedata = null;
		try
		{
			File sd = new File(nameOfImageAlongWithPath);


			if (!sd.exists()) {
				sd.mkdirs();



			}


			//it contain your path of image..im using a temp string..
			String filename=path.substring(path.lastIndexOf("/")+1);
			String fullpath=sd+"/"+filename;

			FileOutputStream fos = new FileOutputStream(fullpath);

			bmp.compress(Bitmap.CompressFormat.JPEG, 50 /* ignored for JPEG */,
					fos);

			fos.close();
			fos.flush();
			if(bmp!=null)
			{
				bmp.recycle();
				bmp=null;
			}

			File file=new File(Environment.getExternalStorageDirectory()+ "/nikalodean", filename);
			BitmapFactory.Options opt=new BitmapFactory.Options();
			opt.inSampleSize = 8;
			opt.inJustDecodeBounds = false;
			opt.inDither = false;
			opt.inPurgeable = true;
			opt.inInputShareable = true;
			opt.inTempStorage = new byte[16*2024];


			//bitmapDownloaded=;

			savedata=new SaveData();
			savedata.setImagePath(fullpath);
			savedata.setBitmap(BitmapFactory.decodeFile(file.getAbsolutePath(), opt));
		}
		catch(Exception e)
		{

		}

		return savedata;

	}



	public InputStream OpenHttpConnection(String urlString) throws IOException//i.e url=http://google.image./
	{
		InputStream in = null;
		int response = -1;

		URL url = new URL(urlString);
		URLConnection conn = url.openConnection();

		if (!(conn instanceof HttpURLConnection))
			throw new IOException("Not an HTTP connection");

		try{
			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();

			response = httpConn.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK) {
				in = httpConn.getInputStream();
			}
		}
		catch (Exception ex)
		{
			throw new IOException("Error connecting");
		}
		return in;
	}


	public  boolean haveNetworkConnection(Context context)
	{
		boolean	haveConnectedWifi = false;
		boolean	haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo[] netInfo = cm.getAllNetworkInfo();

		for (NetworkInfo ni : netInfo)
		{
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
			{
				if (ni.isConnected())
				{
					haveConnectedWifi = true;
					break;
				}
			}

			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
			{
				if (ni.isConnected())
				{
					haveConnectedMobile = true;
					break;
				}
			}
		}
		return haveConnectedWifi || haveConnectedMobile;

	}


	public boolean checkNetworkConnection(Context context)
	{
		boolean pinged = false;

		if(haveNetworkConnection(context))
		{


			pinged=true;
		}
		else
		{

		}


		System.out.println("pingggggg     "+pinged);



		return pinged;
	}







	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		if(currentScreen==screenLogin)
		{
			//finish();
		}
		else if(currentScreen==screenAdapter)
		{
			//listview.setVisibility(View.INVISIBLE);

			currentScreen=screenLogin;
			finish();
		}
		else if(currentScreen==screenShare)
		{
			adapterActivity();
		}
	}



	private void createInstagramIntent(File file) {

		if (isPackageInstalled("com.instagram.android", this)) {

			String caption = "Test Caption";
			// Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			String imageUri = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "test_image", caption);
			System.out.println("uri" +imageUri);
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("image/*"); // set mime type
			//shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(file.getAbsolutePath()));
			shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imageUri));
			shareIntent.putExtra(Intent.EXTRA_TEXT, caption);
			shareIntent.setPackage("com.instagram.android");
			startActivityForResult(shareIntent,6000);




		} else {
			final String url = "https://play.google.com/store/apps/details?id=com.instagram.android&hl=en";
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Alert");
			builder.setMessage("Please install Instagram.");
			builder.setPositiveButton("Install",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
							Intent i = new Intent(Intent.ACTION_VIEW);
							i.setData(Uri.parse(url));
							startActivity(i);
						}
					});
			builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();

						}
					});
			// create alert dialog
			AlertDialog alertDialog = builder.create();
			// show it
			alertDialog.show();
		}
	}


	public void clearApplicationData() {
		File cache = new File("/data/data/com.instagram.android/cache");
		//new File("/data/data/com.instagram.android/cache");
		File appDir = new File(cache.getParent());
		if(appDir.exists()){
			String[] children = {"lib", "files", "shared_prefs", "cache"};
			for(String s : children){
				if(!s.equals("lib")){
					deleteDir(new File(appDir, s));
					Log.i("TAG", "File /data/data/APP_PACKAGE/" + s +" DELETED");
				}
			}
		}
	}

	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = {"lib", "files", "shared_prefs", "cache"};

			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		return dir.delete();
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}


	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();

	}


	public void addContactDialog(final Context context)
	{

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Add Contact");
		builder.setMessage("Please add your number as a contact ");
		builder.setPositiveButton("NEXT",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();



						generateAddContactDialog() ;



					}
				});



		// create alert dialog
		AlertDialog alertDialog = builder.create();
		// show it
		alertDialog.show();


	}



	AlertDialog.Builder  contactAddDialog=null;
	EditText editName;
	EditText editNumber;
	String contactName,contactNumber;

	void generateAddContactDialog()
	{

		contactAddDialog = new AlertDialog.Builder(this);
		LayoutInflater layoutInflater = (LayoutInflater)this.getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.phone_contact, null);
		contactAddDialog.setTitle("Add Contact");

		// Add action buttons
		contactAddDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {



				dialog.dismiss();
				contactName=editName.getText().toString().trim();
				contactNumber=editNumber.getText().toString().trim();
				WritePhoneContact(editName.getText().toString().trim(),editNumber.getText().toString().trim(),MainActivity.this);
			}
		});


		contactAddDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {



				dialog.dismiss();
				thanksWhatsappDialog();

			}
		});


		contactAddDialog.setView(view);
		contactAddDialog.create();
		//pinGenerateDialog.setContentView(view);

		contactAddDialog.show();


		initilize(view);


	}



	private void initilize(View view)
	{


		editName =(EditText)view.findViewById(R.id.myname);
		editNumber=(EditText)view.findViewById(R.id.mycontact);

	}

	public void WritePhoneContact(String displayName, String number,Context cntx /*App or Activity Ctx*/)
	{
		Context contetx 	= cntx; //Application's context or Activity's context
		String strDisplayName 	=  displayName; // Name of the Person to add
		String strNumber 	=  number; //number of the person to add with the Contact

		ArrayList<ContentProviderOperation> cntProOper = new ArrayList<ContentProviderOperation>();
		int contactIndex = cntProOper.size();//ContactSize

		//Newly Inserted contact
		// A raw contact will be inserted ContactsContract.RawContacts table in contacts database.
		cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)//Step1
				.withValue(RawContacts.ACCOUNT_TYPE, null)
				.withValue(RawContacts.ACCOUNT_NAME, null).build());

		//Display name will be inserted in ContactsContract.Data table
		cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step2
				.withValueBackReference(Data.RAW_CONTACT_ID,contactIndex)
				.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
				.withValue(StructuredName.DISPLAY_NAME, strDisplayName) // Name of the contact
				.build());
		//Mobile number will be inserted in ContactsContract.Data table
		cntProOper.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)//Step 3
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID,contactIndex)
				.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
				.withValue(Phone.NUMBER, strNumber) // Number to be added
				.withValue(Phone.TYPE, Phone.TYPE_MOBILE).build()); //Type like HOME, MOBILE etc
		try
		{
			// We will do batch operation to insert all above data
			//Contains the output of the app of a ContentProviderOperation.
			//It is sure to have exactly one of uri or count set
			ContentProviderResult[] contentProresult = null;
			contentProresult = contetx.getContentResolver().applyBatch(ContactsContract.AUTHORITY, cntProOper);
			refreshContactDialog();//apply above data insertion into contacts list
		}
		catch (RemoteException exp)
		{
			Toast.makeText(getApplicationContext(), "Your Contact is not saved", Toast.LENGTH_LONG).show();
			//logs;
		}
		catch (OperationApplicationException exp)
		{
			//logs
			Toast.makeText(getApplicationContext(), "Your Contact is not saved", Toast.LENGTH_LONG).show();
		}
	}


	public void refreshContactDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//w builder.setTitle("Warning");
		builder.setMessage("Contact added succesfully !Please refresh contact list in whatsapp.");
		builder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
						try
						{
							if(isPackageInstalled("com.whatsapp",context)){
								File file = null;

								if(imagePathSelected!=null)
								{
									file =new File(imagePathSelected);
								}

								Intent whatsappIntent = new Intent(android.content.Intent.ACTION_SEND);
								whatsappIntent.setType("image/*");
								whatsappIntent.putExtra(Intent.EXTRA_TEXT, "#NickelodeanFunTravels");
								whatsappIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(file.getAbsolutePath()));


								whatsappIntent.setPackage("com.whatsapp");
								startActivityForResult(Intent.createChooser(whatsappIntent, "Share Image"), 1000);


							}else{

								Toast.makeText(getApplicationContext(), "Please Install Whatsapp", Toast.LENGTH_LONG).show();
							}
						}
						catch(Exception e)
						{

					}


					}
				});


		// create alert dialog
		AlertDialog alertDialog = builder.create();
		// show it
		alertDialog.show();



	}


	public void thanksWhatsappDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//w builder.setTitle("Warning");
		builder.setMessage("Please reply with a thanks to encourage us");
		builder.setPositiveButton("Done",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
						deleteContact(contactName);
					}
				});


		// create alert dialog
		AlertDialog alertDialog = builder.create();
		// show it
		alertDialog.show();



	}


	public void ImagePostedFacebookDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		//w builder.setTitle("Warning");
		builder.setMessage("Showing picture posted on following account.");
		builder.setPositiveButton("ok",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});


		// create alert dialog
		AlertDialog alertDialog = builder.create();
		// show it
		alertDialog.show();



	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try
		{
			if(timerCreate!=null)
			{
				timerCreate.cancel();
				timerCreate=null;
			}
			if(timerForSyncData!=null)
			{
				timerForSyncData.cancel();
				timerForSyncData=null;
			}
		}
		catch(Exception e)
		{

		}
	}


	private void deleteContact(String name) {

		ContentResolver cr = getContentResolver();
		String where = ContactsContract.Data.DISPLAY_NAME + " = ? ";
		String[] params = new String[] {name};

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		ops.add(ContentProviderOperation.newDelete(ContactsContract.RawContacts.CONTENT_URI)
				.withSelection(where, params)
				.build());
		try {
			cr.applyBatch(ContactsContract.AUTHORITY, ops);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OperationApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Toast.makeText(MainActivity.this, "Deleted the contact with name '" + name +"'", Toast.LENGTH_SHORT).show();

	}
}
