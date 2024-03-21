package expense.exp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.kyanogen.signatureview.SignatureView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import expense.exp.BuildConfig;
import expense.exp.R;
import expense.exp.databinding.ActivityCropBinding;
import expense.exp.databinding.ActivitySignatureBinding;

public class SignatureActivity extends AppCompatActivity {
    private SignatureView signatureView;
    private Button btnClear,btnAdd;
    ActivitySignatureBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_signature);
        binding = ActivitySignatureBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        int colorPrimary = ContextCompat.getColor(this, R.color.colorPrimary);
        binding.signatureView.setPenColor(colorPrimary);


        binding.btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.signatureView.clearCanvas();//Clear SignatureView
            }
        });

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File file = new File(directory, System.currentTimeMillis() + ".png");

                if (file.exists()) {
                    file.delete();
                }

                FileOutputStream out = null;
                Bitmap bitmap = binding.signatureView.getSignatureBitmap();
                try {
                    out = new FileOutputStream(file);
                    if (bitmap != null) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    } else {
                        throw new FileNotFoundException();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.flush();
                            out.close();

                            if (bitmap != null) {
                                Uri yourUri = Uri.fromFile(file);

//                                String imagePath = getRealPathFromURI(yourUri);
                                Intent intent = new Intent();
                                intent.putExtra("image", yourUri.toString());
                                setResult(Activity.RESULT_OK, intent);
                                finish();
                                Log.e("##SIGNATURE#",yourUri.toString());
//                                Toast.makeText(getApplicationContext(), "Image saved successfully at " + file.getPath(), Toast.LENGTH_LONG).show();

                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                                    new MyMediaScanner(SignatureActivity.this, file);
                                } else {
                                    ArrayList<String> toBeScanned = new ArrayList<String>();
                                    toBeScanned.add(file.getAbsolutePath());
                                    String[] toBeScannedStr = new String[toBeScanned.size()];
                                    toBeScannedStr = toBeScanned.toArray(toBeScannedStr);
                                    MediaScannerConnection.scanFile(SignatureActivity.this, toBeScannedStr, null,
                                            null);
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    });

    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String result = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return result;
    }
    private class MyMediaScanner implements
            MediaScannerConnection.MediaScannerConnectionClient {

        private MediaScannerConnection mSC;
        private File file;

        MyMediaScanner(Context context, File file) {
            this.file = file;
            mSC = new MediaScannerConnection(context, this);
            mSC.connect();
        }

        @Override
        public void onMediaScannerConnected() {
            mSC.scanFile(file.getAbsolutePath(), null);
        }

        @Override
        public void onScanCompleted(String path, Uri uri) {
            mSC.disconnect();
        }
    }

    public void InfoDialog() {
        String infoMessage = "App version : " + BuildConfig.VERSION_NAME;
        infoMessage = infoMessage + "\n\n" + "SignatureView library version : " +
                com.kyanogen.signatureview.BuildConfig.VERSION_NAME;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.vInfo)
                .setMessage(infoMessage)
                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        Dialog dialog = builder.create();
        dialog.show();
    }
}