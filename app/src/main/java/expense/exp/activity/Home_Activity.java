package expense.exp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
/*import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;*/
import expense.exp.R;
import expense.exp.databinding.ActivityCropBinding;
import expense.exp.databinding.ActivityHomeBinding;
import expense.exp.fragment.Accountant_Fragment;
import expense.exp.fragment.Ads_Fragment;
import expense.exp.fragment.Folder_Fragment;
import expense.exp.fragment.Profile_Fragment;
import expense.exp.fragment.Setting_Fragment;
import expense.exp.helper.BottomNavigationViewHelper;
import expense.exp.helper.FileUtils;
import expense.exp.helper.SharedPrefManager;

/*      1.	Folder screen color is still different
        2.	Not able to view the uploaded document when clicking on the that
        3.	App closed when updating profile, image, university & course not updating*/


public class Home_Activity extends AppCompatActivity {


    public BottomNavigationView bottomNavigationView;


  //  public static ViewPager viewPager;

    MenuItem prevMenuItem;
    ViewPagerAdapter adapter;

    Folder_Fragment folder_fragment;
    Accountant_Fragment accountant_fragment;
    Profile_Fragment profile_fragment;
    Setting_Fragment setting_fragment;
    Ads_Fragment ads_fragment;

    private final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String ACCESS_CAMERA = Manifest.permission.CAMERA;
    private final static int REQUEST_CODE = 1010;
    private final static int REQUEST_PERMISSION_SETTING = 1211;
    SharedPrefManager sharedPrefManager;
    ActivityHomeBinding binding;

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_home_);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
                }).check();


     /*   if (ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, ACCESS_CAMERA) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, ACCESS_CAMERA, Manifest.permission.MANAGE_EXTERNAL_STORAGE}, REQUEST_CODE);
        }*/


        BottomNavigationViewHelper.removeShiftMode(binding.bottomNavigationView);

        sharedPrefManager = new SharedPrefManager(this);
        Log.d("usr_id", new SharedPrefManager(this).getuserinfo().getId());
        Log.d("usr_type", String.valueOf(sharedPrefManager.getuserinfo().getType().equals("user")));

        if (sharedPrefManager.getuserinfo().getType().equals("user")) {

            binding.bottomNavigationView.setVisibility(View.GONE);
            binding.bottomNavigation.setVisibility(View.VISIBLE);
            binding.homeContainer.setVisibility(View.VISIBLE);
            binding.frameContainer.setVisibility(View.GONE);
        } else {
            binding.bottomNavigationView.setVisibility(View.VISIBLE);
            binding.bottomNavigation.setVisibility(View.GONE);
            binding.homeContainer.setVisibility(View.GONE);
            binding.frameContainer.setVisibility(View.VISIBLE);
            Folder_Fragment fragment = new Folder_Fragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameContainer, fragment);
            //  fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        binding.bottomNavigation.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.check_folder:
                            binding.homeContainer.setCurrentItem(0);
                            break;
                        case R.id.check_accountant:
                            binding.homeContainer.setCurrentItem(1);
                            break;
                        case R.id.check_ads:
                            binding.homeContainer.setCurrentItem(2);
                            break;
                        case R.id.check_profile:
                            binding.homeContainer.setCurrentItem(3);
                            break;
                        case R.id.check_setting:
                            binding.homeContainer.setCurrentItem(4);
                            break;
                    }
                    return false;
                });


        binding.bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.check_folder:
                            Folder_Fragment fragment = new Folder_Fragment();
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.frameContainer, fragment);
                            //  fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                            return true;
                        case R.id.check_profile:
                            Profile_Fragment fragment3 = new Profile_Fragment();
                            FragmentManager fragmentManager3 = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
                            fragmentTransaction3.replace(R.id.frameContainer, fragment3);
                            //  fragmentTransaction.addToBackStack(null);
                            fragmentTransaction3.commit();
//                                viewPager.setCurrentItem(1);
                            return true;
                            case R.id.check_setting:
                            Setting_Fragment fragment4= new Setting_Fragment();
                            FragmentManager fragmentManager4 = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction4 = fragmentManager4.beginTransaction();
                            fragmentTransaction4.replace(R.id.frameContainer, fragment4);
                            //  fragmentTransaction.addToBackStack(null);
                            fragmentTransaction4.commit();
//                                viewPager.setCurrentItem(1);
                            return true;
                    }
                    return false;
                });


        binding.homeContainer.setCurrentItem(0);
        binding.homeContainer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    binding.bottomNavigation.getMenu().getItem(position).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                // binding.bottomNavigationView.getMenu().getItem(position).setChecked(true);
                // prevMenuItem = binding.bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        setupViewPager(binding.homeContainer);

        onSharedIntent();

    }

    private void onSharedIntent() {
        Intent receiverdIntent = getIntent();
        String receivedAction = receiverdIntent.getAction();
        String receivedType = receiverdIntent.getType();
        if (receivedAction != null && receivedType != null) {
            if (Intent.ACTION_SEND.equals(receivedAction)) {
                if (receivedType.equalsIgnoreCase("application/pdf")) {
                    handlePdf(receiverdIntent);
                } else if (receivedType.startsWith("image/")) {
                    handleImage(receiverdIntent);
                }
            }
            /*if (receivedType.startsWith("application/")) {
                if (receivedAction.equals(Intent.ACTION_SEND)) {

//                    Uri uri = receiverdIntent.getData();
                    Uri uri = (Uri) receiverdIntent
                            .getParcelableExtra(Intent.EXTRA_STREAM);
                    String path = FileUtils.getPath(this, uri);
                    Log.e("PATH::: ", path);
                    File myFile = new File(path);
                    Log.d("debugging", "name = " + myFile.exists() + " " + myFile.getName());
//                    Log.d("debugging", "name = " + myFile.exists());

                    Toast.makeText(this, "Select Folder to upload dile to", Toast.LENGTH_SHORT).show();

                    Folder_Fragment.recievedFile = myFile;
                }
            }*/
        }

    }

    private void handleImage(Intent intent) {
        Uri pdfFile = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (pdfFile != null) {
            Log.d("Image File Path ", pdfFile.getPath());
            Toast.makeText(this, "Select Folder to upload image to", Toast.LENGTH_SHORT).show();
            /*File myFile = new File(pdfFile.getPath());
            Folder_Fragment.recievedFile = myFile;*/
            String path = FileUtils.getPath(this, pdfFile);
            Log.e("PATH::: ", path);
            File myFile = new File(path);
            Folder_Fragment.recievedFile = myFile;
        }
    }

    private void handlePdf(Intent intent) {
        Uri pdfFile = intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (pdfFile != null) {
            Log.d("Pdf File Path ", pdfFile.getPath());
            Toast.makeText(this, "Select Folder to upload file to", Toast.LENGTH_SHORT).show();
            /*File myFile = new File(pdfFile.getPath());
            Folder_Fragment.recievedFile = myFile;*/
            String path = FileUtils.getPath(this, pdfFile);
            Log.e("PATH::: ", path);
            File myFile = new File(path);
            Folder_Fragment.recievedFile = myFile;
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());


        folder_fragment = new Folder_Fragment();
        accountant_fragment = new Accountant_Fragment();

        profile_fragment = new Profile_Fragment();
        setting_fragment = new Setting_Fragment();
        ads_fragment = new Ads_Fragment();

        adapter.addFragment(folder_fragment);
        adapter.addFragment(accountant_fragment);
        adapter.addFragment(ads_fragment);
        adapter.addFragment(profile_fragment);
        adapter.addFragment(setting_fragment);

        int limit = (adapter.getCount() > 1 ? adapter.getCount() - 1 : 1);
        viewPager.setOffscreenPageLimit(limit);

        viewPager.setAdapter(adapter);
    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }


        @Override
        public Fragment getItem(int position) {


            switch (position) {
                case 0:
                    return Folder_Fragment.newInstance();
                case 1:
                    return Accountant_Fragment.newInstance();
                case 2:
                    return Ads_Fragment.newInstance();
                case 3:
                    return Profile_Fragment.newInstance();
                case 4:
                    return Setting_Fragment.newInstance();
                default:
                    return null;
            }


        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unbinder.unbind();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {

            if (ActivityCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
                    || ActivityCompat.checkSelfPermission(getApplicationContext(), ACCESS_CAMERA) == PackageManager.PERMISSION_DENIED) {
                boolean showRationale = shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE);
                boolean showRationaleCamera = shouldShowRequestPermissionRationale(ACCESS_CAMERA);
                if (!showRationale && !showRationaleCamera) {
                    Toast.makeText(getApplicationContext(), "Storage and Camera Permission is required to scanned and upload your docs to server. ", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, ACCESS_CAMERA, Manifest.permission.MANAGE_EXTERNAL_STORAGE}, REQUEST_CODE);
                }
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {

            if (ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    &&
                    ActivityCompat.checkSelfPermission(this, ACCESS_CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, ACCESS_CAMERA, Manifest.permission.MANAGE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }


        } else if (requestCode == 100 && resultCode == 101) {
            bottomNavigationView.setSelectedItemId(R.id.check_folder);
        }
    }


}
