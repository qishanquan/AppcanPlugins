package com.baofoo;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = this.getIntent().getExtras();
		String BaofooPayurl = (String) bundle.get("BaofooPayurl");
		String OrderMoney = (String) bundle.get("OrderMoney");
		String MemberID = (String) bundle.get("MemberID");
		String TerminalID = (String) bundle.get("TerminalID");
		String TradeDate = (String) bundle.get("TradeDate");
		String TransId = (String) bundle.get("TransId");
		String PayId = (String) bundle.get("PayId");
		String NoticeType = (String) bundle.get("NoticeType");
		String CommodityName = (String) bundle.get("CommodityName");
		String UserName = (String) bundle.get("UserName");
		String AdditionalInfo = (String) bundle.get("AdditionalInfo");
		String PageUrl = (String) bundle.get("PageUrl");
		String ReturnUrl = (String) bundle.get("ReturnUrl");
		String Signature = (String) bundle.get("Signature");

		Map<String, String> orderMap = new HashMap<String, String>();
		orderMap.put("BaofooPayurl", BaofooPayurl);
		orderMap.put("OrderMoney", OrderMoney);
		orderMap.put("MemberID", MemberID);
		orderMap.put("TerminalID", TerminalID);
		orderMap.put("TradeDate", TradeDate);
		orderMap.put("TransId", TransId);
		orderMap.put("PayId", PayId);
		orderMap.put("NoticeType", NoticeType);
		orderMap.put("CommodityName", CommodityName);
		orderMap.put("UserName", UserName);
		orderMap.put("AdditionalInfo", AdditionalInfo);
		orderMap.put("PageUrl", PageUrl);
		orderMap.put("ReturnUrl", ReturnUrl);
		orderMap.put("Signature", Signature);
		
		//setContentView(0);
		
		new OrderService(MainActivity.this, orderMap).execute();
		
//		
//		
//		final Button btnPaytest = (Button) this.findViewById(R.id.make_order_btn);
//		btnPaytest.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				
//				new OrderService(MainActivity.this).execute();
//				
//				/*String orderNo = "201401231045240032182";
//				
//				int ret = UPPayAssistEx.startPay(MainActivity.this, null, null, orderNo, "00");
//				// 如果支付插件未安装，则请求安装支付插件
//				if (ret == UPPayAssistEx.PLUGIN_NOT_FOUND) {
//					AlertDialog.Builder builer = new AlertDialog.Builder(
//							MainActivity.this);
//					builer.setTitle("安装提示");
//					builer.setMessage("请先安装支付插件");
//
//					builer.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							if (dialog instanceof AlertDialog) {
//								UPPayAssistEx.installUPPayPlugin(MainActivity.this);
//							}
//						}
//					});
//					builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							if (dialog instanceof AlertDialog) {
//							}
//						}
//					});
//
//					AlertDialog adlg = builer.create();
//					adlg.show();
//				}*/
//				
//			}
//		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 此方法用来监听支付插件返回结果
		if (data == null) {
			return;
		}

		String str = data.getExtras().getString("pay_result");
		String msg = "";
		if (str.equalsIgnoreCase("success")) {
			msg = "支付成功";
		} else if (str.equalsIgnoreCase("fail")) {
			msg = "支付失败";
		} else if (str.equalsIgnoreCase("cancel")) {
			msg = "支付已被取消";
		}
		AlertDialog dialog = new AlertDialog(this) {
		};
		dialog.setMessage(msg);
		dialog.show();
	}
}
