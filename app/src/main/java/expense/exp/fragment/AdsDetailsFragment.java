package expense.exp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;
import java.util.Objects;
/*
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;*/
import de.hdodenhof.circleimageview.CircleImageView;
import expense.exp.R;
import expense.exp.databinding.AdDetailsFragmentBinding;
import expense.exp.databinding.LayoutAdsBinding;
import expense.exp.helper.MyDialogListener;
import expense.exp.internet.ApiClient;
import expense.exp.internet.ApiInterface;
import expense.exp.internet.model.AdDetails;
import expense.exp.internet.model.AdDetailsRes;
import expense.exp.utils.AppUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by appleboy on 06-07-2018.
 */

public class AdsDetailsFragment extends Fragment implements MyDialogListener {

    View view;
   // @BindView(R.id.back_icon)
    ImageView back;

    //@BindView(R.id.tvTitle)
    TextView tvTitle;

    //@BindView(R.id.tvCategory)
    TextView tvCategory;

   // @BindView(R.id.tvSubCategory)
    TextView tvSubCategory;

   // @BindView(R.id.tvUrl)
    TextView tvUrl;

   // @BindView(R.id.tvPhone)
    TextView tvPhone;

   // @BindView(R.id.tvAddress)
    TextView tvAddress;

   // @BindView(R.id.tvStatus)
    TextView tvStatus;

   // @BindView(R.id.aviDetails)
    AVLoadingIndicatorView avi;

    //@BindView(R.id.profile_image)
    CircleImageView adImage;

   // @BindView(R.id.tvCity)
    TextView tvCity;

   // @BindView(R.id.tvProvince)
    TextView tvProvince;

   // @BindView(R.id.tvPincode)
    TextView tvPincode;

   // Unbinder unbinder;

    public static AdsDetailsFragment newInstance() {
        return new AdsDetailsFragment();
    }

    Intent intent;

    AdDetailsFragmentBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //view = inflater.inflate(R.layout.ad_details_fragment, container, false);
        binding = AdDetailsFragmentBinding.inflate(inflater, container, false);

         /*view.findViewById(R.id.iv_back);
         view.findViewById(R.id.tvTitle);
         view.findViewById(R.id.tvCategory);
         view.findViewById(R.id.tvSubCategory);
         view.findViewById(R.id.tvUrl);
         view.findViewById(R.id.tvPhone);
         view.findViewById(R.id.tvAddress);
         view.findViewById(R.id.tvStatus);
         view.findViewById(R.id.aviDetails);
         view.findViewById(R.id.profile_image);
         view.findViewById(R.id.tvCity);
         view.findViewById(R.id.avi);
         view.findViewById(R.id.tvProvince);
         view.findViewById(R.id.tvPincode);*/


        binding.backIcon.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });


        return binding.getRoot();
    }

    public void setIntent(Intent intent){
        this.intent = intent;

        if(this.intent != null){
           Bundle bundle = this.intent.getExtras();
           String ad_id = bundle.getString("ad_id");
            getAdDetails(ad_id);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //unbinder.unbind();
    }


    @SuppressLint("CheckResult")
    public void getAdDetails(String ad_id) {

//        startAnim();

        ApiInterface apiService = ApiClient.getClient(getActivity())
                .create(ApiInterface.class);


        apiService.getAdDetails(ad_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<AdDetailsRes>() {
                    @Override
                    public void onSuccess(AdDetailsRes adDetailsRes) {
                        Log.d("MMMM", "onSuccess: details");
                        String imageBaseUrl = adDetailsRes.getImageUrl();
                        List<AdDetails> adsDetails = adDetailsRes.getAdsDetails();
                        if(adsDetails != null && adsDetails.size() !=0){
                            AdDetails adDetails = adsDetails.get(0);
                            if(adDetails != null){

                                String title = adDetails.getTitle();
                                String logo = adDetails.getLogo();
                                String cat = adDetails.getCategoryId();
                                String subCay = adDetails.getSubcategoryId();
                                String Url = adDetails.getLink();
                                String phone = adDetails.getContact();
                                String address = adDetails.getAddress();
                                String city = adDetails.getCity();
                                String province = adDetails.getProvince();
                                String pinCode = adDetails.getPinCode();
                                String status = adDetails.getStatus();

                                Log.d("MMMMM", "onSuccess:  city :: "+city);
                                Log.d("MMMMM", "onSuccess:  province :: "+province);
                                Log.d("MMMMM", "onSuccess:  pinCode :: "+pinCode);

                                loadImage(imageBaseUrl,logo,adImage);
                                if(!TextUtils.isEmpty(title)){
                                    binding.tvTitle.setText(requireActivity().getString(R.string.text_ad_title,title));
                                }

                                if(!TextUtils.isEmpty(cat)){
                                    binding.tvCategory.setText(requireActivity().getString(R.string.text_ad_category,cat));
                                }

                                if(!TextUtils.isEmpty(subCay)){
                                    binding.tvSubCategory.setText(requireActivity().getString(R.string.text_ad_sub_category,subCay));
                                }

                                if(!TextUtils.isEmpty(Url)){
                                    binding.tvUrl.setText(requireActivity().getString(R.string.text_ad_url,Url));
                                }

                                if(!TextUtils.isEmpty(phone)){
                                    if (phone.contains("-"))
                                        phone = phone.replace("-", "");

                                    phone = AppUtils.addChar(phone, '-',3);
                                    phone = AppUtils.addChar(phone, '-',7);
                                    binding.tvPhone.setText(requireActivity().getString(R.string.text_ad_phone,phone));
                                }

                                if(!TextUtils.isEmpty(address)){
                                    binding.tvAddress.setText(requireActivity().getString(R.string.text_ad_address,address));
                                }

                                if(!TextUtils.isEmpty(status)){
                                    binding.tvStatus.setText(requireActivity().getString(R.string.text_ad_status,status));
                                }

                                if(!TextUtils.isEmpty(city)){
                                    binding.tvCity.setText(city);
                                }else {
                                    binding.tvCity.setText("N/A");
                                }

                                if(!TextUtils.isEmpty(province)){
                                    binding.tvProvince.setText(province);
                                }else {
                                    binding.tvProvince.setText("N/A");
                                }

                                if(!TextUtils.isEmpty(pinCode)){
                                    binding.tvPincode.setText(pinCode);
                                }else {
                                    binding.tvPincode.setText("N/A");
                                }
                                stopAnim();
                            }
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("MMMM", "onError: details"+ e.getMessage());
                        stopAnim();
                    }
                });


    }

    private void loadImage(String path,String imageObj ,ImageView imageView) {
        Log.d("MMMMM", "loadImage: " + path + imageObj);
        Picasso.get()
                .load(path + imageObj)
                .placeholder(R.drawable.user_profile_icon)
                .error(R.drawable.user_profile_icon)
                .into(imageView);
    }



        void startAnim() {
//        avi.setVisibility(View.VISIBLE);
            binding.aviDetails.show();
        // or avi.smoothToShow();
    }

    void stopAnim() {
        if (binding.aviDetails != null) {
            binding.aviDetails.hide();
//            avi.setVisibility(View.GONE);
        }

    }


    @Override
    public void OnCloseDialog() {
//        getFolder(Integer.parseInt(sharedPrefManager.getuserinfo().getId()));
    }




    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }



    @Override
    public void onResume() {
        super.onResume();
//        getFolder(Integer.parseInt(sharedPrefManager.getuserinfo().getId()));
    }
}
