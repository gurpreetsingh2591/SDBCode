package expense.exp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import expense.exp.R;
import expense.exp.databinding.ActivityCropBinding;
import expense.exp.databinding.ActivityWebBinding;

public class WebActivity extends AppCompatActivity {

    ActivityWebBinding binding;
    String type, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_web);
        binding = ActivityWebBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        type = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("ownerId");
        binding.webView1.getSettings().setJavaScriptEnabled(true);

//        String url = "https://sdb.topnotchhub.com/Documents/Chat/"+id;
        String url = "https://skipdaboxes.ca/Documents/Chat/" + id;
        if (type.equals("chat")) {
            binding.webView1.loadUrl(url);

        } else {
//            webView.loadUrl("https://sdb.topnotchhub.com/ppolicy");
            binding.webView1.loadUrl("https://skipdaboxes.ca/ppolicy");

        }
    }
}
