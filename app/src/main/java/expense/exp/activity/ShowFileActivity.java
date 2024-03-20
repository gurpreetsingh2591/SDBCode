package expense.exp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.browser.customtabs.CustomTabColorSchemeParams;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.trusted.TrustedWebActivityDisplayMode;
import androidx.browser.trusted.TrustedWebActivityIntentBuilder;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/*import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;*/
import expense.exp.ImageGridActivity;
import expense.exp.R;
import expense.exp.adapter.AdapterDialogFolder;
import expense.exp.adapter.File_Adapter;
import expense.exp.adapter.Folder_Adapter;
import expense.exp.chat.Chat;
import expense.exp.databinding.ActivityCropBinding;
import expense.exp.databinding.ActivityShowFileBinding;
import expense.exp.helper.AddFileDialog;
import expense.exp.helper.CustomDialog;
import expense.exp.helper.FileUtils;
import expense.exp.helper.MyDialogListener;
import expense.exp.helper.Pref;
import expense.exp.helper.SharedPrefManager;
import expense.exp.helper.Utils;
import expense.exp.imagepicker.ImagePickerDemo;
import expense.exp.internet.ApiClient;
import expense.exp.internet.ApiInterface;
import expense.exp.internet.model.Document;
import expense.exp.internet.model.Folder;
import expense.exp.internet.model.GetFiles;
import expense.exp.internet.model.GetFolders;
import expense.exp.internet.model.SearchDocumentDataModel;
import expense.exp.internet.model.Status;
import expense.exp.internet.model.SubFolder;
import expense.exp.model_class.Delete_Multi_Folder;
import expense.exp.model_class.FolderFile;
import expense.exp.model_class.MoveMultiDoc;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ShowFileActivity extends AppCompatActivity implements MyDialogListener {

    //@BindView(R.id.folder_recycleview)
    RecyclerView folder_recycleview;

   // @BindView(R.id.empty_txt)
    TextView empty_txt;
    File image_file = null;

   // @BindView(R.id.select_camera)
    FloatingActionButton select_camera;

   // @BindView(R.id.select_pdf)
    FloatingActionButton select_pdf;

   // @BindView(R.id.select_gallery)
    FloatingActionButton select_gallery;

   // @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
   // Unbinder unbinder;
    private final int PICK_IMAGE_CAMERA = 1, PICK_PDF = 4, PICK_IMAGE_GALLERY = 2, INTENT_REQUEST_GET_IMAGES = 13;

    //    int f_id = 0;
    int o_id = 0;
    String f_id;

    String select_id = "";
    private InputStream inputStreamImg;
    private Bitmap bitmap;
    private final String imgPath = null;

   // @BindView(R.id.serach_txt)
    EditText serach_txt;

   // @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    String _path;
    SharedPrefManager sharedPrefManager;
    boolean isPrivate = true;
    File_Adapter folder_adapter;
    ArrayList<String> img_path;
    List<Folder> folders;
    ImageView icon_more;
    List<Document> documents;

   // @BindView(R.id.icon_more)
    ImageView menu;
    private int acc_id = -1;
    public static File recievedFile;
    private List<FolderFile> folderFiles=new ArrayList<>();
    String isFrom = "";
    ActivityShowFileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_show_file);
        binding = ActivityShowFileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       // unbinder = ButterKnife.bind(this);
        sharedPrefManager = new SharedPrefManager(this);
        acc_id = sharedPrefManager.getacc_id();

        isFrom = getIntent().getStringExtra("isFrom");
        o_id = Integer.parseInt(sharedPrefManager.getuserinfo().getId());
        Log.d("search", sharedPrefManager.getuserinfo().getId());
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.folderRecycleview.setLayoutManager(layoutManager);
        f_id = getIntent().getStringExtra("f_id");

       // icon_more = findViewById(R.id.icon_more);
        Log.e("folder_id", String.valueOf(f_id));

//        onMenuClick();
    }

    private void onMenuClick() {
        menu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(Objects.requireNonNull(ShowFileActivity.this), menu);
            //Inflating the Popup using xml file
            popup.getMenuInflater()
                    .inflate(R.menu.subfolder_menu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_create_folder:
                            callDialog();
                            break;

                    }
                    return true;
                }
            });
            popup.show(); //showing popup menu
        });

    }

    private void callDialog() {
        CustomDialog customDialog = new CustomDialog(this, Integer.parseInt(f_id));
        customDialog.show(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().hasExtra("img_list")) {
            ArrayList<Uri> selectedUriList = getIntent().getParcelableArrayListExtra("img_list");
            ArrayList<String> img_path = new ArrayList<>();
            for (Uri path : selectedUriList) {
                img_path.add(path.getPath());
                Log.e("onCreate: ", "o_path " + path.getPath());
            }
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.MANAGE_EXTERNAL_STORAGE}, PICK_IMAGE_CAMERA);
                } else {
                    if (img_path.size() > 0) {
                        onMenuClick();
                        Intent intent = new Intent(this, UploadFileActivity.class);
                        intent.putExtra("f_id", f_id);
                        intent.putExtra("via_camera", img_path);
                        startActivity(intent);
                        finish();

                    } else {
                        initview();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            initview();
        }
    }

    @SuppressLint("RestrictedApi")
    void initview() {
        if (getIntent().hasExtra("f_id")) {
            isPrivate = true;
            binding.selectCamera.setVisibility(View.VISIBLE);
            binding.selectGallery.setVisibility(View.VISIBLE);
            binding.selectPdf.setVisibility(View.VISIBLE);
//            f_id = Integer.parseInt(getIntent().getStringExtra("f_id"));
            f_id = getIntent().getStringExtra("f_id");
        } else {
            isPrivate = false;
            binding.selectCamera.setVisibility(View.GONE);
            binding.selectGallery.setVisibility(View.GONE);
            binding.selectPdf.setVisibility(View.GONE);
            // TODO: 22-12-2018 call Recent Doc api
        }

        binding.selectPdf.setOnClickListener(view -> {

                selectPdf(view);

        });
        binding.selectGallery.setOnClickListener(view -> {

            selectFile(view);

        });
        binding.selectCamera.setOnClickListener(view -> {

            select_fromCamera(view);

        });
        binding.backIcon.setOnClickListener(view -> {

            back(view);

        });


        if (isPrivate) {
            getFiles(f_id);
        } else {
            // TODO: 22-12-2018 call Recent Doc api
            getRecentDocs();
        }

        binding.iconMore.setOnClickListener(view -> {

            if (isFrom.equals("0")){
                openFolderDialog() ;
            }
            else{
                opendialog();
            }

        });

        binding.swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.app_color));

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isPrivate) {
                    getFiles(f_id);
                } else {
                    // TODO: 22-12-2018 call Recent Doc api
                    getRecentDocs();
                }
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
//                if (s.length() != 0) {
//                    searchfolder(String.valueOf(s));
//                }
                if (count == 0) {
                    if (isPrivate)
                        getFiles(f_id);
                    else
                        getRecentDocs();
                } else
                    searchfolder(String.valueOf(s));
            }
        });
    }

    //@OnClick(R.id.select_gallery)
    public void selectFile(View view) {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.MANAGE_EXTERNAL_STORAGE}, PICK_IMAGE_CAMERA);
            } else {
                Intent intent = new Intent(this, UploadFileActivity.class);
                intent.putExtra("f_id", f_id);
                intent.putExtra("type", "png");
                startActivity(intent);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void selectPdf(View view) {

//            String path = Environment.getExternalStorageDirectory() + "/" + "Downloads" + "/";
//            Uri uri = Uri.parse(path);
//            Intent intent = new Intent(Intent.ACTION_PICK);
//            intent.setDataAndType(uri, "*/*");
//            startActivity(intent);

            String path = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
//            String path = Environment.getExternalStorageDirectory() + "/" + "Downloads" + "/";
            Uri uri = Uri.parse(path);
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setDataAndType(uri, "application/pdf");
            startActivityForResult(Intent.createChooser(intent, "Open folder"), PICK_PDF);

//           Intent intent = new Intent();
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            Uri uri = Uri.parse(Environment.getExternalStorageDirectory()+"/Downloads");
//            intent.setDataAndType(uri, "application/pdf");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            intent.addCategory(Intent.CATEGORY_OPENABLE);
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//            startActivityForResult(Intent.createChooser(intent, "Open folder"), PICK_PDF);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unbinder.unbind();
    }

    @SuppressLint("CheckResult")
    public void getRecentDocs() {
        startAnim();
        ApiInterface apiService = ApiClient.getClient(this)
                .create(ApiInterface.class);
        apiService.getRecentDocs(o_id, "10")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<GetFiles>() {

                    @Override
                    public void onSuccess(GetFiles getFiles) {
                        stopAnim();
                        if (binding.emptyTxt.getText() != null) {
                            if (binding.emptyTxt.getVisibility() == View.VISIBLE) {
                                binding.emptyTxt.setVisibility(View.GONE);
                                binding.folderRecycleview.setVisibility(View.VISIBLE);
                                binding.iconMore.setVisibility(View.VISIBLE);
                            }
                        }
                        if (getFiles.getStatus().matches("1")) {
                            stopAnim();
                            documents = getFiles.getDocuments();

                            List<SubFolder> subFolders = getFiles.getSubFolders();
                            folderFiles = combineFolderFiles(documents, subFolders);
                            if (folderFiles.size() != 0) {
                                folder_adapter = new File_Adapter(folderFiles, documents, ShowFileActivity.this, new File_Adapter.ClickListener() {
                                    @Override
                                    public void onPositionClicked(Document datum, int id, int position) {

                                        Intent intent = new Intent(ShowFileActivity.this, DocumentViewActivity.class);
                                        intent.putExtra("file_name", datum.getAttachment());
                                        intent.putExtra("file_id", datum.getId());
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onLongClicked(int position) {

                                    }

                                    @Override
                                    public void onButtonClick(int position) {


                                    }

                                    @Override
                                    public void onPositionClicked(FolderFile datum, int id, int position) {
                                        if (datum.isFolder()) {
                                            if (id == 2) {

                                                if (acc_id == -1) {

                                                    Toast.makeText(ShowFileActivity.this, "Please Select Accountant ", Toast.LENGTH_SHORT).show();

                                                } else {

                                                    assignUnAsAcc(Integer.valueOf(sharedPrefManager.getuserinfo().getId()), acc_id, Integer.valueOf(datum.getId()), "add");
                                                }


                                            } else if (id == 3) {

                                                if (acc_id == -1) {

                                                    Toast.makeText(ShowFileActivity.this, "Please Select Accountant ", Toast.LENGTH_SHORT).show();

                                                } else {

                                                    assignUnAsAcc(Integer.valueOf(sharedPrefManager.getuserinfo().getId()), acc_id, Integer.valueOf(datum.getId()), "remove");
                                                }


                                            } else if (id == 1) {
                                                if (recievedFile == null) {
                                                    Intent intent = new Intent(ShowFileActivity.this, ShowFileActivity.class);
                                                    intent.putExtra("f_id", datum.getId());
                                                    ShowFileActivity.this.startActivity(intent);
                                                } else {
                                                    Intent intent = new Intent(ShowFileActivity.this, UploadPdfActivity.class);
                                                    intent.putExtra("displayName", recievedFile.getName());
                                                    intent.putExtra("type", "pdf");
                                                    intent.putExtra("f_id", datum.getId());
                                                    intent.putExtra("file", recievedFile.getPath());
                                                    intent.putExtra("from", "Folder_Fragment");
                                                    recievedFile = null;
                                                    startActivity(intent);

                                                }
                                            }
                                        } else {
                                            Intent intent = new Intent(ShowFileActivity.this, DocumentViewActivity.class);
                                            intent.putExtra("file_name", datum.getAttachment());
                                            intent.putExtra("file_id", datum.getId());
                                            startActivity(intent);
                                        }
                                    }

                                    @Override
                                    public void onChatCLick(String id, int position) {
                                        openWeb(id);
                                    }

                                    @Override
                                    public void rename(int position) {
                                        callRenameDialog(position);
                                    }
                                });
                                binding.folderRecycleview.setAdapter(folder_adapter);
                            }


                        } else {
                            binding.emptyTxt.setText(R.string.no_recent_file);
                            binding.emptyTxt.setClickable(false);
                            binding.emptyTxt.setVisibility(View.VISIBLE);
                            binding.iconMore.setVisibility(View.GONE);
                        }

                        binding.swipeRefresh.setRefreshing(false);

                    }

                    @Override
                    public void onError(Throwable e) {
                        stopAnim();
                        Log.e("onError: ", e.toString());
                        if (binding.emptyTxt.getText() != null) {
                            binding.emptyTxt.setText(R.string.no_recent_file);
                            binding.emptyTxt.setClickable(false);
                            binding.emptyTxt.setVisibility(View.VISIBLE);
                            binding.swipeRefresh.setRefreshing(false);
                        }
                    }
                });
    }

    private List<FolderFile> combineFolderFiles(List<Document> docs, List<SubFolder> folders) {
        List<FolderFile> folderFiles = new ArrayList<>();
        int docsSize = docs.size();

        if (folders != null){
            int foldersSize = folders.size();
            for (int i = 0; i < foldersSize; i++) {
                SubFolder subFolder = folders.get(i);
                FolderFile folderFile = new FolderFile();
                folderFile.setFolder(true);
                folderFile.setId(subFolder.getId());
                folderFile.setOwnerId(subFolder.getOwnerId());
                folderFile.setAccountantId(subFolder.getAccountantId());
                folderFile.setFolderName(subFolder.getFolderName());
                folderFile.setFolderAsOrNot(subFolder.getFolderAsOrNot());
                folderFile.setDays(subFolder.getDays());
                folderFile.setNumOfDocs(subFolder.getNumOfDocs());
                folderFiles.add(folderFile);
            }
        }

        for (int i = 0; i < docsSize; i++) {
            Document document = docs.get(i);

            FolderFile folderFile = new FolderFile();

            folderFile.setFolder(false);
            folderFile.setId(document.getId());
            folderFile.setDocFolderId(document.getDocFolderId());
            folderFile.setDocOwnerId(document.getDocOwnerId());
            folderFile.setDocName(document.getDocName());
            folderFile.setCost(document.getCost());
            folderFile.setDocSize(document.getDocSize());
            folderFile.setCreated(document.getCreated());
            folderFile.setModified(document.getModified());
            folderFile.setSelect(document.isIs_select());
            folderFile.setAttachment(document.getAttachment());
            folderFiles.add(folderFile);
        }

        return folderFiles;
    }

    @SuppressLint("CheckResult")
    public void getFiles(String f_id) {

        startAnim();

        ApiInterface apiService = ApiClient.getClient(this)
                .create(ApiInterface.class);

        apiService.getFiles(f_id, Pref.getStringValue(this, Utils.selected_year, Utils.this_year))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<GetFiles>() {
                    @Override
                    public void onSuccess(GetFiles getFiles) {
                        stopAnim();
                        if (binding.emptyTxt.getText() != null) {
                            if (binding.emptyTxt.getVisibility() == View.VISIBLE) {
                                binding.emptyTxt.setVisibility(View.GONE);
                                binding.folderRecycleview.setVisibility(View.VISIBLE);
                                binding.iconMore.setVisibility(View.VISIBLE);
                            }
                        }
                        if (getFiles.getStatus().matches("1")) {
                            stopAnim();
                            documents = getFiles.getDocuments();
                            List<SubFolder> subFolders = getFiles.getSubFolders();
                            folderFiles = combineFolderFiles(documents, subFolders);
                            if (folderFiles.size() != 0) {
                                File_Adapter folder_adapter = new File_Adapter(folderFiles, documents, ShowFileActivity.this, new File_Adapter.ClickListener() {
                                    @Override
                                    public void onPositionClicked(Document datum, int id, int position) {
                                        if (datum.getAttachment().toLowerCase().contains(".pdf")) {
                                            Intent intent = new Intent(ShowFileActivity.this, DocumentViewActivity.class);
                                            intent.putExtra("file_name", datum.getAttachment());
                                            intent.putExtra("file_id", datum.getId());
                                            startActivity(intent);
                                        } else if (datum.getAttachment().toLowerCase().contains(".png")) {
//                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://sdb.topnotchhub.com/assets/uploads/docs/" + datum.getAttachment()));
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://skipdaboxes.ca/assets/uploads/docs/" + datum.getAttachment()));
                                            startActivity(browserIntent);
                                            Toast.makeText(ShowFileActivity.this, "name = " +
                                                    datum.getAttachment(), Toast.LENGTH_SHORT).show();
                                            /*startActivity(new Intent(ShowFileActivity.this, Full_View_Image.class)
                                                    .putExtra("img", datum.getAttachment())
                                                    .putExtra("doc_id", ""));*/
                                        } else {
//                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://sdb.topnotchhub.com/assets/uploads/docs/" + datum.getAttachment()));
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://skipdaboxes.ca/assets/uploads/docs/" + datum.getAttachment()));
                                            startActivity(browserIntent);
                                            Toast.makeText(ShowFileActivity.this, "name = " +
                                                    datum.getAttachment(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onLongClicked(int position) {

                                    }

                                    @Override
                                    public void onButtonClick(int position) {

                                        for (int i = 0; i <= documents.size(); i++) {

                                            if (documents.get(i).isIs_select()) {
                                                if (select_id.isEmpty()) {
                                                    select_id = documents.get(i).getId();
                                                } else {
                                                    select_id = select_id + "," + documents.get(i).getId();
                                                    Log.e("selected_id", select_id);
                                                }
                                            }

                                        }
                                    }

                                    @Override
                                    public void onPositionClicked(FolderFile datum, int id, int position) {
                                        if (datum.isFolder()) {
                                            if (id == 2) {

                                                if (acc_id == -1) {

                                                    Toast.makeText(ShowFileActivity.this, "Please Select Accountant ", Toast.LENGTH_SHORT).show();

                                                } else {

                                                    assignUnAsAcc(Integer.valueOf(sharedPrefManager.getuserinfo().getId()), acc_id, Integer.valueOf(datum.getId()), "add");
                                                }


                                            } else if (id == 3) {

                                                if (acc_id == -1) {

                                                    Toast.makeText(ShowFileActivity.this, "Please Select Accountant ", Toast.LENGTH_SHORT).show();

                                                } else {

                                                    assignUnAsAcc(Integer.valueOf(sharedPrefManager.getuserinfo().getId()), acc_id, Integer.valueOf(datum.getId()), "remove");
                                                }


                                            } else if (id == 1) {
                                                if (recievedFile == null) {
                                                    Intent intent = new Intent(ShowFileActivity.this, ShowFileActivity.class);
                                                    intent.putExtra("f_id", datum.getId());
                                                    ShowFileActivity.this.startActivity(intent);
                                                } else {
                                                    Intent intent = new Intent(ShowFileActivity.this, UploadPdfActivity.class);
                                                    intent.putExtra("displayName", recievedFile.getName());
                                                    intent.putExtra("type", "pdf");
                                                    intent.putExtra("f_id", datum.getId());
                                                    intent.putExtra("file", recievedFile.getPath());
                                                    intent.putExtra("from", "Folder_Fragment");
                                                    recievedFile = null;
                                                    startActivity(intent);

                                                }
                                            }
                                        } else {
                                            if (datum.getAttachment().toLowerCase().contains(".pdf")) {
                                                Intent intent = new Intent(ShowFileActivity.this, DocumentViewActivity.class);
                                                intent.putExtra("file_name", datum.getAttachment());
                                                intent.putExtra("file_id", datum.getId());
                                                startActivity(intent);
                                            } else if (datum.getAttachment().toLowerCase().contains(".png")) {
//                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://sdb.topnotchhub.com/assets/uploads/docs/" + datum.getAttachment()));
                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://skipdaboxes.ca/assets/uploads/docs/" + datum.getAttachment()));
                                                startActivity(browserIntent);
                                                Toast.makeText(ShowFileActivity.this, "name = " +
                                                        datum.getAttachment(), Toast.LENGTH_SHORT).show();
                                            /*startActivity(new Intent(ShowFileActivity.this, Full_View_Image.class)
                                                    .putExtra("img", datum.getAttachment())
                                                    .putExtra("doc_id", ""));*/
                                            } else {
//                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://sdb.topnotchhub.com/assets/uploads/docs/" + datum.getAttachment()));
                                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://skipdaboxes.ca/assets/uploads/docs/" + datum.getAttachment()));
                                                startActivity(browserIntent);
                                                Toast.makeText(ShowFileActivity.this, "name = " +
                                                        datum.getAttachment(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onChatCLick(String id, int position) {

                                        openWeb(id);
                                    }

                                    @Override
                                    public void rename(int position) {
                                        callRenameDialog(position);
                                    }
                                });

                                binding.folderRecycleview.setAdapter(folder_adapter);
                            }


                        } else {
                            if (null != binding.emptyTxt.getText()) {
                                binding.emptyTxt.setText(R.string.add_file);
                                binding.emptyTxt.setClickable(true);
                                binding.emptyTxt.setVisibility(View.VISIBLE);
                            }
                        }

                        binding.swipeRefresh.setRefreshing(false);

                    }

                    @Override
                    public void onError(Throwable e) {
                        stopAnim();
                        if (empty_txt != null) {
                            empty_txt.setText(R.string.add_file);
                            empty_txt.setClickable(true);
                            empty_txt.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setRefreshing(false);
                        }


                    }
                });

    }

    void startAnimLoad() {
//        loading_view.setVisibility(View.VISIBLE);

        avi.setVisibility(View.VISIBLE);
        avi.show();
        // or avi.smoothToShow();
    }

    @SuppressLint("CheckResult")
    public void assignUnAsAcc(int user_id, int accountant_id, int f_id, String purpose) {


        startAnimLoad();

        ApiInterface apiService = ApiClient.getClient(ShowFileActivity.this)
                .create(ApiInterface.class);


        apiService.AssignUnAsFolder(user_id, accountant_id, f_id, purpose)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Status>() {
                    @Override
                    public void onSuccess(Status status) {

                        if (status.getStatus().matches("1")) {


                            stopAnim();

                            Toast.makeText(ShowFileActivity.this, status.getMessage(), Toast.LENGTH_LONG).show();


                        } else {

                            stopAnim();
                            Toast.makeText(ShowFileActivity.this, status.getMessage(), Toast.LENGTH_LONG).show();

                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        // Network error
                        stopAnim();
                    }
                });

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

   // @OnClick(R.id.back_icon)
    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
//        startActivity(new Intent(this, Home_Activity.class));
//        finishAffinity();
        super.onBackPressed();
        ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

        if (taskList.get(0).numActivities == 1 && taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
            startActivity(new Intent(this, Home_Activity.class));
            finishAffinity();
        } else
            finish();
//        finish();
        //super.onBackPressed();
    }

    @Override
    public void OnCloseDialog() {
        if (isPrivate) {
            getFiles(f_id);
        }

    }

    @SuppressLint("CheckResult")
    public void searchfolder(String search_value) {
        if (binding.emptyTxt.getVisibility() == View.VISIBLE) {
            binding.emptyTxt.setVisibility(View.GONE);
            binding.folderRecycleview.setVisibility(View.VISIBLE);
            binding.iconMore.setVisibility(View.VISIBLE);
        }

        startAnim();
        ApiInterface apiService = ApiClient.getClient(this)
                .create(ApiInterface.class);

        apiService.documentsearching(search_value, "document", Integer.parseInt(sharedPrefManager.getuserinfo().getId()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SearchDocumentDataModel>() {
                    @Override
                    public void onSuccess(SearchDocumentDataModel status) {

                        if (status.getStatus().matches("1")) {
                            stopAnim();
                            documents = status.getResults();
                            List<SubFolder> subFolders = status.getFolders();
                            folderFiles = combineFolderFiles(documents, subFolders);

                            File_Adapter folder_adapter = new File_Adapter(folderFiles, documents, ShowFileActivity.this, new File_Adapter.ClickListener() {
                                @Override
                                public void onPositionClicked(Document datum, int id, int position) {

                                    /*Log.d("file_name", datum.getDocName());
                                    Intent intent = new Intent(ShowFileActivity.this, DocumentViewActivity.class);
                                    intent.putExtra("file_name", datum.getDocName());
                                    intent.putExtra("file_id", datum.getId());
                                    startActivity(intent);
*/

                                    if (datum.getAttachment().toLowerCase().contains(".pdf")) {
                                        Intent intent = new Intent(ShowFileActivity.this, DocumentViewActivity.class);
                                        intent.putExtra("file_name", datum.getAttachment());
                                        intent.putExtra("file_id", datum.getId());
                                        startActivity(intent);
                                    } else if (datum.getAttachment().toLowerCase().contains(".png")) {
//                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://sdb.topnotchhub.com/assets/uploads/docs/" + datum.getAttachment()));
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://skipdaboxes.ca/assets/uploads/docs/" + datum.getAttachment()));
                                        startActivity(browserIntent);
                                        Toast.makeText(ShowFileActivity.this, "name = " +
                                                datum.getAttachment(), Toast.LENGTH_SHORT).show();
                                            /*startActivity(new Intent(ShowFileActivity.this, Full_View_Image.class)
                                                    .putExtra("img", datum.getAttachment())
                                                    .putExtra("doc_id", ""));*/
                                    } else {
//                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://sdb.topnotchhub.com/assets/uploads/docs/" + datum.getAttachment()));
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://skipdaboxes.ca/assets/uploads/docs/" + datum.getAttachment()));
                                        startActivity(browserIntent);
                                        Toast.makeText(ShowFileActivity.this, "name = " +
                                                datum.getAttachment(), Toast.LENGTH_SHORT).show();
                                    }

                                    //  startActivity(new Intent(ShowFileActivity.this,DocumentViewActivity.class));
                                }

                                @Override
                                public void onLongClicked(int position) {

                                }

                                @Override
                                public void onButtonClick(int position) {


//                                    for (int i=0;i<=documents.size();i++){
//
//                                        if (documents.get(i).isIs_select()) {
//                                            if (select_id.isEmpty()) {
//                                                select_id = documents.get(i).getId();
//                                            } else {
//                                                select_id = select_id + "," + documents.get(i).getId();
//
//                                                Log.e("selected_id",select_id);
//                                            }
//                                        }
//
//                                    }
                                }

                                @Override
                                public void onPositionClicked(FolderFile datum, int id, int position) {
                                    if (datum.isFolder()) {
                                        if (id == 2) {

                                            if (acc_id == -1) {

                                                Toast.makeText(ShowFileActivity.this, "Please Select Accountant ", Toast.LENGTH_SHORT).show();

                                            } else {

                                                assignUnAsAcc(Integer.valueOf(sharedPrefManager.getuserinfo().getId()), acc_id, Integer.valueOf(datum.getId()), "add");
                                            }


                                        } else if (id == 3) {

                                            if (acc_id == -1) {

                                                Toast.makeText(ShowFileActivity.this, "Please Select Accountant ", Toast.LENGTH_SHORT).show();

                                            } else {

                                                assignUnAsAcc(Integer.valueOf(sharedPrefManager.getuserinfo().getId()), acc_id, Integer.valueOf(datum.getId()), "remove");
                                            }


                                        } else if (id == 1) {
                                            if (recievedFile == null) {
                                                Intent intent = new Intent(ShowFileActivity.this, ShowFileActivity.class);
                                                intent.putExtra("f_id", datum.getId());
                                                ShowFileActivity.this.startActivity(intent);
                                            } else {
                                                Intent intent = new Intent(ShowFileActivity.this, UploadPdfActivity.class);
                                                intent.putExtra("displayName", recievedFile.getName());
                                                intent.putExtra("type", "pdf");
                                                intent.putExtra("f_id", datum.getId());
                                                intent.putExtra("file", recievedFile.getPath());
                                                intent.putExtra("from", "Folder_Fragment");
                                                recievedFile = null;
                                                startActivity(intent);

                                            }
                                        }
                                    } else {
                                        if (datum.getAttachment().toLowerCase().contains(".pdf")) {
                                            Intent intent = new Intent(ShowFileActivity.this, DocumentViewActivity.class);
                                            intent.putExtra("file_name", datum.getAttachment());
                                            intent.putExtra("file_id", datum.getId());
                                            startActivity(intent);
                                        } else if (datum.getAttachment().toLowerCase().contains(".png")) {
//                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://sdb.topnotchhub.com/assets/uploads/docs/" + datum.getAttachment()));
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://skipdaboxes.ca/assets/uploads/docs/" + datum.getAttachment()));
                                            startActivity(browserIntent);
                                            Toast.makeText(ShowFileActivity.this, "name = " +
                                                    datum.getAttachment(), Toast.LENGTH_SHORT).show();
                                            /*startActivity(new Intent(ShowFileActivity.this, Full_View_Image.class)
                                                    .putExtra("img", datum.getAttachment())
                                                    .putExtra("doc_id", ""));*/
                                        } else {
//                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://sdb.topnotchhub.com/assets/uploads/docs/" + datum.getAttachment()));
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://skipdaboxes.ca/assets/uploads/docs/" + datum.getAttachment()));
                                            startActivity(browserIntent);
                                            Toast.makeText(ShowFileActivity.this, "name = " +
                                                    datum.getAttachment(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onChatCLick(String id, int position) {
                                    openWeb(id);
                                }

                                @Override
                                public void rename(int position) {
                                    callRenameDialog(position);
                                }
                            });


                            binding.folderRecycleview.setAdapter(folder_adapter);


                        } else {
                            stopAnim();
                            binding.folderRecycleview.setVisibility(View.GONE);
                            binding.emptyTxt.setText("Not Found");
                            binding.emptyTxt.setVisibility(View.VISIBLE);

                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        binding.emptyTxt.setVisibility(View.VISIBLE);
                        stopAnim();
                    }


                });


    }

    private void callRenameDialog(int folderId) {
        CustomDialog customDialog = new CustomDialog(this, 0);
        customDialog.renameDialogShow(this, String.valueOf(folderId));
    }

    @SuppressLint("ResourceType")
   // @OnClick(R.id.select_camera)
    public void select_fromCamera(View view) {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, PICK_IMAGE_CAMERA);
        Intent intent = new Intent(getApplicationContext(), ImagePickerDemo.class);
        intent.putExtra("picker", "multi");
        intent.putExtra("f_id", f_id);
        startActivity(intent);
    }


//    @OnClick(R.id.empty_txt)
//    public void select_fromGallery(View view) {
//        Intent pickPhoto = new Intent(Intent.ACTION_GET_CONTENT);
//        pickPhoto.setType("image/*");
//        startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
//    }

    public void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ShowFileActivity.this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            } else
                Toast.makeText(ShowFileActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(ShowFileActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @SuppressLint("Range")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputStreamImg = null;
//for normal camera
//        if (requestCode == PICK_IMAGE_CAMERA) {
//            try {
//                bitmap = (Bitmap) data.getExtras().get("data");
//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//
//                Log.e("Activity", "Pick from Camera::>>> ");
//
//                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//                createDirectoryAndSaveFile(bitmap, "IMG_" + timeStamp + ".jpg");
//
//                AddFileDialog addFileDialog = new AddFileDialog(this, this, f_id, bitmap, image_file);
//                Objects.requireNonNull(addFileDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                addFileDialog.show();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        if (requestCode == INTENT_REQUEST_GET_IMAGES && resultCode == AppCompatActivity.RESULT_OK) {

            ArrayList<Uri> image_uris = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
            Log.e("onActivityResult: ", String.valueOf(image_uris.size()));
            ArrayList<String> path = new ArrayList<>();
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.MANAGE_EXTERNAL_STORAGE}, PICK_IMAGE_CAMERA);
                } else {
                    Intent intent = new Intent(this, UploadFileActivity.class);
                    intent.putExtra("f_id", f_id);
                    intent.putExtra("via_camera", image_uris);
                    intent.putExtra("type", "png");
                    startActivity(intent);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (requestCode == PICK_IMAGE_GALLERY) {

            if (data != null) {

                File image_file = new File(getSelectedImage(data).get(0));
                try {
                    if (image_file.exists()) {
                        bitmap = BitmapFactory.decodeFile(image_file.getAbsolutePath());
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    }

                    Log.e("onActivityResult: ", " selectedImage " + getSelectedImage(data).get(0));
                    AddFileDialog addFileDialog = new AddFileDialog(this, this, f_id, bitmap, image_file);
                    addFileDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    addFileDialog.show();

                    //  profile_image.setImageBitmap(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else if (requestCode == PICK_PDF) {

            if (data != null) {
////                ClipData clipData = data.getClipData();
//                Uri fileuri = data.getData();
//                /*for (int count =0; count<clipData.getItemCount(); count++){
//                    Uri uri = clipData.getItemAt(count).getUri();
//                    //do something
//                    Log.e("onActivityResult: ", String.valueOf(data.getClipData().getItemAt(count).getUri()));
//
//                }*/
//
//                String docFilePath = getFileNameByUri(this, fileuri);
////                Log.e("onActivityResult: ", docFilePath);
                List<String> paths = new ArrayList<>();
                if (data.getClipData() != null) {
                    Toast.makeText(this, "" + data.getClipData().getItemCount() + " files selected", Toast.LENGTH_SHORT).show();
                    // Getting the length of data and logging up the logs using index
                    for (int index = 0; index < data.getClipData().getItemCount(); index++) {
                        paths.add(FileUtils.getPath(ShowFileActivity.this,
                                data.getClipData().getItemAt(index).getUri()));
                        Log.d("debugging", " path = ");
                    }
                    Log.d("debugging", " count = " + data.getClipData().getItemCount());
                    Intent intent = new Intent(this, UploadPdfActivity.class);
                    intent.putExtra("displayName", "" + data.getClipData().getItemCount() + " files");
                    intent.putExtra("type", "pdf");
                    intent.putExtra("f_id", f_id);
                    intent.putExtra("from", "Folder_");
                    intent.putExtra("file", new Gson().toJson(paths));
                    startActivity(intent);
                } else {
                    Uri uri = data.getData();
                    String path = FileUtils.getPath(this, uri);
                    File myFile = new File(path);
                    paths.add(path);
                    String displayName = null;

                    if (uri.getPath().startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            }
                        } finally {
                            cursor.close();
                        }
                    }
                    else if (uri.getPath().startsWith("file://")) {
                        displayName = myFile.getName();
                    }

//                    Uri uri = data.getData();
//                    String uriString = uri.toString();
//                    File myFile = new File(uriString);
//                    String path = myFile.getAbsolutePath();
//                    String displayName = null;
//                    paths.add(path);
//                    if (uriString.startsWith("content://")) {
//                        Cursor cursor = null;
//                        try {
//                            cursor = getContentResolver().query(uri, null, null, null, null);
//                            if (cursor != null && cursor.moveToFirst()) {
//                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
//                            }
//                        } finally {
//                            cursor.close();
//                        }
//                    } else if (uriString.startsWith("file://")) {
//                        displayName = myFile.getName();
//                    }
                    Intent intent = new Intent(this, UploadPdfActivity.class);
                    intent.putExtra("displayName", displayName);
                    intent.putExtra("type", "pdf");
                    intent.putExtra("f_id", f_id);
                    intent.putExtra("from", "Folder_");
                    intent.putExtra("file", new Gson().toJson(paths));
                    startActivity(intent);
                }
            }


        }
//        else if (requestCode == INTENT_REQUEST_GET_IMAGES && resultCode == Activity.RESULT_OK) {
//            ArrayList<Uri> image_uris = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
//            Log.e("data_img", String.valueOf(image_uris));
//            if (data != null) {
//                image_path = new ArrayList<>();
//
////                image_path = new ArrayList<>(getSelectedImage(data));
//                startActivity(new Intent(this, UploadFileActivity.class)
//                        .putExtra("f_id", f_id)
//                        .putExtra("via_camera", true));
//            }
//        }
    }

    private ArrayList<String> getSelectedImage(Intent data) {

        ArrayList<String> result = new ArrayList<>();

        ClipData clipData;
        clipData = data.getClipData();
        if (clipData != null) {
            for (int i = 0; i < clipData.getItemCount(); i++) {
                ClipData.Item videoItem = clipData.getItemAt(i);
                Uri videoURI = videoItem.getUri();
                String filePath = getPath(this, videoURI);
                result.add(filePath);
            }
        } else {
            Uri videoURI = data.getData();
            String filePath = getPath(this, videoURI);
            result.add(filePath);
        }

        return result;
    }


    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private String getFileNameByUri(Context context, Uri uri) {
        String filepath = "";//default fileName
        //Uri filePathUri = uri;
        File file;
        if (uri.getScheme().compareTo("content") == 0) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{android.provider.MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.ORIENTATION}, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();

            String mImagePath = cursor.getString(column_index);
            cursor.close();
            filepath = mImagePath;

        } else if (uri.getScheme().compareTo("file") == 0) {
            try {
                file = new File(new URI(uri.toString()));
                if (file.exists())
                    filepath = file.getAbsolutePath();

            } catch (URISyntaxException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            filepath = uri.getPath();
        }
        return filepath;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        final String column = "_data";
        final String[] projection = {
                column
        };

        try (Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                null)) {
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public String getRealPathFromURI(Uri uri) {
        String wholeID = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // Where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel,
                new String[]{id}, null);

        String filePath = "";

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }

        cursor.close();
        return filePath;
    }


    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name));

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/" + getString(R.string.app_name) + "/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard/" + getString(R.string.app_name) + "/"), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();

            image_file = file;


        } catch (Exception e) {
            e.printStackTrace();
            image_file = null;
        }


    }


    private void opendialog() {

        PopupMenu popup = new PopupMenu(Objects.requireNonNull(this), binding.iconMore);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.menu_dialog, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tv_move:
                        movedialog();
                        break;

                    case R.id.tv_delete:

                        if(folderFiles.size()!=0) {
                            for (int i = 0; i < folderFiles.size(); i++) {
                                if (folderFiles.get(i).isSelect()) {
                                    if (select_id.isEmpty()) {
                                        select_id = folderFiles.get(i).getId();
                                    } else {
                                        select_id = select_id + "," + folderFiles.get(i).getId();
                                        Log.e("selected_id", select_id);
                                    }
                                }

                            }
                        }
                        if (select_id.isEmpty()) {
                            Toast.makeText(ShowFileActivity.this, "Please Select Document", Toast.LENGTH_SHORT).show();
                        } else {
                            DeleteFolder(select_id, sharedPrefManager.getuserinfo().getId());

                        }
                        break;
                    case R.id.menu_create_folder:
                        callDialog();
                        break;

                }
                return true;
            }
        });
        popup.show(); //showing popup menu

    }

    @SuppressLint("NonConstantResourceId")
    private void openFolderDialog() {

        PopupMenu popup = new PopupMenu(Objects.requireNonNull(this), binding.iconMore);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.menu__folder, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.tv_move:
                    movedialog();
                    break;

                case R.id.tv_delete:

                    for (int i = 0; i < folderFiles.size(); i++) {
                        if (folderFiles.get(i).isSelect()) {
                            if (select_id.isEmpty()) {
                                select_id = folderFiles.get(i).getId();
                            } else {
                                select_id = select_id + "," + folderFiles.get(i).getId();
                                Log.e("selected_id", select_id);
                            }
                        }

                    }

                    if (select_id.isEmpty()) {
                        Toast.makeText(ShowFileActivity.this, "Please Select Document", Toast.LENGTH_SHORT).show();
                    } else {
                        DeleteFolder(select_id, sharedPrefManager.getuserinfo().getId());

                    }
                    break;

            }
            return true;
        });
        popup.show(); //showing popup menu

    }


    private void movedialog() {
        Dialog dialog = new Dialog(this, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_move);
        folder_recycleview = dialog.findViewById(R.id.folder1_recycleview);


        getFolder(Integer.parseInt(sharedPrefManager.getuserinfo().getId()));

        ImageView cancel_icon = dialog.findViewById(R.id.cancel_icon);
        cancel_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

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

                        if (getFolders.getStatus().matches("1")) {
                            stopAnim();

                            folders = getFolders.getFolders();
                            folders = combineParentSubFolders(getFolders.getFolders());
                            // TODO: 19-12-2018 Folder Details
                            if (folders.size() != 0) {
                                AdapterDialogFolder folder_adapter = new AdapterDialogFolder(folders, ShowFileActivity.this, new Folder_Adapter.ClickListener() {
                                    @Override
                                    public void onPositionClicked(Folder datum, int id, int position) {
                                        for (int i = 0; i < folderFiles.size(); i++) {
                                            if (folderFiles.get(i).isSelect()) {
                                                if (select_id.isEmpty()) {
                                                    select_id = folderFiles.get(i).getId();
                                                } else {
                                                    select_id = select_id + "," + folderFiles.get(i).getId();
                                                    Log.e("selected_id", select_id);
                                                }
                                            }
                                        }

                                        if (select_id.isEmpty()) {
                                            Toast.makeText(ShowFileActivity.this, "Please Select Document", Toast.LENGTH_SHORT).show();
                                        } else {

                                            MoveFolder(select_id, sharedPrefManager.getuserinfo().getId(), folders.get(position).getId());

                                        }
                                    }

                                    @Override
                                    public void onLongClicked(int position) {

                                    }

                                });
                                final LinearLayoutManager layoutManager = new LinearLayoutManager(ShowFileActivity.this);
                                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                folder_recycleview.setLayoutManager(layoutManager);
                                folder_recycleview.setAdapter(folder_adapter);

                            }


                        } else {

                            stopAnim();

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        stopAnim();

                    }
                });
    }

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
            folder1.setSubfolder(true);
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
        folder1.setSubfolder(false);
        return folder1;
    }


    public void DeleteFolder(String DocId, String doc_owner_id) {

        startAnim();


        ApiInterface apiService = ApiClient.getClient(this)
                .create(ApiInterface.class);


        apiService.DeleteMultipleDoc(DocId, doc_owner_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Delete_Multi_Folder>() {

                    @Override
                    public void onSuccess(Delete_Multi_Folder delete_multi_folder) {
                        stopAnim();

                        if (delete_multi_folder.getStatus().matches("1")) {
                            Log.e("exeption", "=" + delete_multi_folder.getStatus());
                            Toast.makeText(ShowFileActivity.this, "Document Deleted Successfully", Toast.LENGTH_SHORT).show();


                            startActivity(new Intent(ShowFileActivity.this, Home_Activity.class));
                            finish();

                        } else {

                            Toast.makeText(ShowFileActivity.this, "Error", Toast.LENGTH_SHORT).show();
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


    @SuppressLint("CheckResult")
    public void MoveFolder(String DocId, String doc_owner_id, String TargetFolderId) {

        startAnim();


        ApiInterface apiService = ApiClient.getClient(this)
                .create(ApiInterface.class);


        apiService.MoveMultipleDoc(DocId, doc_owner_id, TargetFolderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MoveMultiDoc>() {

                    @Override
                    public void onSuccess(MoveMultiDoc moveFolder) {
                        stopAnim();

                        if (moveFolder.getStatus().matches("3")) {
                            Log.e("exeption", "=" + moveFolder.getStatus());


                            startActivity(new Intent(ShowFileActivity.this, Home_Activity.class));
                            finish();

                        } else {
                            startActivity(new Intent(ShowFileActivity.this, Home_Activity.class));
                            finish();
//                            Toast.makeText(ShowFileActivity.this, "Error", Toast.LENGTH_SHORT).show();
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


    private  void openWeb(String id){

//        String url = "https://sdb.topnotchhub.com/Documents/Chat/"+id;
        String url = "https://skipdaboxes.ca/Documents/Chat/"+id;

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
