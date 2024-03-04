package expense.exp.adapter;

import android.content.Context;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;


import expense.exp.R;
import expense.exp.helper.SharedPrefManager;
import expense.exp.internet.model.Folder;
import expense.exp.internet.model.SubFolder;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 14-05-2018.
 */

public class Folder_Adapter extends RecyclerView.Adapter<Folder_Adapter.MyViewHolder> {

    private List<Folder> datumList;
    private ClickListener clickListener;
    private Context context;
    private List<Folder> tempList;
    SharedPrefManager sharedPrefManager;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView folder_txt, total_doc;
        public ImageView play_list, img_option_menu;
        public ToggleButton toggleButton;

        public MyViewHolder(View view) {
            super(view);
            folder_txt = view.findViewById(R.id.foldername);
            total_doc = view.findViewById(R.id.total_doc);
            toggleButton = view.findViewById(R.id.add_remove);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onPositionClicked(datumList.get(getPosition()), 1, getPosition());
                    clickListener.onLongClicked(getPosition());
                }
            });
        }
    }


    public Folder_Adapter( boolean isDialog,List<Folder> datumList, Context context, ClickListener clickListener) {
        this.datumList = datumList;
        this.tempList = datumList;
        this.clickListener = clickListener;
        this.context = context;
        sharedPrefManager = new SharedPrefManager(context);

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_folder, parent, false);

        return new MyViewHolder(itemView);
    }

    public void deleteItem(int position) {
        datumList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, datumList.size());

    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        //      Music_Model music_Model = Music_ModelsList.get(position);
//            holder.title.setText(music_Model.getMusic_titel());
        holder.toggleButton.setVisibility(View.GONE);
        Folder data = datumList.get(position);

        holder.folder_txt.setText(data.getFolderName());
        holder.total_doc.setText(data.getNumOfDocs());


//        if (sharedPrefManager.getuserinfo().getType().equals("user")){
//            holder.toggleButton.setVisibility(View.VISIBLE);
//        }else {
//            holder.toggleButton.setVisibility(View.GONE);
//        }


    }

    @Override
    public int getItemCount() {
        return datumList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public interface ClickListener {

        void onPositionClicked(Folder datum, int id, int position);

        void onLongClicked(int position);
    }

    public void filter(String charText) {
        charText = charText.toLowerCase();
        ArrayList<Folder> nList = new ArrayList<Folder>();
        if (charText.length() == 0) {
            nList.addAll(tempList);
        } else {
            for (Folder wp : tempList) {
                String value = wp.getFolderName();
                if (value.toLowerCase().contains(charText.toLowerCase()))//contains for less accurate result nd matches for accurate result
                {
                    nList.add(wp);
                }
            }
        }
        datumList = nList;

        notifyDataSetChanged();


    }

}
