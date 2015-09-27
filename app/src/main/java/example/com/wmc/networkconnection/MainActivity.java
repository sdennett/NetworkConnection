package example.com.wmc.networkconnection;

/**
 * http://www.tutorialspoint.com/android/android_network_connection.htm
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity {
    private ProgressDialog progressDialog;
    private Bitmap bitmap = null;
    private final ArrayList<Bitmap> bitmapArray = new ArrayList<>();
    Button b1 ,b2, b3, b4, b5, b6;
    private final int SOFTDRINKS = 0;
    private final int GOLF = 7;
    private final int TRAINS = 14;
    private final int BEERS = 21;
    private final int MIN = 1;
    private final int MAX = 4;
    private int THEME = BEERS;
    private Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int rn = rand.nextInt((MAX - MIN) + 1) + MIN;
        Toast.makeText(getApplicationContext(),"Random = " + rn, Toast.LENGTH_LONG).show();
        switch (rn) {
            case 1:
                THEME = SOFTDRINKS;
                break;
            case 2:
                THEME = GOLF;
                break;
            case 3:
                THEME = TRAINS;
                break;
            case 4:
                THEME = BEERS;
                break;
            default:
                THEME = BEERS;
        }

        b1 = (Button) findViewById(R.id.button1);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);
        b5 = (Button) findViewById(R.id.button5);
        b6 = (Button) findViewById(R.id.button6);
        TextView screenTitle = (TextView) findViewById(R.id.textView);

        String[] url = getResources().getStringArray(R.array.urls);
        String[] bn = getResources().getStringArray(R.array.buttons);

        screenTitle.setText(bn[THEME + 0]);
        b1.setText(bn[THEME + 1]);
        b2.setText(bn[THEME + 2]);
        b3.setText(bn[THEME + 3]);
        b4.setText(bn[THEME + 4]);
        b5.setText(bn[THEME + 5]);
        b6.setText(bn[THEME + 6]);

        downloadImage(url[THEME + 0]);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkInternetConenction();
                String[] url = getResources().getStringArray(R.array.urls);
                downloadImage(url[THEME + 1]);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkInternetConenction();
                String[] url = getResources().getStringArray(R.array.urls);
                downloadImage(url[THEME + 2]);
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkInternetConenction();
                String[] url = getResources().getStringArray(R.array.urls);
                downloadImage(url[THEME + 3]);
            }
        });
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkInternetConenction();
                String[] url = getResources().getStringArray(R.array.urls);
                downloadImage(url[THEME + 4]);
            }
        });
        b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkInternetConenction();
                String[] url = getResources().getStringArray(R.array.urls);
                downloadImage(url[THEME + 5]);
            }
        });
        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checkInternetConenction();
                String[] url = getResources().getStringArray(R.array.urls);
                downloadImage(url[THEME + 6]);
            }
        });
    }

    private Bitmap downloadImage(String urlStr) {
        progressDialog = ProgressDialog.show(this, "", "Downloading Image . . .");
        final String url = urlStr;

        new Thread() {
            public void run() {
                InputStream in = null;

                Message msg = Message.obtain();
                msg.what = 1;

                try {
                    in = openHttpConnection(url);
                    bitmap = BitmapFactory.decodeStream(in);
                    Bundle b = new Bundle();
                    b.putParcelable("bitmap", bitmap);
                    msg.setData(b);
                    in.close();
                }

                catch (IOException e1) {
                    e1.printStackTrace();
                }
                messageHandler.sendMessage(msg);
            }
        }.start();
        return bitmap;
    }

    private InputStream openHttpConnection(String urlStr) {
        InputStream in = null;
        int resCode = -1;

        try {
            URL url = new URL(urlStr);
            URLConnection urlConn = url.openConnection();

            if (!(urlConn instanceof HttpURLConnection)) {
                throw new IOException("URL is not an Http URL");
            }
            HttpURLConnection httpConn = (HttpURLConnection) urlConn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            resCode = httpConn.getResponseCode();

            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        }

        catch (MalformedURLException e) {
            e.printStackTrace();
        }

        catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }

    private Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ImageView img = (ImageView) findViewById(R.id.imageView);
            img.setImageBitmap((Bitmap) (msg.getData().getParcelable("bitmap")));
            progressDialog.dismiss();
        }
    };

    private boolean checkInternetConenction() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||

                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            //Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        }else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }
}
