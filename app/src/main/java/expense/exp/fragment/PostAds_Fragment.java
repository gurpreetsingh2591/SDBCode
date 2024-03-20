package expense.exp.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/*import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;*/
import de.hdodenhof.circleimageview.CircleImageView;
import expense.exp.R;
import expense.exp.activity.AdsPackageListActivity;
import expense.exp.adapter.CategoriesAdapter;
import expense.exp.adapter.SubCategoriesAdapter;
import expense.exp.databinding.LayoutAdsBinding;
import expense.exp.databinding.LayoutPostAdsBinding;
import expense.exp.helper.MyDialogListener;
import expense.exp.helper.SharedPrefManager;
import expense.exp.internet.ApiClient;
import expense.exp.internet.ApiInterface;
import expense.exp.internet.model.CategoriesRes;
import expense.exp.internet.model.Category;
import expense.exp.internet.model.Status;
import expense.exp.internet.model.SubCategoriesRes;
import expense.exp.internet.model.SubCategory;
import expense.exp.utils.AppUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by appleboy on 06-07-2018.
 */

public class PostAds_Fragment extends Fragment implements MyDialogListener, AdapterView.OnItemSelectedListener {

    View view;
    //@BindView(R.id.back_icon)
    ImageView back;
    //Unbinder unbinder;

    //@BindView(R.id.spinnerCategory)
    Spinner spinnerCategory;

    //@BindView(R.id.spinnerProvince)
    Spinner spinnerProvince;

   // @BindView(R.id.spinnerSubCategory)
    Spinner spinnerSubCategory;

   // @BindView(R.id.add_folder_btn)
    Button buttonPostAd;

   // @BindView(R.id.edit_title)
    EditText edtTitle;

   // @BindView(R.id.edit_url)
    EditText edtUrl;

   // @BindView(R.id.edit_phone)
    EditText edtPhone;

   // @BindView(R.id.edit_address)
    EditText edtAddress;

   // @BindView(R.id.editCity)
    EditText editCity;

   // @BindView(R.id.editPinCode)
    EditText editPinCode;

   // @BindView(R.id.avi)
    AVLoadingIndicatorView avi;

   // @BindView(R.id.profile_image)
    CircleImageView profile_image;

   // @BindView(R.id.tvUploadPhoto)
    TextView tvUploadPhoto;

    private String selectedCatId;
    private String selectedSubCatId;
    String province;

    public static PostAds_Fragment newInstance() {
        return new PostAds_Fragment();
    }

    CategoriesAdapter adapter;
    SubCategoriesAdapter subCategoriesAdapter;
    private String userId;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private Bitmap bitmap;
    private String imgPath = null;
    private InputStream inputStreamImg;
    File image_file = null;
LayoutPostAdsBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      //  view = inflater.inflate(R.layout.layout_post_ads, container, false);
      //  unbinder = ButterKnife.bind(this, view);
        binding = LayoutPostAdsBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }

    public int textLength = 0;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SharedPrefManager sharedPrefManager = new SharedPrefManager(getActivity());
        binding.backIcon.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        userId = sharedPrefManager.getuserinfo().getId();
        getAdCategories();
        binding.spinnerCategory.setOnItemSelectedListener(this);
        binding.spinnerSubCategory.setOnItemSelectedListener(this);
        binding.spinnerProvince.setOnItemSelectedListener(this);
        setListeners();
        setSpinner();
    }

    private void setSpinner() {

        List<String> categories = new ArrayList<String>();
        categories.add("Alberta");
        categories.add("British Columbia");
        categories.add("Manitoba");
        categories.add("New Brunswick");
        categories.add("Newfoundland and Labrador");
        categories.add("Nova Scotia");
        categories.add("Ontario");
        categories.add("Prince Edward Island");
        categories.add("Saskatchewan");
        categories.add("Yukon");
        categories.add("Nunavut");
        categories.add("Northwest Territories");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        binding.spinnerProvince.setAdapter(dataAdapter);
    }

    private void setListeners() {
        binding.editPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String text = binding.editPhone.getText().toString();
                textLength = binding.editPhone.getText().length();

                if (text.endsWith("-") || text.endsWith(" "))
                    return;

                if (textLength == 4) {

                    if (!text.contains("-")) {
                        binding.editPhone.setText(new StringBuilder(text).insert(text.length() - 1, "-").toString());
                        binding.editPhone.setSelection(binding.editPhone.getText().length());
                    }

                } else if (textLength == 8) {
                    binding.editPhone.setText(new StringBuilder(text).insert(text.length() - 1, "-").toString());
                    binding.editPhone.setSelection(binding.editPhone.getText().length());

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.addFolderBtn.setOnClickListener(v -> {
            String title = binding.editTitle.getText().toString().trim();
            String url = binding.editUrl.getText().toString().trim();
            String phone = binding.editPhone.getText().toString().trim();
            phone = phone.replace("-", "");
            String city = binding.editCity.getText().toString().trim();
            String pincode = binding.editPinCode.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();

            if (TextUtils.isEmpty(title)) {
                Toast.makeText(getContext(), "Please enter ads title", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(url)) {
                Toast.makeText(getContext(), "Please enter ads url", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!validatePhoneNumber(phone)) {
                Toast.makeText(getContext(), "Please enter contact", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(address)) {
                Toast.makeText(getContext(), "Please enter ads address", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(selectedCatId)) {
                Toast.makeText(getContext(), "Please select at least one category", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(selectedSubCatId)) {
                Toast.makeText(getContext(), "Please select at least one SubCategory", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(city)) {
                Toast.makeText(getContext(), "Please enter city", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(pincode)) {
                Toast.makeText(getContext(), "Please enter pincode", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(province)) {
                Toast.makeText(getContext(), "Please enter province", Toast.LENGTH_SHORT).show();
                return;
            }

            if (image_file == null) {
                Toast.makeText(getContext(), "Please add an image", Toast.LENGTH_SHORT).show();
                return;
            }

            postAd(userId, title, selectedCatId, selectedSubCatId, url, phone, address, "active", image_file, city, pincode, province);
        });
    }

    public boolean validatePhoneNumber(String phone) {
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getContext(), getString(R.string.err_enter_mobile_number), Toast.LENGTH_SHORT).show();
            return false;
        } else if (!AppUtils.isValidPhone(phone)) {
            Toast.makeText(getContext(), getString(R.string.error_phone_number), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getAdapter() instanceof CategoriesAdapter) {
            Category item = ((Category) parent.getItemAtPosition(position));
            if (position != 0) {

                selectedCatId = item.getId();
                if (!TextUtils.isEmpty(selectedCatId)) {
                    getAdSubCategories(selectedCatId);
                }
            }
        } else if (parent.getAdapter() instanceof SubCategoriesAdapter) {
            SubCategory item = ((SubCategory) parent.getItemAtPosition(position));
            if (position != 0) {
                selectedSubCatId = item.getId();
            }
        } else if (parent.getId() == R.id.spinnerProvince) {
            province = parent.getItemAtPosition(position).toString();


        }


    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }

   // @OnClick(R.id.profile_image)
    public void profile_image(View view) {


        try {

            if (ActivityCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_IMAGE_CAMERA);
            } else {
                selectImage();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_IMAGE_CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            selectImage();
        } else {

            // We were not granted permission this time, so don't try to show the contact picker
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
      //  unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputStreamImg = null;


        if (requestCode == PICK_IMAGE_CAMERA) {
            try {


                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                Log.e("Activity", "Pick from Camera::>>> ");

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

                createDirectoryAndSaveFile(bitmap, "IMG_" + timeStamp + ".jpg");

                //imgPath = destination.getAbsolutePath();
                binding.profileImage.setImageBitmap(bitmap);
                binding.tvUploadPhoto.setVisibility(View.INVISIBLE);


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {

            if (data != null) {


                Uri selectedImage = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    Log.e("Activity", "Pick from Gallery::>>> ");

                    imgPath = getRealPathFromURI(selectedImage);
                    image_file = new File(imgPath);
                    binding.profileImage.setImageBitmap(bitmap);
                    binding.tvUploadPhoto.setVisibility(View.INVISIBLE);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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

    private void selectImage() {
        try {
            PackageManager pm = getActivity().getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getActivity().getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
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
                Toast.makeText(getActivity(), "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    @SuppressLint("CheckResult")
    public void getAdCategories() {
        startAnim();

        ApiInterface apiService = ApiClient.getClient(getActivity())
                .create(ApiInterface.class);


        apiService.getAdCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<CategoriesRes>() {
                    @Override
                    public void onSuccess(CategoriesRes categoriesRes) {
                        List<Category> categoryList = new ArrayList<>();
                        Category mm = new Category();
                        mm.setName("Please Select Category");
                        categoryList.add(mm);
                        categoryList.addAll(categoriesRes.getCategories());

                        if (categoryList != null && categoryList.size() != 0) {
                            adapter = new CategoriesAdapter(getActivity(), categoryList);
                            binding.spinnerCategory.setAdapter(adapter);
                        }
                        stopAnim();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopAnim();


                    }
                });


    }


    @SuppressLint("CheckResult")
    public void getAdSubCategories(String catId) {

        startAnim();

        ApiInterface apiService = ApiClient.getClient(getActivity())
                .create(ApiInterface.class);


        apiService.getAdSuCategories(catId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<SubCategoriesRes>() {
                    @Override
                    public void onSuccess(SubCategoriesRes categoriesRes) {
                        List<SubCategory> categoryList = new ArrayList<>();
                        SubCategory mm = new SubCategory();
                        mm.setName("Please Select SubCategory");
                        categoryList.add(0, mm);
                        categoryList.addAll(categoriesRes.getSubcategories());

                        if (categoryList != null && categoryList.size() != 0) {
                            subCategoriesAdapter = new SubCategoriesAdapter(getActivity(), categoryList);
                            binding.spinnerSubCategory.setAdapter(subCategoriesAdapter);
                        }
                        stopAnim();

                    }

                    @Override
                    public void onError(Throwable e) {
                        stopAnim();


                    }
                });


    }

    @SuppressLint("CheckResult")
    public void postAd(String userId, String title, String catId, String subCatId, String url,
                       String contact, String address, String status, File file, String city, String pincode, String province) {

        Log.d("MMMMM", "postAd:  city " + city);
        Log.d("MMMMM", "postAd:  pincode " + pincode);
        Log.d("MMMMM", "postAd:  province " + province);
        startAnim();

        MultipartBody.Part multipartBody = null;

        Log.d("debugging", "Upload file " + (file == null));
        if (file == null) {
//            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
//             multipartBody = MultipartBody.Part.createFormData("uploadFile","", requestFile);

        } else {

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            multipartBody = MultipartBody.Part.createFormData("logo", file.getName(), requestFile);
        }

        RequestBody body_title = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody body_catId = RequestBody.create(MediaType.parse("text/plain"), catId);
        RequestBody body_subCatId = RequestBody.create(MediaType.parse("text/plain"), subCatId);
        RequestBody body_url = RequestBody.create(MediaType.parse("text/plain"), url);
        RequestBody body_contact = RequestBody.create(MediaType.parse("text/plain"), contact);
        RequestBody body_address = RequestBody.create(MediaType.parse("text/plain"), address);
        RequestBody body_status = RequestBody.create(MediaType.parse("text/plain"), status);
        RequestBody body_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userId));

        RequestBody body_city = RequestBody.create(MediaType.parse("text/plain"), city);
        RequestBody body_pincode = RequestBody.create(MediaType.parse("text/plain"), pincode);
        RequestBody body_province = RequestBody.create(MediaType.parse("text/plain"), province);


        ApiInterface apiService = ApiClient.getClient(getActivity())
                .create(ApiInterface.class);


        apiService.postAd(body_id, body_title, body_catId, body_subCatId, body_url, body_contact,
                body_address, body_status, multipartBody, body_city, body_pincode, body_province)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Status>() {
                    @Override
                    public void onSuccess(Status categoriesRes) {
                        stopAnim();
                        requireActivity().startActivity(new Intent(getActivity(), AdsPackageListActivity.class));
                        getActivity().finish();

                    }

                    @Override
                    public void onError(Throwable e) {
                        stopAnim();
                    }
                });


    }


    void startAnim() {
        binding.avi.setVisibility(View.VISIBLE);
        binding.avi.show();

        // or avi.smoothToShow();
    }

    void stopAnim() {
        if (binding.avi.getIndicator() != null) {
            binding.avi.hide();
            binding.avi.setVisibility(View.GONE);
        }

        // or avi.smoothToHide();
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

    //=====================================================


    void startAnimLoad() {
//        loading_view.setVisibility(View.VISIBLE);
//
//        avi_load.setVisibility(View.VISIBLE);
//        avi_load.show();
        // or avi.smoothToShow();
    }

    void stopAnimLoad() {
//        avi_load.hide();
//        avi_load.setVisibility(View.GONE);
//        loading_view.setVisibility(View.GONE);
        // or avi.smoothToHide();
    }


    @Override
    public void onResume() {
        super.onResume();
//        getFolder(Integer.parseInt(sharedPrefManager.getuserinfo().getId()));
    }
}
