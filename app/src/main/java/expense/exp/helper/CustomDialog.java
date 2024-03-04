package expense.exp.helper;

import android.annotation.SuppressLint;
import android.app.Dialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;



import expense.exp.R;
import expense.exp.internet.ApiClient;
import expense.exp.internet.ApiInterface;
import expense.exp.internet.model.Status;
import expense.exp.internet.model.Status_model;
import expense.exp.toast.CustomToast;
import com.wang.avi.AVLoadingIndicatorView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by admin on 30-06-2018.
 */

public class CustomDialog {

    Dialog dialog;

    AVLoadingIndicatorView avi;
    MyDialogListener myDialogListener;

    RelativeLayout cardView;

    Button button;
    int subFolder= -1;// 1 - subFolder, 0 - New Folder

    public CustomDialog(MyDialogListener myDialogListener,int subFolder) {
        this.myDialogListener = myDialogListener;
        this.subFolder = subFolder;
    }

    public void show(final AppCompatActivity activity) {
        dialog = new Dialog(activity, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_layout);
        avi = dialog.findViewById(R.id.avi);


        final SharedPrefManager sharedPrefManager = new SharedPrefManager(activity);

        cardView = dialog.findViewById(R.id.loading_view);

        final EditText folder_name = dialog.findViewById(R.id.edit_floder);
        button = dialog.findViewById(R.id.add_folder_btn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String getfoldername = folder_name.getText().toString();

                if (getfoldername.equals("") || getfoldername.length() == 0) {

                    new CustomToast().Show_Toast(activity, view,
                            "Please Enter FolderName.");
                } else {
                    addFolder(getfoldername, Integer.parseInt(sharedPrefManager.getuserinfo().getId()), activity);
                }
            }
        });


        ImageView dialogButton = dialog.findViewById(R.id.cancel_icon);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialogListener.OnCloseDialog();
                dialog.dismiss();

            }
        });


        dialog.show();

    }

    public void renameDialogShow(final AppCompatActivity activity, final String folder_id) {
        dialog = new Dialog(activity, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_layout);
        avi = dialog.findViewById(R.id.avi);


        final SharedPrefManager sharedPrefManager = new SharedPrefManager(activity);

        cardView = dialog.findViewById(R.id.loading_view);

        final EditText folder_name = dialog.findViewById(R.id.edit_floder);
        button = dialog.findViewById(R.id.add_folder_btn);
        folder_name.setHint("Enter new name");
        button.setText("Rename Folder");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String getfoldername = folder_name.getText().toString();

                if (getfoldername.equals("") || getfoldername.length() == 0) {

                    new CustomToast().Show_Toast(activity, view,
                            "Please Enter FolderName.");
                } else {
                    renameFolder(getfoldername, sharedPrefManager.getuserinfo().getId(), folder_id, activity);
                }
            }
        });


        ImageView dialogButton = dialog.findViewById(R.id.cancel_icon);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialogListener.OnCloseDialog();
                dialog.dismiss();

            }
        });


        dialog.show();

    }

    @SuppressLint("CheckResult")
    private void renameFolder(String folder_name, String user_id, String folder_id, final AppCompatActivity activity) {
        button.setEnabled(false);
        ApiInterface apiInterface = ApiClient.getClient(activity)
                .create(ApiInterface.class);
        startAnim();
        apiInterface.renameFile(user_id, folder_id, folder_name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Status>() {
                    @Override
                    public void onSuccess(Status status) {
                        if (status.getStatus().matches("1")) {
                            stopAnim();
                            button.setEnabled(true);
                            myDialogListener.OnCloseDialog();
                            dialog.dismiss();
                        } else {
                            button.setEnabled(true);
                            stopAnim();
                            myDialogListener.OnCloseDialog();
                            dialog.dismiss();
                            Toast.makeText(activity, status.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @SuppressLint("CheckResult")
    private void addFolder(String folder_name, int user_id, final AppCompatActivity activity) {

        button.setEnabled(false);


        ApiInterface apiService = ApiClient.getClient(activity)
                .create(ApiInterface.class);

        startAnim();

        apiService.addFolders(subFolder,user_id, folder_name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Status_model>() {
                    @Override
                    public void onSuccess(Status_model status) {

                        if (status.getStatus().matches("1")) {

                            Toast.makeText(activity, status.getMessage(), Toast.LENGTH_LONG).show();

                            stopAnim();
                            button.setEnabled(true);
                            myDialogListener.OnCloseDialog();
                            dialog.dismiss();


                        } else {
                            button.setEnabled(true);
                            stopAnim();
                            myDialogListener.OnCloseDialog();
                            dialog.dismiss();
                            Toast.makeText(activity, status.getMessage(), Toast.LENGTH_LONG).show();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        button.setEnabled(true);
                        stopAnim();
                        myDialogListener.OnCloseDialog();
                        dialog.dismiss();
                    }
                });

    }

    void startAnim() {
        cardView.setVisibility(View.VISIBLE);
        avi.setVisibility(View.VISIBLE);
        avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim() {
        cardView.setVisibility(View.GONE);
        avi.hide();
        avi.setVisibility(View.GONE);
        // or avi.smoothToHide();
    }


}