package expense.exp.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import expense.exp.internet.model.Document;
import expense.exp.internet.model.GetFiles;
import expense.exp.internet.model.SubFolder;
import expense.exp.model_class.FolderFile;

public abstract class PredBaseViewHolder<T> extends RecyclerView.ViewHolder {

    public PredBaseViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void bind(FolderFile folderFile);
}
