package expense.exp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;



import expense.exp.activity.UploadFileActivity;
import expense.exp.adapter.GridImage_Adapter;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropFragment;
import com.yalantis.ucrop.UCropFragmentCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class ImageGridActivity extends AppCompatActivity implements GridImage_Adapter.ClickListener, UCropFragmentCallback {
    RecyclerView grid_rv;
    String f_id;
    ImageView iv_done,iv_back;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";
    public static ArrayList<String> image_path = new ArrayList<>();
    private static final int REQUEST_SELECT_PICTURE_FOR_FRAGMENT = 0x02;
    private boolean mShowLoader;
    private UCropFragment fragment;
    private GridImage_Adapter adapter;

    int pos=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_grid);

        image_path=new ArrayList<String>();
        init();
    }
    private void init(){
        grid_rv=findViewById(R.id.grid_rv);
        iv_done=findViewById(R.id.iv_done);
        iv_back=findViewById(R.id.iv_back);
        f_id=getIntent().getStringExtra("f_id");

        if (image_path!=null) {
            image_path=getIntent().getStringArrayListExtra("via_camera");
        }


        adapter = new GridImage_Adapter(getApplicationContext(), image_path,this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        grid_rv.setLayoutManager(layoutManager);
        grid_rv.setAdapter(adapter);


        iv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImageGridActivity.this, UploadFileActivity.class);
                        intent.putExtra("f_id", f_id);
                        intent.putExtra("via_camera", image_path);
                        startActivity(intent);
                        finish();

            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        finish();

            }
        });
    }

    @Override
    public void onPositionClicked(ArrayList<String> datum, int position) {

        pos=position;
        Random random = new Random();
        int minSizePixels = 800;
        int maxSizePixels = 2400;
//        Uri uri = Uri.parse(image_path.get(position));
        Uri uri=Uri.fromFile(new File((image_path.get(position))));
// Uri uri = Uri.parse(String.format(Locale.getDefault(), "https://unsplash.it/%d/%d/?random",
//                minSizePixels + random.nextInt(maxSizePixels - minSizePixels),
//                minSizePixels + random.nextInt(maxSizePixels - minSizePixels)));

        startCrop(uri);
//        datum.remove(position);

    }



    private void startCrop(@NonNull Uri uri) {
        Log.e("uriiiiiiii",uri.toString());
        String destinationFileName = uri.toString();

                destinationFileName += ".jpg";

        Log.e("destinationFileName",destinationFileName);
        UCrop uCrop = UCrop.of(uri, Uri.parse(destinationFileName));

        uCrop.useSourceImageAspectRatio();
//      new UCrop(uri).output(uCrop).withFixedSize(640, 640).start(this);
        uCrop = basisConfig(uCrop);
        uCrop = advancedConfig(uCrop);

//                                                               else start uCrop ActivityuCrop.start(ImageGridActivity.this);

        uCrop.start(ImageGridActivity.this);
    }

    public void setupFragment(UCrop uCrop) {
        fragment = uCrop.getFragment(uCrop.getIntent(this).getExtras());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment, UCropFragment.TAG)
                .commitAllowingStateLoss();

//        setupViews(uCrop.getIntent(this).getExtras());
    }





    /**
     * In most cases you need only to set crop aspect ration and max size for resulting image.
     *
     * @param uCrop - ucrop builder instance
     * @return - ucrop builder instance
     */
    private UCrop basisConfig(@NonNull UCrop uCrop) {


        return uCrop;
    }

    /**
     * Sometimes you want to adjust more options, it's done via {@link com.yalantis.ucrop.UCrop.Options} class.
     *
     * @param uCrop - ucrop builder instance
     * @return - ucrop builder instance
     */
    private UCrop advancedConfig(@NonNull UCrop uCrop) {
        UCrop.Options options = new UCrop.Options();
        options.setFreeStyleCropEnabled(true);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        return uCrop.withOptions(options);
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {

//            image_path.add(resultUri.getPath()); ;
            image_path.set(pos,resultUri.getPath());
            adapter.updateList(image_path); // add this
            adapter.notifyDataSetChanged();
//

            Log.e("resultUri", String.valueOf(resultUri));
        } else {
            Toast.makeText(ImageGridActivity.this,"toast_cannot_retrieve_cropped_image", Toast.LENGTH_SHORT).show();
        }
    }

    private static void startWithUri(ImageGridActivity imageGridActivity, Uri resultUri) {


        Intent intent = new Intent(imageGridActivity, ImageGridActivity.class);
        intent.setData(resultUri);
        imageGridActivity.startActivity(intent);
    }

    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Toast.makeText(ImageGridActivity.this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ImageGridActivity.this,"toast_unexpected_error", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void loadingProgress(boolean showLoader) {
        mShowLoader = showLoader;
        supportInvalidateOptionsMenu();
    }

    @Override
    public void onCropFinish(UCropFragment.UCropResult result) {
        switch (result.mResultCode) {
            case RESULT_OK:
                handleCropResult(result.mResultData);
                break;
            case UCrop.RESULT_ERROR:
                handleCropError(result.mResultData);
                break;
        }
        removeFragmentFromScreen();
    }


    public void removeFragmentFromScreen() {
        getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commit();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        image_path = getIntent().getStringArrayListExtra("via_camera");
        if (resultCode == RESULT_OK) {

           if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);

               Log.e("dataaaaaaaaa",data.toString());

            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }

    @Override
    public void onLongClicked(int position) {

    }
}
