package expense.exp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import expense.exp.R;
import expense.exp.helper.SharedPrefManager;
import expense.exp.internet.model.Folder;

public class AdapterDialogFolder extends RecyclerView.Adapter<DialogFolderViewHolder> {
    private final List<Folder> datumList;
    private final Folder_Adapter.ClickListener clickListener;
    private final Context context;
    private final List<Folder> tempList;
    SharedPrefManager sharedPrefManager;

    public AdapterDialogFolder(List<Folder> datumList, Context context, Folder_Adapter.ClickListener clickListener) {
        this.datumList = datumList;
        this.tempList = datumList;
        this.clickListener = clickListener;
        this.context = context;
        sharedPrefManager = new SharedPrefManager(context);

    }

    @NonNull
    @Override
    public DialogFolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == R.layout.row_item_folder) {
            View itemView = inflater
                    .inflate(R.layout.row_item_folder, parent, false);
            return new ParentFolderView(itemView);
        }
        if (viewType == R.layout.row_item_subfolder) {
            View itemView = inflater
                    .inflate(R.layout.row_item_subfolder, parent, false);
            return new ChildFolderViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DialogFolderViewHolder holder, int position) {
        if (datumList.get(position).isSubfolder()) {
            ((ChildFolderViewHolder) holder).bind(datumList.get(position));
        } else
            ((ParentFolderView) holder).bind(datumList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        if (datumList.get(position).isSubfolder())
            return R.layout.row_item_subfolder;
        else
            return R.layout.row_item_folder;

    }

    @Override
    public int getItemCount() {
        return datumList == null ? 0 : datumList.size();
    }

    public class ChildFolderViewHolder extends DialogFolderViewHolder {
        public TextView folder_txt, total_doc;
        public ImageView play_list, img_option_menu;
        public ToggleButton toggleButton;

        public ChildFolderViewHolder(@NonNull View view) {
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

        @Override
        public void bind(Folder folder) {
            toggleButton.setVisibility(View.GONE);
            Folder data = datumList.get(getAdapterPosition());
            folder_txt.setText(data.getFolderName());
            total_doc.setText(data.getNumOfDocs());
        }
    }

    public class ParentFolderView extends DialogFolderViewHolder {
        public TextView folder_txt, total_doc;
        public ImageView play_list, img_option_menu;
        public ToggleButton toggleButton;

        public ParentFolderView(@NonNull View view) {
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

        @Override
        public void bind(Folder folder) {
            toggleButton.setVisibility(View.GONE);
            Folder data = datumList.get(getAdapterPosition());
            folder_txt.setText(data.getFolderName());
            total_doc.setText(data.getNumOfDocs());

        }
    }
}
