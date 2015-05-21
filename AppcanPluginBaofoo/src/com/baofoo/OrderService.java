package com.baofoo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.unionpay.UPPayAssistEx;

@SuppressLint("SimpleDateFormat")
public class OrderService extends AsyncTask<Integer, Void, Boolean> {

	private MainActivity mainActivity; // 附属界面
	private AlertDialog dialog; // 全局对话框
	private String orderNo = null; // 保留请求序列号，此序列号与商户自身订单号无关，请注意
	
	private String baofoo_payurl = "";
	private String orderMoney;
	private String memberID = "";
	private String terminalID = "";
	private String tradeDate = "";
	private String transId = "";
	private String payId = "";
	private String noticeType = "";
	private String commodityName = "";
	private String userName = "";
	private String additionalInfo = "";
	private String pageUrl = "";
	private String returnUrl = "";
	private String signature = "";
	
	public OrderService(MainActivity mainActivity, Map<String, String> prams) {
		this.mainActivity = mainActivity;
		this.baofoo_payurl = getMapVal(prams, "BaofooPayurl");
		this.orderMoney = getMapVal(prams,"OrderMoney");
		this.memberID = getMapVal(prams,"MemberID");
		this.terminalID = getMapVal(prams,"TerminalID");
		this.tradeDate = getMapVal(prams,"TradeDate");
		this.transId = getMapVal(prams,"TransId");
		this.payId = getMapVal(prams,"PayId");
		this.noticeType = getMapVal(prams,"NoticeType");
		this.commodityName = getMapVal(prams,"CommodityName");
		this.userName = getMapVal(prams,"UserName");
		this.additionalInfo = getMapVal(prams,"AdditionalInfo");
		this.pageUrl = getMapVal(prams,"PageUrl");
		this.returnUrl = getMapVal(prams,"ReturnUrl");
		this.signature = getMapVal(prams,"Signature");
	}
	
	public String getMapVal(Map<String, String> prams, String key) {
		return prams.get(key)==null?"":prams.get(key);
	}
	
	@Override
	protected void onPreExecute() {
		// 处理前先弹出一个处理等待对话框
		dialog = new AlertDialog(mainActivity) {
		};
		dialog.setMessage("正在创建宝付支付订单");
		dialog.setCancelable(false);
		dialog.show();
	}

	@Override
	protected Boolean doInBackground(Integer... params) {
		try {
			getOrderNo();
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}

		return true;
	}

	@Override
	protected void onPostExecute(Boolean isErr) {
		// 处理结束后处理
		// 关闭全局对话框
		dialog.dismiss();
		// 判断处理是否出错
		if (isErr) {
			// 请求打开支付界面
			int ret = UPPayAssistEx.startPay(mainActivity, null, null, orderNo, "00");
			// mainActivity.getIntent().getExtras().putInt("fromId", 88888);
			// 如果支付插件未安装，则请求安装支付插件
			if (ret == UPPayAssistEx.PLUGIN_NOT_FOUND) {
				AlertDialog.Builder builer = new AlertDialog.Builder(mainActivity);
				builer.setTitle("安装提示");
				builer.setMessage("请先安装支付插件");

				builer.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (dialog instanceof AlertDialog) {
							UPPayAssistEx.installUPPayPlugin(mainActivity);
						}
					}
				});
				builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (dialog instanceof AlertDialog) {
						}
					}
				});

				AlertDialog adlg = builer.create();
				adlg.show();
			}
		} else {
			AlertDialog msgdialog = new AlertDialog(mainActivity) {
			};
			msgdialog.setMessage("创建订单失败");
			msgdialog.show();
		}
	}

	// 请求商户服务，获取订单交易流水号,本方法写于商户的服务器端,请求宝付接口，获取交易流水号
	private void getOrderNo() throws Exception {
		String url = baofoo_payurl;
		String params = "";

		params = getParams4();

		URL myURL = new URL(url);
		HttpURLConnection con = (HttpURLConnection) myURL.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setUseCaches(false);
		con.setConnectTimeout(30000);
		con.connect();

		DataOutputStream out = new DataOutputStream(con.getOutputStream());
		out.writeBytes(params);
		out.flush();
		out.close();

		BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

		String s = reader.readLine();
		reader.close();
		con.disconnect();

		orderNo = s.substring(s.indexOf("tradeNo=") + 8, s.indexOf("respCode") - 2);
	}

	private String getParams4() throws Exception {
		String keyType = "1";
		String CommodityAmount = "1";
		String interfaceVersion = "4.0";
		String money = orderMoney;
		String MemberID = memberID;
		String TerminalID = terminalID;
		String TradeDate = tradeDate;
		String TransId = transId;
		String PayId = payId;
		String NoticeType = noticeType;
		String CommodityName = URLEncoder.encode(commodityName, "utf-8"); // 需URL编码
		String UserName = URLEncoder.encode(userName, "utf-8"); // 需URL编码
		String AdditionalInfo = URLEncoder.encode(additionalInfo, "utf-8"); // 需URL编码
		String PageUrl = URLEncoder.encode(pageUrl, "utf-8"); // 页面通知地址 ,需URL编码
		String ReturnUrl = URLEncoder.encode(returnUrl, "utf-8"); // 服务器通知地址 ,需URL编码
		String Signature = signature;	//MD5签名

		StringBuilder s = new StringBuilder("");
		s.append("PayID=").append(PayId);
		s.append("&MemberID=").append(MemberID);
		s.append("&TerminalID=").append(TerminalID);
		s.append("&TradeDate=").append(TradeDate);
		s.append("&OrderMoney=").append(money);
		s.append("&TransId=").append(TransId);
		s.append("&ReturnUrl=").append(ReturnUrl);
		s.append("&PageUrl=").append(PageUrl);
		s.append("&KeyType=").append(keyType);
		s.append("&Signature=").append(Signature);
		s.append("&CommodityName=").append(CommodityName);
		s.append("&CommodityAmount=").append(CommodityAmount);
		s.append("&UserName=").append(UserName);
		s.append("&AdditionalInfo=").append(AdditionalInfo);
		s.append("&InterfaceVersion=").append(interfaceVersion);
		s.append("&noticeType=").append(NoticeType);
		
		return s.toString();
	}

	/**
	 * MD5加密函数
	 */
	public String md5(String str, String encode) throws Exception {
		if (str == null) {
			return null;
		}
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(str.getBytes(encode));
		byte[] digest = md5.digest();

		StringBuffer hexString = new StringBuffer();
		String strTemp;
		for (int i = 0; i < digest.length; i++) {
			strTemp = Integer.toHexString((digest[i] & 0x000000FF) | 0xFFFFFF00).substring(6);
			hexString.append(strTemp);
		}

		return hexString.toString();
	}
}
