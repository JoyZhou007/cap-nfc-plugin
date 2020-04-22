package com.nfc.plugin;

import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;

@NativePlugin()
public class NFCPlugin extends Plugin {

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

    /**
     * send task info message to NFC
     * @param call
     */
    @PluginMethod()
    public void sendTaskInfo(PluginCall call) {
        String value = call.getString("taskInfo");
        JSObject ret = new JSObject();
        ret.put("taskInfo", value);
        call.success(ret);
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
    }


}
