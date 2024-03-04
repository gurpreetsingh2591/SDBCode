package expense.exp.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;



import expense.exp.R;
import expense.exp.activity.Full_View_Image;
import expense.exp.activity.UploadFileActivity;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by admin on 30-07-2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<String> android;
    private Context context;

    public RecyclerViewAdapter(Context context, ArrayList<String> android) {
        this.android = android;
        this.context = context;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_gride_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder viewHolder, final int i) {

        Log.e("img_url", String.valueOf(UploadFileActivity.image_path.get(i)));

        File imgFile = new File(String.valueOf(UploadFileActivity.image_path.get(i)));
        if (imgFile.exists()) {
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//            viewHolder.img_android.setImageBitmap(myBitmap);
            Glide.with(context)
                    .load(UploadFileActivity.image_path.get(i))
                    .thumbnail(0.1f)
                    .dontAnimate()
                    .centerCrop()
                    .placeholder(gun0912.tedbottompicker.R.drawable.ic_gallery)
                    .error(gun0912.tedbottompicker.R.drawable.img_error)
                    .into(viewHolder.img_android);

            viewHolder.img_android.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, Full_View_Image.class)
                            .putExtra("img", String.valueOf(UploadFileActivity.image_path.get(i)))
                             .putExtra("doc_id",""));
                }
            });

        }
        viewHolder.iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadFileActivity.image_path.remove(i);
                notifyDataSetChanged();
            }
        });

//        Picasso.get()
//                .load(String.valueOf(android.get(i)))
//                .placeholder(R.drawable.file_icon)
//                .error(R.drawable.file_icon)
//                .into(viewHolder.img_android);

    }

    @Override
    public int getItemCount() {
        return UploadFileActivity.image_path.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img_android, iv_remove;

        public ViewHolder(View view) {
            super(view);
            img_android = view.findViewById(R.id.imageView);
            iv_remove = view.findViewById(R.id.iv_remove);
            iv_remove.bringToFront();
        }
    }

    public void clear() {
        final int size = UploadFileActivity.image_path.size();
        UploadFileActivity.image_path.clear();
        notifyItemRangeRemoved(0, size);
    }

}
