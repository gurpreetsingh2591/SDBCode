package expense.exp.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
/*
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;*/
import expense.exp.R;
import expense.exp.activity.NotificationActivity;
import expense.exp.activity.ShowFileActivity;
import expense.exp.activity.UploadPdfActivity;
import expense.exp.adapter.Assign_Folder_Adapter;
import expense.exp.adapter.Folder_Adapter;
import expense.exp.databinding.LayoutFolderBinding;
import expense.exp.helper.CustomDialog;
import expense.exp.helper.MyDialogListener;
import expense.exp.helper.Pref;
import expense.exp.helper.SharedPrefManager;
import expense.exp.helper.Utils;
import expense.exp.internet.ApiClient;
import expense.exp.internet.ApiInterface;
import expense.exp.internet.model.Folder;
import expense.exp.internet.model.FolderResult;
import expense.exp.internet.model.GetFolders;
import expense.exp.internet.model.SearchFolderDataModel;
import expense.exp.internet.model.Status;
import expense.exp.internet.model.SubFolder;
import expense.exp.model_class.Data_Folder;
import expense.exp.model_class.Delete_Folfer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by admin on 06-07-2018.
 */

public class Folder_Fragment extends Fragment implements MyDialogListener {

    View view;

    ImageView menu;

    int acc_id = -1;
    private SharedPrefManager sharedPrefManager;

    public static File recievedFile;

    public static Folder_Fragment newInstance() {
        return new Folder_Fragment();
    }

    Assign_Folder_Adapter adapter;
    List<Folder> folders;


    LayoutFolderBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       // view = inflater.inflate(R.layout.layout_folder, container,false);
         binding = LayoutFolderBinding.inflate(inflater, container, false);


        initUI();
        return binding.getRoot();
    }

    public void initUI() {
        sharedPrefManager = new SharedPrefManager(getActivity());

        acc_id = sharedPrefManager.getacc_id();


        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.folderRecycleview.setLayoutManager(layoutManager);

        getFolder(Integer.parseInt(sharedPrefManager.getuserinfo().getId()));
        binding.swipeRefresh.setColorSchemeColors(getActivity().getResources().getColor(R.color.app_color));

        notificationCount(sharedPrefManager.getuserinfo().getEmail());

        if (sharedPrefManager.getuserinfo().getType().equals("user")) {
            binding.notification.setVisibility(View.VISIBLE);
            binding.txtCount.setVisibility(View.VISIBLE);
        } else {
            binding.notification.setVisibility(View.GONE);
            binding.txtCount.setVisibility(View.GONE);
        }
        binding.notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), NotificationActivity.class));
//
            }
        });
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getFolder(Integer.parseInt(sharedPrefManager.getuserinfo().getId()));


            }
        });

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

                if (adapter != null) {
                    adapter.filter(s.toString());

                }

            }
        });

        binding.menu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(requireContext(), binding.menu);
            //Inflating the Popup using xml file

            popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.menu_select_year:
                        yearPicker();
                        break;
                    case R.id.menu_recent_file:
                        startActivity(new Intent(getContext(), ShowFileActivity.class).putExtra("isFrom", "0"));
                        break;
                    case R.id.menu_create_folder:
                        callDialog();
                        break;

                }
                return true;
            });
            popup.show(); //showing popup menu
        });
    }

    private void yearPicker() {
        LayoutInflater li = LayoutInflater.from(getContext());

        View promptsView = li.inflate(R.layout.month_year_picker_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        alertDialogBuilder.setView(promptsView);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        final NumberPicker mSpinner = promptsView.findViewById(R.id.picker_year);
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int yrlist = calendar.get(Calendar.YEAR);
        ArrayList<String> year = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            year.add(String.valueOf(yrlist));
            yrlist--;
        }
        Collections.reverse(year);

        mSpinner.setWrapSelectorWheel(true);
        mSpinner.setMinValue(Integer.parseInt(year.get(0)));
        mSpinner.setMaxValue(Integer.parseInt(year.get(year.size() - 1)));
        mSpinner.setValue(Integer.parseInt(year.get(year.size() - 1)));
        final Button mButton = promptsView.findViewById(R.id.btn_year);

        mButton.setOnClickListener(v -> {
            alertDialog.dismiss();
            startAnim();
            Pref.setStringValue(getContext(), Utils.selected_year, String.valueOf(mSpinner.getValue()));
            getFolder(Integer.parseInt(sharedPrefManager.getuserinfo().getId()));
        });

        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    private void callDialog() {
        CustomDialog customDialog = new CustomDialog(this, 0);
        customDialog.show((AppCompatActivity) getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // unbind the view to free some memory
        //  unbinder.unbind();
    }

//    @OnClick(R.id.add_folder)
//    public void show_dailog(View view) {
//        CustomDialog customDialog = new CustomDialog(this);
//        customDialog.show(getActivity());
//    }

    private List<Folder> combineParentSubFolders(List<Folder> folders) {
        List<Folder> folderList = new ArrayList<>();
        for (Folder folder : folders) {
            folderList.add(loop(folder));
            if (folder.getSubFolders().size() > 0) {
                folderList.addAll(loopSubFolder(folder.getSubFolders()));
            }
        }
        return folderList;
    }

    private List<Folder> loopSubFolder(List<SubFolder> subFolders) {
        List<Folder> folderList = new ArrayList<>();
        for (SubFolder folder : subFolders) {
            Folder folder1 = new Folder();
            folder1.setId(folder.getId());
            folder1.setOwnerId(folder.getOwnerId());
            folder1.setAccountantId(folder.getAccountantId());
            folder1.setDays(folder.getDays());
            folder1.setFolderAsOrNot(folder.getFolderAsOrNot());
            folder1.setFolderName(folder.getFolderName());
            folder1.setNumOfDocs(folder.getNumOfDocs());
            folderList.add(folder1);
        }
        return folderList;
    }

    private Folder loop(Folder folder) {
        Folder folder1 = new Folder();
        folder1.setId(folder.getId());
        folder1.setOwnerId(folder.getOwnerId());
        folder1.setAccountantId(folder.getAccountantId());
        folder1.setDays(folder.getDays());
        folder1.setFolderAsOrNot(folder.getFolderAsOrNot());
        folder1.setFolderName(folder.getFolderName());
        folder1.setNumOfDocs(folder.getNumOfDocs());
        return folder1;
    }

    @SuppressLint("CheckResult")
    public void getFolder(int user_id) {

        startAnim();

        ApiInterface apiService = ApiClient.getClient(getActivity())
                .create(ApiInterface.class);


        apiService.getFolders(user_id, Pref.getStringValue(getContext(), Utils.selected_year, Utils.this_year))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<GetFolders>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(GetFolders getFolders) {
                        if (!binding.emptyTxt.toString().isEmpty()) {
                            if (binding.emptyTxt.getVisibility() == View.VISIBLE) {
                                binding.emptyTxt.setVisibility(View.GONE);
                            }
                        }

                        if (getFolders.getStatus().matches("1")) {
                            stopAnim();

                            folders = getFolders.getFolders();
                            if (recievedFile != null)
                                folders = combineParentSubFolders(getFolders.getFolders());
                            // TODO: 19-12-2018 Folder Details
                            if (folders.size() != 0) {

                                adapter = new Assign_Folder_Adapter(recievedFile,folders, getActivity(), new Assign_Folder_Adapter.ClickListener() {
                                    @SuppressLint("LongLogTag")
                                    @Override
                                    public void onPositionClicked(Folder datum, int id, int position) {


                                       /* if (datum.getFolderAsOrNot().equals("Assigned")){
                                            assignUnAsAcc(Integer.valueOf(sharedPrefManager.getuserinfo().getId()), acc_id, Integer.valueOf(datum.getId()), "add");

                                        }
                                        else{
                                            assignUnAsAcc(Integer.valueOf(sharedPrefManager.getuserinfo().getId()), acc_id, Integer.valueOf(datum.getId()), "remove");

                                        }*/

                                        Log.e("#########ID############", String.valueOf(id));
                                        if (id == 2) {

                                            if (acc_id == -1) {

                                                Toast.makeText(getActivity(), "Please Select Accountant ", Toast.LENGTH_SHORT).show();

                                            } else {
                                                Log.e("#########ID1############", String.valueOf(id));
                                                assignUnAsAcc(Integer.valueOf(sharedPrefManager.getuserinfo().getId()), acc_id, Integer.valueOf(datum.getId()), "add");
                                            }


                                        } else if (id == 3) {

                                            if (acc_id == -1) {

                                                Toast.makeText(getActivity(), "Please Select Accountant ", Toast.LENGTH_SHORT).show();

                                            } else {
                                                Log.e("#########ID2############", String.valueOf(id));

                                                assignUnAsAcc(Integer.valueOf(sharedPrefManager.getuserinfo().getId()), acc_id, Integer.valueOf(datum.getId()), "remove");
                                            }


                                        }
                                        if (id == 1) {
                                            if (recievedFile == null) {
                                            Intent intent = new Intent(getActivity(), ShowFileActivity.class);
                                            intent.putExtra("f_id", datum.getId());
                                                intent.putExtra("isFrom","1");
                                            getActivity().startActivity(intent);
                                            getActivity().finish();
                                            } else {
                                                Intent intent = new Intent(getActivity(), UploadPdfActivity.class);
                                                intent.putExtra("displayName", recievedFile.getName());
                                                intent.putExtra("type", "pdf");
                                                intent.putExtra("f_id", datum.getId());
                                                intent.putExtra("file", recievedFile.getPath());
                                                intent.putExtra("from", "Folder_Fragment");
                                                recievedFile = null;
                                                startActivity(intent);

                                            }
                                        }

                                    }

                                    @Override
                                    public void rename(int position) {
                                        callRenameDialog(position);

                                    }

                                    @Override
                                    public void onLongClicked(Folder datum, int position) {
                                        /*if (recievedFile != null) {
                                            Intent intent = new Intent(getActivity(), UploadPdfActivity.class);
                                            intent.putExtra("displayName", recievedFile.getName());
                                            intent.putExtra("type", "pdf");
                                            intent.putExtra("f_id", datum.getId());
                                            intent.putExtra("file", recievedFile.getPath());
                                            intent.putExtra("from", "Folder_Fragment");
                                            recievedFile = null;
                                            startActivity(intent);
                                        }*/
//                                        DeleteFolder(folders.get(position).getId(),folders.get(position).getOwnerId());
//
//                                        AlertDialog.Builder ADB = new AlertDialog.Builder(getActivity());
//                                        ADB.setTitle("Delete Folder ");
//                                        ADB.setMessage("Are you sure want to detele folder?");
//                                        ADB.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                                                                        public void onClick(DialogInterface dialog, int id) {
//                                                DeleteFolder(folders.get(position).getId(),folders.get(position).getOwnerId());
//
//                                            }
//                                        })
//                                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                                                    public void onClick(DialogInterface dialog, int id) {
//                                //                        finish();
//                                                    }});
//                                        AlertDialog dialog2 = ADB.show();


                                    }
                                });

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
        customDialog.renameDialogShow((AppCompatActivity) getActivity(), folders.get(pos).getId());
    }


    void startAnim() {
        binding.avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim() {
        if (binding.avi != null) {
            binding.avi.hide();
        }

        // or avi.smoothToHide();
    }


    @Override
    public void OnCloseDialog() {
        getFolder(Integer.parseInt(sharedPrefManager.getuserinfo().getId()));
    }


    @SuppressLint("CheckResult")
    public void searchfolder(String search_value) {


        startAnim();
        ApiInterface apiService = ApiClient.getClient(getActivity())
                .create(ApiInterface.class);


        apiService.searching(search_value, "folder", Integer.parseInt(sharedPrefManager.getuserinfo().getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SearchFolderDataModel>() {
                    @Override
                    public void onSuccess(SearchFolderDataModel status) {


                        if (binding.emptyTxt.getVisibility() == View.VISIBLE) {
                            binding.emptyTxt.setVisibility(View.GONE);
                            binding.folderRecycleview.setVisibility(View.VISIBLE);
                        }


                        if (status.getStatus().matches("1")) {
                            stopAnim();


                            List<FolderResult> folderResults = status.getResults();

                            Log.d("FolderResult", String.valueOf(folderResults.size()));

                            List<Folder> folders = new ArrayList<>();

                            for (FolderResult folderResult : folderResults) {

                                Folder folder = new Folder();

                                folder.setId(folderResult.getId());
                                folder.setAccountantId(folderResult.getAccId());
                                folder.setFolderName(folderResult.getFolderName());
                                folder.setNumOfDocs("0");
                                folder.setOwnerId(folderResult.getOwnerId());
                                folder.setFolderAsOrNot(folderResult.getModified());
                                folders.add(folder);

                            }


                            Folder_Adapter folder_adapter = new Folder_Adapter(false,folders, getActivity(), new Folder_Adapter.ClickListener() {
                                @Override
                                public void onPositionClicked(Folder datum, int id, int position) {

                                    Intent intent = new Intent(getActivity(), ShowFileActivity.class);
                                    intent.putExtra("f_id", datum.getId());
                                    intent.putExtra("isFrom","1");
                                    getActivity().startActivity(intent);
                                    getActivity().finish();

                                }

                                //
//                                @Override
                                public void onLongClicked(int position) {

//                                    DeleteFolder(folders.get(position).getId(),folders.get(position).getOwnerId());

//                                    dialog(position);

                                }
                            });
//                            folder_recycleview.removeAllViews();


                            binding.folderRecycleview.setAdapter(folder_adapter);


                            //  Toast.makeText(getActivity(), status.getMessage(), Toast.LENGTH_SHORT).show();

                        } else {


                            stopAnim();

                            binding.folderRecycleview.setVisibility(View.GONE);
                            binding.emptyTxt.setText("Folder Not Found");

                            binding.emptyTxt.setVisibility(View.VISIBLE);

                            //  Toast.makeText(getActivity(), status.getMessage(), Toast.LENGTH_SHORT).show();


                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        stopAnim();


                    }
                });


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


                            getFolder(Integer.parseInt(sharedPrefManager.getuserinfo().getId()));
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
    public void notificationCount(String email) {


        startAnimLoad();

        ApiInterface apiService = ApiClient.getClient(getActivity())
                .create(ApiInterface.class);


        apiService.getNotificationCount(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Status>() {
                    @Override
                    public void onSuccess(Status status) {

                        if (status.getStatus().matches("1")) {


                            stopAnimLoad();


                            if (status.getNotifications().equals("0")){
                                binding.txtCount.setVisibility(View.GONE);
                            }
                            else {

                                if (sharedPrefManager.getuserinfo().getType().equals("user")){
                                    binding.notification.setVisibility(View.VISIBLE);
                                    binding.txtCount.setVisibility(View.VISIBLE);
                                    binding.txtCount.setText(status.getNotifications());
                                }
                                else {
                                    binding.notification.setVisibility(View.GONE);
                                    binding.txtCount.setVisibility(View.GONE);
                                }
//                                txtCount.setVisibility(View.VISIBLE);

                            }

                        } else {

                            stopAnimLoad();

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
        binding.loadingView.setVisibility(View.GONE);
        // or avi.smoothToHide();
    }


    //Delete Folder Api


    @SuppressLint("CheckResult")
    public void DeleteFolder(String folder_id, String foldre_owner_id) {

        startAnim();


        ApiInterface apiService = ApiClient.getClient(getActivity())
                .create(ApiInterface.class);


        apiService.DeleteFolder(folder_id, foldre_owner_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Delete_Folfer>() {

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(Delete_Folfer moveFolder) {
                        stopAnim();

                        if (moveFolder.getStatus().matches("1")) {
                            Log.e("exeption", "=" + moveFolder.getStatus());

                            adapter.notifyDataSetChanged();

                        } else {

                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        // Network error

                        Log.e("exeption", "=" + e);
                        stopAnim();
                    }
                });

    }


    private void dialog(int p) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure want to delete this folder?");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DeleteFolder(folders.get(p).getId(), folders.get(p).getOwnerId());

            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        finish();
                    }

                    // Create the AlertDialog object and return it


                });
    }

    @Override
    public void onResume() {
        super.onResume();
        getFolder(Integer.parseInt(sharedPrefManager.getuserinfo().getId()));
    }
}
