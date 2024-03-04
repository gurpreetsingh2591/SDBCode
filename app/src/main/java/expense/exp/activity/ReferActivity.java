package expense.exp.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import expense.exp.R;
import expense.exp.databinding.ActivityCropBinding;
import expense.exp.helper.SharedPrefManager;
import expense.exp.internet.model.User;


public class ReferActivity extends AppCompatActivity {
    private ImageView back_icon;
    private TextView tv_referral_code;
    private Button btn_share_referral, btn_referral_copy;
    SharedPrefManager sharedPrefManager;
    private String userReferalLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);
       // binding = ActivityCropBinding.inflate(getLayoutInflater());
        //setContentView(binding.getRoot());
        init();
    }

    private void init() {

        back_icon = findViewById(R.id.back_icon);
        tv_referral_code = findViewById(R.id.tv_referral_code);
        btn_share_referral = findViewById(R.id.btn_share_referral);
        btn_referral_copy = findViewById(R.id.btn_referral_copy);

        sharedPrefManager = new SharedPrefManager(this);
        User user = sharedPrefManager.getuserinfo();
        userReferalLink = sharedPrefManager.getReferalLink();
        tv_referral_code.setText(user.getMycode());

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_share_referral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                share(user.getMycode());
            }
        });

        btn_referral_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText(getString(R.string.referral_code_copied), tv_referral_code.getText().toString());
                clipboard.setPrimaryClip(clip);
            }
        });


    }


    private void share(String refferal) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        String shareMessage= userReferalLink;
        refferal = shareMessage+"\n\nRefferal Code - "+refferal;
        intent.putExtra(Intent.EXTRA_TEXT, refferal);
        startActivity(Intent.createChooser(intent, null));
    }

}