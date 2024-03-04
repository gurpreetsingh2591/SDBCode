package expense.exp.fragment;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import expense.exp.R;
import expense.exp.activity.AdsPackageListActivity;
import expense.exp.activity.ChangePassword;
import expense.exp.activity.Home_Activity;
import expense.exp.activity.Login_activity;
import expense.exp.activity.PlanListActivity;
import expense.exp.activity.ReferActivity;
import expense.exp.chat.Chat;
import expense.exp.databinding.LayoutFolderBinding;
import expense.exp.databinding.LayoutSettingBinding;
import expense.exp.helper.SharedPrefManager;

/*import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;*/
import expense.exp.internet.model.User;

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
        //view = inflater.inflate(R.layout.layout_setting, container, false);
        binding = LayoutSettingBinding.inflate(inflater, container, false);

        sharedPrefManager = new SharedPrefManager(getActivity());

        // unbinder = ButterKnife.bind(this, view);

        User user = sharedPrefManager.getuserinfo();

        url = "https://skipdaboxes.ca/Users/getAlerts/" + user.getId();
//        cardview_points.setVisibility(View.VISIBLE);
//        txtPoints.setText("My Points : "+user.getMypoints());

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
