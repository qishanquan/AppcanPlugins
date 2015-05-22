package com.baofoo;

import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;

import android.app.ActivityGroup;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.RelativeLayout;

public class UexBaofoo extends EUExBase {


	public UexBaofoo(Context context, EBrowserView view) {
		super(context, view);
	}

	// 用于打开一个由一个Activity单独控制的窗口。
	@SuppressWarnings("deprecation")
	public void toPayActivity(String[] params) {
		final String MemberID = params[0];	
		final String TerminalID = params[1];	
		final String TradeDate = params[2];	
		final String TransId = params[3];	
		final String OrderMoney = params[4];
		final String PayId = params[5];	
		final String NoticeType = params[6];	
		final String CommodityName = params[7];	
		final String UserName = params[8];	
		final String AdditionalInfo = params[9];	
		final String PageUrl = params[10];	
		final String ReturnUrl = params[11];	
		final String Signature = params[12];	
		final String BaofooPayurl = params[13];	
		
		((ActivityGroup) mContext).runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(mContext, MainActivity.class);
				intent.putExtra("OrderMoney", OrderMoney);
				intent.putExtra("MemberID", MemberID);
				intent.putExtra("TerminalID", TerminalID);
				intent.putExtra("TradeDate", TradeDate);
				intent.putExtra("TransId", TransId);
				intent.putExtra("PayId", PayId);
				intent.putExtra("NoticeType", NoticeType);
				intent.putExtra("CommodityName", CommodityName);
				intent.putExtra("UserName", UserName);
				intent.putExtra("AdditionalInfo", AdditionalInfo);
				intent.putExtra("PageUrl", PageUrl);
				intent.putExtra("ReturnUrl", ReturnUrl);
				intent.putExtra("Signature", Signature);
				intent.putExtra("BaofooPayurl", BaofooPayurl);
				
				LocalActivityManager mgr = ((ActivityGroup) mContext).getLocalActivityManager();
				mgr.removeAllActivities();
				Window window = mgr.startActivity("mainAct", intent);
				View mMapDecorView = window.getDecorView();
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				lp.leftMargin = 0;
				lp.topMargin = 0;
				addViewToCurrentWindow(mMapDecorView, lp);
			}
		});
	}


	// clean something
	@Override
	protected boolean clean() {
		return false;
	}

}
