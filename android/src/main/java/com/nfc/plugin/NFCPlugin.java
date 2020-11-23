package com.nfc.plugin;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.LHCZ_Ind.ReaderTransferApi;
import com.example.LHCZ_Ind.utils.BmpParse29;
import com.example.LHCZ_Ind.utils.BmpUtils29;
import com.getcapacitor.JSObject;
import com.getcapacitor.NativePlugin;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.nfc.plugin.capnfcplugin.R;

import org.json.JSONException;


@NativePlugin(
        permissions = {
                Manifest.permission.NFC
        }
)

public class NFCPlugin extends Plugin {

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
  private boolean HighSpeed = true;
  private String strNdef = "";

  private ReaderTransferApi readerApi;

  public static Handler mHandler;
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


  ProductInfor productInfor;
  //  public static String taskNumber = "", taskNumbershort = "", inspectionCategory = "", priority = "", partNumber = "", taskResult = "", taskStatus = "",
//    partTypeVersion = "", bindingType = "", partType = "", partCount = "", processbar = "", partNumbershort = "";
  public static TaskInfoEntity taskInfoEntity;

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

  public static boolean isStartWrite = false;
  public static int count = 10;

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

    System.out.println("sendTaskInfo----start  isStartWrite=" + isStartWrite);
    call.resolve(ret);
    //获取前端iTask信息
    JSObject taskInfo = call.getObject("taskInfo", new JSObject());

    taskInfoEntity = new TaskInfoEntity();
    if (!call.getData().has("bindingType")) {//（0代表任务卡） （1代表零件卡）
      taskInfoEntity.bindingType = taskInfo.getString("bindingType");
    }

    if (!call.getData().has("clearType")) {//ture 刷屏和写ndef ;false 	快速清屏，清ndef
      try {
        taskInfoEntity.clearType = taskInfo.getBoolean("clearType");
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

    if (!call.getData().has("partNumber")) {//零件编号
      taskInfoEntity.partNumber = taskInfo.getString("partNumber");
//        partNumber = "DF_PriductLine_20201022003_004";
//      partNumber = "DF_P_20201022003_004";
      if (!TextUtils.isEmpty(taskInfoEntity.partNumber)) {
        String partNumbershort = "";
        //partnumber是根据后面的下划线分割，中间的日期不展示
        String[] partNumberArray = taskInfoEntity.partNumber.split("_");
        if (partNumberArray.length == 4) {
          partNumbershort = partNumberArray[0] + "_" + partNumberArray[1] + " " + partNumberArray[3];
        } else if (partNumberArray.length == 3) {
          partNumbershort = partNumberArray[0] + " " + partNumberArray[2];
        }
        taskInfoEntity.partNumbershort = partNumbershort;
        System.out.println("partNumbershort=" + partNumbershort);
      }
    }
    if (!call.getData().has("taskNumber")) {//任务编号
      taskInfoEntity.taskNumber = taskInfo.getString("taskNumber");
      //taskNumber根据下划线，展示后面的日期就可以
      if (!TextUtils.isEmpty(taskInfoEntity.taskNumber)) {
        String[] taskNumberArray = taskInfoEntity.taskNumber.split("_");
        String taskNumbershort = "";
        if (taskNumberArray.length > 1) {
          taskNumbershort = taskNumberArray[1];
        }
        taskInfoEntity.taskNumbershort = taskNumbershort;
        System.out.println("taskNumbershort=" + taskNumbershort);
      }
    }
    if (!call.getData().has("priority")) {//优先级
      taskInfoEntity.priority = taskInfo.getString("priority");
    }
    if (!call.getData().has("inspectionCategory")) {//送检类型
      taskInfoEntity.inspectionCategory = taskInfo.getString("inspectionCategory");
    }
    if (!call.getData().has("taskResult")) {//结果
      taskInfoEntity.taskResult = taskInfo.getString("taskResult");
    }
    if (!call.getData().has("taskStatus")) {//任务状态
      taskInfoEntity.taskStatus = taskInfo.getString("taskStatus");
    }
    if (!call.getData().has("partType")) {//零件型号
      taskInfoEntity.partType = taskInfo.getString("partType");
    }
    if (!call.getData().has("partTypeVersion")) {//零件版本
      taskInfoEntity.partTypeVersion = taskInfo.getString("partTypeVersion");
    }
    if (!call.getData().has("partCount")) {//零件个数
      taskInfoEntity.partCount = taskInfo.getString("partCount");
    }
    if (!call.getData().has("processbar")) {//总bar个数
      taskInfoEntity.processbar = taskInfo.getString("processbar");
    }
    //是否service 写卡
    isStartWrite = true;
  }

  // plugin 写卡方式
  private void writeInfo(PluginCall call) {

    strNdef = "{\"TaskNumber\":\"" + taskInfoEntity.taskNumber + "\",\"PartNumber\":\"" + taskInfoEntity.partNumber + "\"}";

    String strContent = "bindingType=" + taskInfoEntity.bindingType + ",priority=" + taskInfoEntity.priority + ",inspectionCategory=" + taskInfoEntity.inspectionCategory + ",partType=" + taskInfoEntity.partType
            + ",partTypeVersion=" + taskInfoEntity.partTypeVersion + "taskNumber=" + taskInfoEntity.taskNumber + ",taskNumbershort=" + taskInfoEntity.taskNumbershort + ",partCount=" + taskInfoEntity.partCount +
            ",taskResult=" + taskInfoEntity.taskResult + ",processbar=" + taskInfoEntity.processbar + ",partNumber=" + taskInfoEntity.partNumber + ",partNumbershort=" + taskInfoEntity.partNumbershort;
    System.out.println(strContent + "==NFCPlugin----strNdef=" + strNdef);
    productInfor = new ProductInfor(taskInfoEntity.taskNumber, unit, spec,
            grade, field, price, barCode, qrCode, lcdInch);

    tagPriceView = new TagPriceView(getContext(), productInfor,
            0x29);
//联合自定义Vie画布局
//    if (mBackgroundBitmap == null) {
//      mBackgroundBitmap = Bitmap.createBitmap(screenWidth, screenHeight,
//        Bitmap.Config.RGB_565);
//
//      canvas = new Canvas(mBackgroundBitmap);
//    }

//        tagPriceView.draw(canvas);
    mBackgroundBitmap = getViewPartsCard();//2020-10-25  layout布局转bitmap布局

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

    //首先判断tag是否在读卡器上面，在读卡器上面就可以手动刷，否则提示标签不在读卡器上面
    if (readerApi.checkCardOnReader()) {
      System.out.println("NFCPlugin----quickClrUpdateWriteNdefTag");
      //手动刷屏test
      readerApi.quickClrUpdateWriteNdefTag(NFTData, NFTData.length, 0, HighSpeed, invert, strNdef);
    } else {
      System.out.println("NFCPlugin----Tag isn't on the reader!");
      Toast.makeText(getContext(), "Tag isn't on the reader!", Toast.LENGTH_SHORT).show();
    }
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
    // 创建�??个负责更新进度条的Handler
    mHandler = new Handler() {
      @Override
      public void handleMessage(Message msg) {
        if (msg.what == ReaderTransferApi.PLAY_COMPLETE_SOUND_CMD) {//NFCPlugin 写卡返回状态
          if (READ_NDEF_AFTER_UPDATE_TAG_LCD_FLAG
                  && !READ_ONLY_NDEF_FLAG) {
            StringBuffer strBufNdef = new StringBuffer();
            int ret = readerApi.readNdef(strBufNdef);

//            if (ret == 0) {
//              Toast.makeText(getContext(), strBufNdef, Toast.LENGTH_LONG).show();
//            } else if (ret == 1) {
//              Toast.makeText(getContext(), "Symbol is Off!", Toast.LENGTH_LONG).show();
//            } else if (ret == 3) {
//              Toast.makeText(getContext(), "The tag Ndef data exceed the l004 bytes limit!", Toast.LENGTH_LONG).show();
//            } else if (ret == 4) {
//              Toast.makeText(getContext(), "不是ndef数据", Toast.LENGTH_LONG).show();
//            }


            byte[] uid = new byte[30];
            int len = readerApi.readerReadTagUid(uid);
            String cardId = "";
            if (len != 0) {
              try {
                cardId = getHexString(uid).substring(0, 20);
              } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
              }
            }
            cardId = cardId.replace(" ", "");//去掉空格

            System.out.println("NFCPlugin----ret==" + ret + ",cardId=" + cardId);
            //刷屏结果发送前端
            JSObject writeResult = new JSObject();
            writeResult.put("writeNfcResult", ret);
            writeResult.put("cardId", cardId + "");
            notifyListeners("writeNFCResult", writeResult, true);
          }
        }else if(msg.what==100005){//service 写卡成功

          TaskInfoEntity  returnInfo=new TaskInfoEntity();
          returnInfo= (TaskInfoEntity) msg.obj;
          JSObject writeResult = new JSObject();
          writeResult.put("writeNfcResult", returnInfo.taskResult);
          writeResult.put("cardId", returnInfo.cardId);
          notifyListeners("writeNFCResult", writeResult, true);
        }else if(msg.what==100006){//service 服务写卡失败
          TaskInfoEntity  returnInfo=new TaskInfoEntity();
          returnInfo= (TaskInfoEntity) msg.obj;
          JSObject writeResult = new JSObject();
          writeResult.put("writeNfcResult", returnInfo.taskResult);
          writeResult.put("cardId", returnInfo.cardId);
          notifyListeners("writeNFCResult", writeResult, true);
        }
      }
    };
    readerApi = new ReaderTransferApi(getContext(), mHandler);
  }



  @Override
  protected void handleOnDestroy() {
    super.handleOnDestroy();
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


  //设置自定义布局样式  layout转bitmap
  public Bitmap getViewPartsCard() {
    View view = View.inflate(getContext(), R.layout.task_info, null);

    int measuredWidth = View.MeasureSpec.makeMeasureSpec(screenWidth, View.MeasureSpec.EXACTLY);
    int measuredHeight = View.MeasureSpec.makeMeasureSpec(screenHeight, View.MeasureSpec.AT_MOST);

    LinearLayout ll_states = (LinearLayout) view.findViewById(R.id.ll_states);
    TextView tv_priority = (TextView) view.findViewById(R.id.tv_priority);
    TextView tv_parttypeversion = (TextView) view.findViewById(R.id.tv_parttypeversion);
    TextView tv_parttype = (TextView) view.findViewById(R.id.tv_parttype);
    TextView tv_tasknumber = (TextView) view.findViewById(R.id.tv_tasknumber);
    TextView tv_partNumber = (TextView) view.findViewById(R.id.tv_partNumber);
    TextView tv_result = (TextView) view.findViewById(R.id.tv_result);
    TextView tv_taskstatus = (TextView) view.findViewById(R.id.tv_taskstatus);
    TextView tv_inspectioncategory = (TextView) view.findViewById(R.id.tv_inspectioncategory);
    TextView tv_partCount = (TextView) view.findViewById(R.id.tv_partCount);


    Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/APARAJB.TTF");//字体库
    tv_priority.setTypeface(font);//设置字体
    tv_parttypeversion.setTypeface(font);
    tv_parttype.setTypeface(font);
    tv_tasknumber.setTypeface(font);
    tv_partNumber.setTypeface(font);
    tv_result.setTypeface(font);
//    tv_taskstatus.setTypeface(font);
    tv_inspectioncategory.setTypeface(font);
    tv_partCount.setTypeface(font);

    //字段赋值
    setTextVisisility(tv_priority, taskInfoEntity.priority);
    setTextVisisility(tv_inspectioncategory, taskInfoEntity.inspectionCategory);
    setTextVisisility(tv_result, taskInfoEntity.taskResult);
    setTextVisisility(tv_taskstatus, taskInfoEntity.taskStatus);
    setTextVisisility(tv_parttype, taskInfoEntity.partType);
    setTextVisisility(tv_parttypeversion, taskInfoEntity.partTypeVersion);
    setTextVisisility(tv_tasknumber, taskInfoEntity.taskNumbershort);//任务编号
    setTextVisisility(tv_partNumber, taskInfoEntity.partNumbershort);//工件编号
    setTextVisisility(tv_partCount, taskInfoEntity.partCount);//零件个数

//    if (!TextUtils.isEmpty(bindingType)) {
//      if (bindingType.equals("1")) {//0代表任务卡 1代表零件卡
//        setTextVisisility(tv_tasknumber,partNumber);//工件编号
//        setTextVisisility(tv_partNumber,taskNumber);//任务编号
//      } else {
//        setTextVisisility(tv_tasknumber,taskNumber);
//        setTextVisisility(tv_partNumber,partCount);
//      }
//    }

    if (!TextUtils.isEmpty(taskInfoEntity.processbar)) {
      ll_states.removeAllViews();
      for (int i = 0; i < Integer.valueOf(taskInfoEntity.processbar); i++) {
        View cardstates = View.inflate(getContext(), R.layout.item_task_states, null);
        ll_states.addView(cardstates);
        View view1 = cardstates.findViewById(R.id.view_1);
        if (i < 2) {
          view1.setBackground(getContext().getResources().getDrawable(R.drawable.shape_fillet));
        } else {
          view1.setBackground(getContext().getResources().getDrawable(R.drawable.shape_circular_kong));
        }
      }
    }


    view.measure(measuredWidth, measuredHeight);
    view.layout(0, 0, screenWidth, screenHeight);
    view.buildDrawingCache();
    Bitmap bitmap = view.getDrawingCache();

    return bitmap;

  }

  private void setTextVisisility(TextView tv, String strContent) {
    if (!TextUtils.isEmpty(strContent)) {
      tv.setText(strContent);
    } else {
      tv.setVisibility(View.GONE);
    }
  }

  public Bitmap getViewTaskCard() {

    View view = View.inflate(getContext(), R.layout.task_card, null);
//        View view =  getLayoutInflater().inflate(layoutId, null);
    int measuredWidth = View.MeasureSpec.makeMeasureSpec(screenWidth, View.MeasureSpec.EXACTLY);
    int measuredHeight = View.MeasureSpec.makeMeasureSpec(screenHeight, View.MeasureSpec.AT_MOST);

    TextView tv_priority = (TextView) view.findViewById(R.id.tv_priority);
    TextView tv_parttypeversion = (TextView) view.findViewById(R.id.tv_parttypeversion);
    TextView tv_parttypemodel = (TextView) view.findViewById(R.id.tv_parttypemodel);
    LinearLayout ll_states = (LinearLayout) view.findViewById(R.id.ll_states);
    TextView tv_number = (TextView) view.findViewById(R.id.tv_number);
    TextView tv_gjnumber = (TextView) view.findViewById(R.id.tv_gjnumber);
    TextView tv_result = (TextView) view.findViewById(R.id.tv_result);
    TextView tv_taskstatus = (TextView) view.findViewById(R.id.tv_taskstatus);
    TextView tv_inspectioncategory = (TextView) view.findViewById(R.id.tv_result);
    Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/APARAJB.TTF");

    tv_priority.setTypeface(font);//设置字体
    tv_priority.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
    tv_parttypeversion.setTypeface(font);
    tv_parttypemodel.setTypeface(font);
    tv_number.setTypeface(font);
    tv_gjnumber.setTypeface(font);
    tv_result.setTypeface(font);
//    tv_taskstatus.setTypeface(font);
//    tv_inspectioncategory.setTypeface(font);


    ll_states.removeAllViews();
    for (int i = 0; i < 6; i++) {
      View cardstates = View.inflate(getContext(), R.layout.item_task_states, null);
//            View roomViews = getLayoutInflater().inflate(R.layout.item_task_states, null);
      ll_states.addView(cardstates);
      View view1 = cardstates.findViewById(R.id.view_1);
      if (i < 2) {
        view1.setBackground(getContext().getResources().getDrawable(R.drawable.shape_fillet));
      } else {
        view1.setBackground(getContext().getResources().getDrawable(R.drawable.shape_circular_kong));
      }
    }

    view.measure(measuredWidth, measuredHeight);
    view.layout(0, 0, screenWidth, screenHeight);
    view.buildDrawingCache();
    Bitmap bitmap = view.getDrawingCache();

    return bitmap;

  }


}
