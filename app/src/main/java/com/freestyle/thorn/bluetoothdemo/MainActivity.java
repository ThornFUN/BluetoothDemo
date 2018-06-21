package com.freestyle.thorn.bluetoothdemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Iterator;
import java.util.Set;

/**
 * author：Thorn on 2018/06/21
 */
public class MainActivity extends AppCompatActivity {

    private Button btn_fun1,btn_fun2,btn_fun3;


    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_fun1 = findViewById(R.id.btn_fun1);
        btn_fun2 = findViewById(R.id.btn_fun2);
        btn_fun3 = findViewById(R.id.btn_fun3);

        //创建一个IntentFilter对象，将其action指定为BluetoothDevice.ACTION_FOUND,有选择的作用
        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);

        //生成一个DiscoverReceiver的对象
        DiscoverReceiver discoverReceiver = new DiscoverReceiver();

        //注册广播接收器
        registerReceiver(discoverReceiver, intentFilter);

        //得到BluetoothAdapter的对象
        final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        //1. 扫描已经配对成功的蓝牙设备，打印这些蓝牙设备
        btn_fun1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if(bluetoothAdapter != null){
                    Toast.makeText(MainActivity.this,"bluetooth available",Toast.LENGTH_SHORT).show();
                    if(!bluetoothAdapter.isEnabled()){
                        //调用isEnable方法判断当前蓝牙设备是否可用
                        //提示用户打开蓝牙
                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivity(intent);
                    }

                    //获得检测到的蓝牙设备的集合
                    Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
                    if(devices.size()>0){
                        //迭代的方式从Set集合中取值
                        for(Iterator iterator = devices.iterator(); iterator.hasNext();){
                            BluetoothDevice bluetoothDevice = (BluetoothDevice) iterator.next();
                            //打印蓝牙设备的地址
                            Toast.makeText(MainActivity.this,bluetoothDevice.getAddress(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(MainActivity.this,"bluetooth not such function",Toast.LENGTH_SHORT).show();
                }
            }


        });

        // 2. 打开本设备的蓝牙可见性300秒
        btn_fun2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //创建一个Intent对象，并给其action赋值
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                //将一个键值对放入Intent中，用于设置可见状态持续时间
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION
                        , 300);//大于300秒的会被忽略掉，也就是说最大就是300秒
                startActivity(discoverableIntent);

            }
        });

        //3. 扫描周围的蓝牙设备，并打印所有扫描到的设备名称（配合功能2 ）
        btn_fun3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //开始扫描周围的设备
                //一次扫描最少要12s
                //没扫描到一个设备，系统就会发送一个广播，下面的DiscoverReceiver
                //这个类就是用来接收接收系统发送的广播消息
                bluetoothAdapter.startDiscovery();
            }
        });







    }



    class DiscoverReceiver extends BroadcastReceiver {

        /**
         * @param content
         * @param intent
         */
        @Override
        public void onReceive(Context content, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            Toast.makeText(MainActivity.this,device.getAddress(),Toast.LENGTH_SHORT).show();
        }

    }
}
