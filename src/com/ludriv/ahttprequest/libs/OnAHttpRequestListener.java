package com.ludriv.ahttprequest.libs;

public interface OnAHttpRequestListener
{
	public void onAHttpRequestSuccess(AHttpRequest request, String response);
	public void onAHttpRequestFail(AHttpRequest request, int code);
}
