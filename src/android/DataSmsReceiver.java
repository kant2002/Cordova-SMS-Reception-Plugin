/*
Copyright (C) 2013 by Pierre-Yves Orban
Copyright (C) 2014 by Neeraj Tuteja
%

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/

package com.akurdyumov.cordova.smsinbox;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;
import org.apache.cordova.LOG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class DataSmsReceiver extends BroadcastReceiver {
	private static final String SMS_RECEIVED = "android.intent.action.DATA_SMS_RECEIVED";
	private CallbackContext callback_receive;
	private boolean isReceiving = true;

	@Override
	public void onReceive(final Context context, final Intent intent) {
		// Retrieves a map of extended data from the intent.

		if (intent != null && SMS_RECEIVED.equals(intent.getAction())) {
			final Bundle bundle = intent.getExtras();

			try {
				if (bundle != null) {
					final Object[] pdusObj = (Object[]) bundle.get("pdus");

					for (int i = 0; i < pdusObj.length; i++) {
						SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
						String senderNum = currentMessage.getDisplayOriginatingAddress();
						String message = currentMessage.getDisplayMessageBody();
						LOG.d("SmsReceiver", "senderNum: " + senderNum
							  + "; message: " + message);
					}
				}
			} catch (Exception e) {
				LOG.e("SmsReceiver", "Exception smsReceiver" + e);
			}
		}
	}
	
	public void broadcast(boolean v) {
		this.broadcast = v;
	}
	
	public void startReceiving(CallbackContext ctx) {
		this.callback_receive = ctx;
		this.isReceiving = true;
	}

	public void stopReceiving() {
		this.callback_receive = null;
		this.isReceiving = false;
	}
}
