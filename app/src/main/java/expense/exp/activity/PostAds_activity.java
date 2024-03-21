package expense.exp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import expense.exp.R;
import expense.exp.databinding.ActivityCropBinding;
import expense.exp.databinding.ActivityDocumentViewBinding;
import expense.exp.databinding.ActivityPostAdsBinding;
import expense.exp.fragment.PostAds_Fragment;
import expense.exp.helper.Utils;


public class PostAds_activity extends AppCompatActivity {

    private static FragmentManager fragmentManager;
    ActivityPostAdsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_post_ads);

        binding = ActivityPostAdsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fragmentManager = getSupportFragmentManager();

        // If savedinstnacestate is null then replace login fragment
        if (savedInstanceState == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frameContainer, new PostAds_Fragment(),
                            Utils.PostAds_Fragment).commit();
        }


    }

    // Replace Login Fragment with animation
    public void replaceLoginFragment() {
        fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                .replace(R.id.frameContainer, new PostAds_Fragment(),
                        Utils.Login_Fragment).commit();
    }

    @Override
    public void onBackPressed() {

//        // Find the tag of signup and forgot password fragment
//        Fragment SignUp_Fragment = fragmentManager
//                .findFragmentByTag(Utils.SignUp_Fragment);
//        Fragment ForgotPassword_Fragment = fragmentManager
//                .findFragmentByTag(Utils.ForgotPassword_Fragment);
//
//        // Check if both are null or not
//        // If both are not null then replace login fragment else do backpressed
//        // task
//
//        if (SignUp_Fragment != null)
//            replaceLoginFragment();
//        else if (ForgotPassword_Fragment != null)
//            replaceLoginFragment();
//        else
            super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameContainer);
        fragment.onActivityResult(requestCode, resultCode, data);
    }
}
