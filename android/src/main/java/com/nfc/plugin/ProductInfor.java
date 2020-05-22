/***********************************************************
Copyright(C), USC company
FileName	: ProductInfor.java
Author		: Guoyuankai
Date	  	: 2016/12/02
Description	: 
Version		: V1.0
History		: 
--------------------------------
2016/12/02: created
***********************************************************/

package com.nfc.plugin;

public class ProductInfor {

	String productName;    		//Ʒ��
	String unit;            	//��λ
	String specification; 		//���?
	String grade; 				//�ȼ�
	String field;               //����
	String price;               //�۸�
	String ean13Code;          //��ά��
	String qrCode;
	String lcdInch;	
	

	public ProductInfor(String productName, String unit, String specification,
			String grade, String field, String price,String ean13Code, String qrCode,String lcdInch) {
		super();
		this.productName = productName;
		this.unit = unit;
		this.specification = specification;
		this.grade = grade;
		this.field = field;
		this.price = price;
		this.ean13Code = ean13Code;
		this.qrCode = qrCode;
		this.lcdInch = lcdInch;
	}


	public String getQrCode() {
		return qrCode;
	}


	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}


	public String getPrice() {
		return price;
	}


	public void setPrice(String price) {
		this.price = price;
	}


	public String getProductName() {
		return productName;
	}


	public void setProductName(String productName) {
		this.productName = productName;
	}


	public String getUnit() {
		return unit;
	}


	public void setUnit(String unit) {
		this.unit = unit;
	}


	public String getSpecification() {
		return specification;
	}


	public void setSpecification(String specification) {
		this.specification = specification;
	}


	public String getGrade() {
		return grade;
	}


	public void setGrade(String grade) {
		this.grade = grade;
	}


	public String getField() {
		return field;
	}


	public void setField(String field) {
		this.field = field;
	}



	public String getEan13Code() {
		return ean13Code;
	}

	public void setEan13Code(String ean13Code) {
		this.ean13Code = ean13Code;
	}


	public String getLcdInch() {
		return lcdInch;
	}


	public void setLcdInch(String lcdInch) {
		this.lcdInch = lcdInch;
	}


	@Override
	public String toString() {
		return "ProductInfor [productName=" + productName + ", unit=" + unit
				+ ", specification=" + specification + ", grade=" + grade
				+ ", field=" + field + ", price=" + price + ", ean13Code="
				+ ean13Code + ", qrCode=" + qrCode + ", lcdInch=" + lcdInch
				+ "]";
	}



	
}
