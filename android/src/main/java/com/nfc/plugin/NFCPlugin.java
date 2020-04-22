package com.nfc.plugin;

import android.content.IntentFilter;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

@NativePlugin()
public class NFCPlugin extends Plugin {

    private MyFirstReceiver myFirstReceiver;
    public static final String BROADCAST_ACTION_DISC = "com.nfc.plugin.MyFirstReceiver";


    @PluginMethod()
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", value);
        call.resolve(ret);
    }

    @PluginMethod()
    public void testEvent(PluginCall call) {
        bridge.triggerWindowJSEvent("myCustomEvent", "{ 'dataKey': 'dataValue' }");
        call.success();
    }

    /**
     * send task info message to NFC
     * @param call
     */
    @PluginMethod()
    public void sendTaskInfo(PluginCall call) {
        String value = call.getString("taskInfo");
        JSObject ret = new JSObject();
        ret.put("taskInfo", value);
        call.resolve(ret);
    }

    @Override
    protected void handleOnStart() {
        super.handleOnStart();
        System.out.println("AwesomePlugin----start");
        JSObject ret = new JSObject();
        ret.put("value", "some value");
        // send read NFC result event
        notifyListeners("readNFCResult", ret,true);
        // begin write NFC info event
        notifyListeners("beginWriteNFC", ret,true);
        // send has written NFC info end event
        notifyListeners("writeNFCResult", ret,true);

        myFirstReceiver = new MyFirstReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION_DISC);
        getContext().registerReceiver(myFirstReceiver, intentFilter);
    }


}
