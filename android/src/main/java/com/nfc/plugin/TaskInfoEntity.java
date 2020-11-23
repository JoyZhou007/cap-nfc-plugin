package com.nfc.plugin;

public class TaskInfoEntity {

  public String process;//
  public String priority;//优先级
  public String inspectionCategory;//送检类型
  public String partTypeVersion;//零件版本
  public String partNumber;//工件编号
  public String partType;//零件型号
  public String taskNumber;//任务编号
  public String taskResult;//结果
  public String taskStatus;//任务状态
  public String processbar;//展示总共bar的个数
  public String bindingType;// 0代表任务卡 1代表零件卡
  public String partCount;//零件个数
  public String taskNumbershort;
  public String partNumbershort;
  public boolean clearType=false;//api 调用 true就是需要清屏；false就是刷卡和写ndef


  public String cardId;
  public String taskNdef;//NDEF数据
}
