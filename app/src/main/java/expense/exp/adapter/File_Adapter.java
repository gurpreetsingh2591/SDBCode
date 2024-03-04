package expense.exp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import expense.exp.R;
import expense.exp.helper.Pref;
import expense.exp.helper.SharedPrefManager;
import expense.exp.helper.Utils;
import expense.exp.internet.ApiClient;
import expense.exp.internet.ApiInterface;
import expense.exp.internet.model.Document;
import expense.exp.internet.model.Folder;
import expense.exp.internet.model.GetFiles;
import expense.exp.internet.model.SubFolder;
import expense.exp.model_class.Delete_Folfer;
import expense.exp.model_class.FolderFile;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by admin on 14-05-2018.
 */

public class File_Adapter extends RecyclerView.Adapter<PredBaseViewHolder> {

    private final List<Document> datumList;
    private final ClickListener clickListener;
    private GetFiles getFiles;
    private Context context;
    private final List<FolderFile> folderFiles;
    private SharedPrefManager sharedPrefManager;


    public class MyViewHolder extends PredBaseViewHolder {
        public TextView folder_txt, subtitel;
        public ImageView thumbnail_image, chatImg, img_option_menu;
        public ToggleButton toggleButton;
        public RadioButton radio_btn;

        public MyViewHolder(View view) {
            super(view);
            folder_txt = view.findViewById(R.id.foldername);
            thumbnail_image = view.findViewById(R.id.thumbnail_image);
            radio_btn = view.findViewById(R.id.radio_btn);
            chatImg = view.findViewById(R.id.chatImg);


            if (sharedPrefManager.getuserinfo().getType().equals("user")){
                chatImg.setVisibility(View.VISIBLE);

            }else {
                chatImg.setVisibility(View.GONE);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onPositionClicked(folderFiles.get(getAdapterPosition()), 1, getAdapterPosition());
//                    clickListener.onButtonClick(getAdapterPosition());
                }
            });

//            chatImg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    clickListener.onChatCLick(folderFiles.get(getAdapterPosition()), getAdapterPosition());
////                    clickListener.onButtonClick(getAdapterPosition());
//                }
//            });

            radio_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    clickListener.onPositionClicked(datumList.get(getAdapterPosition()), 1, getAdapterPosition());
                    clickListener.onButtonClick(getAdapterPosition());
                    folderFiles.get(getAdapterPosition()).setSelect(true);
                    notifyDataSetChanged();
                }
            });

        }

        @Override
        public void bind(FolderFile folderFile) {
            folder_txt.setText(folderFile.getDocName());

            radio_btn.setChecked(folderFile.isSelect());

            radio_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    folderFile.setSelect(!folderFile.isSelect());
                    notifyDataSetChanged();
                }
            });

            chatImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onChatCLick(folderFile.getId(), getAdapterPosition());
//                    clickListener.onButtonClick(getAdapterPosition());
                }
            });
        }


    }


    public File_Adapter(List<FolderFile> folderFiles, List<Document> datumList, Context context, ClickListener clickListener) {
        this.datumList = datumList;
        this.getFiles = getFiles;
        this.folderFiles = folderFiles;
        this.clickListener = clickListener;
        this.context = context;
        sharedPrefManager=new SharedPrefManager(context);

    }

    @Override
    public PredBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == R.layout.row_item_file) {
            View itemView = inflater.inflate(R.layout.row_item_file, parent, false);
            return new MyViewHolder(itemView);
        }
        if (viewType == R.layout.row_assign_item_folder) {
            View itemView = inflater
                    .inflate(R.layout.row_assign_item_folder, parent, false);
            return new FolderViewHolder(itemView);
        }

        return null;
    }

    public class FolderViewHolder extends PredBaseViewHolder {
        public TextView folder_txt, total_doc, tv_rename;
        public ImageView play_list, img_option_menu;
        public ToggleButton toggleButton;

        public FolderViewHolder(View view) {
            super(view);
            folder_txt = view.findViewById(R.id.foldername);
            tv_rename = view.findViewById(R.id.tv_rename);
            toggleButton = view.findViewById(R.id.add_remove);

            total_doc = view.findViewById(R.id.total_doc);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onPositionClicked(folderFiles.get(getPosition()), 1, getPosition());
                    clickListener.onLongClicked(getPosition());
                }
            });

            tv_rename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.rename(Integer.parseInt(folderFiles.get(getAdapterPosition()).getId()));
                }
            });

        }

        @Override
        public void bind(FolderFile folderFile) {

            folder_txt.setText(folderFile.getFolderName());
            String desc = "documents " + folderFile.getNumOfDocs() + " | Year : " + Pref.getStringValue(context, Utils.selected_year, "");
            total_doc.setText(desc);


            SharedPreferences sharedPrefs = context.getSharedPreferences("assignfolder", MODE_PRIVATE);
            Boolean a = sharedPrefs.getBoolean(folderFile.getId() + getAdapterPosition(), false);


            if (sharedPrefManager.getuserinfo().getType().equals("user")){
                toggleButton.setVisibility(View.VISIBLE);

            }else {
                toggleButton.setVisibility(View.GONE);
            }
            if (a) {
                toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.remove_acc_icon));
                toggleButton.setChecked(true);

            } else {
                toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.add_acc_icon));
                toggleButton.setChecked(false);

            }

            toggleButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (toggleButton.isChecked()) {
                        toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.remove_acc_icon));
                        SharedPreferences.Editor editor = context.getSharedPreferences("folder", MODE_PRIVATE).edit();
                        editor.putBoolean(folderFile.getId() + getAdapterPosition(), true);
                        clickListener.onPositionClicked(folderFile, 2, getAdapterPosition());
                        editor.commit();
                    } else {
                        toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.add_acc_icon));
                        SharedPreferences.Editor editor = context.getSharedPreferences("lol", MODE_PRIVATE).edit();
                        editor.putBoolean(folderFile.getId() + getAdapterPosition(), false);
                        clickListener.onPositionClicked(folderFile, 3, getAdapterPosition());
                        editor.commit();

                    }

                }
            });


            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    // TODO Auto-generated method stub
                    Log.v("long clicked","pos: " + getAdapterPosition());

                    Log.e("longClick", String.valueOf(getAdapterPosition()));

                    AlertDialog.Builder ADB = new AlertDialog.Builder(context);
                    ADB.setTitle("Delete Folder ");
                    ADB.setMessage("Are you sure want to delete folder?");
                    ADB.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            DeleteFolder(folderFiles.get(getAdapterPosition()).getId(),folderFiles.get(getAdapterPosition())
                                    .getOwnerId(),getAdapterPosition());

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
                            folderFiles.remove(position);

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


    public void deleteItem(int position) {
        datumList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, datumList.size());

    }


    @Override
    public void onBindViewHolder(PredBaseViewHolder holder, final int position) {
        if (folderFiles.get(position).isFolder()) {
            ((FolderViewHolder) holder).bind(folderFiles.get(position));
        } else
            ((MyViewHolder) holder).bind(folderFiles.get(position));

    }

    @Override
    public int getItemCount() {
        return folderFiles == null ? 0 : folderFiles.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (folderFiles.get(position).isFolder())
            return R.layout.row_assign_item_folder;
        else
            return R.layout.row_item_file;
    }


    public interface ClickListener {

        void onPositionClicked(Document datum, int id, int position);

        void onLongClicked(int position);

        void onButtonClick(int position);

        void onPositionClicked(FolderFile datum, int id, int position);

        void onChatCLick(String id, int position);

        void rename(int position);

    }

}
