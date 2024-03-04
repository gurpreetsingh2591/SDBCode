package expense.exp.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import expense.exp.R;
import expense.exp.internet.model.Ads;
import expense.exp.internet.model.Folder;
import expense.exp.internet.model.SubFolder;

/**
 * Created by appleboy on 14-05-2018.
 */

public class Ads_Adapter extends RecyclerView.Adapter<Ads_Adapter.MyViewHolder> {

    private List<Ads> datumList;
    private List<Ads> tempList;
    private ClickListener clickListener;
    private final Context context;
    private String imageBaseUrl;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView folder_txt, total_doc;
        public ImageView tv_rename;
        public CircleImageView ivAdsImage;
        public ToggleButton toggleButton;
        public RelativeLayout rowRoot;

        public MyViewHolder(View view) {
            super(view);
            folder_txt = view.findViewById(R.id.foldername);
            tv_rename = view.findViewById(R.id.tv_rename);
            toggleButton = view.findViewById(R.id.add_remove);
            total_doc = view.findViewById(R.id.total_doc);
            rowRoot = view.findViewById(R.id.rowRoot);
            ivAdsImage = view.findViewById(R.id.ivAdsImage);
        }


    }

    public Ads_Adapter(Context context) {
        this.context = context;
    }

    public void setListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setData(List<Ads> datumList) {
        this.datumList = datumList;
        this.tempList = datumList;
    }

    public void setData(String  imageBaseUrl) {
        this.imageBaseUrl = imageBaseUrl;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_ads, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Ads data = datumList.get(position);

        String adsImageLink = datumList.get(position).getLogo();
        holder.folder_txt.setText(data.getTitle());
        if (!TextUtils.isEmpty(data.getLink()))
            holder.total_doc.setText(data.getLink());
        loadImage(imageBaseUrl,adsImageLink,holder.ivAdsImage);

        SharedPreferences sharedPrefs = context.getSharedPreferences("assignfolder", MODE_PRIVATE);
        Boolean a = sharedPrefs.getBoolean(data.getId() + position, false);
        holder.rowRoot.setOnClickListener(view1 -> clickListener.onPositionClicked(datumList.get(position), 1, position));
        holder.tv_rename.setOnClickListener(view1 -> clickListener.onEditClicked(datumList.get(position), 1, position));


    }

    private void loadImage(String path, String imageObj, ImageView imageView) {
        Picasso.get()
                .load(path + imageObj)
                .placeholder(R.drawable.user_profile_icon)
                .error(R.drawable.user_profile_icon)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return datumList == null ? 0 : datumList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface ClickListener {
        void onPositionClicked(Ads datum, int id, int position);

        void onEditClicked(Ads datum, int id, int position);
    }


    public void filter(String charText) {
        ArrayList<Ads> nList = new ArrayList<Ads>();
        if (charText == null || TextUtils.isEmpty(charText)) {
            nList.addAll(tempList);
        } else {
            charText = charText.toLowerCase();

            if (charText.length() == 0) {
                nList.addAll(tempList);
            } else {
                for (Ads wp : tempList) {
                    String value = wp.getTitle();
                    if (value.toLowerCase().contains(charText.toLowerCase()))//contains for less accurate result nd matches for accurate result
                    {
                        nList.add(wp);
                    }
                }
            }
        }
        datumList = nList;

        notifyDataSetChanged();


    }

    private List<Folder> combineParentSubFolders(List<Folder> folders) {
        List<Folder> folderList = new ArrayList<>();
        for (Folder folder : folders) {
            folderList.add(loop(folder));
            if (folder.getSubFolders().size() > 0) {
                folderList.addAll(loopSubFolder(folder.getSubFolders()));
            }
        }
        return folderList;
    }

    private List<Folder> loopSubFolder(List<SubFolder> subFolders) {
        List<Folder> folderList = new ArrayList<>();
        for (SubFolder folder : subFolders) {
            Folder folder1 = new Folder();
            folder1.setId(folder.getId());
            folder1.setOwnerId(folder.getOwnerId());
            folder1.setAccountantId(folder.getAccountantId());
            folder1.setDays(folder.getDays());
            folder1.setFolderAsOrNot(folder.getFolderAsOrNot());
            folder1.setFolderName(folder.getFolderName());
            folder1.setNumOfDocs(folder.getNumOfDocs());
            folderList.add(folder1);
        }
        return folderList;
    }

    private Folder loop(Folder folder) {
        Folder folder1 = new Folder();
        folder1.setId(folder.getId());
        folder1.setOwnerId(folder.getOwnerId());
        folder1.setAccountantId(folder.getAccountantId());
        folder1.setDays(folder.getDays());
        folder1.setFolderAsOrNot(folder.getFolderAsOrNot());
        folder1.setFolderName(folder.getFolderName());
        folder1.setNumOfDocs(folder.getNumOfDocs());
        return folder1;
    }
}
