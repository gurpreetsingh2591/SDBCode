package expense.exp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import expense.exp.R;
import expense.exp.adapter.Assign_Folder_Adapter;
import expense.exp.adapter.Folder_Adapter;
import expense.exp.databinding.ActivityAssignfolderBinding;
import expense.exp.databinding.ActivityPlanListBinding;
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
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/*import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;*/
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class AssignFolderAccountantActivity extends AppCompatActivity implements MyDialogListener {


    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
   // @BindView(R.id.folder_recycleview)
    RecyclerView folder_recycleview;
   // @BindView(R.id.empty_txt)
    TextView empty_txt;
    File image_file = null;
    //@BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    //Unbinder unbinder;
    int f_id = -1;
    int o_id = -1;
    int acc_id = -1;
   // @BindView(R.id.serach_txt)
    EditText serach_txt;
   // @BindView(R.id.acc_name)
    TextView acc_name;
    String _path;
    SharedPrefManager sharedPrefManager;
   // @BindView(R.id.loading_view)
    RelativeLayout loading_view;
   // @BindView(R.id.avi_load)
    AVLoadingIndicatorView avi_load;

   // @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    private InputStream inputStreamImg;
    private Bitmap bitmap;
    private String imgPath = null;
    ActivityAssignfolderBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_assignfolder);
        binding = ActivityAssignfolderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //unbinder = ButterKnife.bind(this);
        sharedPrefManager = new SharedPrefManager(this);

        Log.d("search", sharedPrefManager.getuserinfo().getId());

        Intent intent = getIntent();

        f_id = 1;

        acc_id = Integer.parseInt(intent.getStringExtra("acc_id"));
        acc_name.setText(intent.getStringExtra("acc_name"));


        Log.d("f_id", String.valueOf(f_id));
        o_id = Integer.parseInt(sharedPrefManager.getuserinfo().getId());


        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.folderRecycleview.setLayoutManager(layoutManager);

//        getFiles(f_id);

        getFolder(o_id);


        binding.swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.app_color));
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getFolder(o_id);


            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
       // unbinder.unbind();


    }


    void startAnim() {
        binding.avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim() {
        binding.avi.hide();
        // or avi.smoothToHide();
    }

    //@OnClick(R.id.back_icon)
    public void back(View view) {

        onBackPressed();
    }

    @Override
    public void onBackPressed() {


        super.onBackPressed();
    }

    @Override
    public void OnCloseDialog() {

        getFolder(f_id);

    }


    //@OnClick(R.id.serach_btn)
    public void search(View view) {
        String search_value = serach_txt.getText().toString();

        if (search_value.equals("") || search_value.length() == 0) {


            //serach_txt.setError("Please Enter Searching Value.");

        } else {


            searchfolder(search_value);
        }


    }

    @SuppressLint("CheckResult")
    public void searchfolder(String search_value)

    {


        startAnim();
        ApiInterface apiService = ApiClient.getClient(this)
                .create(ApiInterface.class);


        apiService.searching(search_value, "folder", Integer.parseInt(sharedPrefManager.getuserinfo().getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SearchFolderDataModel>() {
                    @Override
                    public void onSuccess(SearchFolderDataModel status) {


                        if (status.getStatus().matches("1")) {
                            stopAnim();

                            List<FolderResult> folderResults = status.getResults();


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


                            Folder_Adapter folder_adapter = new Folder_Adapter(false,folders, AssignFolderAccountantActivity.this, new Folder_Adapter.ClickListener() {
                                @Override
                                public void onPositionClicked(Folder datum, int id, int position) {

                                    Intent intent = new Intent(AssignFolderAccountantActivity.this, ShowFileActivity.class);
                                    intent.putExtra("f_id", datum.getId());

                                    startActivity(intent);
                                    finish();


                                }

                                @Override
                                public void onLongClicked(int position) {

                                }

//                                @Override
//                                public void onLongClicked(int position) {
//
//                                }
                            });
                            binding.folderRecycleview.removeAllViews();

                            binding.folderRecycleview.setAdapter(folder_adapter);


                        } else {

                            stopAnim();


                            Toast.makeText(AssignFolderAccountantActivity.this, "No Folder Found", Toast.LENGTH_SHORT).show();


                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        stopAnim();


                    }
                });


    }


    @SuppressLint("CheckResult")
    public void getFolder(int user_id) {

        startAnim();

        ApiInterface apiService = ApiClient.getClient(this)
                .create(ApiInterface.class);


        apiService.getFolders(user_id, Pref.getStringValue(this, Utils.selected_year, Utils.this_year))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<GetFolders>() {
                    @Override
                    public void onSuccess(GetFolders getFolders) {


                        if (binding.emptyTxt.getVisibility() == View.VISIBLE) {
                            binding.emptyTxt.setVisibility(View.GONE);
                        }


                        if (getFolders.getStatus().matches("1")) {
                            stopAnim();

                            List<Folder> folders = getFolders.getFolders();
                            if (folders.size() != 0) {

                                Assign_Folder_Adapter folder_adapter = new Assign_Folder_Adapter(null,getFolders.getFolders(), AssignFolderAccountantActivity.this, new Assign_Folder_Adapter.ClickListener() {
                                    @Override
                                    public void onPositionClicked(Folder datum, int id, int position) {

                                        if (id == 2) {


                                            assignUnAsAcc(Integer.valueOf(sharedPrefManager.getuserinfo().getId()), acc_id, Integer.valueOf(datum.getId()), "add");

                                        } else if (id == 3) {

                                            assignUnAsAcc(Integer.valueOf(sharedPrefManager.getuserinfo().getId()), acc_id, Integer.valueOf(datum.getId()), "remove");

                                        }

                                    }

                                    @Override
                                    public void rename(int position) {

                                    }

                                    @Override
                                    public void onLongClicked(Folder folder,int position) {

                                    }

                                });

                                binding.folderRecycleview.setAdapter(folder_adapter);

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


    @SuppressLint("CheckResult")
    public void assignUnAsAcc(int user_id, int accountant_id, int f_id, String purpose) {


        startAnimLoad();

        ApiInterface apiService = ApiClient.getClient(this)
                .create(ApiInterface.class);


        apiService.AssignUnAsFolder(user_id, accountant_id, f_id, purpose)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Status>() {
                    @Override
                    public void onSuccess(Status status) {

                        if (status.getStatus().matches("1")) {


                            stopAnimLoad();

                            Toast.makeText(AssignFolderAccountantActivity.this, status.getMessage(), Toast.LENGTH_LONG).show();


                        } else {

                            stopAnimLoad();
                            Toast.makeText(AssignFolderAccountantActivity.this, status.getMessage(), Toast.LENGTH_LONG).show();

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


}








