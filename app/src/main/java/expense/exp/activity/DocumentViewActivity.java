package expense.exp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import expense.exp.R;
import expense.exp.adapter.Folder_Adapter;
import expense.exp.databinding.ActivityCropBinding;
import expense.exp.databinding.ActivityDocumentViewBinding;
import expense.exp.helper.Pref;
import expense.exp.helper.SharedPrefManager;
import expense.exp.helper.Utils;
import expense.exp.internet.ApiClient;
import expense.exp.internet.ApiInterface;
import expense.exp.internet.model.Folder;
import expense.exp.internet.model.GetFolders;
import expense.exp.internet.model.SearchDocumentDataModel;
import expense.exp.model_class.MoveFolder;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class DocumentViewActivity extends AppCompatActivity implements Folder_Adapter.ClickListener {
    AVLoadingIndicatorView avi;
    PDFView pdfView;
    String url, file_id;
    ImageView back_icon;
    TextView tv_hearder;
    ImageView iv_back;
    List<Folder> folders;
    int acc_id = -1;
    String dest_file_path = "test.pdf";
    int downloadedSize = 0, totalsize;
    float per = 0;
    Uri path;
    RecyclerView folder_recycleview;
    @SuppressLint("SetJavaScriptEnabled")
    private SharedPrefManager sharedPrefManager;
    ActivityDocumentViewBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_document_view);
        binding = ActivityDocumentViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        sharedPrefManager = new SharedPrefManager(this);

        Intent intent = getIntent();

        String s = intent.getStringExtra("file_name");
        file_id = intent.getStringExtra("file_id");

        Log.e("#########file######",s);
//        url = "http://sdb.topnotchhub.com/assets/uploads/docs/" + s;
        url = "https://skipdaboxes.ca/assets/uploads/docs/" + s;
        String doc = "<iframe src='http://docs.google.com/viewer?url=https://www.google.co.in/' width='100%' height='100%'  style='border: none;'></iframe>";




        binding.tvHearder.setText(s);
        Log.e("doc_path", url);

        binding.avi.bringToFront();
        Asynctask asynctask = new Asynctask();
        asynctask.execute();
        binding.backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendialog();
            }
        });

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        downloadAndOpenPDF();

    }

    void downloadAndOpenPDF() {
        new Thread(new Runnable() {
            public void run() {
                Uri path = Uri.fromFile(downloadFile(url));

                Log.e("pDFPATH",path.toString());
            }
        }).start();

    }

    @Override
    public void onPositionClicked(Folder datum, int id, int position) {

    }

    @Override
    public void onLongClicked(int position) {

    }

//    @Override
//    public void onLongClicked(int position) {
//
//    }

    private class Asynctask extends AsyncTask<Void, Void, Void> {
        InputStream input;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startAnim();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                input = new URL(url).openStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            binding.pdfView.fromStream(input)
                    .onRender(new OnRenderListener() {
                        @Override
                        public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                            binding.pdfView.fitToWidth();
                            stopAnim();
                        }
                    })
                    .load();
        }
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

    private void opendialog() {

        PopupMenu popup = new PopupMenu(Objects.requireNonNull(this), binding.backIcon);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.dialog, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tv_print:
                        print();
                        break;
                    case R.id.tv_share:
                        shareIntent();
                        break;
                    case R.id.tv_move:
                        movedialog();
                        break;

                    case R.id.tv_delete:
                        DeleteFolder(file_id, sharedPrefManager.getuserinfo().getId());
                        break;

                }
                return true;
            }
        });
        popup.show(); //showing popup menu

    }

    private void shareIntent() {

//        File file = new File(url);
        File sdDir = Environment.getExternalStorageDirectory();
        File file = new File(sdDir + "/Download/expense.pdf" /* what you want to load in SD card */);
        if (!file.isFile()) {
            Log.e("uploadFile", "Source File not exist :" + "nooooooooooo");
        }
        else {


                Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);

                Intent intent = new Intent();
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
//                    intent.addCategory(Intent.CATEGORY_LAUNCHER);
//                }
                intent.putExtra(Intent.EXTRA_TEXT, "#SkipDaBox App");
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.setType("application/pdf");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setAction(Intent.ACTION_SEND);
                startActivity(Intent.createChooser(intent, null));
        }
//        return true;
//        File file = new File(Environment.getExternalStorageDirectory(), "expense.pdf");





    }

    private void print() {
        String str = "com.microsoft.office.word";


        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "expense.pdf");
        if (file.exists()) {
            Uri uri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/msword");
            PackageManager packageManager = getPackageManager();
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(Intent.createChooser(intent, "Choose app to open document"));
            } else {
                //Launch PlayStore
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + str)));

                } catch (android.content.ActivityNotFoundException n) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + str)));
                }
            }
        }

    }

    private void movedialog() {
        Dialog dialog = new Dialog(this, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_move);


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
    public void DeleteFolder(String DocId, String doc_owner_id) {

        startAnim();


        ApiInterface apiService = ApiClient.getClient(this)
                .create(ApiInterface.class);


        apiService.deleteDoc(DocId, doc_owner_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SearchDocumentDataModel>() {

                    @Override
                    public void onSuccess(SearchDocumentDataModel searchDocumentDataModel) {
                        stopAnim();

                        if (searchDocumentDataModel.getStatus().matches("1")) {
                            Log.e("exeption", "=" + searchDocumentDataModel.getStatus());


                            startActivity(new Intent(DocumentViewActivity.this, Home_Activity.class));
                            finish();

                        } else {

                            Toast.makeText(DocumentViewActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                            // TODO: 19-12-2018 Folder Details
                            if (folders.size() != 0) {

                                Folder_Adapter folder_adapter = new Folder_Adapter(false,folders, DocumentViewActivity.this, new Folder_Adapter.ClickListener() {
                                    @Override
                                    public void onPositionClicked(Folder datum, int id, int position) {

                                        MoveFolder(file_id, sharedPrefManager.getuserinfo().getId(), folders.get(position).getId());
                                    }

                                    @Override
                                    public void onLongClicked(int position) {

                                    }

                                });
                                final LinearLayoutManager layoutManager = new LinearLayoutManager(DocumentViewActivity.this);
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


    @SuppressLint("CheckResult")
    public void MoveFolder(String DocId, String doc_owner_id, String TargetFolderId) {

        startAnim();


        ApiInterface apiService = ApiClient.getClient(this)
                .create(ApiInterface.class);


        apiService.MoveDoc(DocId, doc_owner_id, TargetFolderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MoveFolder>() {

                    @Override
                    public void onSuccess(MoveFolder moveFolder) {
                        stopAnim();

                        if (moveFolder.getStatus().matches("1")) {
                            Log.e("exeption", "=" + moveFolder.getStatus());


                            startActivity(new Intent(DocumentViewActivity.this, Home_Activity.class));
                            finish();

                        } else {

                            Toast.makeText(DocumentViewActivity.this, "Error", Toast.LENGTH_SHORT).show();
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

    File downloadFile(String dwnload_file_path) {
        File file = null;
        try {

            URL url = new URL(dwnload_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);

            // connect
            urlConnection.connect();
            File mFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "");
            File dir = new File(mFolder.getAbsolutePath() + "");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.US);
            Date now = new Date();
            String fileName = "/expense.pdf";

//            // set the path where we want to save the file
//            File SDCardRoot = Environment.getExternalStorageDirectory();
//            // create a new file, to save the downloaded file
//            file = new File(SDCardRoot, dest_file_path);
//
//            FileOutputStream fileOutput = new FileOutputStream(file);

            // Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();


            file = new File(dir, fileName);
            FileOutputStream fileOutput = null;
            fileOutput = new FileOutputStream(file);
            // this is the total size of the file which we are
            // downloading
//            totalsize = urlConnection.getContentLength();
//            setText("Starting PDF download...");

            // create a buffer...
            byte[] buffer = new byte[1024 * 1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                per = ((float) downloadedSize / totalsize) * 100;
//                setText("Total PDF File size  : "
//                        + (totalsize / 1024)
//                        + " KB\n\nDownloading PDF " + (int) per
//                        + "% complete");
            }
            // close the output stream when complete //
            fileOutput.close();
//            Toast.makeText(this, "Download Complete. Open PDF Application installed in the device.", Toast.LENGTH_SHORT).show();
//            setText("Download Complete. Open PDF Application installed in the device.");

        } catch (final MalformedURLException e) {
            Toast.makeText(this, "Some error occured. Press back and try again.", Toast.LENGTH_SHORT).show();
            Log.e("uploadFile", "Source File not exist :" + e);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            Log.e("uploadFile", "Source File not exist :" + e1);
            e1.printStackTrace();
        }


        Log.e("fileeee", String.valueOf(file));
        return file;
    }
}