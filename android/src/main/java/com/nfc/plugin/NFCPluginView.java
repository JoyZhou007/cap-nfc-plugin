package com.nfc.plugin;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.LHCZ_Ind.ReaderTransferApi;
import com.example.LHCZ_Ind.utils.BmpParse29;
import com.example.LHCZ_Ind.utils.BmpUtils29;
import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;


@NativePlugin(
        permissions = {
                Manifest.permission.NFC
        }
)

public class NFCPluginView extends Plugin {

    private final int NFC_REQUEST_PERMISSION = 2102;
    private MyFirstReceiver myFirstReceiver;
    public static final String BROADCAST_ACTION_DISC = "com.nfc.plugin.MyFirstReceiver";

    protected TagPriceView tagPriceView;
    private static boolean READ_UID_CMD_FLAG = false;
    private static boolean WRITE_ONLY_NDEF_FLAG = false;
    private static boolean READ_ONLY_NDEF_FLAG = false;
    private static boolean UPDATE_ONLY_TAG_LCD_FLAG = false;
    private static boolean UPDATA_TAG_LCD_AND_WRITE_NDEF_FLAG = false;
    private static boolean QUICK_CLEAR_UPDATA_TAG_LCD_AND_WRITE_NDEF_FLAG = false;
    private static boolean READ_NDEF_AFTER_UPDATE_TAG_LCD_FLAG = true;


    private boolean invert = false;
    private boolean HighSpeed = false;
    private String strNdef = "";

    private ReaderTransferApi readerApi;

    private Handler mHandler;
    private String wriNdefInfor = null;

    Bitmap mBackgroundBitmap = null, whiteBlackBitmap;
    BmpUtils29 bmpUtils;
    int screenWidth = 296;
    int screenHeight = 128;
    Canvas canvas;

    protected BmpParse29 bmpParse = null;
    // ******NFC CONFIG**************
    protected byte[] NFTData = new byte[4736];

    String proName = "NFC电子货架标签";
    String unit = "个";
    String spec = "2.X\"";
    String grade = "0001";
    String field = "深圳";
    String price = "66.00";
    String barCode = "6901285991240";
    String qrCode = "http://www.uswiot.cn";
    String lcdInch = "2.9";

    @PluginMethod()
    public void echo(PluginCall call) {
        String value = call.getString("value");
        JSObject ret = new JSObject();
        ret.put("value", value);
        call.resolve(ret);

        //首先判断tag是否在读卡器上面，在读卡器上面就可以手动刷，否则提示标签不在读卡器上面
        if (readerApi.checkCardOnReader()) {
            System.out.println("NFCPlugin----quickClrUpdateWriteNdefTag");
            Toast.makeText(getContext(), "NFCPlugin----quickClrUpdateWriteNdefTag", Toast.LENGTH_SHORT).show();
            //手动刷屏test
            readerApi.quickClrUpdateWriteNdefTag(NFTData, NFTData.length, 0, HighSpeed, invert, strNdef);

//        StringBuffer strBufNdef = new StringBuffer();
//        int rets = readerApi.readNdef(strBufNdef);
//        if(rets == 0)
//        {
//          Toast.makeText(getContext(), "111111111="+strBufNdef ,Toast.LENGTH_LONG).show();
//        }
//        else if(rets == 1)
//        {
//          Toast.makeText(getContext(), "Symbol is Off!", Toast.LENGTH_LONG).show();
//        }
//        else if(rets == 3)
//        {
//          Toast.makeText(getContext(), "The tag Ndef data exceed the l004 bytes limit!", Toast.LENGTH_LONG).show();
//        }
//        else if(rets == 4)
//        {
//          Toast.makeText(getContext(), "不是ndef数据",Toast.LENGTH_LONG).show();
//        }
//        System.out.println("NFCPlugin----ret=="+rets);
//
        } else {
            System.out.println("NFCPlugin----Tag isn't on the reader!");
            Toast.makeText(getContext(), "Tag isn't on the reader!", Toast.LENGTH_SHORT).show();
        }


    }

    @PluginMethod()
    public void testEvent(PluginCall call) {
        bridge.triggerWindowJSEvent("myCustomEvent", "{ 'dataKey': 'dataValue' }");
        call.success();
    }

    /**
     * send task info message to NFC
     *
     * @param call
     */
    @PluginMethod()
    public void sendTaskInfo(final PluginCall call) {
        String value = call.getString("taskInfo");
        JSObject ret = new JSObject();
        ret.put("taskInfo", value);

        System.out.println("sendTaskInfo----start");
        call.resolve(ret);

    }

    @Override
    protected void handleOnStart() {
        super.handleOnStart();
        System.out.println("NFCPlugin----start");
        JSObject ret = new JSObject();
        ret.put("value", "some value");
        // send read NFC result event
//        notifyListeners("readNFCResult", ret,true);
        // begin write NFC info event
        notifyListeners("beginWriteNFC", ret, true);
        // send has written NFC info end event
//        notifyListeners("writeNFCResult", ret,true);

        initData();
    }


    private void initData() {
        ProductInfor productInfor = new ProductInfor(proName, unit, spec,
                grade, field, price, barCode, qrCode, lcdInch);

        tagPriceView = new TagPriceView(getContext(), productInfor,
                0x29);

        strNdef = proName + "\r\n" + unit + "\r\n" + spec + "\r\n" + grade
                + "\r\n" + price + "\r\n" + barCode + "\r\n" + field + "\r\n"
                + qrCode + "\r\n" + lcdInch;
        System.out.println("NFCPlugin----strNdef=" + strNdef);
        if (mBackgroundBitmap == null) {
            mBackgroundBitmap = Bitmap.createBitmap(screenWidth, screenHeight,
                    Bitmap.Config.RGB_565);

            canvas = new Canvas(mBackgroundBitmap);
        }

        tagPriceView.draw(canvas);

        bmpUtils = new BmpUtils29();
        // 24bitsBMP
        whiteBlackBitmap = BmpUtils29.convertToBlackWhite(mBackgroundBitmap);


        // BmpParse鐟欙絾鐎介崳锟�
        bmpParse = BmpParse29.getInstance();

        NFTData = bmpParse.bmpRAWData2NFCData(BmpUtils29
                        .getBmpDotData(BmpUtils29.getBmpData(mBackgroundBitmap,
                                BmpUtils29.BLACK_WHITE_COLOR)),
                BmpUtils29.BLACK_WHITE_COLOR, BmpUtils29.REVERSE_COLOR);
        NFTData = HalLcd_SetRAMValue(NFTData, 4736);


        // 创建�??个负责更新进度条的Handler
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ReaderTransferApi.PLAY_COMPLETE_SOUND_CMD) {
                    if (READ_NDEF_AFTER_UPDATE_TAG_LCD_FLAG
                            && !READ_ONLY_NDEF_FLAG) {
                        System.out.println("NFCPlugin----1111");
                        StringBuffer strBufNdef = new StringBuffer();
                        int ret = readerApi.readNdef(strBufNdef);
                        if (ret == 0) {
                            Toast.makeText(getContext(), strBufNdef, Toast.LENGTH_LONG).show();
                        } else if (ret == 1) {
                            Toast.makeText(getContext(), "Symbol is Off!", Toast.LENGTH_LONG).show();
                        } else if (ret == 3) {
                            Toast.makeText(getContext(), "The tag Ndef data exceed the l004 bytes limit!", Toast.LENGTH_LONG).show();
                        } else if (ret == 4) {
                            Toast.makeText(getContext(), "不是ndef数据", Toast.LENGTH_LONG).show();
                        }
                        System.out.println("NFCPlugin----ret==" + ret);
                        Toast.makeText(getContext(), "ret=" + ret, Toast.LENGTH_LONG).show();
                    }
//                    runOnUiThread(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            // TODO Auto-generated method
//                            Toast.makeText(getContext(), "发送完成!刷屏花费时间=" + readerApi.TotalTime + "ms", Toast.LENGTH_LONG).show();
//
//                        }
//                    });
                }
            }
        };
        readerApi = new ReaderTransferApi(getContext(), mHandler);
    }


    @Override
    protected void handleOnDestroy() {
        super.handleOnDestroy();
//    getContext().unregisterReceiver(myFirstReceiver);//注销广播

        readerApi.closeReader(getContext());
    }

    public static Bitmap invertBitmap(Bitmap bitmap) {

        int sWidth; // width
        int sHeight; // height
        int sRow; // Row--height
        int sCol; // col--width
        int sPixel = 0;
        int sIndex;

        // ARGB values
        int sA = 0;
        int sR = 0;
        int sG = 0;
        int sB = 0;

        int[] sPixels;
        int[] pStore;
        int[] sOriginal;

        sWidth = bitmap.getWidth();
        sHeight = bitmap.getHeight();
        sPixels = new int[sWidth * sHeight];
        bitmap.getPixels(sPixels, 0, sWidth, 0, 0, sWidth, sHeight);

        sIndex = 0;
        for (sRow = 0; sRow < sHeight; sRow++) {
            sIndex = sRow * sWidth;
            for (sCol = 0; sCol < sWidth; sCol++) {
                sPixel = sPixels[sIndex];
                sA = (sPixel >> 24) & 0xff;
                sR = (sPixel >> 16) & 0xff;
                sG = (sPixel >> 8) & 0xff;
                sB = sPixel & 0xff;

                sR = 255 - sR;
                sG = 255 - sG;
                sB = 255 - sB;

                sPixel = ((sA & 0xff) << 24 | (sR & 0xff) << 16
                        | (sG & 0xff) << 8 | sB & 0xff);

                sPixels[sIndex] = sPixel;

                sIndex++;
            }
        }
        Bitmap re_bitmap = Bitmap.createBitmap(sWidth, sHeight,
                Bitmap.Config.RGB_565);
        re_bitmap.setPixels(sPixels, 0, sWidth, 0, 0, sWidth, sHeight);
        return re_bitmap;
    }

    // 起始点在左上角，纵向扫描 �??
    // 取模时，纵向取模，字节不反向�??1bytes=8 pixels
    byte[] HalLcd_SetRAMValue(byte[] datas, int num) {
        int i;
        byte tempOriginal;
        int tempcol = 0;
        int templine = 0;
        byte[] sendData = new byte[num];
        int maxColumnBytes = 296;
        int maxLineBytes = 16;

        if (num == 4736) // 2.9 black
        {
            maxColumnBytes = 296;
            maxLineBytes = 16;

        } else if (num == 4000) // 2.13 black
        {
            maxColumnBytes = 250;
            maxLineBytes = 16;

        } else if (num == 15000)// 4.2 black
        {
            maxColumnBytes = 300;
            maxLineBytes = 50;

        }

        for (i = 0; i < num; i++) {

            tempOriginal = datas[templine * maxColumnBytes + tempcol];

            templine++;
            // if (templine >= MAX_LINE_BYTES)
            if (templine >= maxLineBytes) {
                tempcol++;
                templine = 0;
            }
            sendData[i] = tempOriginal;

        }
        return sendData;
    }

    protected static String getHexString(byte[] data) throws Exception {
        String szDataStr = "";
        for (int ii = 0; ii < data.length; ii++) {
            szDataStr += String.format("%02X ", data[ii] & 0xFF);
        }
        return szDataStr;
    }
}
