package expense.exp.activity;

import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import expense.exp.R;
import expense.exp.databinding.ActivityCropBinding;
import expense.exp.databinding.ActivityFullViewImageBinding;
import expense.exp.helper.SharedPrefManager;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

/*import butterknife.BindView;
import butterknife.ButterKnife;*/

public class Full_View_Image extends AppCompatActivity{
    //@BindView(R.id.img)
    PhotoView img;

    Uri url;
    final int PIC_CROP = 1;
    int acc_id = -1;
    private SharedPrefManager sharedPrefManager;
ActivityFullViewImageBinding binding;
    private Uri mCropImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_full_view_image);
      //  ButterKnife.bind(this);
        binding = ActivityFullViewImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPrefManager = new SharedPrefManager(this);

        acc_id = sharedPrefManager.getacc_id();


        if (getIntent().hasExtra("img")) {
            url = Uri.parse(getIntent().getStringExtra("img"));
            Log.e("FullView Path: ", url.getPath());
            Glide.with(this)
                    .load(url.getPath())
                    .placeholder(gun0912.tedbottompicker.R.drawable.ic_gallery)
                    .into(img);
//            startCropImageActivity(Uri.parse(url.getPath()));
        } else if (getIntent().hasExtra("pdf")) {

        }
    }


}
