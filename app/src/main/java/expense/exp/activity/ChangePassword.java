package expense.exp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



import expense.exp.R;
import expense.exp.databinding.ActivityAssignfolderBinding;
import expense.exp.databinding.ActivityChangePasswordBinding;
import expense.exp.helper.SharedPrefManager;
import expense.exp.helper.Utils;
import expense.exp.internet.ApiClient;
import expense.exp.internet.ApiInterface;
import expense.exp.internet.model.Status;
import expense.exp.toast.CustomToast;

/*import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;*/
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ChangePassword extends AppCompatActivity {

   // Unbinder unbinder;

   // @BindView(R.id.old_password)
    EditText old_password;
   // @BindView(R.id.new_password)
    EditText new_password;
    //@BindView(R.id.confirm_password)
    EditText confirm_password;
    SharedPrefManager sharedPrefManager;

    ActivityChangePasswordBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_change_password);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       // unbinder = ButterKnife.bind(this);

        sharedPrefManager=new SharedPrefManager(this);
        binding.backIcon.setOnClickListener(v -> {
            back(v);
        });
        binding.change.setOnClickListener(v -> {
            change(v);
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
       // unbinder.unbind();


    }


    //@OnClick(R.id.back_icon)
    public void back(View view)
    {
        onBackPressed();
    }

    //@OnClick(R.id.change)
    public void change(View view)
    {

        String getold_password = binding.oldPassword.getText().toString();
        String getConfirmPassword = binding.confirmPassword.getText().toString();
        String getnewPassword = binding.newPassword.getText().toString();


        if (getold_password.equals("") || getold_password.length() == 0) {
            new CustomToast().Show_Toast(this, view,
                    "Please Enter Old Password.");


        } else if (TextUtils.isEmpty(getold_password) || getold_password.length() < 8) {
            new CustomToast().Show_Toast(this, view,
                    "You must have 8 characters in your old password");

        } else if (!Utils.isAlphaNumeric(getold_password)) {
            new CustomToast().Show_Toast(this, view,
                    "Password must be alphanumeric");
        } else if (getnewPassword.equals("") || getnewPassword.length() == 0) {
            new CustomToast().Show_Toast(this, view,
                    "Please Enter New Password.");


        } else if (TextUtils.isEmpty(getnewPassword) || getnewPassword.length() < 8) {
            new CustomToast().Show_Toast(this, view,
                    "You must have 8 characters in your new password");

        } else if (!Utils.isAlphaNumeric(getnewPassword)) {
            new CustomToast().Show_Toast(this, view,
                    "Password must be alphanumeric");
        } else if (getConfirmPassword.equals("") || getConfirmPassword.length() == 0) {
            new CustomToast().Show_Toast(this, view,
                    "Please Enter ConfirmPassword.");
        } else if (!Utils.isAlphaNumeric(getConfirmPassword)) {
            new CustomToast().Show_Toast(this, view,
                    "Password must be alphanumeric");
        } else if (!getConfirmPassword.equals(getnewPassword)) {
            new CustomToast().Show_Toast(this, view,
                    "Both password doesn't match.");
            binding.newPassword.setText("");

            binding.confirmPassword.setText("");

        }else
        {
            userLogin( binding.oldPassword.getText().toString(), binding.newPassword.getText().toString());
        }



    }


    @SuppressLint("CheckResult")
    public void userLogin(String old_password, String password) {


        ApiInterface apiService = ApiClient.getClient(this)
                .create(ApiInterface.class);


        apiService.changepass(Integer.parseInt(sharedPrefManager.getuserinfo().getId()),old_password, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Status>() {
                    @Override
                    public void onSuccess(Status status) {

                        if (status.getStatus().matches("1"))
                        {
                            Toast.makeText(ChangePassword.this, ""+status.getMessage(), Toast.LENGTH_SHORT).show();
                            setResult(101);
                            finish();
                        }else
                        {
                            Toast.makeText(ChangePassword.this, ""+status.getMessage(), Toast.LENGTH_SHORT).show();


                        }





                    }

                    @Override
                    public void onError(Throwable e) {
                        // Network error
                    }
                });

    }
}
