package expense.exp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.trusted.TrustedWebActivityDisplayMode;
import androidx.browser.trusted.TrustedWebActivityIntentBuilder;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import expense.exp.R;
import expense.exp.adapter.AdsPackageListAdapter;
import expense.exp.databinding.ActivityPlanListBinding;
import expense.exp.helper.SharedPrefManager;
import expense.exp.internet.ApiClient;
import expense.exp.internet.ApiInterface;
import expense.exp.model_class.AdsPackage;
import expense.exp.model_class.AdsPackageResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class AdsPackageListActivity extends AppCompatActivity {

    private RecyclerView plansRecycler;
    private ImageView back_icon;
    private TextView title;
    private List<AdsPackage>planLists;
    SharedPrefManager sharedPrefManager;
    ActivityPlanListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_plan_list);
        binding = ActivityPlanListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        sharedPrefManager = new SharedPrefManager(this);

        binding.title.setText("Ads Packages");
        getAdsPackages(sharedPrefManager.getuserinfo().getId());
        binding.backIcon.setOnClickListener(v -> finish());
    }

    @SuppressLint("CheckResult")
    public void getAdsPackages(String UserId) {
//        startAnim();
        ApiInterface apiService = ApiClient.getClient(this)
                .create(ApiInterface.class);
        apiService.getAdsPackages(UserId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<AdsPackageResponse>() {

                    @Override
                    public void onSuccess(AdsPackageResponse planListResponse) {

                        if (planListResponse.getStatus().matches("1")) {
////                            stopAnim();
//
                            planLists = new ArrayList<>();

                            planLists.addAll(planListResponse.getPackages());


                            final LinearLayoutManager layoutManager = new LinearLayoutManager(AdsPackageListActivity.this);
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            binding.plansRecycler.setLayoutManager(layoutManager);
                            AdsPackageListAdapter accountant_fragm = new AdsPackageListAdapter(planLists, AdsPackageListActivity.this, planListResponse.getMyplan(), (datumList, id, position) -> {
//                                String url = "https://sdb.topnotchhub.com/Advertisement/adPlans"+"/"+sharedPrefManager.getuserinfo().getId()+"/"+datumList.get(position).getId();
                                String url = "https://skipdaboxes.ca/Advertisement/adPlans"+"/"+sharedPrefManager.getuserinfo().getId()+"/"+datumList.get(position).getId();
                                openWeb(url);

                            });
                            binding.plansRecycler.setAdapter(accountant_fragm);

                            }

//
                    }

                    @Override
                    public void onError(Throwable e) {
//                        stopAnim();

                    }
                });
    }


    private  void openWeb(String url){


        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);


//        CustomTabColorSchemeParams darkModeColorScheme = new CustomTabColorSchemeParams.Builder()
//                .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
//                .setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
//                .build();
//
//        TrustedWebActivityIntentBuilder twaBuilder = new TrustedWebActivityIntentBuilder(Uri.parse(url))
//                .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
//                .setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
//                .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
//                .setColorSchemeParams(
//                        CustomTabsIntent.COLOR_SCHEME_DARK, darkModeColorScheme
//                )
//                .setDisplayMode(new TrustedWebActivityDisplayMode.DefaultMode());

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
//                TwaLauncher.CCT_FALLBACK_STRATEGY
//        );
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}