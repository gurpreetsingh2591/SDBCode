package expense.exp.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import expense.exp.ImageGridActivity;
import expense.exp.R;


import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by admin on 30-07-2018.
 */

public class GridImage_Adapter extends RecyclerView.Adapter<GridImage_Adapter.ViewHolder> {
    private  ArrayList<String> selectedUriList ;
    private Context context;
    private ClickListener clickListener;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";

    public GridImage_Adapter(Context context,  ArrayList<String> selectedUriList,ClickListener clickListener) {
        this.selectedUriList = selectedUriList;
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    public GridImage_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_grid_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridImage_Adapter.ViewHolder viewHolder, final int i) {

//        Log.e("img_url", String.valueOf(UploadFileActivity.image_path.get(i)));

        File imgFile = new File(String.valueOf(ImageGridActivity.image_path.get(i)));
        if (imgFile.exists()) {
//            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//            viewHolder.img_android.setImageBitmap(myBitmap);
            Glide.with(context)
                    .load(ImageGridActivity.image_path.get(i))
                    .thumbnail(0.1f)
                    .dontAnimate()
                    .centerCrop()
                    .placeholder(gun0912.tedbottompicker.R.drawable.ic_gallery)
                    .error(gun0912.tedbottompicker.R.drawable.img_error)
                    .into(viewHolder.img_android);

//            viewHolder.img_android.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    context.startActivity(new Intent(context, Full_View_Image.class)
//                            .putExtra("img", String.valueOf(UploadFileActivity.image_path.get(i)))
//                             .putExtra("doc_id",""));
//                }
//            });

        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onPositionClicked(selectedUriList, i);
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
        return ImageGridActivity.image_path.size();
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

    public void updateList(ArrayList<String> picturesList) {
        this.selectedUriList = picturesList;
        notifyDataSetChanged();
    }
    public interface ClickListener {

        void onPositionClicked(ArrayList<String> datum, int position);

        void onLongClicked(int position);
    }
}
