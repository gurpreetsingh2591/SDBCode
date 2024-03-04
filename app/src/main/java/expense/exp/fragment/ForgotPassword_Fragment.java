package expense.exp.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;



import expense.exp.R;
import expense.exp.activity.Login_activity;
import expense.exp.helper.Utils;
import expense.exp.internet.ApiClient;
import expense.exp.internet.ApiInterface;
import expense.exp.internet.model.Status;
import expense.exp.toast.CustomToast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static expense.exp.helper.Utils.SignUp_Fragment;

public class ForgotPassword_Fragment extends Fragment implements
        OnClickListener {
    private static View view;

    private static EditText emailId;
    private static Button submit;
    private ImageView back_icon;
    private TextView createAccount;
    private static FragmentManager fragmentManager;
    private FragmentTransaction transaction;


    public ForgotPassword_Fragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.forgotpassword_layout, container,
                false);
        initViews();
        setListeners();
        return view;
    }

    // Initialize the views
    private void initViews() {
        fragmentManager = getActivity().getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        emailId = view.findViewById(R.id.registered_emailid);
        submit = view.findViewById(R.id.forgot_button);
        back_icon = view.findViewById(R.id.back_icon);
        createAccount= view.findViewById(R.id.createAccount);


    }

    // Set Listeners over buttons
    private void setListeners() {
        back_icon.setOnClickListener(this);
        submit.setOnClickListener(this);
        createAccount.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.forgot_button:

                // Call Submit button task
                submitButtonTask();
                break;

            case R.id.back_icon: {

                // Replace login fragment
                new Login_activity().replaceLoginFragment();
                break;
            }
            case R.id.createAccount:{

                // Replace signup frgament with animation
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.right_enter, R.anim.left_out)
                        .replace(R.id.frameContainer, new Registration_Fargment(),
                                SignUp_Fragment).commit();
            }

        }


    }

    private void submitButtonTask() {
        String getEmailId = emailId.getText().toString();

        // Pattern for email id validation
        Pattern p = Pattern.compile(Utils.regEx);

        // Match the pattern
        Matcher m = p.matcher(getEmailId);

        // First check if email id is not null else show error toast
        if (getEmailId.equals("") || getEmailId.length() == 0)

            new CustomToast().Show_Toast(getActivity(), view,
                    "Please enter your Email Id.");

            // Check if email id is valid or not
        else if (!m.find())
            new CustomToast().Show_Toast(getActivity(), view,
                    "Your Email Id is Invalid.");

            // Else submit email id and fetch passwod or do your stuff
        else {

            user_forgotpass(getEmailId);

        }

        //forgotpassword_call(emailId.getText().toString());

    }

    public void user_forgotpass(String email)
    {


        ApiInterface apiService = ApiClient.getClient(getActivity())
                .create(ApiInterface.class);


        apiService.userForgotPass(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Status>() {
                    @Override
                    public void onSuccess(Status status) {

                        if (status.getStatus().matches("1"))
                        {

                            new CustomToast().Show_Toast_successfully(getActivity(), view,
                                    "kindly check email");
                            getActivity().onBackPressed();
                        }
                        else {
                            new CustomToast().Show_Toast(getActivity(), view,
                                    status.getMessage());
                        }




                    }

                    @Override
                    public void onError(Throwable e) {
                        // Network error
                    }
                });

    }


}