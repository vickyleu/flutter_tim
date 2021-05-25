//
//  AdvancedMsgListener.swift
//  tencent_im_sdk_plugin
//
//  Created by 林智 on 2020/12/18.
//

import Foundation
import ImSDK_Smart
import Hydra

class AdvancedMsgListener: NSObject, V2TIMAdvancedMsgListener {
	/// 新消息通知
	public func onRecvNewMessage(_ msg: V2TIMMessage!) {
		async({
			_ -> Int in
			TencentImSDKPlugin.invokeListener(type: ListenerType.onRecvNewMessage, method: "advancedMsgListener", data: try await(V2MessageEntity.init(message: msg!).getDictAll()))
			
			return 1
		}).then({
			value in
		})
		
	}
	
	/// C2C已读回执
	public func onRecvC2CReadReceipt(_ receiptList: [V2TIMMessageReceipt]!) {
		var data: [[String: Any]] = [];
		for item in receiptList {
			data.append(V2MessageReceiptEntity.getDict(info: item));
		}
		TencentImSDKPlugin.invokeListener(type: ListenerType.onRecvC2CReadReceipt, method: "advancedMsgListener", data: data)
	}
	
	/// 消息撤回
	public func onRecvMessageRevoked(_ msgID: String!) {
		TencentImSDKPlugin.invokeListener(type: ListenerType.onRecvMessageRevoked, method: "advancedMsgListener", data: msgID)
	}
	
}
