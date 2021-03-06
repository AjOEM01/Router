package com.example.android.router;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditDevice extends AppCompatActivity {

    Devices device;
    int position;
    MyDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_device);

        getIncomingIntent();
    }

    private void getIncomingIntent() {
        //making sure there are extras. Otherwise app will crash!
        if(getIntent().hasExtra("clicked_device")) {
//          device = getIntent().getParcelableExtra("clicked_device"); //If data was parceled
            device = (Devices) getIntent().getSerializableExtra("clicked_device");
            loadData();
        }

        if(getIntent().hasExtra("position")) {
            position = getIntent().getIntExtra("position",position);
//            Log.d("position : " ,"" + position);
        }
    }

    private void loadData() {
        final EditText nickName,ipAdd,macAdd, download, upload, connectionType;
        String downloadString, uploadString, connectionTypeString;
        TextView deviceName;
        Button saveBtn;
        final Spinner spinner;
        final List<String> deviceTypes = new ArrayList<>();
        Collections.addAll(deviceTypes, "Laptop","Mobile","Desktop","Printer","Router");
        final List<Integer> images = new ArrayList<>();
        images.add(R.drawable.icon_laptop_bubble_black);
        images.add(R.drawable.icon_mobile_bubble_black);
        images.add(R.drawable.icon_desktop_bubble_black);
        images.add(R.drawable.icon_printer_bubble_black);
        images.add(R.drawable.icon_router_bubble_black);

        nickName = findViewById(R.id.nick_name_edit_text);
        ipAdd = findViewById(R.id.ip_edit_text);
        macAdd = findViewById(R.id.mac_edit_text);
        deviceName = findViewById(R.id.domain_value);
        download = findViewById(R.id.download_edit_text);
        upload = findViewById(R.id.upload_edit_text);
        connectionType = findViewById(R.id.connection_type_edit_text);

        saveBtn = findViewById(R.id.ED_save_btn);
        spinner = findViewById(R.id.ED_spinner);

        dbHandler = new MyDBHandler(this,null,null,1);

        nickName.setText(device.getNickName());
        ipAdd.setText(device.getiPAdd());
        ipAdd.setInputType(InputType.TYPE_NULL); //android:editable="false" is deprecated so use this. But inputType = "none" doesnt work on xml.
        macAdd.setText(device.getmACAdd().toUpperCase());
        macAdd.setInputType(InputType.TYPE_NULL);
        deviceName.setText(device.getDeviceName());
        deviceName.setInputType(InputType.TYPE_NULL);

        if (device.getDownSpeed() == -1 ) downloadString = "Unlimited";
        else downloadString = device.getDownSpeed() + " " + device.getDownTUString();

        download.setText(downloadString);
        download.setInputType(InputType.TYPE_NULL);

        if (device.getUpSpeed() == -1) uploadString = "Unlimited";
        else uploadString = device.getUpSpeed() + " " + device.getUpTUString();

        upload.setText(uploadString);
        upload.setInputType(InputType.TYPE_NULL);

        if (device.getLan() == 1) connectionTypeString = "LAN";
        else connectionTypeString = "WAN";

        connectionType.setText(connectionTypeString);
        connectionType.setInputType(InputType.TYPE_NULL);

        EditDeviceSpinnerAdapter editDeviceSpinnerAdapter = new
                EditDeviceSpinnerAdapter(getApplicationContext(),images,deviceTypes);

        spinner.setAdapter(editDeviceSpinnerAdapter);
        final int position = images.indexOf(device.getImg());
        spinner.post(new Runnable() { // setting the saved image from the database for the device
            @Override
            public void run() {
                spinner.setSelection(position);
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                device.setImg(images.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                device.setNickName(String.valueOf(nickName.getText()));
                dbHandler.updateHandler(device);
                Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    class EditDeviceSpinnerAdapter extends BaseAdapter {
        private Context eDSACTX;
        private List<Integer> images;
        private List<String> deviceTypes;
        private LayoutInflater inflater;

        EditDeviceSpinnerAdapter(Context eDSACTX, List<Integer> images, List<String> deviceTypes) {
            this.eDSACTX = eDSACTX;
            this.images = images;
            this.deviceTypes = deviceTypes;
            inflater = (LayoutInflater.from(eDSACTX));
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.spinner_items,null);
            ImageView icon = view.findViewById(R.id.spinner1_imageview);
//        TextView deviceType = view.findViewById(R.id.spinner1_textview);
            icon.setImageResource(images.get(i));
//        deviceType.setText(deviceTypes.get(i));
            return view;
        }
    }
}
