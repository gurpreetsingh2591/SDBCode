package expense.exp.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.trusted.TrustedWebActivityDisplayMode;
import androidx.browser.trusted.TrustedWebActivityIntentBuilder;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import expense.exp.R;
import expense.exp.activity.AdsPackageListActivity;
import expense.exp.activity.ChangePassword;
import expense.exp.activity.Home_Activity;
import expense.exp.activity.Login_activity;
import expense.exp.activity.PlanListActivity;
import expense.exp.activity.ReferActivity;
import expense.exp.activity.SplashActivity;
import expense.exp.chat.Chat;
import expense.exp.databinding.LayoutFolderBinding;
import expense.exp.databinding.LayoutSettingBinding;
import expense.exp.helper.SharedPrefManager;

/*import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;*/
import expense.exp.internet.ApiClient;
import expense.exp.internet.ApiInterface;
import expense.exp.internet.model.GetAds;
import expense.exp.internet.model.User;
import expense.exp.model_class.Delete_Folfer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by admin on 06-07-2018.
 */

public class Setting_Fragment extends Fragment {
    View view;
    //Unbinder unbinder;

    // @BindView(R.id.cardview_changepassword)
    CardView cardview_changepassword;

    //@BindView(R.id.cardview_points)
    CardView cardview_points;

    // @BindView(R.id.cardview_refer)
    CardView cardview_refer;

    // @BindView(R.id.txtPoints)
    TextView txtPoints;
    SharedPrefManager sharedPrefManager;

    private int REQ_CODE = 100;


    // @BindView(R.id.cardview_logout)
    CardView cardview_logout;

    // @BindView(R.id.cardview_plans)
    CardView cardview_plans;

    // @BindView(R.id.cardview_chat)
    CardView cardview_chat;

    // @BindView(R.id.cardview_ads_package)
    CardView cardview_ads_package;

    // @BindView(R.id.cardviewAlerts)
    CardView cardviewAlerts;
    LayoutSettingBinding binding;
    String url = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // view = inflater.inflate(R.layout.layout_setting, container, false);
        binding = LayoutSettingBinding.inflate(inflater, container, false);

        sharedPrefManager = new SharedPrefManager(getContext());

        // unbinder = ButterKnife.bind(this, view);

        User user = sharedPrefManager.getuserinfo();

        url = "https://skipdaboxes.ca/Users/getAlerts/" + user.getId();
        binding.cardviewPoints.setVisibility(View.VISIBLE);
        binding.txtPoints.setText("My Points : "+user.getMypoints());

        if (sharedPrefManager.getuserinfo().getType().equals("user")) {
            binding.cardviewPoints.setVisibility(View.VISIBLE);
            binding.cardviewAdsPackage.setVisibility(View.VISIBLE);
            binding.cardviewAlerts.setVisibility(View.VISIBLE);
            binding.txtPoints.setText("My Points : " + user.getMypoints());
        } else {
            binding.cardviewPoints.setVisibility(View.GONE);
            binding.cardviewAdsPackage.setVisibility(View.GONE);
            binding.cardviewAlerts.setVisibility(View.GONE);
        }

        clickInit();
        return binding.getRoot();
    }

    public static Setting_Fragment newInstance() {
        return new Setting_Fragment();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // unbind the view to free some memory
        //  unbinder.unbind();
    }


     public void clickInit(){

        binding.cardviewChangepassword.setOnClickListener(v -> {
            changepassword(v);
        } );
        binding.cardviewLogout.setOnClickListener(v -> {
            logout(v);
        } );
        binding.cardviewPlans.setOnClickListener(v -> {
            plans(v);
        } );
        binding.cardviewChat.setOnClickListener(v -> {
            chat(v);
        } );
        binding.backIcon.setOnClickListener(v -> {
            back_press(v);
        } );
        binding.cardviewRefer.setOnClickListener(v -> {
            referEarn(v);
        } );
        binding.cardviewAdsPackage.setOnClickListener(v -> {
            adsPackage(v);
        } );
        binding.cardviewAlerts.setOnClickListener(v -> {
            alerts(v);
        } );
        binding.cardViewDeleteAccount.setOnClickListener(v -> {
            showAlert(getContext(),"Delete Account", "Are you sure to delete your account permanently");

        } );

     }

    public  void showAlert(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        // Positive Button (Yes)
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Action to perform when "Yes" is clicked
            Toast.makeText(context, "Yes clicked", Toast.LENGTH_SHORT).show();
            getDeleteUser(Integer.parseInt(sharedPrefManager.getuserinfo().getId()));
        });

        // Negative Button (No)
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Action to perform when "No" is clicked
                //Toast.makeText(context, "No clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    // @OnClick(R.id.cardview_changepassword)
    public void changepassword(View view) {

//        getActivity().startActivity(new Intent(getActivity(), ChangePassword.class));
        getActivity().startActivityForResult(new Intent(getActivity(), ChangePassword.class), REQ_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    // @OnClick(R.id.cardview_logout)
    public void logout(View view) {
        sharedPrefManager.logout();
        getActivity().startActivity(new Intent(getActivity(), Login_activity.class));
        getActivity().finish();

    }

    // @OnClick(R.id.cardview_plans)
    public void plans(View view) {

        getActivity().startActivity(new Intent(getActivity(), PlanListActivity.class));

    }

    //  @OnClick(R.id.cardview_chat)
    public void chat(View view) {

        Intent intent = new Intent(getActivity(), Chat.class);
        intent.putExtra("receiverID", "1");
        intent.putExtra("CHAT_ID", "123");
        intent.putExtra("Email", "jtest@gmail.com");
        intent.putExtra("Name", "Test");
        getActivity().startActivity(intent);

//        getActivity().startActivity(new Intent(getActivity(), ChatActivity.class));

    }

    // @OnClick(R.id.back_icon)
    public void back_press(View view) {

        Home_Activity.viewPager.setCurrentItem(2, true);
    }

    //  @OnClick(R.id.cardview_refer)
    public void referEarn(View view) {

        getActivity().startActivity(new Intent(getActivity(), ReferActivity.class));
    }

    // @OnClick(R.id.cardview_ads_package)
    public void adsPackage(View view) {

        getActivity().startActivity(new Intent(getActivity(), AdsPackageListActivity.class));

    }

    // @OnClick(R.id.cardviewAlerts)
    public void alerts(View view) {

        openWeb(url);

    }

    @SuppressLint("CheckResult")
    public void getDeleteUser(int user_id) {

        startAnim();

        ApiInterface apiService = ApiClient.getClient(getActivity())
                .create(ApiInterface.class);


        apiService.getDeleteUser(user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribeWith(new DisposableSingleObserver<Delete_Folfer>() {
                    @Override
                    public void onSuccess(Delete_Folfer deleteUser) {

                            stopAnim();
                        if (deleteUser.getStatus().matches("1")) {
                            Log.e("exeption", "=" + deleteUser.getStatus());

                            Intent i = new Intent(getActivity(), Login_activity.class);
                            startActivity(i);


                        } else {

                            Toast.makeText(getContext(), "Something went wrong, Please try again", Toast.LENGTH_SHORT).show();
                        }



                    }

                    @Override
                    public void onError(Throwable e) {
                        stopAnim();


                    }
                });


    }

    void startAnim() {
        binding.avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim() {
        if ( binding.avi != null) {
            binding.avi.hide();
        }

        // or avi.smoothToHide();
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    private void openWeb(String url) {


        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);


//        CustomTabColorSchemeParams darkModeColorScheme = new CustomTabColorSchemeParams.Builder()
//                .setToolbarColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark))
//                .setNavigationBarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark))
//                .build();
//
//        TrustedWebActivityIntentBuilder twaBuilder = new TrustedWebActivityIntentBuilder(Uri.parse(url))
//                .setToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
//                .setNavigationBarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark))
//                .setColorScheme(CustomTabsIntent.COLOR_SCHEME_SYSTEM)
//                .setColorSchemeParams(
//                        CustomTabsIntent.COLOR_SCHEME_DARK, darkModeColorScheme
//                )
//                .setDisplayMode(new TrustedWebActivityDisplayMode.DefaultMode());

//        TwaLauncher mTwaLauncher = new TwaLauncher(getActivity());
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
