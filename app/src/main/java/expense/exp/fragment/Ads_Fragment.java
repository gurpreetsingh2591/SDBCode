package expense.exp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.util.List;
import java.util.Objects;

/*import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;*/
import expense.exp.R;
import expense.exp.activity.AdsDetailsActivity;
import expense.exp.activity.AdsPackageListActivity;
import expense.exp.activity.EditAds_activity;
import expense.exp.activity.PostAds_activity;
import expense.exp.adapter.Ads_Adapter;
import expense.exp.databinding.LayoutAccountantBinding;
import expense.exp.databinding.LayoutAdsBinding;
import expense.exp.helper.CustomDialog;
import expense.exp.helper.MyDialogListener;
import expense.exp.helper.SharedPrefManager;
import expense.exp.internet.ApiClient;
import expense.exp.internet.ApiInterface;
import expense.exp.internet.model.Ads;
import expense.exp.internet.model.GetAds;
import expense.exp.internet.model.Status;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by appleboy on 14-05-2018.
 */

public class Ads_Fragment extends Fragment implements MyDialogListener,Ads_Adapter.ClickListener {

    View view;
    int acc_id = -1;
    private SharedPrefManager sharedPrefManager;

    public static File recievedFile;

    public static Ads_Fragment newInstance() {
        return new Ads_Fragment();
    }

    Ads_Adapter adapter;
    List<Ads> adsList;
    private String imageBaseUrl;

    LayoutAdsBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // view = inflater.inflate(R.layout.layout_ads, container, false);
        //unbinder = ButterKnife.bind(this, view);
        binding = LayoutAdsBinding.inflate(inflater, container, false);
        sharedPrefManager = new SharedPrefManager(getActivity());
        acc_id = sharedPrefManager.getacc_id();
        adapter = new Ads_Adapter(getActivity());

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.folderRecycleview.setLayoutManager(layoutManager);
        getMyAdsList(Integer.parseInt(sharedPrefManager.getuserinfo().getId()));
        binding.swipeRefresh.setColorSchemeColors(getActivity().getResources().getColor(R.color.app_color));


        binding.swipeRefresh.setOnRefreshListener(() -> getMyAdsList(Integer.parseInt(sharedPrefManager.getuserinfo().getId())));

        binding.serachTxt.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (adapter != null && !TextUtils.isEmpty(s.toString()) && adapter.getItemCount() !=0) {
                    adapter.filter(s.toString());

                }else {
                    if(adapter != null && adapter.getItemCount() !=0) {
                        adapter.filter(null);
                    }
                }

            }
        });

        binding.menu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(requireContext(),  binding.menu);
            popup.getMenuInflater().inflate(R.menu.menu_post_ads, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.menu_post_ads:
                        callDialog();
                        break;

                        case R.id.menu_post:
                            packages();
                        break;
                }
                return true;
            });
            popup.show(); //showing popup menu
        });
        return binding.getRoot();
    }

    @Override
    public void onPositionClicked(Ads datum, int id, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("ad_id",datum.getId());
        Intent intent = new Intent(getActivity(), AdsDetailsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onEditClicked(Ads datum, int id, int position) {
        Bundle bundle = new Bundle();
        bundle.putString("ad_id",datum.getId());
        bundle.putString("image_base_urls",imageBaseUrl);
        bundle.putSerializable("ad_object",datum);
        Intent intent = new Intent(getActivity(), EditAds_activity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void callDialog() {
        requireActivity().startActivity(new Intent(getActivity(), PostAds_activity.class));
    }

    private void packages() {
        Intent intent = new Intent(getActivity(), AdsPackageListActivity.class);
        getActivity().startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
       // unbinder.unbind();
    }




    @SuppressLint("CheckResult")
    public void getMyAdsList(int user_id) {

        startAnim();

        ApiInterface apiService = ApiClient.getClient(getActivity())
                .create(ApiInterface.class);


        apiService.getAdsList(user_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribeWith(new DisposableSingleObserver<GetAds>() {
                    @Override
                    public void onSuccess(GetAds getFolders) {
                        if ( !binding.emptyTxt.toString().isEmpty()) {
                            if ( binding.emptyTxt.getVisibility() == View.VISIBLE) {
                                binding.emptyTxt.setVisibility(View.GONE);
                            }
                        }
                        imageBaseUrl = getFolders.getImageUrl();
                        if (getFolders.getStatus().matches("1")) {
                            stopAnim();

                            adsList = getFolders.getAds();
                            if (recievedFile != null)
                                adsList = getFolders.getAds();
                            if (adsList.size() != 0) {

                                adapter.setData(adsList);
                                adapter.setData(imageBaseUrl);
                                adapter.setListener(Ads_Fragment.this);

                                binding.folderRecycleview.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }


                        } else {

                            binding.emptyTxt.setVisibility(View.VISIBLE);
                            stopAnim();


                        }

                        binding.swipeRefresh.setRefreshing(false);


                    }

                    @Override
                    public void onError(Throwable e) {
                        stopAnim();
                        binding.emptyTxt.setVisibility(View.VISIBLE);
                        binding.swipeRefresh.setRefreshing(false);


                    }
                });


    }

    private void callRenameDialog(int pos) {
        CustomDialog customDialog = new CustomDialog(this, 0);
        customDialog.renameDialogShow((AppCompatActivity) getActivity(), adsList.get(pos).getId());
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
    public void OnCloseDialog() {
        getMyAdsList(Integer.parseInt(sharedPrefManager.getuserinfo().getId()));
    }




    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    //=====================================================

    @SuppressLint("CheckResult")
    public void assignUnAsAcc(int user_id, int accountant_id, int f_id, String purpose) {


        startAnimLoad();

        ApiInterface apiService = ApiClient.getClient(getActivity())
                .create(ApiInterface.class);


        apiService.AssignUnAsFolder(user_id, accountant_id, f_id, purpose)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Status>() {
                    @Override
                    public void onSuccess(Status status) {

                        if (status.getStatus().matches("1")) {


                            stopAnimLoad();

                            Toast.makeText(getActivity(), status.getMessage(), Toast.LENGTH_LONG).show();


                        } else {

                            stopAnimLoad();
                            Toast.makeText(getActivity(), status.getMessage(), Toast.LENGTH_LONG).show();

                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        // Network error
                        stopAnimLoad();
                    }
                });

    }

    void startAnimLoad() {
        binding.loadingView.setVisibility(View.VISIBLE);
        binding.aviLoad.setVisibility(View.VISIBLE);
        binding.aviLoad.show();
        // or avi.smoothToShow();
    }

    void stopAnimLoad() {
        binding.aviLoad.hide();
        binding.aviLoad.setVisibility(View.GONE);
        binding.aviLoad.setVisibility(View.GONE);
        // or avi.smoothToHide();
    }


    //Delete Folder Api


    @Override
    public void onResume() {
        super.onResume();
        getMyAdsList(Integer.parseInt(sharedPrefManager.getuserinfo().getId()));
    }


}
