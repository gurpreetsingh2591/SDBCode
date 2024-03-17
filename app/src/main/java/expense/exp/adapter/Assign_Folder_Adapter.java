package expense.exp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;



import expense.exp.R;
import expense.exp.helper.Pref;
import expense.exp.helper.SharedPrefManager;
import expense.exp.helper.Utils;
import expense.exp.internet.ApiClient;
import expense.exp.internet.ApiInterface;
import expense.exp.internet.model.Folder;
import expense.exp.internet.model.GetFolders;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import expense.exp.internet.model.SubFolder;
import expense.exp.model_class.Delete_Folfer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by admin on 14-05-2018.
 */

public class Assign_Folder_Adapter extends RecyclerView.Adapter<Assign_Folder_Adapter.MyViewHolder> {


    private List<Folder> datumList;
    private List<Folder> tempList;
    private ClickListener clickListener;
    private Context context;
    private SharedPrefManager sharedPrefManager;
    public Assign_Folder_Adapter(List<Folder> folders, DisposableSingleObserver<GetFolders> getFoldersDisposableSingleObserver, ClickListener clickListener) {

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView folder_txt, total_doc, tv_rename;
        public ImageView play_list, img_option_menu;
        public ToggleButton toggleButton;

        public MyViewHolder(View view) {
            super(view);
            folder_txt = view.findViewById(R.id.foldername);
            tv_rename = view.findViewById(R.id.tv_rename);
            toggleButton = view.findViewById(R.id.add_remove);

            total_doc = view.findViewById(R.id.total_doc);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onPositionClicked(datumList.get(getPosition()), 1, getPosition());
                }
            });

            tv_rename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.rename(getAdapterPosition());
                }
            });

        }


    }


    public Assign_Folder_Adapter(File receivedFile, List<Folder> datumList, Context context, ClickListener clickListener) {
        this.datumList = datumList;
        this.tempList = datumList;
        this.clickListener = clickListener;
        this.context = context;

        sharedPrefManager=new SharedPrefManager(context);
//        if (receivedFile!=null)
//            this.datumList = combineParentSubFolders(datumList);
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


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_assign_item_folder, parent, false);

        return new MyViewHolder(itemView);
    }

    public void deleteItem(int position) {
        datumList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, datumList.size());

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        //      Music_Model music_Model = Music_ModelsList.get(position);
//            holder.title.setText(music_Model.getMusic_titel());

        final Folder data = datumList.get(position);

        holder.folder_txt.setText(data.getFolderName());
        String desc = "documents " + data.getNumOfDocs() + " | Year : " + Pref.getStringValue(context, Utils.selected_year, "");
        holder.total_doc.setText(desc);


        SharedPreferences sharedPrefs = context.getSharedPreferences("assignfolder", MODE_PRIVATE);
        Boolean a = sharedPrefs.getBoolean(data.getId() + position, false);


        if (sharedPrefManager.getuserinfo().getType().equals("user")){
           holder.toggleButton.setVisibility(View.VISIBLE);
        }else {
            holder.toggleButton.setVisibility(View.GONE);
        }

        if (a) {
            holder.toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.remove_acc_icon));
            holder.toggleButton.setChecked(true);

        } else {
            holder.toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.add_acc_icon));
            holder.toggleButton.setChecked(false);

        }

        if (data.getFolderAsOrNot().equals("Assigned")){
            holder.toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.remove_acc_icon));


        }else {
            holder.toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.add_acc_icon));


        }

        holder.toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (data.getFolderAsOrNot().equals("Assigned")) {
                    holder.toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.remove_acc_icon));
                    SharedPreferences.Editor editor = context.getSharedPreferences("folder", MODE_PRIVATE).edit();
                    editor.putBoolean(data.getId() + position, true);
                    clickListener.onPositionClicked(data, 3, position);
                    editor.commit();
                } else {
                    holder.toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.add_acc_icon));
                    SharedPreferences.Editor editor = context.getSharedPreferences("lol", MODE_PRIVATE).edit();
                    editor.putBoolean(data.getId() + position, false);
                    clickListener.onPositionClicked(data, 2, position);
                    editor.commit();

                }

            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                Log.v("long clicked","pos: " + position);

                Log.e("longClick", String.valueOf(position));

                AlertDialog.Builder ADB = new AlertDialog.Builder(context);
                ADB.setTitle("Delete Folder ");
                ADB.setMessage("Are you sure want to delete folder?");
                ADB.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DeleteFolder(datumList.get(position).getId(),datumList.get(position).getOwnerId(),position);

                    }
                })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //                        finish();
                            }});
                AlertDialog dialog2 = ADB.show();

                return true;
            }
        });
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

        void rename(int position);
        void onLongClicked(Folder datum,int position);
    }

    public interface RenameClick {
        void rename(int position);
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

    @SuppressLint("CheckResult")
    public void DeleteFolder(String folder_id, String foldre_owner_id,int position) {

//        context.startAnim();


        ApiInterface apiService = ApiClient.getClient(context)
                .create(ApiInterface.class);


        apiService.DeleteFolder(folder_id, foldre_owner_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Delete_Folfer>() {

                    @Override
                    public void onSuccess(Delete_Folfer moveFolder) {
//                        stopAnim();

                        if (moveFolder.getStatus().matches("1")) {
                            Log.e("exeption", "=" + moveFolder.getStatus());

                            notifyDataSetChanged();
                            datumList.remove(position);

                        } else {

                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        // Network error

                        Log.e("exeption", "=" + e);
//                        stopAnim();
                    }
                });

    }
}
