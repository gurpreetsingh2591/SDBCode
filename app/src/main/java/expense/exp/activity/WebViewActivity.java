package expense.exp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.trusted.TrustedWebActivityDisplayMode;
import androidx.browser.trusted.TrustedWebActivityIntentBuilder;
import androidx.core.content.ContextCompat;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;



import expense.exp.R;
import expense.exp.databinding.ActivityCropBinding;
import expense.exp.databinding.ActivityWebViewBinding;

public class WebViewActivity extends AppCompatActivity {

    private String package_id,user_id;
    ActivityWebViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_web_view);
        binding = ActivityWebViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        user_id = getIntent().getStringExtra("user_id");
        package_id = getIntent().getStringExtra("package_id");


        binding.webView1.setWebViewClient(new myWebClient());
        binding.backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        String url = "https://sdb.topnotchhub.com/Payments/accountPlans"+"/"+user_id+"/"+package_id;
        String url = "https://skipdaboxes.ca/Payments/accountPlans"+"/"+user_id+"/"+package_id;


        binding.webView1.getSettings().setJavaScriptEnabled(true);
        binding.webView1.getSettings().getDomStorageEnabled();
        binding.webView1.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        binding.webView1.loadUrl(url);


        CustomTabColorSchemeParams darkModeColorScheme = new CustomTabColorSchemeParams.Builder()
                .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .build();

        TrustedWebActivityIntentBuilder twaBuilder = new TrustedWebActivityIntentBuilder(Uri.parse(url))
                .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
                .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
                .setColorSchemeParams(
                        CustomTabsIntent.COLOR_SCHEME_DARK, darkModeColorScheme
                )
                .setDisplayMode(new TrustedWebActivityDisplayMode.DefaultMode());

//        TwaLauncher mTwaLauncher = new TwaLauncher(this);
//        mTwaLauncher.launch(
//                twaBuilder,
//                null,
//                new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                },
//        TwaLauncher.CCT_FALLBACK_STRATEGY
//        );
    }


    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

        }
    }
}