package com.nfc.plugin;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.ui.Toast;

@NativePlugin(
        permissions = {
                Manifest.permission.NFC
        }
)
public class NFCPlugin extends Plugin {


    private MyFirstReceiver myFirstReceiver;
    public static final String BROADCAST_ACTION_DISC = "com.nfc.plugin.MyFirstReceiver";
    private final int NFC_REQUEST_PERMISSION = 1000001;
    protected NfcAdapter mNfcAdapter;
    private PendingIntent mPendingIntent;

    @PluginMethod()
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", value);
        call.success(ret);
    }

    @PluginMethod()
    public void testEvent(PluginCall call) {
        bridge.triggerWindowJSEvent("myCustomEvent", "{ 'dataKey': 'dataValue' }");
        call.success();
    }

    @Override
    protected void handleOnStart() {
        super.handleOnStart();
        System.out.println("NfcPlugin----start");
        JSObject ret = new JSObject();
        ret.put("value", "some value");
        notifyListeners("readNFC", ret);

        initAdapter();

        myFirstReceiver = new MyFirstReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION_DISC);
        getContext().registerReceiver(myFirstReceiver, intentFilter);

    }


    //NFC 操作
    @Override
    protected void handleOnNewIntent(Intent intent) {
        super.handleOnNewIntent(intent);

        System.out.println("NfcPlugin----handleOnNewIntent");
        PluginCall savedCall = getSavedCall();

        if (mNfcAdapter != null) {
            Toast.show(getContext(), "识别出NFC");
            System.out.println("NfcPlugin==有NFC功能");
        }
    }

    @Override
    protected void handleOnResume() {
        super.handleOnResume();
        System.out.println("NfcPlugin----handleOnResume");

    }


    @Override
    protected void handleOnPause() {
        super.handleOnPause();
        System.out.println("NfcPlugin----handleOnPause");
    }

    @PluginMethod()
    public void getCurrentPosition(PluginCall call) {
        if (!hasPermission(Manifest.permission.NFC)) {
            saveCall(call);
            pluginRequestAllPermissions();
            System.out.println("无NFC ");
            return;
        } else {
            System.out.println("有NFC ");
        }
    }


    @Override
    protected void handleRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.handleRequestPermissionsResult(requestCode, permissions, grantResults);

        PluginCall savedCall = getSavedCall();
        if (savedCall == null) {
            return;
        }

        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                savedCall.error("User denied location permission");
                return;
            }
        }
        if (requestCode == NFC_REQUEST_PERMISSION) {

        }
    }


    private void initAdapter() {
        if (!hasRequiredPermissions()) {
            pluginRequestAllPermissions();
        }
        mNfcAdapter = NfcAdapter.getDefaultAdapter(getContext());
        mPendingIntent = PendingIntent.getActivity(getContext(), 0,
                new Intent(getContext(), getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);


    }
}
