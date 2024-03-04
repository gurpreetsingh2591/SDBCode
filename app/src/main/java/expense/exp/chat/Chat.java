package expense.exp.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import expense.exp.R;
import expense.exp.activity.PlanListActivity;
import expense.exp.helper.SharedPrefManager;

public class Chat extends AppCompatActivity {
    LinearLayout layout;
    ImageView sendButton,back_icon;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    View layoutChat;
    RecyclerView rvChat;
    SharedPrefManager sharedPrefManager;
    String ownerId;
    private ArrayList<ChatResponse>userDetailsArrayList=new ArrayList<>();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_layout);

        layout = findViewById(R.id.layout1);
        messageArea = findViewById(R.id.messageArea);
        sendButton = findViewById(R.id.sendButton);
        scrollView = findViewById(R.id.scrollView);
        back_icon = findViewById(R.id.back_icon);
        layoutChat = findViewById(R.id.layoutChat);
        rvChat = findViewById(R.id.rvChat);
        sharedPrefManager = new SharedPrefManager(this);

        ownerId = getIntent().getStringExtra("ownerId");
        Firebase.setAndroidContext(this);
//        reference1 = new Firebase("https://android-chat-app-e711d.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
//        reference2 = new Firebase("https://android-chat-app-e711d.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);
        reference1 = new Firebase("https://sdba-a16a3-default-rtdb.firebaseio.com/messages/" +sharedPrefManager.getuserinfo().getId() + "_" + ownerId);
        reference2 = new Firebase("https://console.firebase.google.com/project/sdba-a16a3/firestore/data/messages/" + "test2" + "_" + "test");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", sharedPrefManager.getuserinfo().getName());
                    map.put("email", sharedPrefManager.getuserinfo().getEmail());
                    map.put("image", sharedPrefManager.getuserinfo().getImage());
                    map.put("id", ownerId);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                }
                messageArea.setText("");
            }
        });


        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                ChatResponse userDetails = new ChatResponse();
                userDetails.setUsername(map.get("user").toString());
                userDetails.setMesssage(map.get("message").toString());
                userDetailsArrayList.add(userDetails);

                Log.e("#####userDetailsArrayList#######", String.valueOf(userDetailsArrayList.size()));
                if(userName.equals(sharedPrefManager.getuserinfo().getName())){
//                    addMessageBox("You:-\n" + message, 1);
                    setAdapter("1");
                }
                else{
//                    addMessageBox(UserDetails.chatWith + ":-\n" + message, 2);
                    setAdapter("2");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    private void setAdapter(String type){

        final LinearLayoutManager layoutManager = new LinearLayoutManager(Chat.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvChat.setLayoutManager(layoutManager);
        ChatListAdapter adapter = new ChatListAdapter(userDetailsArrayList,Chat.this,type);
        rvChat.setAdapter(adapter);
    }
    public void addMessageBox(String message, int type){

        LinearLayout sender_messsage_text_linearLayout,receiver_message_text_linearLayout;
        TextView sender_text_time,receiver_text_time;

//        sender_messsage_text_linearLayout = findViewById(R.id.sender_messsage_text_linearLayout);
//        receiver_message_text_linearLayout = findViewById(R.id.receiver_message_text_linearLayout);
//        sender_text_time = findViewById(R.id.sender_text_time);
//        receiver_text_time = findViewById(R.id.receiver_text_time);
        TextView textView = new TextView(Chat.this);
        textView.setText(message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 0, 10, 10);
        textView.setPadding(15,10,15,10);
        textView.setLayoutParams(lp);

        if(type == 1) {
            textView.setBackgroundResource(R.drawable.background_left);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
//            sender_text_time.setText(message);
//            receiver_message_text_linearLayout.setVisibility(View.GONE);
//            sender_messsage_text_linearLayout.setVisibility(View.VISIBLE);

        }
        else{
            textView.setBackgroundResource(R.drawable.background_right);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_END);
//            receiver_text_time.setText(message);
//            receiver_message_text_linearLayout.setVisibility(View.VISIBLE);
//            sender_messsage_text_linearLayout.setVisibility(View.GONE);
        }
//        ((ViewGroup)layoutChat.getParent()).removeView(layoutChat);
        layout.addView(textView);

        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}