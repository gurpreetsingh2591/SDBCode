package expense.exp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.trusted.TrustedWebActivityDisplayMode;
import androidx.browser.trusted.TrustedWebActivityIntentBuilder;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;
import expense.exp.R;
import expense.exp.adapter.NotificationListAdapter;
import expense.exp.databinding.ActivityCropBinding;
import expense.exp.helper.SharedPrefManager;
import expense.exp.internet.ApiClient;
import expense.exp.internet.ApiInterface;
import expense.exp.model_class.NotificationList;
import expense.exp.model_class.NotificationListResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


public class NotificationActivity extends AppCompatActivity {
    private RecyclerView notiRecycler;
    private ImageView back_icon;
    private List<NotificationList> notificationLists;
    SharedPrefManager sharedPrefManager;
    private TextView empty_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
       // binding = ActivityCropBinding.inflate(getLayoutInflater());
       // setContentView(binding.getRoot());
        notiRecycler = findViewById(R.id.notiRecycler);
        back_icon = findViewById(R.id.back_icon);
        empty_txt = findViewById(R.id.empty_txt);
//        avi = findViewById(R.id.avi);
        sharedPrefManager = new SharedPrefManager(this);

//        getFolder(sharedPrefManager.getuserinfo().getType(),sharedPrefManager.getuserinfo().getId());

        getNotifications(sharedPrefManager.getuserinfo().getEmail());

        Log.e("UserType###########",sharedPrefManager.getuserinfo().getType());
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @SuppressLint("CheckResult")
    public void getNotifications(String email) {
//        startAnim();
        ApiInterface apiService = ApiClient.getClient(this)
                .create(ApiInterface.class);
        apiService.getNotifications(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<NotificationListResponse>() {

                    @Override
                    public void onSuccess(NotificationListResponse planListResponse) {

                        if (planListResponse.getStatus().matches("1")) {
////                            stopAnim();
//
                            notificationLists = new ArrayList<>();

                            notificationLists.addAll(planListResponse.getNotifications());


                            if (notificationLists.size()>0){
                                notiRecycler.setVisibility(View.VISIBLE);
                                empty_txt.setVisibility(View.GONE);

                            }
                            else{
                                notiRecycler.setVisibility(View.GONE);
                                empty_txt.setVisibility(View.VISIBLE);
                            }
                            final LinearLayoutManager layoutManager = new LinearLayoutManager(NotificationActivity.this);
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            notiRecycler.setLayoutManager(layoutManager);
//                            PlanListAdapter adapter = new PlanListAdapter(planLists,PlanListActivity.this,this);
                            NotificationListAdapter accountant_fragm = new NotificationListAdapter(notificationLists, NotificationActivity.this, new NotificationListAdapter.ClickListener() {
                                @Override
                                public void onPositionClicked(List<NotificationList> datumList, int id, int position) {


//                                    String url = "https://sdb.topnotchhub.com/Documents/Chat/"+notificationLists.get(position).getDoc_id();
                                    String url = "https://skipdaboxes.ca/Documents/Chat/"+notificationLists.get(position).getDoc_id();
                                    openWeb(url);

                                }


                            });
                            notiRecycler.setAdapter(accountant_fragm);

                        }

//
                    }

                    @Override
                    public void onError(Throwable e) {
//                        stopAnim();
                        empty_txt.setVisibility(View.VISIBLE);
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
}