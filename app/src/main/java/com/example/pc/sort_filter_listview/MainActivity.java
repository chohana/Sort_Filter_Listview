package com.example.pc.sort_filter_listview;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    CustomListAdapter Adapter;
    ListView listView;
    ArrayList<String> itemList = new ArrayList<>();
    EditText editText, searchEdit;
    Button button;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listadddel);

        editText = (EditText)findViewById(R.id.addItem);
        searchEdit = (EditText)findViewById(R.id.searchItem);
        button = (Button)findViewById(R.id.addButton);

        // 리스트 뷰 레이아웃 준비
        listView = (ListView)findViewById(R.id.listView);
        listView.setTextFilterEnabled(true);


        //test용
        itemList.add("1");
        itemList.add("가나다");



//        button.setOnClickListener(buttonOnClickListeer);
//        searchEdit.addTextChangedListener(editTextWatcher);

        // 어댑터 생성
        Adapter = new CustomListAdapter(MainActivity.this, itemList);

        // 리스트 뷰 어댑터 연결
        listView.setAdapter(Adapter);

        button.setOnClickListener(buttonOnClickListeer);
        searchEdit.addTextChangedListener(editTextWatcher);
        listView.setOnItemClickListener(onItemClickListener);

        listView.setAdapter(Adapter);

    }

    // Button OnClickListner
    Button.OnClickListener buttonOnClickListeer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            itemList.add(editText.getText().toString());

            for(int i = 0; i< itemList.size(); i++){
                Log.d("after button click : ", ""+itemList.get(i));
            }
            Collections.sort(itemList);

//            Adapter = new CustomListAdapter(MainActivity.this, itemList); //추가
            Adapter.copyItems.clear();
            Adapter.copyItems.addAll(itemList);
            Adapter.notifyDataSetChanged();
            editText.setText("");
        }
    };

    //EditText addTextChangeListener
    TextWatcher editTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String text = searchEdit.getText().toString().toLowerCase(Locale.getDefault());
            Adapter.filter(text);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    //리스트 아이템 OnItemClickListener
    AdapterView.OnItemClickListener onItemClickListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    final LinearLayout linear = (LinearLayout)
                            View.inflate(MainActivity.this, R.layout.dialog_box, null);
                    String msg;
                    final EditText editItem = (EditText)linear.findViewById(R.id.dialogEdit);
                    msg = ""+itemList.get(position);
                    Log.d("dialog msg", "" + msg);
                    editItem.setText(msg);

                    new android.app.AlertDialog.Builder(MainActivity.this)
                            .setTitle("텍스트 수정")
                            .setIcon(R.mipmap.ic_launcher)
                            .setView(linear)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    itemList.set(position, editItem.getText().toString());
                                    Collections.sort(itemList);
//                                    Adapter = new CustomListAdapter(MainActivity.this, itemList);  //추가
                                    Adapter.copyItems.clear();
                                    Adapter.copyItems.addAll(itemList);
                                    Adapter.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            })
                            .show();
                }
            };

}
