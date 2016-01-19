package org.teacherbucks.holder;

import org.teacherbucks.model.Voucher;

public class VoucherHolder {
	
public static Voucher voucher = new Voucher();
	
	public static Voucher getVoucher(){
		return voucher;
	}
	
	public static void setVoucher(Voucher voucher){
		VoucherHolder.voucher = voucher;
	}
	
	public static void removeVoucherData(){
		VoucherHolder.voucher = null;
	}

}
