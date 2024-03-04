package expense.exp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import expense.exp.R;
import expense.exp.fragment.AdsDetailsFragment;
import expense.exp.helper.Utils;


public class AdsDetailsActivity extends AppCompatActivity {

    public AdsDetailsFragment adsDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_ads);

        adsDetailsFragment = new AdsDetailsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.frameContainer, adsDetailsFragment,
                            Utils.AdsDetails_Fragment).commit();

            if (getIntent() != null)
                adsDetailsFragment.setIntent(getIntent());
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameContainer);
        fragment.onActivityResult(requestCode, resultCode, data);
    }
}
