package expense.exp.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import expense.exp.R;
import expense.exp.activity.Home_Activity;
import expense.exp.activity.WebActivity;
import expense.exp.helper.SharedPrefManager;
import expense.exp.helper.Utils;
import expense.exp.internet.ApiClient;
import expense.exp.internet.ApiInterface;
import expense.exp.internet.model.Login;
import expense.exp.internet.model.UserInfo;
import expense.exp.model_class.StudentDataModal;
import expense.exp.model_class.UserIfo;
import expense.exp.toast.CustomToast;

import com.wang.avi.AVLoadingIndicatorView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static expense.exp.helper.Utils.SignUp_Fragment;

public class Login_Fragment extends Fragment implements OnClickListener {
    private static final String EMAIL = "email";
    private static View view;
    private static EditText emailid, password;
    private static Button loginButton;
    private static TextView forgotPassword, signUp;
    //  private static CheckBox show_hide_password;
    private static LinearLayout loginLayout;
    private static Animation shakeAnimation;
    private static FragmentManager fragmentManager;
    //private AppPreferences _appPrefs;
    private FragmentTransaction transaction;
    private ProgressDialog dialog;

    private SharedPrefManager sharedPrefManager;
    RelativeLayout term_condition;

    AVLoadingIndicatorView avi;
    RelativeLayout loading_view;

    public Login_Fragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_layout, container, false);
        sharedPrefManager = new SharedPrefManager(getActivity());


        initViews();
        setListeners();
        return view;
    }

    // Initiate Views
    private void initViews() {
        fragmentManager = getActivity().getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        // _appPrefs= new AppPreferences(getActivity());

        emailid = view.findViewById(R.id.login_emailid);
        password = view.findViewById(R.id.login_password);
        loginButton = view.findViewById(R.id.loginBtn);
        forgotPassword = view.findViewById(R.id.forgot_password);
        signUp = view.findViewById(R.id.createAccount);
//        show_hide_password = (CheckBox) view
//                .findViewById(R.id.show_hide_password);
        loginLayout = view.findViewById(R.id.login_layout);
        avi = view.findViewById(R.id.avi);
        loading_view = view.findViewById(R.id.loading_view);
        term_condition = view.findViewById(R.id.term_condition);


        // Load ShakeAnimation
        shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.shake);
        getKeyHash();


        // Setting text selector over textviews

    }

    // Set Listeners
    private void setListeners() {
        loginButton.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        signUp.setOnClickListener(this);
        term_condition.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginBtn:
                checkValidation();
                break;

            case R.id.forgot_password:

                // Replace forgot password fragment with animation
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameContainer,
                                new ForgotPassword_Fragment(),
                                Utils.ForgotPassword_Fragment).commit();


                break;
            case R.id.createAccount:
                // Replace signup frgament with animation
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameContainer, new Registration_Fargment(),
                                SignUp_Fragment).commit();

                break;
            case R.id.term_condition:
                // Replace signup frgament with animation
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("type","login");
                startActivity(intent);
                break;

        }

    }

    // Check Validation before login
    private void checkValidation() {
        // Get email id and password
        String getEmailId = emailid.getText().toString();
        String getPassword = password.getText().toString();

        // Check patter for email id
        Pattern p = Pattern.compile(Utils.regEx);

        Matcher m = p.matcher(getEmailId);

        if (getEmailId.equals("") || getEmailId.length() == 0) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "Please Enter Email.");

        } else if (!m.find()) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "Your Email Id is Invalid.");
        } else if (getPassword.equals("") || getPassword.length() == 0) {

            new CustomToast().Show_Toast(getActivity(), view,
                    "Please Enter Password.");
        } else if (TextUtils.isEmpty(getPassword) || getPassword.length() < 8) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "You must have 8 characters in your password");

        } else if (!Utils.isAlphaNumeric(getPassword)) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "Password must be alphanumeric");
        }else {


            userLogin(getEmailId, getPassword);


        }


    }


    private void getKeyHash() {

        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(
                    "com.admin.soundjoc",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void userLogin(String email, String password) {

        startAnim();


        ApiInterface apiService = ApiClient.getClient(getActivity())
                .create(ApiInterface.class);


        apiService.userLogin(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Login>() {
                    @Override
                    public void onSuccess(Login login) {

                        stopAnim();

                        if (login.getStatus().matches("1")) {
                            Log.e("exeption:: ", "=" + login.getStatus());

                            sharedPrefManager.setLogiWithF(true);
                            sharedPrefManager.serUser_info(login.getUser());

                            user_info((login.getUser().getId()));

                            Log.e("user_id", login.getUser().getId());
                            getActivity().startActivity(new Intent(getActivity(), Home_Activity.class));
                            getActivity().finish();

                        } else {
                            Toast.makeText(getActivity(), login.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void user_info(String userid) {

        ApiInterface apiService = ApiClient.getClient(getActivity())
                .create(ApiInterface.class);


        apiService.getuserInfo(userid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<UserInfo>() {
                    @Override
                    public void onSuccess(UserInfo login) {

                        if (login.getStatus().matches("1")) {

                            StudentDataModal studentDataModal = login.getStudent();

                            Log.e("studentdata", String.valueOf(login.getStudent()));
                            sharedPrefManager.setStudent_info(login.getStudent());
                            sharedPrefManager.serUser_info(login.getUser());
                            sharedPrefManager.setImageUrl(login.getImageurl());
                            sharedPrefManager.setUserReferalLink(login.getReferlink());
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        // Network error
                        handleStudentObj(userid);
                    }
                });

    }

    private void handleStudentObj(String userid) {
        ApiInterface apiService = ApiClient.getClient(getActivity())
                .create(ApiInterface.class);


        apiService.getuserIfo(userid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<UserIfo>() {
                    @Override
                    public void onSuccess(UserIfo login) {

                        if (login.getStatus().matches("1")) {

//                            sharedPrefManager.setStudent_info(studentDataModal);
                            sharedPrefManager.serUser_info(login.getUser());
                            sharedPrefManager.setImageUrl(login.getImageurl());
                            sharedPrefManager.setUserReferalLink(login.getReferlink());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // Network error
                        Log.e("ERR ", e.toString());

                    }
                });
    }


    void startAnim() {
        loading_view.setVisibility(View.VISIBLE);

        avi.setVisibility(View.VISIBLE);
        avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim() {
        avi.hide();
        avi.setVisibility(View.GONE);
        loading_view.setVisibility(View.GONE);
        // or avi.smoothToHide();
    }


}
