/***********************************************************
Copyright(C), USC company
FileName	: TagPriceView.java
Author		: Guoyuankai
Date	  	: 2016/12/02
Description	: 
Version		: V1.0
History		: 
--------------------------------
2016/12/01: created
2017/5/12:  add china version view 
 ***********************************************************/

package com.nfc.plugin;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.example.LHCZ_Ind.domain.ProductInfor;
import com.example.LHCZ_Ind.utils.EAN13;
import com.example.LHCZ_Ind.R;
//import com.example.tagview.utils.EAN13CodeBuilder;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class TagPriceView extends View {

	Context context;
	Paint paint;
	ProductInfor proInfo;
	int fontsize = 40;
	Typeface font;
	// Bitmap bitmap;
	int inch;

	public TagPriceView(Context context, ProductInfor proInfo, int inch) {
		super(context);
		this.context = context;
		this.paint = new Paint();
		this.proInfo = proInfo;
		this.inch = inch;
		// bitmap=Bitmap.createBitmap(250, 122, Config.ARGB_8888);

	}

	public TagPriceView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	private Bitmap drawBarcode() {
		// String barcode = "7761010114033430";
		String barcode = this.proInfo.getEan13Code().toString();
		EAN13 code = new EAN13();

		code.setData(barcode, 10/* ���Ӵ����textHeight ���� */);

		Bitmap bitmap = code.getBitmap(110, 25);
		return bitmap;
	}

	private Bitmap drawBarcode75() {
		// String barcode = "7761010114033430";
		String barcode = this.proInfo.getEan13Code().toString();
		EAN13 code = new EAN13();

		code.setData(barcode, 20/* ���Ӵ����textHeight ���� */);

		Bitmap bitmap = code.getBitmap(245, 65);
		return bitmap;
	}
	
	private void drawTagPicCh213(Canvas paramCanvas) {

		int leftEdge = 8;
		float yBegin = 44;
		float yStep = 21;// 36;

		float xPriceFlag = leftEdge + 10;
		float yPriceFlag = yBegin + 2 * yStep + 22;

		float yqrCode = 0;
		float xqrCode = leftEdge + 157;

		float ybarCode = 83;
		float xbarCode = leftEdge + 125;

		paramCanvas.clipRect(new Rect(0, 0, 250, 122));

		paramCanvas.drawColor(Color.WHITE);
		paramCanvas.save();
		paramCanvas.clipRect(new Rect(0, 0, 250, 25));
//		font = Typeface.createFromAsset(getContext().getAssets(),
//				"fonts/kaiti.ttf");
//		this.paint.setTypeface(font);
		this.paint.setFakeBoldText(true);
		this.paint.setColor(Color.BLACK);

		this.paint.setTextSize(20);

		paramCanvas.drawText(this.proInfo.getProductName().toString(),
				leftEdge, 20, this.paint);
		paramCanvas.restore();

		this.paint.setColor(Color.BLACK);
		this.paint.setTextSize(16);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.unit) + ":",
				leftEdge, yBegin, this.paint);
		int i1 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.unit) + ":");

		this.paint.setTextSize(15);
		paramCanvas.drawText(this.proInfo.getUnit().toString(), leftEdge + i1,
				yBegin, this.paint);
		this.paint.setTextSize(16);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.spec) + ":",
				leftEdge + 80, yBegin, this.paint);
		int i2 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.spec) + ":");
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/Roboto-Regular.ttf");
		// this.paint.setTypeface(font);
		paramCanvas.drawText(this.proInfo.getSpecification().toString(),
				leftEdge + i2 + 79, yBegin, this.paint);
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/kaiti.ttf");
		// this.paint.setTypeface(font);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.grade) + ":",
				leftEdge, yBegin + yStep, this.paint);
		int i3 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.grade) + ":");
		this.paint.setTextSize(15);
		paramCanvas.drawText(this.proInfo.getGrade().toString(), leftEdge + i3,
				yBegin + yStep, this.paint);

		this.paint.setTextSize(16);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.field) + ":",
				leftEdge + 79, yBegin + yStep, this.paint);
		int i4 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.field) + ":");
		paramCanvas.drawText(this.proInfo.getField().toString(), leftEdge + i4
				+ 80, yBegin + yStep, this.paint);

		font = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/APARAJB.TTF");
		this.paint.setTypeface(font);

		int priPos = this.proInfo.getPrice().toString().indexOf(".");
		int priLength = this.proInfo.getPrice().length();
		this.paint.setTextSize(55);// 133

		if (priPos != -1) {

			String priceL = "";
			String priceR = "";
			priceL = this.proInfo.getPrice().toString()
					.substring(0, priPos + 1).trim();
			priceR = this.proInfo.getPrice().toString()
					.substring(priPos + 1, priLength).trim();
			paramCanvas.drawText(priceL, xPriceFlag, yPriceFlag, this.paint);
			int i5 = (int) this.paint.measureText(priceL);
			this.paint.setTextSize(45);// 108
			paramCanvas.drawText(priceR, xPriceFlag + i5, yPriceFlag - 9,
					this.paint);

		}

		else {
			paramCanvas.drawText(this.proInfo.getPrice().toString(), leftEdge,
					yPriceFlag, this.paint);
		}

		if (!this.proInfo.getQrCode().toString().isEmpty()) {
			paramCanvas.save();

			paramCanvas
					.drawBitmap(
							generateBitmap(this.proInfo.getQrCode().toString(),
									90, 90), xqrCode, yqrCode, this.paint);
			// paramCanvas.drawBitmap(big(generateBitmap("深圳市联合智能卡有限公司",80,80)),
			// xqrCode, yqrCode, this.paint);

			paramCanvas.restore();
		}

		if (!this.proInfo.getEan13Code().toString().isEmpty()) {
			paramCanvas.save();

			paramCanvas.drawBitmap(drawBarcode(), xbarCode, ybarCode,
					this.paint);
			paramCanvas.restore();
		}

	}

	
	public void drawTagPicCh213_2_BWR(Canvas paramCanvas) {

		int leftEdge = 6;
		float yBegin = 38;
		float yStep = 21;// 36;

		//float xPriceFlag = leftEdge + 5;
		float xPriceFlag = leftEdge + 2;
		float yPriceFlag = yBegin + 2 * yStep + 16;

		float yqrCode = 0;
		float xqrCode = leftEdge + 135;//125;

		float ybarCode = 79;
		float xbarCode = leftEdge + 99;

		paramCanvas.clipRect(new Rect(0, 0, 212, 104));

		paramCanvas.drawColor(Color.WHITE);
		paramCanvas.save();
		paramCanvas.clipRect(new Rect(0, 0, 212, 19));
		font = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/lishu.ttf");
		this.paint.setTypeface(font);
		this.paint.setFakeBoldText(true);
		this.paint.setColor(0xFFB21F2E);

		this.paint.setTextSize(16);
		//this.paint.setTextSize(18);

		paramCanvas.drawText(this.proInfo.getProductName().toString(),
				leftEdge, 17, this.paint);
		paramCanvas.restore();


		this.paint.setColor(Color.BLACK);
		this.paint.setTextSize(13);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.unit) + ":",
				leftEdge, yBegin, this.paint);
		int i1 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.unit) + ":");

		// this.paint.setTextSize(11);
		paramCanvas.drawText(this.proInfo.getUnit().toString(), leftEdge + i1 + 2,
				yBegin, this.paint);
		//this.paint.setTextSize(12);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.spec) + ":",
				leftEdge + 74, yBegin, this.paint);
		int i2 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.spec) + ":");
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/Roboto-Regular.ttf");
		// this.paint.setTypeface(font);
		paramCanvas.drawText(this.proInfo.getSpecification().toString(),
				leftEdge + i2 + 75, yBegin, this.paint);
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/kaiti.ttf");
		// this.paint.setTypeface(font);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.grade) + ":",
				leftEdge, yBegin + yStep, this.paint);
		int i3 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.grade) + ":");
		// this.paint.setTextSize(11);
		paramCanvas.drawText(this.proInfo.getGrade().toString(), leftEdge + i3 + 2,
				yBegin + yStep, this.paint);

		//this.paint.setTextSize(12);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.field) + ":",
				leftEdge + 74, yBegin + yStep, this.paint);
		int i4 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.field) + ":");
		paramCanvas.drawText(this.proInfo.getField().toString(), leftEdge + i4
				+ 75, yBegin + yStep, this.paint);

		font = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/APARAJB.TTF");
		this.paint.setTypeface(font);
		this.paint.setColor(0xFFB21F2E);
		int priPos = this.proInfo.getPrice().toString().indexOf(".");
		int priLength = this.proInfo.getPrice().length();
		this.paint.setTextSize(49);// 133

		if (priPos != -1) {

			String priceL = "";
			String priceR = "";
			priceL = this.proInfo.getPrice().toString()
					.substring(0, priPos + 1).trim();
			priceR = this.proInfo.getPrice().toString()
					.substring(priPos + 1, priLength).trim();
			paramCanvas.drawText(priceL, xPriceFlag, yPriceFlag, this.paint);
			int i5 = (int) this.paint.measureText(priceL);
			this.paint.setTextSize(34);// 108
			paramCanvas.drawText(priceR, xPriceFlag + i5, yPriceFlag - 9,
					this.paint);

		}

		else {
			paramCanvas.drawText(this.proInfo.getPrice().toString(), leftEdge,
					yPriceFlag, this.paint);
		}

		if (!this.proInfo.getQrCode().toString().isEmpty()) {
			paramCanvas.save();

			paramCanvas
					.drawBitmap(
							generateBitmap(this.proInfo.getQrCode().toString(),
									80, 80), xqrCode, yqrCode, this.paint);
			// paramCanvas.drawBitmap(big(generateBitmap("深圳市联合智能卡有限公司",80,80)),
			// xqrCode, yqrCode, this.paint);

			paramCanvas.restore();
		}

		if (!this.proInfo.getEan13Code().toString().isEmpty()) {
			paramCanvas.save();

			paramCanvas.drawBitmap(drawBarcode(), xbarCode, ybarCode,
					this.paint);
			paramCanvas.restore();
		}

	}

	public void drawTagPicCh213_2_BW(Canvas paramCanvas) {
		
		


		int leftEdge = 6;
		float yBegin = 38;
		float yStep = 21;// 36;

//		float xPriceFlag = leftEdge + 5;
//		float yPriceFlag = yBegin + 2 * yStep + 16;

		float yqrCode = 0;
		float xqrCode = leftEdge + 135;//125;

		float ybarCode = 79;
		float xbarCode = leftEdge + 99;

		paramCanvas.clipRect(new Rect(0, 0, 212, 104));

		paramCanvas.drawColor(Color.WHITE);
		font = Typeface.createFromAsset(getContext().getAssets(),
		"fonts/lishu.ttf");
		this.paint.setTypeface(font);
		this.paint.setFakeBoldText(true);
//		paramCanvas.save();
//		paramCanvas.clipRect(new Rect(0, 0, 212, 24));
//		font = Typeface.createFromAsset(getContext().getAssets(),
//				"fonts/kaiti.ttf");
//		this.paint.setTypeface(font);
//		this.paint.setFakeBoldText(true);
//		this.paint.setColor(0xFFB21F2E);
//
//		this.paint.setTextSize(16);
//
//		paramCanvas.drawText(this.proInfo.getProductName().toString(),
//				leftEdge, 23, this.paint);
//		paramCanvas.restore();

		this.paint.setColor(Color.BLACK);
		this.paint.setTextSize(13);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.unit) + ":",
				leftEdge, yBegin, this.paint);
		int i1 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.unit) + ":");

		// this.paint.setTextSize(11);
		paramCanvas.drawText(this.proInfo.getUnit().toString(), leftEdge + i1 + 2,
				yBegin, this.paint);
		//this.paint.setTextSize(12);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.spec) + ":",
				leftEdge + 74, yBegin, this.paint);
		int i2 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.spec) + ":");
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/Roboto-Regular.ttf");
		// this.paint.setTypeface(font);
		paramCanvas.drawText(this.proInfo.getSpecification().toString(),
				leftEdge + i2 + 73, yBegin, this.paint);
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/kaiti.ttf");
		// this.paint.setTypeface(font);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.grade) + ":",
				leftEdge, yBegin + yStep, this.paint);
		int i3 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.grade) + ":");
		// this.paint.setTextSize(11);
		paramCanvas.drawText(this.proInfo.getGrade().toString(), leftEdge + i3 + 2,
				yBegin + yStep, this.paint);

		//this.paint.setTextSize(12);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.field) + ":",
				leftEdge + 75, yBegin + yStep, this.paint);
		int i4 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.field) + ":");
		paramCanvas.drawText(this.proInfo.getField().toString(), leftEdge + i4
				+ 75, yBegin + yStep, this.paint);



		if (!this.proInfo.getQrCode().toString().isEmpty()) {
			paramCanvas.save();

			paramCanvas
					.drawBitmap(
							generateBitmap(this.proInfo.getQrCode().toString(),
									80, 80), xqrCode, yqrCode, this.paint);
			// paramCanvas.drawBitmap(big(generateBitmap("深圳市联合智能卡有限公司",80,80)),
			// xqrCode, yqrCode, this.paint);

			paramCanvas.restore();
		}

		if (!this.proInfo.getEan13Code().toString().isEmpty()) {
			paramCanvas.save();

			paramCanvas.drawBitmap(drawBarcode(), xbarCode, ybarCode,
					this.paint);
			paramCanvas.restore();
		}

	
		
		
	}

	public void drawTagPicCh213_2_R(Canvas paramCanvas) {

		int leftEdge = 6;
		float yBegin = 38;
		float yStep = 21;// 36;

		//float xPriceFlag = leftEdge + 5;
		float xPriceFlag = leftEdge + 2;
		float yPriceFlag = yBegin + 2 * yStep + 16;


		paramCanvas.clipRect(new Rect(0, 0, 212, 104));

		paramCanvas.drawColor(Color.WHITE);
		paramCanvas.save();
		paramCanvas.clipRect(new Rect(0, 0, 212, 19));
		font = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/lishu.ttf");
		this.paint.setTypeface(font);
		this.paint.setFakeBoldText(true);
		this.paint.setColor(Color.BLACK);

		this.paint.setTextSize(16);

		paramCanvas.drawText(this.proInfo.getProductName().toString(),
				leftEdge, 17, this.paint);
		paramCanvas.restore();
		
		font = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/APARAJB.TTF");
		this.paint.setTypeface(font);
		this.paint.setColor(Color.BLACK);
		int priPos = this.proInfo.getPrice().toString().indexOf(".");
		int priLength = this.proInfo.getPrice().length();
		this.paint.setTextSize(49);// 133

		if (priPos != -1) {

			String priceL = "";
			String priceR = "";
			priceL = this.proInfo.getPrice().toString()
					.substring(0, priPos + 1).trim();
			priceR = this.proInfo.getPrice().toString()
					.substring(priPos + 1, priLength).trim();
			paramCanvas.drawText(priceL, xPriceFlag, yPriceFlag, this.paint);
			int i5 = (int) this.paint.measureText(priceL);
			this.paint.setTextSize(34);// 108
			paramCanvas.drawText(priceR, xPriceFlag + i5, yPriceFlag - 9,
					this.paint);

		}

		else {
			paramCanvas.drawText(this.proInfo.getPrice().toString(), leftEdge,
					yPriceFlag, this.paint);
		}

	}
	
	
	
	
	private void drawTagPicCh29(Canvas paramCanvas) {
		// (14 + 6);
		/*
		 * int i = 2 + 34; int j = 2 + 50; int k = 2 + 66; int m = 2 + 82; int n
		 * = 2 + 98;
		 */

		int leftEdge = 8;
		float yBegin = 44;
		float yStep = 21;// 36;

		float xPriceFlag = leftEdge + 21;
		float yPriceFlag = yBegin + 2 * yStep + 30;

		float yqrCode = 0;
		float xqrCode = leftEdge + 197;

		float ybarCode = 87;
		float xbarCode = leftEdge + 170;

		paramCanvas.clipRect(new Rect(0, 0, 296, 128));

		paramCanvas.drawColor(Color.WHITE);
		paramCanvas.save();
		paramCanvas.clipRect(new Rect(0, 0, 296, 26));
//		font = Typeface.createFromAsset(getContext().getAssets(),
//				"fonts/kaiti.ttf");
//		this.paint.setTypeface(font);
		this.paint.setFakeBoldText(true);
		this.paint.setColor(Color.BLACK);
		// this.paint.setFakeBoldText(true);
		this.paint.setTextSize(20);
		// paramCanvas.drawColor(Color.GRAY);
		// int i1 = (int)(400 -
		// this.paint.measureText(this.proInfo.getProductName().toString())) /
		// 2;
		paramCanvas.drawText(this.proInfo.getProductName().toString(),
				leftEdge, 20, this.paint);
		paramCanvas.restore();
		// font = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
		// 将字体文件保存在assets/fonts/目录下，创建Typeface对象
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/Roboto-Regular.ttf");
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/simkai.ttf");
		// this.paint.setTypeface(font);
		// this.paint.setFakeBoldText(false);
		this.paint.setColor(Color.BLACK);
		this.paint.setTextSize(16);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.unit) + ":",
				leftEdge, yBegin, this.paint);
		int i1 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.unit) + ":");
		// font = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
		// this.paint.setTypeface(font);
		this.paint.setTextSize(15);
		paramCanvas.drawText(this.proInfo.getUnit().toString(), leftEdge + i1,
				yBegin, this.paint);
		this.paint.setTextSize(16);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.spec) + ":",
				leftEdge + 110, yBegin, this.paint);
		int i2 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.spec) + ":");

		
		paramCanvas.drawText(this.proInfo.getSpecification().toString(),
				leftEdge + i2 + 110, yBegin, this.paint);
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/kaiti.ttf");
		// this.paint.setTypeface(font);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.grade) + ":",
				leftEdge, yBegin + yStep, this.paint);
		int i3 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.grade) + ":");
		// font = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
		// this.paint.setTypeface(font);
		this.paint.setTextSize(15);
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/simfang.ttf");
		// this.paint.setTypeface(font);
		paramCanvas.drawText(this.proInfo.getGrade().toString(), leftEdge + i3,
				yBegin + yStep, this.paint);

		this.paint.setTextSize(16);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.field) + ":",
				leftEdge + 110, yBegin + yStep, this.paint);
		int i4 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.field) + ":");
		// font = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
		// this.paint.setTypeface(font);
		// this.paint.setTextSize(15);
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/simfang.ttf");
		// this.paint.setTypeface(font);
		paramCanvas.drawText(this.proInfo.getField().toString(), leftEdge + i4
				+ 110, yBegin + yStep, this.paint);

		//
		// paramCanvas.save();
		// paramCanvas.clipRect(new Rect(leftEdge, (int)(yBegin+yStep), 240,
		// 112));
		font = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/APARAJB.TTF");
		this.paint.setTypeface(font);
		// this.paint.setTextSize(133);
		// this.paint.setFakeBoldText(true);
		// paramCanvas.drawText("99.", leftEdge+80, yPriceFlag, this.paint);
		// this.paint.setFakeBoldText(false);
		int priPos = this.proInfo.getPrice().toString().indexOf(".");
		int priLength = this.proInfo.getPrice().length();
		this.paint.setTextSize(55);// 133
		// this.paint.setTextSize(70);//133
		if (priPos != -1) 
		{
			// this.paint.setTextSize(133);//31
			// font = Typeface.create(Typeface.SANS_SERIF,
			// Typeface.BOLD_ITALIC);
			// this.paint.setTypeface(font);
            
			String priceL = "";
			String priceR = "";
			priceL = this.proInfo.getPrice().toString()
					.substring(0, priPos + 1).trim();
			priceR = this.proInfo.getPrice().toString()
					.substring(priPos + 1, priLength).trim();
			
			int mesuPrice = (int) this.paint.measureText(this.proInfo.getPrice().toString());
			int i5;
            if(mesuPrice < 143)
            {
			 paramCanvas.drawText(priceL, xPriceFlag, yPriceFlag, this.paint);
			 i5 = (int) this.paint.measureText(priceL);
			 this.paint.setTextSize(45);// 108
			 paramCanvas.drawText(priceR, xPriceFlag + i5, yPriceFlag - 9,
					this.paint);
            }else{
             paramCanvas.drawText(priceL, leftEdge, yPriceFlag, this.paint);
   			 i5 = (int) this.paint.measureText(priceL);
   			 this.paint.setTextSize(45);// 108
   			 paramCanvas.drawText(priceR, leftEdge + i5, yPriceFlag - 9,
   					this.paint);	
            	
            }
            

		}

		else {
			paramCanvas.drawText(this.proInfo.getPrice().toString(), leftEdge,
					yPriceFlag, this.paint);
		}

		if (!this.proInfo.getQrCode().toString().isEmpty()) {
			paramCanvas.save();

			paramCanvas
					.drawBitmap(
							generateBitmap(this.proInfo.getQrCode().toString(),
									90, 90), xqrCode, yqrCode, this.paint);
			// paramCanvas.drawBitmap(big(generateBitmap("深圳市联合智能卡有限公司",80,80)),
			// xqrCode, yqrCode, this.paint);

			paramCanvas.restore();
		}

		if (!this.proInfo.getEan13Code().toString().isEmpty()) {
			paramCanvas.save();


			// paramCanvas.drawBitmap(generateBitmap("深圳市联合智能卡有限公司",80,80),
			// xqrCode, yqrCode, this.paint);
			paramCanvas.drawBitmap(drawBarcode(), xbarCode, ybarCode,
					this.paint);
			paramCanvas.restore();
		}

	}

	public void drawTagPicCh29BWR(Canvas paramCanvas) {
		// (14 + 6);
		/*
		 * int i = 2 + 34; int j = 2 + 50; int k = 2 + 66; int m = 2 + 82; int n
		 * = 2 + 98;
		 */

		int leftEdge = 8;
		float yBegin = 44;
		float yStep = 21;// 36;

		float xPriceFlag = leftEdge + 13;
		float yPriceFlag = yBegin + 2 * yStep + 26;

		float yqrCode = 0;
		float xqrCode = leftEdge + 197;

		float ybarCode = 83;
		float xbarCode = leftEdge + 165;

		paramCanvas.clipRect(new Rect(0, 0, 296, 128));

		paramCanvas.drawColor(Color.WHITE);
		paramCanvas.save();
		paramCanvas.clipRect(new Rect(0, 0, 296, 24));
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/kaiti.ttf");
		font = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/lishu.ttf");
		this.paint.setTypeface(font);
		this.paint.setFakeBoldText(true);
		this.paint.setColor(0xFFB21F2E);
		// this.paint.setFakeBoldText(true);
		this.paint.setTextSize(20);
		// paramCanvas.drawColor(Color.GRAY);
		// int i1 = (int)(400 -
		// this.paint.measureText(this.proInfo.getProductName().toString())) /
		// 2;
		paramCanvas.drawText(this.proInfo.getProductName().toString(),
				leftEdge, 23, this.paint);
		paramCanvas.restore();
		// font = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
		// 将字体文件保存在assets/fonts/目录下，创建Typeface对象
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/Roboto-Regular.ttf");
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/simkai.ttf");
		// this.paint.setTypeface(font);
		// this.paint.setFakeBoldText(false);
		this.paint.setColor(Color.BLACK);
		this.paint.setTextSize(16);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.unit) + ":",
				leftEdge, yBegin, this.paint);
		int i1 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.unit) + ":");
		// font = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
		// this.paint.setTypeface(font);
		this.paint.setTextSize(15);
		paramCanvas.drawText(this.proInfo.getUnit().toString(), leftEdge + i1,
				yBegin, this.paint);
		this.paint.setTextSize(16);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.spec) + ":",
				leftEdge + 120, yBegin, this.paint);
		int i2 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.spec) + ":");
		// font = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
		// this.paint.setTypeface(font);
		// this.paint.setTextSize(18);
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/Roboto-Regular.ttf");
		// this.paint.setTypeface(font);
		paramCanvas.drawText(this.proInfo.getSpecification().toString(),
				leftEdge + i2 + 120, yBegin, this.paint);
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/kaiti.ttf");
		// this.paint.setTypeface(font);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.grade) + ":",
				leftEdge, yBegin + yStep, this.paint);
		int i3 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.grade) + ":");
		// font = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
		// this.paint.setTypeface(font);
		this.paint.setTextSize(15);
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/simfang.ttf");
		// this.paint.setTypeface(font);
		paramCanvas.drawText(this.proInfo.getGrade().toString(), leftEdge + i3,
				yBegin + yStep, this.paint);

		this.paint.setTextSize(16);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.field) + ":",
				leftEdge + 120, yBegin + yStep, this.paint);
		int i4 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.field) + ":");
		// font = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
		// this.paint.setTypeface(font);
		// this.paint.setTextSize(15);
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/simfang.ttf");
		// this.paint.setTypeface(font);
		paramCanvas.drawText(this.proInfo.getField().toString(), leftEdge + i4
				+ 120, yBegin + yStep, this.paint);

		//
		// paramCanvas.save();
		// paramCanvas.clipRect(new Rect(leftEdge, (int)(yBegin+yStep), 240,
		// 112));
		font = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/APARAJB.TTF");
		this.paint.setTypeface(font);
		this.paint.setColor(0xFFB21F2E);
		// this.paint.setTextSize(133);
		// this.paint.setFakeBoldText(true);
		// paramCanvas.drawText("99.", leftEdge+80, yPriceFlag, this.paint);
		// this.paint.setFakeBoldText(false);
		int priPos = this.proInfo.getPrice().toString().indexOf(".");
		int priLength = this.proInfo.getPrice().length();
		this.paint.setTextSize(55);// 133
		// this.paint.setTextSize(70);//133
		if (priPos != -1) {
			// this.paint.setTextSize(133);//31
			// font = Typeface.create(Typeface.SANS_SERIF,
			// Typeface.BOLD_ITALIC);
			// this.paint.setTypeface(font);

			String priceL = "";
			String priceR = "";
			priceL = this.proInfo.getPrice().toString()
					.substring(0, priPos + 1).trim();
			priceR = this.proInfo.getPrice().toString()
					.substring(priPos + 1, priLength).trim();
			paramCanvas.drawText(priceL, xPriceFlag, yPriceFlag, this.paint);
			int i5 = (int) this.paint.measureText(priceL);
			this.paint.setTextSize(45);// 108
			paramCanvas.drawText(priceR, xPriceFlag + i5, yPriceFlag - 9,
					this.paint);

		}

		else {
			paramCanvas.drawText(this.proInfo.getPrice().toString(), leftEdge,
					yPriceFlag, this.paint);
		}

		if (!this.proInfo.getQrCode().toString().isEmpty()) {
			paramCanvas.save();

			paramCanvas
					.drawBitmap(
							generateBitmap(this.proInfo.getQrCode().toString(),
									90, 90), xqrCode, yqrCode, this.paint);
			// paramCanvas.drawBitmap(big(generateBitmap("深圳市联合智能卡有限公司",80,80)),
			// xqrCode, yqrCode, this.paint);

			paramCanvas.restore();
		}

		if (!this.proInfo.getEan13Code().toString().isEmpty()) {
			paramCanvas.save();

			paramCanvas
					.drawBitmap(
							generateBitmap(this.proInfo.getQrCode().toString(),
									90, 90), xqrCode, yqrCode, this.paint);
			// paramCanvas.drawBitmap(generateBitmap("深圳市联合智能卡有限公司",80,80),
			// xqrCode, yqrCode, this.paint);
			paramCanvas.drawBitmap(drawBarcode(), xbarCode, ybarCode,
					this.paint);
			paramCanvas.restore();
		}

	}

	public void drawTagPicCh29_BW(Canvas paramCanvas) {

		// (14 + 6);
		/*
		 * int i = 2 + 34; int j = 2 + 50; int k = 2 + 66; int m = 2 + 82; int n
		 * = 2 + 98;
		 */

		int leftEdge = 8;
		float yBegin = 44;
		float yStep = 21;// 36;

		// float xPriceFlag = leftEdge + 10;
		// float yPriceFlag = yBegin + 2 * yStep + 22;

		float yqrCode = 0;
		float xqrCode = leftEdge + 197;

		float ybarCode = 83;
		float xbarCode = leftEdge + 165;

		paramCanvas.clipRect(new Rect(0, 0, 296, 128));

		paramCanvas.drawColor(Color.WHITE);
		// paramCanvas.save();
		// paramCanvas.clipRect(new Rect(0, 0, 296, 24));
		font = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/lishu.ttf");
		this.paint.setTypeface(font);
		this.paint.setFakeBoldText(true);
		// this.paint.setColor(Color.BLACK);
		// // this.paint.setFakeBoldText(true);
		// this.paint.setTextSize(20);
		// // paramCanvas.drawColor(Color.GRAY);
		// // int i1 = (int)(400 -
		// // this.paint.measureText(this.proInfo.getProductName().toString()))
		// /
		// // 2;
		// paramCanvas.drawText(this.proInfo.getProductName().toString(),
		// leftEdge, 23, this.paint);
		// paramCanvas.restore();
		// font = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
		// 将字体文件保存在assets/fonts/目录下，创建Typeface对象
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/Roboto-Regular.ttf");
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/simkai.ttf");
		// this.paint.setTypeface(font);
		// this.paint.setFakeBoldText(false);
		this.paint.setColor(Color.BLACK);
		this.paint.setTextSize(16);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.unit) + ":",
				leftEdge, yBegin, this.paint);
		int i1 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.unit) + ":");
		// font = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
		// this.paint.setTypeface(font);
		this.paint.setTextSize(15);
		paramCanvas.drawText(this.proInfo.getUnit().toString(), leftEdge + i1,
				yBegin, this.paint);
		this.paint.setTextSize(16);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.spec) + ":",
				leftEdge + 120, yBegin, this.paint);
		int i2 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.spec) + ":");
		// font = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
		// this.paint.setTypeface(font);
		// this.paint.setTextSize(18);
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/Roboto-Regular.ttf");
		// this.paint.setTypeface(font);
		paramCanvas.drawText(this.proInfo.getSpecification().toString(),
				leftEdge + i2 + 120, yBegin, this.paint);
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/kaiti.ttf");
		// this.paint.setTypeface(font);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.grade) + ":",
				leftEdge, yBegin + yStep, this.paint);
		int i3 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.grade) + ":");
		// font = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
		// this.paint.setTypeface(font);
		this.paint.setTextSize(15);
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/simfang.ttf");
		// this.paint.setTypeface(font);
		paramCanvas.drawText(this.proInfo.getGrade().toString(), leftEdge + i3,
				yBegin + yStep, this.paint);

		this.paint.setTextSize(16);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.field) + ":",
				leftEdge + 120, yBegin + yStep, this.paint);
		int i4 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.field) + ":");
		// font = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
		// this.paint.setTypeface(font);
		// this.paint.setTextSize(15);
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/simfang.ttf");
		// this.paint.setTypeface(font);
		paramCanvas.drawText(this.proInfo.getField().toString(), leftEdge + i4
				+ 120, yBegin + yStep, this.paint);

		//
		// paramCanvas.save();
		// paramCanvas.clipRect(new Rect(leftEdge, (int)(yBegin+yStep), 240,
		// 112));

		if (!this.proInfo.getQrCode().toString().isEmpty()) {
			paramCanvas.save();

			paramCanvas
					.drawBitmap(
							generateBitmap(this.proInfo.getQrCode().toString(),
									90, 90), xqrCode, yqrCode, this.paint);
			// paramCanvas.drawBitmap(big(generateBitmap("深圳市联合智能卡有限公司",80,80)),
			// xqrCode, yqrCode, this.paint);

			paramCanvas.restore();
		}

		if (!this.proInfo.getEan13Code().toString().isEmpty()) {
			paramCanvas.save();

			paramCanvas
					.drawBitmap(
							generateBitmap(this.proInfo.getQrCode().toString(),
									90, 90), xqrCode, yqrCode, this.paint);
			// paramCanvas.drawBitmap(generateBitmap("深圳市联合智能卡有限公司",80,80),
			// xqrCode, yqrCode, this.paint);
			paramCanvas.drawBitmap(drawBarcode(), xbarCode, ybarCode,
					this.paint);
			paramCanvas.restore();
		}

	}

	public void drawTagPicCh29_R(Canvas paramCanvas) {

		int leftEdge = 8;
		float yBegin = 44;
		float yStep = 21;// 36;

		float xPriceFlag = leftEdge + 13;
		// float yPriceFlag = yBegin + 2 * yStep + 22;
		float yPriceFlag = yBegin + 2 * yStep + 26;

		paramCanvas.clipRect(new Rect(0, 0, 296, 128));

		paramCanvas.drawColor(Color.WHITE);
		paramCanvas.save();
		paramCanvas.clipRect(new Rect(0, 0, 296, 24));
		font = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/lishu.ttf");
		this.paint.setTypeface(font);
		this.paint.setFakeBoldText(true);
		this.paint.setColor(Color.BLACK);
		// this.paint.setFakeBoldText(true);
		this.paint.setTextSize(20);
		// paramCanvas.drawColor(Color.GRAY);
		// int i1 = (int)(400 -
		// this.paint.measureText(this.proInfo.getProductName().toString())) /
		// 2;
		paramCanvas.drawText(this.proInfo.getProductName().toString(),
				leftEdge, 23, this.paint);
		paramCanvas.restore();

		font = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/APARAJB.TTF");
		this.paint.setTypeface(font);
		// this.paint.setTextSize(133);
		// this.paint.setFakeBoldText(true);
		// paramCanvas.drawText("99.", leftEdge+80, yPriceFlag, this.paint);
		// this.paint.setFakeBoldText(false);
		int priPos = this.proInfo.getPrice().toString().indexOf(".");
		int priLength = this.proInfo.getPrice().length();
		this.paint.setTextSize(55);// 133
		// this.paint.setTextSize(70);//133
		if (priPos != -1) {
			// this.paint.setTextSize(133);//31
			// font = Typeface.create(Typeface.SANS_SERIF,
			// Typeface.BOLD_ITALIC);
			// this.paint.setTypeface(font);

			String priceL = "";
			String priceR = "";
			priceL = this.proInfo.getPrice().toString()
					.substring(0, priPos + 1).trim();
			priceR = this.proInfo.getPrice().toString()
					.substring(priPos + 1, priLength).trim();
			paramCanvas.drawText(priceL, xPriceFlag, yPriceFlag, this.paint);
			int i5 = (int) this.paint.measureText(priceL);
			this.paint.setTextSize(45);// 108
			paramCanvas.drawText(priceR, xPriceFlag + i5, yPriceFlag - 9,
					this.paint);

		}

		else {
			paramCanvas.drawText(this.proInfo.getPrice().toString(), leftEdge,
					yPriceFlag, this.paint);
		}

	}

	
	
	
	
	
	private void drawTagPicCh42(Canvas paramCanvas)
	  {
	  //  (14 + 6);
	  /*  int i = 2 + 34;
	    int j = 2 + 50;
	    int k = 2 + 66;
	    int m = 2 + 82;
	    int n = 2 + 98;
	    */

	    
	    int leftEdge = 10;
	    float yBegin = 76;
	    float yStep = 34;//36;
	    
	    float xPriceFlag = leftEdge+80;
	    float yPriceFlag = yBegin + 3*yStep+110;
	    	    
	    float yqrCode = 40;
	    float xqrCode = leftEdge + 260;
	    
	    float ybarCode = 153;
	    float xbarCode = leftEdge + 260;
	   
		float lPriceSize = 0;
		float rPriceSize = 0;
	    
	  //  paramCanvas.setBitmap(bitmap);
	    paramCanvas.clipRect(new Rect(0, 0, 400, 400));
	   // paramCanvas.clipRect(new Rect(0, 0, 256, 122));
	    paramCanvas.drawColor(Color.WHITE);
	    
		if (!this.proInfo.getQrCode().toString().isEmpty()) {
			paramCanvas.save();

			paramCanvas
					.drawBitmap(
							generateBitmap(this.proInfo.getQrCode().toString(),
									111, 111), xqrCode, yqrCode, this.paint);
			// paramCanvas.drawBitmap(big(generateBitmap("深圳市联合智能卡有限公司",80,80)),
			// xqrCode, yqrCode, this.paint);

			paramCanvas.restore();
		}
	    
	    paramCanvas.save();
	    paramCanvas.clipRect(new Rect(0, 0, 400, 43));
	  //  paramCanvas.clipRect(new Rect(0, 0, 256, 23));
	//    setFont("fonts/SIMSUN.TTC", 20);
	   // font = Typeface.createFromFile("/system/fonts/simsun.ttc");
	//	Typeface face = Typeface.createFromAsset (getAssets() , "fonts/GODEX.TTF");  
	  //  this.paint.setTypeface(font);
	   // font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
	    //font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-BoldItalic.ttf");
	   // font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Bold.ttf");
	    font = Typeface.createFromAsset(getContext().getAssets(), "fonts/simfang.ttf");
	    this.paint.setTypeface(font);
	    this.paint.setFakeBoldText(true);
	    this.paint.setColor(Color.BLACK);
	   // this.paint.setFakeBoldText(true);
	    this.paint.setTextSize(35);
	   // paramCanvas.drawColor(Color.GRAY);
	   // int i1 = (int)(400 - this.paint.measureText(this.proInfo.getProductName().toString())) / 2;
	    paramCanvas.drawText(this.proInfo.getProductName().toString(), leftEdge, 38, this.paint);
	    paramCanvas.restore();
	    //font = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
    //将字体文件保存在assets/fonts/目录下，创建Typeface对象 
	    //font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
	    font = Typeface.createFromAsset(getContext().getAssets(), "fonts/simfang.ttf");
	    this.paint.setTypeface(font);
	   // this.paint.setFakeBoldText(false);
	    this.paint.setColor(Color.BLACK);
	    this.paint.setTextSize(25);
	    paramCanvas.drawText(this.context.getResources().getString(R.string.unit)+":", leftEdge+3, yBegin, this.paint);
	    int i1 = (int)this.paint.measureText(this.context.getResources().getString(R.string.unit)+":");
	   // font = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
	   // this.paint.setTypeface(font);
	    this.paint.setTextSize(23);
	    paramCanvas.drawText(this.proInfo.getUnit().toString(), leftEdge+i1+5+3, yBegin, this.paint);
	
	    this.paint.setTextSize(25);
	    paramCanvas.drawText(this.context.getResources().getString(R.string.field) + ":", leftEdge+3, yBegin+yStep, this.paint);
	    int i2 = (int)this.paint.measureText(this.context.getResources().getString(R.string.field) + ":");  
	    paramCanvas.drawText(this.proInfo.getField().toString(), leftEdge+i2+5+3, yBegin+yStep, this.paint);
	    
	    
	    paramCanvas.drawText(this.context.getResources().getString(R.string.spec) + ":", leftEdge+3, yBegin+yStep+yStep, this.paint);
	    int i3 = (int)this.paint.measureText(this.context.getResources().getString(R.string.field) + ":");  
	    paramCanvas.drawText(this.proInfo.getSpecification().toString(), leftEdge+i3+5+3, yBegin+yStep+yStep, this.paint);

	    paramCanvas.drawText(this.context.getResources().getString(R.string.grade) + ":", leftEdge+3, yBegin+yStep+yStep+yStep, this.paint);
	    int i4 = (int)this.paint.measureText(this.context.getResources().getString(R.string.grade) + ":");  
	    paramCanvas.drawText(this.proInfo.getGrade().toString(), leftEdge+i4+5+3, yBegin+yStep+yStep+yStep, this.paint); 
	    
	    font = Typeface.createFromAsset(getContext().getAssets(), "fonts/simfang.ttf");
	    this.paint.setTypeface(font); 
	    this.paint.setTextSize(82);
	    this.paint.setFakeBoldText(false);

	    paramCanvas.drawText("�??", leftEdge, yPriceFlag, this.paint);

	    
//		if (!this.proInfo.getQrCode().toString().isEmpty()) {
//			paramCanvas.save();
//
//			paramCanvas
//					.drawBitmap(
//							generateBitmap(this.proInfo.getQrCode().toString(),
//									111, 111), xqrCode, yqrCode, this.paint);
//			// paramCanvas.drawBitmap(big(generateBitmap("深圳市联合智能卡有限公司",80,80)),
//			// xqrCode, yqrCode, this.paint);
//
//			paramCanvas.restore();
//		}
	    
	    if (!this.proInfo.getEan13Code().toString().isEmpty())
	    {
	      paramCanvas.save();

	      //paramCanvas.drawBitmap(big(generateBitmap("深圳市联合智能卡有限公司",80,80)), xqrCode, yqrCode, this.paint);
	      paramCanvas.drawBitmap(drawBarcode(), xbarCode, ybarCode, this.paint); 
	      paramCanvas.restore();
	    }
	    
//////////////////////////////////////////////////// draw price///////////////////////////////
	    font = Typeface.createFromAsset(getContext().getAssets(), "fonts/APARAJB.TTF");
	    this.paint.setTypeface(font); 
	//    this.paint.setTextSize(133);
	    //this.paint.setFakeBoldText(true);
	   // paramCanvas.drawText("99.", leftEdge+80, yPriceFlag, this.paint);
	    
		int priPos = this.proInfo.getPrice().toString().indexOf(".");
		int priLength = this.proInfo.getPrice().length();

		// this.paint.setTextSize(70);//133
		if (priPos != -1) {
			if(priLength <= 6)
			{	
				lPriceSize = 140;
				rPriceSize = 113;
			}
			else if(priLength == 7)
			{	
				lPriceSize = 120;
				rPriceSize = 93;
			}
			else if(priLength == 8)
			{	
				lPriceSize = 110;
				rPriceSize = 83;
			}
			else
			{
				lPriceSize = 110 - 12*(priLength - 8);
				rPriceSize = 83 - 12*(priLength - 8);
				
			}	
			this.paint.setTextSize(lPriceSize);// 133
			// this.paint.setTextSize(133);//31
			// font = Typeface.create(Typeface.SANS_SERIF,
			// Typeface.BOLD_ITALIC);
			// this.paint.setTypeface(font);

			String priceL = "";
			String priceR = "";
			priceL = this.proInfo.getPrice().toString()
					.substring(0, priPos + 1).trim();
			priceR = this.proInfo.getPrice().toString()
					.substring(priPos + 1, priLength).trim();
			paramCanvas.drawText(priceL, xPriceFlag, yPriceFlag, this.paint);
			int i5 = (int) this.paint.measureText(priceL);
			this.paint.setTextSize(rPriceSize);// 108
			paramCanvas.drawText(priceR, xPriceFlag + i5, yPriceFlag - 18,
					this.paint);

		}
	    
	    else
	    {
			if(priLength <= 4)
			{	
				lPriceSize = 140;
			
			}
			else if(priLength <= 5)
			{	
				lPriceSize = 130;
			
			}
			else if(priLength == 6)
			{	
				lPriceSize = 110;
				
			}
			else if(priLength == 7)
			{	
				lPriceSize = 100;
			
			}
			else
			{
				lPriceSize = 100 - 8*(priLength - 7);
							
			}
	    	this.paint.setTextSize(lPriceSize);// 133
	    	paramCanvas.drawText(this.proInfo.getPrice().toString(), xPriceFlag, yPriceFlag, this.paint);
	    }
	    
	    
//////////////////////////////////////////////////////////////////////////////////////////////	    
	    
	    
	  }
	

	public void drawTagPicCh42BWR(Canvas paramCanvas)
	  {
	  //  (14 + 6);
	  /*  int i = 2 + 34;
	    int j = 2 + 50;
	    int k = 2 + 66;
	    int m = 2 + 82;
	    int n = 2 + 98;
	    */

	    
	    int leftEdge = 10;
	    float yBegin = 76;
	    float yStep = 34;//36;
	    
	    float xPriceFlag = leftEdge+80;
	    float yPriceFlag = yBegin + 3*yStep+110;
	    	    
	    float yqrCode = 40;
	    float xqrCode = leftEdge + 260;
	    
	    float ybarCode = 153;
	    float xbarCode = leftEdge + 260;
	   
		float lPriceSize = 0;
		float rPriceSize = 0;
	    
	  //  paramCanvas.setBitmap(bitmap);
	    paramCanvas.clipRect(new Rect(0, 0, 400, 400));
	   // paramCanvas.clipRect(new Rect(0, 0, 256, 122));
	    paramCanvas.drawColor(Color.WHITE);
	    
	    
		if (!this.proInfo.getQrCode().toString().isEmpty()) {
			paramCanvas.save();

			paramCanvas
					.drawBitmap(
							generateBitmap(this.proInfo.getQrCode().toString(),
									111, 111), xqrCode, yqrCode, this.paint);
			// paramCanvas.drawBitmap(big(generateBitmap("深圳市联合智能卡有限公司",80,80)),
			// xqrCode, yqrCode, this.paint);

			paramCanvas.restore();
		}  
	    
	    
	    
	    paramCanvas.save();
	    paramCanvas.clipRect(new Rect(0, 0, 400, 43));
	  //  paramCanvas.clipRect(new Rect(0, 0, 256, 23));
	//    setFont("fonts/SIMSUN.TTC", 20);
	   // font = Typeface.createFromFile("/system/fonts/simsun.ttc");
	//	Typeface face = Typeface.createFromAsset (getAssets() , "fonts/GODEX.TTF");  
	  //  this.paint.setTypeface(font);
	   // font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
	    //font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-BoldItalic.ttf");
	   // font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Bold.ttf");
	    font = Typeface.createFromAsset(getContext().getAssets(), "fonts/simfang.ttf");
	    this.paint.setTypeface(font);
	    this.paint.setFakeBoldText(true);
	    //this.paint.setColor(Color.BLACK);
	    this.paint.setColor(0xFFB21F2E);
	   // this.paint.setFakeBoldText(true);
	    this.paint.setTextSize(35);
	   // paramCanvas.drawColor(Color.GRAY);
	   // int i1 = (int)(400 - this.paint.measureText(this.proInfo.getProductName().toString())) / 2;
	    paramCanvas.drawText(this.proInfo.getProductName().toString(), leftEdge, 38, this.paint);
	    paramCanvas.restore();
	    //font = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
  //将字体文件保存在assets/fonts/目录下，创建Typeface对象 
	    //font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
	    font = Typeface.createFromAsset(getContext().getAssets(), "fonts/simfang.ttf");
	    this.paint.setTypeface(font);
	   // this.paint.setFakeBoldText(false);
	    this.paint.setColor(Color.BLACK);
	    this.paint.setTextSize(25);
	    paramCanvas.drawText(this.context.getResources().getString(R.string.unit)+":", leftEdge+3, yBegin, this.paint);
	    int i1 = (int)this.paint.measureText(this.context.getResources().getString(R.string.unit)+":");
	   // font = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
	   // this.paint.setTypeface(font);
	    this.paint.setTextSize(23);
	    paramCanvas.drawText(this.proInfo.getUnit().toString(), leftEdge+i1+5+3, yBegin, this.paint);
	
	    this.paint.setTextSize(25);
	    paramCanvas.drawText(this.context.getResources().getString(R.string.field) + ":", leftEdge+3, yBegin+yStep, this.paint);
	    int i2 = (int)this.paint.measureText(this.context.getResources().getString(R.string.field) + ":");  
	    paramCanvas.drawText(this.proInfo.getField().toString(), leftEdge+i2+5+3, yBegin+yStep, this.paint);
	    
	    
	    paramCanvas.drawText(this.context.getResources().getString(R.string.spec) + ":", leftEdge+3, yBegin+yStep+yStep, this.paint);
	    int i3 = (int)this.paint.measureText(this.context.getResources().getString(R.string.field) + ":");  
	    paramCanvas.drawText(this.proInfo.getSpecification().toString(), leftEdge+i3+5+3, yBegin+yStep+yStep, this.paint);

	    paramCanvas.drawText(this.context.getResources().getString(R.string.grade) + ":", leftEdge+3, yBegin+yStep+yStep+yStep, this.paint);
	    int i4 = (int)this.paint.measureText(this.context.getResources().getString(R.string.grade) + ":");  
	    paramCanvas.drawText(this.proInfo.getGrade().toString(), leftEdge+i4+5+3, yBegin+yStep+yStep+yStep, this.paint); 
	    
	    font = Typeface.createFromAsset(getContext().getAssets(), "fonts/simfang.ttf");
	    this.paint.setTypeface(font); 
	    this.paint.setTextSize(82);
	    this.paint.setFakeBoldText(false);

	    paramCanvas.drawText("�??", leftEdge, yPriceFlag, this.paint);

	    
//		if (!this.proInfo.getQrCode().toString().isEmpty()) {
//			paramCanvas.save();
//
//			paramCanvas
//					.drawBitmap(
//							generateBitmap(this.proInfo.getQrCode().toString(),
//									111, 111), xqrCode, yqrCode, this.paint);
//			// paramCanvas.drawBitmap(big(generateBitmap("深圳市联合智能卡有限公司",80,80)),
//			// xqrCode, yqrCode, this.paint);
//
//			paramCanvas.restore();
//		}
	    
	    if (!this.proInfo.getEan13Code().toString().isEmpty())
	    {
	      paramCanvas.save();

	      //paramCanvas.drawBitmap(big(generateBitmap("深圳市联合智能卡有限公司",80,80)), xqrCode, yqrCode, this.paint);
	      paramCanvas.drawBitmap(drawBarcode(), xbarCode, ybarCode, this.paint); 
	      paramCanvas.restore();
	    }
	    
////////////////////////////////////////////////////draw price///////////////////////////////
	    font = Typeface.createFromAsset(getContext().getAssets(), "fonts/APARAJB.TTF");
	    this.paint.setColor(0xFFB21F2E);
	    this.paint.setTypeface(font); 
	//    this.paint.setTextSize(133);
	    //this.paint.setFakeBoldText(true);
	   // paramCanvas.drawText("99.", leftEdge+80, yPriceFlag, this.paint);
	    
		int priPos = this.proInfo.getPrice().toString().indexOf(".");
		int priLength = this.proInfo.getPrice().length();

		// this.paint.setTextSize(70);//133
		if (priPos != -1) {
			if(priLength <= 6)
			{	
				lPriceSize = 140;
				rPriceSize = 113;
			}
			else if(priLength == 7)
			{	
				lPriceSize = 120;
				rPriceSize = 93;
			}
			else if(priLength == 8)
			{	
				lPriceSize = 110;
				rPriceSize = 83;
			}
			else
			{
				lPriceSize = 110 - 12*(priLength - 8);
				rPriceSize = 83 - 12*(priLength - 8);
				
			}	
			this.paint.setTextSize(lPriceSize);// 133
			// this.paint.setTextSize(133);//31
			// font = Typeface.create(Typeface.SANS_SERIF,
			// Typeface.BOLD_ITALIC);
			// this.paint.setTypeface(font);

			String priceL = "";
			String priceR = "";
			priceL = this.proInfo.getPrice().toString()
					.substring(0, priPos + 1).trim();
			priceR = this.proInfo.getPrice().toString()
					.substring(priPos + 1, priLength).trim();
			paramCanvas.drawText(priceL, xPriceFlag, yPriceFlag, this.paint);
			int i5 = (int) this.paint.measureText(priceL);
			this.paint.setTextSize(rPriceSize);// 108
			paramCanvas.drawText(priceR, xPriceFlag + i5, yPriceFlag - 18,
					this.paint);

		}
	    
	    else
	    {
			if(priLength <= 4)
			{	
				lPriceSize = 140;
			
			}
			else if(priLength <= 5)
			{	
				lPriceSize = 130;
			
			}
			else if(priLength == 6)
			{	
				lPriceSize = 110;
				
			}
			else if(priLength == 7)
			{	
				lPriceSize = 100;
			
			}
			else
			{
				lPriceSize = 100 - 8*(priLength - 7);
							
			}
	    	this.paint.setTextSize(lPriceSize);// 133
	    	paramCanvas.drawText(this.proInfo.getPrice().toString(), xPriceFlag, yPriceFlag, this.paint);
	    }
	    
	    
//////////////////////////////////////////////////////////////////////////////////////////////	    
	    
	    
	  }
	
	
	public void drawTagPicCh42_BW(Canvas paramCanvas)
	  {
	  //  (14 + 6);
	  /*  int i = 2 + 34;
	    int j = 2 + 50;
	    int k = 2 + 66;
	    int m = 2 + 82;
	    int n = 2 + 98;
	    */

	    
	    int leftEdge = 10;
	    float yBegin = 76;
	    float yStep = 34;//36;
	    
	    float xPriceFlag = leftEdge+80;
	    float yPriceFlag = yBegin + 3*yStep+110;
	    	    
	    float yqrCode = 40;
	    float xqrCode = leftEdge + 260;
	    
	    float ybarCode = 153;
	    float xbarCode = leftEdge + 260;
	   
		float lPriceSize = 0;
		float rPriceSize = 0;
	    
	  //  paramCanvas.setBitmap(bitmap);
	    paramCanvas.clipRect(new Rect(0, 0, 400, 400));
	   // paramCanvas.clipRect(new Rect(0, 0, 256, 122));
	    paramCanvas.drawColor(Color.WHITE);
//	    paramCanvas.save();
//	    paramCanvas.clipRect(new Rect(0, 0, 400, 40));
//	  //  paramCanvas.clipRect(new Rect(0, 0, 256, 23));
//	//    setFont("fonts/SIMSUN.TTC", 20);
//	   // font = Typeface.createFromFile("/system/fonts/simsun.ttc");
//	//	Typeface face = Typeface.createFromAsset (getAssets() , "fonts/GODEX.TTF");  
//	  //  this.paint.setTypeface(font);
//	   // font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
//	    //font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-BoldItalic.ttf");
//	   // font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Bold.ttf");
//	    font = Typeface.createFromAsset(getContext().getAssets(), "fonts/simfang.ttf");
//	    this.paint.setTypeface(font);
//	    this.paint.setFakeBoldText(true);
//	    //this.paint.setColor(Color.BLACK);
//	    this.paint.setColor(0xFFB21F2E);
//	   // this.paint.setFakeBoldText(true);
//	    this.paint.setTextSize(35);
//	   // paramCanvas.drawColor(Color.GRAY);
//	   // int i1 = (int)(400 - this.paint.measureText(this.proInfo.getProductName().toString())) / 2;
//	    paramCanvas.drawText(this.proInfo.getProductName().toString(), leftEdge, 38, this.paint);
//	    paramCanvas.restore();
	    //font = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
//将字体文件保存在assets/fonts/目录下，创建Typeface对象 
	    //font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
	    font = Typeface.createFromAsset(getContext().getAssets(), "fonts/simfang.ttf");
	    this.paint.setTypeface(font);
	   // this.paint.setFakeBoldText(false);
	    this.paint.setColor(Color.BLACK);
	    this.paint.setTextSize(25);
	    paramCanvas.drawText(this.context.getResources().getString(R.string.unit)+":", leftEdge+3, yBegin, this.paint);
	    int i1 = (int)this.paint.measureText(this.context.getResources().getString(R.string.unit)+":");
	   // font = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
	   // this.paint.setTypeface(font);
	    this.paint.setTextSize(23);
	    paramCanvas.drawText(this.proInfo.getUnit().toString(), leftEdge+i1+5+3, yBegin, this.paint);
	
	    this.paint.setTextSize(25);
	    paramCanvas.drawText(this.context.getResources().getString(R.string.field) + ":", leftEdge+3, yBegin+yStep, this.paint);
	    int i2 = (int)this.paint.measureText(this.context.getResources().getString(R.string.field) + ":");  
	    paramCanvas.drawText(this.proInfo.getField().toString(), leftEdge+i2+5+3, yBegin+yStep, this.paint);
	    
	    
	    paramCanvas.drawText(this.context.getResources().getString(R.string.spec) + ":", leftEdge+3, yBegin+yStep+yStep, this.paint);
	    int i3 = (int)this.paint.measureText(this.context.getResources().getString(R.string.field) + ":");  
	    paramCanvas.drawText(this.proInfo.getSpecification().toString(), leftEdge+i3+5+3, yBegin+yStep+yStep, this.paint);

	    paramCanvas.drawText(this.context.getResources().getString(R.string.grade) + ":", leftEdge+3, yBegin+yStep+yStep+yStep, this.paint);
	    int i4 = (int)this.paint.measureText(this.context.getResources().getString(R.string.grade) + ":");  
	    paramCanvas.drawText(this.proInfo.getGrade().toString(), leftEdge+i4+5+3, yBegin+yStep+yStep+yStep, this.paint); 
	    
	    font = Typeface.createFromAsset(getContext().getAssets(), "fonts/simfang.ttf");
	    this.paint.setTypeface(font); 
	    this.paint.setTextSize(82);
	    this.paint.setFakeBoldText(false);

	    paramCanvas.drawText("�??", leftEdge, yPriceFlag, this.paint);

	    
		if (!this.proInfo.getQrCode().toString().isEmpty()) {
			paramCanvas.save();

			paramCanvas
					.drawBitmap(
							generateBitmap(this.proInfo.getQrCode().toString(),
									111, 111), xqrCode, yqrCode, this.paint);
			// paramCanvas.drawBitmap(big(generateBitmap("深圳市联合智能卡有限公司",80,80)),
			// xqrCode, yqrCode, this.paint);

			paramCanvas.restore();
		}
	    
	    if (!this.proInfo.getEan13Code().toString().isEmpty())
	    {
	      paramCanvas.save();

	      //paramCanvas.drawBitmap(big(generateBitmap("深圳市联合智能卡有限公司",80,80)), xqrCode, yqrCode, this.paint);
	      paramCanvas.drawBitmap(drawBarcode(), xbarCode, ybarCode, this.paint); 
	      paramCanvas.restore();
	    }
	    
////////////////////////////////////////////////////draw price///////////////////////////////
//	    font = Typeface.createFromAsset(getContext().getAssets(), "fonts/APARAJB.TTF");
//	    this.paint.setColor(0xFFB21F2E);
//	    this.paint.setTypeface(font); 
//	//    this.paint.setTextSize(133);
//	    //this.paint.setFakeBoldText(true);
//	   // paramCanvas.drawText("99.", leftEdge+80, yPriceFlag, this.paint);
//	    
//		int priPos = this.proInfo.getPrice().toString().indexOf(".");
//		int priLength = this.proInfo.getPrice().length();
//
//		// this.paint.setTextSize(70);//133
//		if (priPos != -1) {
//			if(priLength <= 6)
//			{	
//				lPriceSize = 140;
//				rPriceSize = 113;
//			}
//			else if(priLength == 7)
//			{	
//				lPriceSize = 120;
//				rPriceSize = 93;
//			}
//			else if(priLength == 8)
//			{	
//				lPriceSize = 110;
//				rPriceSize = 83;
//			}
//			else
//			{
//				lPriceSize = 110 - 12*(priLength - 8);
//				rPriceSize = 83 - 12*(priLength - 8);
//				
//			}	
//			this.paint.setTextSize(lPriceSize);// 133
//			// this.paint.setTextSize(133);//31
//			// font = Typeface.create(Typeface.SANS_SERIF,
//			// Typeface.BOLD_ITALIC);
//			// this.paint.setTypeface(font);
//
//			String priceL = "";
//			String priceR = "";
//			priceL = this.proInfo.getPrice().toString()
//					.substring(0, priPos + 1).trim();
//			priceR = this.proInfo.getPrice().toString()
//					.substring(priPos + 1, priLength).trim();
//			paramCanvas.drawText(priceL, xPriceFlag, yPriceFlag, this.paint);
//			int i5 = (int) this.paint.measureText(priceL);
//			this.paint.setTextSize(rPriceSize);// 108
//			paramCanvas.drawText(priceR, xPriceFlag + i5, yPriceFlag - 18,
//					this.paint);
//
//		}
//	    
//	    else
//	    {
//			if(priLength <= 4)
//			{	
//				lPriceSize = 140;
//			
//			}
//			else if(priLength <= 5)
//			{	
//				lPriceSize = 130;
//			
//			}
//			else if(priLength == 6)
//			{	
//				lPriceSize = 110;
//				
//			}
//			else if(priLength == 7)
//			{	
//				lPriceSize = 100;
//			
//			}
//			else
//			{
//				lPriceSize = 100 - 8*(priLength - 7);
//							
//			}
//	    	this.paint.setTextSize(lPriceSize);// 133
//	    	paramCanvas.drawText(this.proInfo.getPrice().toString(), xPriceFlag, yPriceFlag, this.paint);
//	    }
//	    
	    
//////////////////////////////////////////////////////////////////////////////////////////////	    
	    
	    
	  }
	
	
	
	public void drawTagPicCh42_R(Canvas paramCanvas)
	  {
	  //  (14 + 6);
	  /*  int i = 2 + 34;
	    int j = 2 + 50;
	    int k = 2 + 66;
	    int m = 2 + 82;
	    int n = 2 + 98;
	    */

	    
	    int leftEdge = 10;
	    float yBegin = 76;
	    float yStep = 34;//36;
	    
	    float xPriceFlag = leftEdge+80;
	    float yPriceFlag = yBegin + 3*yStep+110;
	    	    
	    float yqrCode = 40;
	    float xqrCode = leftEdge + 260;
	    
	    float ybarCode = 153;
	    float xbarCode = leftEdge + 260;
	   
		float lPriceSize = 0;
		float rPriceSize = 0;
	    
	  //  paramCanvas.setBitmap(bitmap);
	    paramCanvas.clipRect(new Rect(0, 0, 400, 400));
	   // paramCanvas.clipRect(new Rect(0, 0, 256, 122));
	    paramCanvas.drawColor(Color.WHITE);
	    paramCanvas.save();
	    paramCanvas.clipRect(new Rect(0, 0, 400, 43));
	  //  paramCanvas.clipRect(new Rect(0, 0, 256, 23));
	//    setFont("fonts/SIMSUN.TTC", 20);
	   // font = Typeface.createFromFile("/system/fonts/simsun.ttc");
	//	Typeface face = Typeface.createFromAsset (getAssets() , "fonts/GODEX.TTF");  
	  //  this.paint.setTypeface(font);
	   // font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
	    //font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-BoldItalic.ttf");
	   // font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Bold.ttf");
	    font = Typeface.createFromAsset(getContext().getAssets(), "fonts/simfang.ttf");
	    this.paint.setTypeface(font);
	    this.paint.setFakeBoldText(true);
	    this.paint.setColor(Color.BLACK);
	    //this.paint.setColor(0xFFB21F2E);
	   // this.paint.setFakeBoldText(true);
	    this.paint.setTextSize(35);
	   // paramCanvas.drawColor(Color.GRAY);
	   // int i1 = (int)(400 - this.paint.measureText(this.proInfo.getProductName().toString())) / 2;
	    paramCanvas.drawText(this.proInfo.getProductName().toString(), leftEdge, 38, this.paint);
	    paramCanvas.restore();


	    
////////////////////////////////////////////////////draw price///////////////////////////////
	    font = Typeface.createFromAsset(getContext().getAssets(), "fonts/APARAJB.TTF");
	    //this.paint.setColor(0xFFB21F2E);
	    this.paint.setTypeface(font); 
	//    this.paint.setTextSize(133);
	    //this.paint.setFakeBoldText(true);
	   // paramCanvas.drawText("99.", leftEdge+80, yPriceFlag, this.paint);
	    
		int priPos = this.proInfo.getPrice().toString().indexOf(".");
		int priLength = this.proInfo.getPrice().length();

		// this.paint.setTextSize(70);//133
		if (priPos != -1) {
			if(priLength <= 6)
			{	
				lPriceSize = 140;
				rPriceSize = 113;
			}
			else if(priLength == 7)
			{	
				lPriceSize = 120;
				rPriceSize = 93;
			}
			else if(priLength == 8)
			{	
				lPriceSize = 110;
				rPriceSize = 83;
			}
			else
			{
				lPriceSize = 110 - 12*(priLength - 8);
				rPriceSize = 83 - 12*(priLength - 8);
				
			}	
			this.paint.setTextSize(lPriceSize);// 133
			// this.paint.setTextSize(133);//31
			// font = Typeface.create(Typeface.SANS_SERIF,
			// Typeface.BOLD_ITALIC);
			// this.paint.setTypeface(font);

			String priceL = "";
			String priceR = "";
			priceL = this.proInfo.getPrice().toString()
					.substring(0, priPos + 1).trim();
			priceR = this.proInfo.getPrice().toString()
					.substring(priPos + 1, priLength).trim();
			paramCanvas.drawText(priceL, xPriceFlag, yPriceFlag, this.paint);
			int i5 = (int) this.paint.measureText(priceL);
			this.paint.setTextSize(rPriceSize);// 108
			paramCanvas.drawText(priceR, xPriceFlag + i5, yPriceFlag - 18,
					this.paint);

		}
	    
	    else
	    {
			if(priLength <= 4)
			{	
				lPriceSize = 140;
			
			}
			else if(priLength <= 5)
			{	
				lPriceSize = 130;
			
			}
			else if(priLength == 6)
			{	
				lPriceSize = 110;
				
			}
			else if(priLength == 7)
			{	
				lPriceSize = 100;
			
			}
			else
			{
				lPriceSize = 100 - 8*(priLength - 7);
							
			}
	    	this.paint.setTextSize(lPriceSize);// 133
	    	paramCanvas.drawText(this.proInfo.getPrice().toString(), xPriceFlag, yPriceFlag, this.paint);
	    }
	    
	    
//////////////////////////////////////////////////////////////////////////////////////////////	    
	    
	    
	  }
	
	
	private void drawTagPicCh75(Canvas paramCanvas) {
		// (14 + 6);
		/*
		 * int i = 2 + 34; int j = 2 + 50; int k = 2 + 66; int m = 2 + 82; int n
		 * = 2 + 98;
		 */

		int leftEdge = 8 + 20;
		float yBegin = 132;
		float yStep = 83;// 36;

		float xPriceFlag = leftEdge + 10;
		float yPriceFlag = (float)(yBegin + (float)2.7 * yStep);

		float yqrCode = 70;
		float xqrCode = leftEdge + 365;

		float ybarCode = yqrCode + 220;
		float xbarCode = leftEdge + 365;

		paramCanvas.clipRect(new Rect(0, 0, 640, 384));

		paramCanvas.drawColor(Color.WHITE);
		
		
		
		if (!this.proInfo.getQrCode().toString().isEmpty()) {
			paramCanvas.save();

			paramCanvas
					.drawBitmap(
							generateBitmap(this.proInfo.getQrCode().toString(),
									250, 250), xqrCode, yqrCode, this.paint);
			// paramCanvas.drawBitmap(big(generateBitmap("深圳市联合智能卡有限公司",80,80)),
			// xqrCode, yqrCode, this.paint);

			paramCanvas.restore();
		}
		
		
		
		
//		paramCanvas.save();
//		paramCanvas.clipRect(new Rect(0, 0, 640, 24));
		font = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/simfang.ttf");
		this.paint.setTypeface(font);
		this.paint.setFakeBoldText(true);
		this.paint.setColor(Color.BLACK);
		// this.paint.setFakeBoldText(true);
		this.paint.setTextSize(60);

		paramCanvas.drawText(this.proInfo.getProductName().toString(),
				leftEdge, 80, this.paint);

		this.paint.setColor(Color.BLACK);
		this.paint.setTextSize((float) 35.0);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.unit) + ":",
				leftEdge, yBegin, this.paint);
		int i1 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.unit) + ":");
		// font = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
		// this.paint.setTypeface(font);
	
		paramCanvas.drawText(this.proInfo.getUnit().toString(), leftEdge + i1,
				yBegin, this.paint);
		//this.paint.setTextSize(35);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.spec) + ":",
				leftEdge + 190, yBegin, this.paint);
		int i2 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.spec) + ":");
		// font = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
		// this.paint.setTypeface(font);
		// this.paint.setTextSize(18);
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/Roboto-Regular.ttf");
		// this.paint.setTypeface(font);
		paramCanvas.drawText(this.proInfo.getSpecification().toString(),
				leftEdge + i2 + 190, yBegin, this.paint);
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/kaiti.ttf");
		// this.paint.setTypeface(font);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.grade) + ":",
				leftEdge, yBegin + yStep, this.paint);
		int i3 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.grade) + ":");
		// font = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
		// this.paint.setTypeface(font);
		//this.paint.setTextSize(35);
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/simfang.ttf");
		// this.paint.setTypeface(font);
		paramCanvas.drawText(this.proInfo.getGrade().toString(), leftEdge + i3,
				yBegin + yStep, this.paint);

		//this.paint.setTextSize(35);
		paramCanvas.drawText(
				this.context.getResources().getString(R.string.field) + ":",
				leftEdge + 190, yBegin + yStep, this.paint);
		int i4 = (int) this.paint.measureText(this.context.getResources()
				.getString(R.string.field) + ":");
		// font = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
		// this.paint.setTypeface(font);
		// this.paint.setTextSize(15);
		// font = Typeface.createFromAsset(getContext().getAssets(),
		// "fonts/simfang.ttf");
		// this.paint.setTypeface(font);
		paramCanvas.drawText(this.proInfo.getField().toString(), leftEdge + i4
				+ 190, yBegin + yStep, this.paint);

		//
		// paramCanvas.save();
		// paramCanvas.clipRect(new Rect(leftEdge, (int)(yBegin+yStep), 240,
		// 112));
		font = Typeface.createFromAsset(getContext().getAssets(),
				"fonts/APARAJB.TTF");
		this.paint.setTypeface(font);
		// this.paint.setTextSize(133);
		// this.paint.setFakeBoldText(true);
		// paramCanvas.drawText("99.", leftEdge+80, yPriceFlag, this.paint);
		// this.paint.setFakeBoldText(false);
		int priPos = this.proInfo.getPrice().toString().indexOf(".");
		int priLength = this.proInfo.getPrice().length();
		this.paint.setTextSize(165);// 133
		// this.paint.setTextSize(70);//133
		if (priPos != -1) {
			// this.paint.setTextSize(133);//31
			// font = Typeface.create(Typeface.SANS_SERIF,
			// Typeface.BOLD_ITALIC);
			// this.paint.setTypeface(font);

			String priceL = "";
			String priceR = "";
			priceL = this.proInfo.getPrice().toString()
					.substring(0, priPos + 1).trim();
			priceR = this.proInfo.getPrice().toString()
					.substring(priPos + 1, priLength).trim();
			paramCanvas.drawText(priceL, xPriceFlag, yPriceFlag, this.paint);
			int i5 = (int) this.paint.measureText(priceL);
			this.paint.setTextSize(120);// 108
			paramCanvas.drawText(priceR, xPriceFlag + i5, yPriceFlag - 30,
					this.paint);

		}

		else {
			paramCanvas.drawText(this.proInfo.getPrice().toString(), leftEdge,
					yPriceFlag, this.paint);
		}

//		if (!this.proInfo.getQrCode().toString().isEmpty()) {
//			paramCanvas.save();
//
//			paramCanvas
//					.drawBitmap(
//							generateBitmap(this.proInfo.getQrCode().toString(),
//									250, 250), xqrCode, yqrCode, this.paint);
//			// paramCanvas.drawBitmap(big(generateBitmap("深圳市联合智能卡有限公司",80,80)),
//			// xqrCode, yqrCode, this.paint);
//
//			paramCanvas.restore();
//		}

		if (!this.proInfo.getEan13Code().toString().isEmpty()) {
			paramCanvas.save();


			// paramCanvas.drawBitmap(generateBitmap("深圳市联合智能卡有限公司",80,80),
			// xqrCode, yqrCode, this.paint);
			paramCanvas.drawBitmap(drawBarcode75(), xbarCode, ybarCode,
					this.paint);
			paramCanvas.restore();
		}

	}

	private Bitmap generateBitmap(String content, int width, int height) {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		Map<EncodeHintType, String> hints = new HashMap<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		try {
			BitMatrix encode = qrCodeWriter.encode(content,
					BarcodeFormat.QR_CODE, width, height, hints);
			int[] pixels = new int[width * height];
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					if (encode.get(j, i)) {
						pixels[i * width + j] = 0x00000000;
					} else {
						pixels[i * width + j] = 0xffffffff;
					}
				}
			}
			return Bitmap.createBitmap(pixels, 0, width, width, height,
					Bitmap.Config.RGB_565);
		} catch (WriterException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
		if (this.inch == 0x21)
			drawTagPicCh213(canvas);
		else if (this.inch == 0x29)
			drawTagPicCh29(canvas);
		else if (this.inch == 0x42)
			drawTagPicCh42(canvas);
		else if (this.inch == 0x75)
			drawTagPicCh75(canvas);
		super.draw(canvas);
	}

}
