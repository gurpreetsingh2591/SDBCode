package expense.exp.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.trusted.TrustedWebActivityDisplayMode;
import androidx.browser.trusted.TrustedWebActivityIntentBuilder;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import expense.exp.R;
import expense.exp.activity.Home_Activity;
import expense.exp.activity.ShowFileActivity;
import expense.exp.adapter.Accountant_Adapter;
import expense.exp.databinding.LayoutAccountantBinding;
import expense.exp.databinding.LayoutFolderBinding;
import expense.exp.helper.SharedPrefManager;
import expense.exp.internet.ApiClient;
import expense.exp.internet.ApiInterface;
import expense.exp.internet.model.Acc;
import expense.exp.internet.model.AccountantResult;
import expense.exp.internet.model.GetAccountant;
import expense.exp.internet.model.SearchAccountantDataModel;
import expense.exp.internet.model.Status;
import expense.exp.model_class.Data_Folder;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;*/
import expense.exp.toast.CustomToast;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by admin on 06-07-2018.
 */

public class Accountant_Fragment extends Fragment {

    View view;

    SharedPrefManager sharedPrefManager;
    LayoutAccountantBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // view = inflater.inflate(R.layout.layout_accountant, container, false);
        binding = LayoutAccountantBinding.inflate(inflater, container, false);
        sharedPrefManager = new SharedPrefManager(getActivity());

        //unbinder = ButterKnife.bind(this, view);


        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.accountantRecycleview.setLayoutManager(layoutManager);

        getAccountant();

        binding.swipeRefresh.setColorSchemeColors(requireActivity().getResources().getColor(R.color.app_color));

        binding.swipeRefresh.setOnRefreshListener(this::getAccountant);

        Log.e("mobile", sharedPrefManager.getuserinfo().getMobile());

        binding.serachTxt.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
//                if (s.length() != 0) {
//
//
//                    searchfolder(String.valueOf(s));
//                }
                searchfolder(String.valueOf(s));
            }
        });

        binding.menu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(requireContext(), binding.menu);
            //Inflating the Popup using xml file

            popup.getMenuInflater().inflate(R.menu.menu_accountant, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_invite:
                            openWeb(sharedPrefManager.getuserinfo().getId());
                            break;

                    }
                    return true;
                }
            });
            popup.show(); //showing popup menu
        });

        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // unbind the view to free some memory
        //unbinder.unbind();
    }


    public static Accountant_Fragment newInstance() {
        return new Accountant_Fragment();
    }


    @SuppressLint("CheckResult")
    public void getAccountant() {

        startAnim();

        ApiInterface apiService = ApiClient.getClient(getActivity())
                .create(ApiInterface.class);


        apiService.getAccountant(Integer.parseInt(sharedPrefManager.getuserinfo().getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<GetAccountant>() {
                    @Override
                    public void onSuccess(GetAccountant getAccountant) {

                        if (!binding.emptyTxt.toString().isEmpty()) {
                            if (binding.emptyTxt.getVisibility() == View.VISIBLE) {
                                binding.emptyTxt.setVisibility(View.GONE);
                                binding.accountantRecycleview.setVisibility(View.VISIBLE);

                            }
                        }


                        if (getAccountant.getStatus().matches("1")) {
                            stopAnim();

                            if (getAccountant.getMyaccountantId() != null) {
                                sharedPrefManager.setacc_id(Integer.parseInt(getAccountant.getMyaccountantId()));

                            }

                            Log.e("AccountId", String.valueOf(sharedPrefManager.getacc_id()));

                            List<Acc> accs = getAccountant.getAccs();
                            if (accs.size() != 0) {

                                Accountant_Adapter accountant_fragm = new Accountant_Adapter(accs, getActivity(), new Accountant_Adapter.ClickListener() {
                                    @Override
                                    public void onPositionClicked(final Acc datum, int id, int position) {


                                        if (id == 1) {

                                            if (sharedPrefManager.getuserinfo().getMobile().equals("0") || sharedPrefManager.getuserinfo().getName().equals("")) {
                                                Home_Activity.viewPager.setCurrentItem(3);

                                                new CustomToast().Show_Toast(getActivity(), view, "Please Enter your Mobile number and username.");
                                            } else {
                                                assignUnAsAcc(Integer.valueOf(sharedPrefManager.getuserinfo().getId()), Integer.valueOf(datum.getId()), "add", datum);

                                                sharedPrefManager.setacc_id(Integer.parseInt(datum.getId()));

                                            }
//                                            assignUnAsAcc(Integer.valueOf(sharedPrefManager.getuserinfo().getId()), Integer.valueOf(datum.getId()), "add", datum);
////
//                                                sharedPrefManager.setacc_id(Integer.parseInt(datum.getId()));
////

                                        } else if (id == 2) {

                                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

                                            // Setting Dialog Title
                                            alertDialog.setTitle("Confirm Remove...");

                                            // Setting Dialog Message
                                            alertDialog.setMessage("Are you sure you want to remove accountant ?");

                                            // Setting Icon to Dialog
                                            alertDialog.setIcon(R.drawable.error_icon);

                                            // Setting Positive "Yes" Button
                                            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {

                                                    assignUnAsAcc(Integer.valueOf(sharedPrefManager.getuserinfo().getId()), Integer.valueOf(datum.getId()), "remove", datum);

                                                    sharedPrefManager.removeacc_id();


                                                }
                                            });

                                            // Setting Negative "NO" Button
                                            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Write your code here to invoke NO event
                                                    Toast.makeText(getActivity(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                                    dialog.cancel();
                                                }
                                            });

                                            // Showing Alert Message
                                            alertDialog.show();


                                        }

                                    }

                                    @Override
                                    public void onLongClicked(int position) {

                                    }
                                });

                                binding.accountantRecycleview.setAdapter(accountant_fragm);


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
                        Log.d("Throwable", e.getMessage());

                    }
                });

    }

    void startAnim() {
        binding.avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim() {
        binding.avi.hide();
        // or avi.smoothToHide();
    }


    @SuppressLint("CheckResult")
    public void assignUnAsAcc(int user_id, int accountant_id, String purpose, Acc datum) {


        startAnimLoad();
        ApiInterface apiService = ApiClient.getClient(getActivity())
                .create(ApiInterface.class);


        apiService.assignUnAsAcc(user_id, accountant_id, purpose)
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


    @SuppressLint("CheckResult")
    public void searchfolder(String search_value) {

        if (binding.emptyTxt.getVisibility() == View.VISIBLE) {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.accountantRecycleview.setVisibility(View.VISIBLE);
        }


        startAnim();
        ApiInterface apiService = ApiClient.getClient(getActivity())
                .create(ApiInterface.class);


        apiService.accountantsearching(search_value, "accountant", Integer.parseInt(sharedPrefManager.getuserinfo().getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SearchAccountantDataModel>() {
                    @Override
                    public void onSuccess(SearchAccountantDataModel status) {


                        if (status.getStatus().matches("1")) {
                            stopAnim();

                            List<AccountantResult> accountantResults = status.getResults();

                            List<Acc> accs = new ArrayList<>();


                            for (AccountantResult accountantResult : accountantResults) {

                                Acc acc = new Acc();

                                acc.setId(accountantResult.getId());
                                acc.setImage(accountantResult.getImage());
                                acc.setName(accountantResult.getName());

                                accs.add(acc);


                            }

                            Accountant_Adapter accountant_fragm = new Accountant_Adapter(accs, getActivity(), new Accountant_Adapter.ClickListener() {
                                @Override
                                public void onPositionClicked(Acc datum, int id, int position) {


                                    if (id == 1) {

                                        if (sharedPrefManager.getuserinfo().getMobile().equals("0")) {
                                            Home_Activity.viewPager.setCurrentItem(3);
                                            Profile_Fragment.newInstance();
                                            new CustomToast().Show_Toast(getActivity(), view,
                                                    "Please Enter your Mobile number and username.");
                                        } else {
                                            assignUnAsAcc(Integer.valueOf(sharedPrefManager.getuserinfo().getId()), Integer.valueOf(datum.getId()), "add", datum);

                                            sharedPrefManager.setacc_id(Integer.parseInt(datum.getId()));

                                        }
//                                        assignUnAsAcc(Integer.valueOf(sharedPrefManager.getuserinfo().getId()), Integer.valueOf(datum.getId()), "add", datum);

                                    } else if (id == 2) {

                                        assignUnAsAcc(Integer.valueOf(sharedPrefManager.getuserinfo().getId()), Integer.valueOf(datum.getId()), "remove", datum);

//                                    } else if (id == 1) {
//                                        Intent intent = new Intent(getActivity(), AssignFolderAccountantActivity.class);
//                                        intent.putExtra("acc_id", datum.getId());
//                                        intent.putExtra("acc_name", datum.getName());
//                                        intent.putExtra("acc_img", datum.getImage());
//                                        startActivity(intent);
                                    }


                                }

                                @Override
                                public void onLongClicked(int position) {

                                }
                            });

                            binding.accountantRecycleview.setAdapter(accountant_fragm);


                            // Toast.makeText(getActivity(), status.getMessage(), Toast.LENGTH_SHORT).show();

                        } else {

                            stopAnim();

                            binding.emptyTxt.setText("Accountant not found");
                            binding.emptyTxt.setVisibility(View.VISIBLE);
                            binding.accountantRecycleview.setVisibility(View.GONE);

                            Toast.makeText(getActivity(), "Accountant not found", Toast.LENGTH_SHORT).show();


                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        stopAnim();


                    }
                });


    }

    // @OnClick(R.id.back_icon)
    public void back_press(View view) {

        Home_Activity.viewPager.setCurrentItem(0, true);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
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
        binding.loadingView.setVisibility(View.GONE);
        // or avi.smoothToHide();
    }

    private void openWeb(String id) {

//        String url = "https://sdb.topnotchhub.com/Documents/Chat/"+id;
        String url = "https://skipdaboxes.ca/Users/sendInvite/" + id;

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);


//        CustomTabColorSchemeParams darkModeColorScheme = new CustomTabColorSchemeParams.Builder()
//                .setToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark))
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
