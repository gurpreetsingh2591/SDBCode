package expense.exp.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import expense.exp.internet.model.Folder;

public abstract class DialogFolderViewHolder<T> extends RecyclerView.ViewHolder {

    public DialogFolderViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void bind(Folder folder);
}
