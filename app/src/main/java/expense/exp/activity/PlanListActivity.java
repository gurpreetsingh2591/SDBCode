package expense.exp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.List;
import expense.exp.R;
import expense.exp.adapter.PlanListAdapter;
import expense.exp.databinding.ActivityCropBinding;
import expense.exp.databinding.ActivityPlanListBinding;
import expense.exp.helper.SharedPrefManager;
import expense.exp.internet.ApiClient;
import expense.exp.internet.ApiInterface;
import expense.exp.model_class.PlanList;
import expense.exp.model_class.PlanListResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class PlanListActivity extends AppCompatActivity {

//    private AVLoadingIndicatorView avi;

    private List<PlanList>planLists;
    SharedPrefManager sharedPrefManager;
    ActivityPlanListBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_plan_list);
        binding = ActivityPlanListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPrefManager = new SharedPrefManager(this);

        getFolder(sharedPrefManager.getuserinfo().getType(),sharedPrefManager.getuserinfo().getId());

        Log.e("UserType###########",sharedPrefManager.getuserinfo().getType());
        binding.backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @SuppressLint("CheckResult")
    public void getFolder(String userType, String UserId) {
//        startAnim();
        ApiInterface apiService = ApiClient.getClient(this)
                .create(ApiInterface.class);
        apiService.getPlans(userType,UserId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<PlanListResponse>() {

                    @Override
                    public void onSuccess(PlanListResponse planListResponse) {

                        if (planListResponse.getStatus().matches("1")) {
////                            stopAnim();
//
                            planLists = new ArrayList<>();

                            planLists.addAll(planListResponse.getPackages());


                            final LinearLayoutManager layoutManager = new LinearLayoutManager(PlanListActivity.this);
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            binding.plansRecycler.setLayoutManager(layoutManager);
//                            PlanListAdapter adapter = new PlanListAdapter(planLists,PlanListActivity.this,this);
                            PlanListAdapter accountant_fragm = new PlanListAdapter(planLists, PlanListActivity.this, planListResponse.getMyplan(),new PlanListAdapter.ClickListener() {
                                @Override
                                public void onPositionClicked(List<PlanList> datumList, int id, int position) {

//                                    Intent intent = new Intent(PlanListActivity.this, WebViewActivity.class);
//                                    intent.putExtra("package_id",datumList.get(position).getId());
//                                    intent.putExtra("user_id",sharedPrefManager.getuserinfo().getId());
//                                    startActivity(intent);
//                                  finish();

//                                    String url = "https://sdb.topnotchhub.com/Payments/accountPlans"+"/"+sharedPrefManager.getuserinfo().getId()+"/"+datumList.get(position).getId();
                                    String url = "https://skipdaboxes.ca/Payments/accountPlans"+"/"+sharedPrefManager.getuserinfo().getId()+"/"+datumList.get(position).getId();

                                    openWeb(url);

                                }

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