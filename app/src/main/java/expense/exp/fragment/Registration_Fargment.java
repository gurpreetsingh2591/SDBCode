package expense.exp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import expense.exp.R;
import expense.exp.activity.Login_activity;
import expense.exp.activity.WebActivity;
import expense.exp.helper.Utils;
import expense.exp.internet.ApiClient;
import expense.exp.internet.ApiInterface;
import expense.exp.internet.model.Status;
import expense.exp.toast.CustomToast;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by admin on 10-07-2018.
 */

public class Registration_Fargment extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener {
    private static View view;
    private static EditText fullName, emailId,
            password, confirmPassword,edtReferral;
    private static TextView login,ters;
    private static Button signUpButton;
    ProgressDialog dialog;
    //private static CheckBox terms_conditions;
    private ImageView back_icon;
    private Spinner usertypeSpinner;
    String item=null;

    AVLoadingIndicatorView avi;
    RelativeLayout loading_view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.signup_layout, container, false);


        initViews();
        setListeners();
        return view;
    }

    // Initialize all views
    private void initViews() {
        fullName = view.findViewById(R.id.fullName);
        emailId = view.findViewById(R.id.userEmailId);
        edtReferral = view.findViewById(R.id.edtReferral);
//		mobileNumber = (EditText) view.findViewById(R.id.mobileNumber);
        back_icon = view.findViewById(R.id.back_icon);
        password = view.findViewById(R.id.password);
        confirmPassword = view.findViewById(R.id.confirmPassword);
        signUpButton = view.findViewById(R.id.signUpBtn);
        login = view.findViewById(R.id.already_user);
        ters = view.findViewById(R.id.ters);

        avi=view.findViewById(R.id.avi);
        loading_view=view.findViewById(R.id.loading_view);

        usertypeSpinner = view.findViewById(R.id.usertypeSpinner);
        List<String> categories = new ArrayList<String>();
        categories.add("User");
        categories.add("Student");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        usertypeSpinner.setAdapter(dataAdapter);


    }

    // Set Listeners
    private void setListeners() {
        signUpButton.setOnClickListener(this);
        login.setOnClickListener(this);
        back_icon.setOnClickListener(this);
        ters.setOnClickListener(this);
        // Spinner click listener
        usertypeSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
         item = parent.getItemAtPosition(position).toString();

    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpBtn:

                // Call checkValidation method
                checkValidation();
                break;

            case R.id.already_user:

                // Replace login fragment
                new Login_activity().replaceLoginFragment();
                break;

            case R.id.back_icon:

                // Replace login fragment
                new Login_activity().replaceLoginFragment();
                break;

            case R.id.ters:

                // Replace signup frgament with animation
                Intent intent=new Intent(getActivity(), WebActivity.class);
                intent.putExtra("type","login");
                startActivity(intent);
                break;


        }

    }

    // Check Validation Method
    private void checkValidation() {

        // Get all edittext texts
      //  String getFullName = fullName.getText().toString();
        String getEmailId = emailId.getText().toString();
//		String getMobileNumber = mobileNumber.getText().toString();
//		String getLocation = location.getText().toString();
        String getPassword = password.getText().toString();
        String getReferral = edtReferral.getText().toString();
        String getConfirmPassword = confirmPassword.getText().toString();

        // Pattern match for email id
        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);


        //userfeild

//        if (getFullName.equals("") || getFullName.length() == 0) {
//            new CustomToast().Show_Toast(getActivity(), view,
//                    "Please Enter Username.");
//        } else


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

        } else if (getConfirmPassword.equals("") || getConfirmPassword.length() == 0) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "Please Enter ConfirmPassword.");
        } else if (!getConfirmPassword.equals(getPassword)) {
            new CustomToast().Show_Toast(getActivity(), view,
                    "Both password doesn't match.");
            password.setText("");
            confirmPassword.setText("");

        } else if (item==null){

            new CustomToast().Show_Toast(getActivity(), view,
                    "select type of user ");

        }
        else {
            user_registration(getEmailId,getPassword,item,getReferral);
        }


    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.cancel();
        }
    }


    public void user_registration(String email,String password,String type,String code)
    {

        startAnim();


        ApiInterface apiService = ApiClient.getClient(getActivity())
                .create(ApiInterface.class);


        apiService.userRegistration(email,password,type,code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Status>() {
                    @Override
                    public void onSuccess(Status status) {

                    // Log.d("status__",status.getMessage());
                        stopAnim();

                     if (status.getStatus().equals("1"))
                     {
                         Toast.makeText(getActivity(), status.getMessage(), Toast.LENGTH_SHORT).show();

                         new Login_activity().replaceLoginFragment();
                     }

                     else {
                         Toast.makeText(getActivity(), status.getMessage(), Toast.LENGTH_SHORT).show();

                     }

                    }

                    @Override
                    public void onError(Throwable e) {
                        // Network error
                        stopAnim();

                    }
                });

    }


    void startAnim(){
        loading_view.setVisibility(View.VISIBLE);

        avi.setVisibility(View.VISIBLE);
        avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim(){
        avi.hide();
        avi.setVisibility(View.GONE);
        loading_view.setVisibility(View.GONE);
        // or avi.smoothToHide();
    }




}
