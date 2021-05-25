//
//  SDKManager.swift
//  tencent_im_sdk_plugin
//
//  Created by 林智 on 2020/12/24.
//

import Foundation
import   ImSDK_Smart

class SDKManager {
	var channel: FlutterMethodChannel
	
	private var sdkListenr = SDKListener();
	
	private var conversationListener = ConversationListener();
	
	private var friendshipListener = FriendshipListener();
	
	private var groupListener = GroupListener();
	
	private var simpleMsgListener = SimpleMsgListener();
	
	private var advancedMsgListener = AdvancedMsgListener();
	
	private var apnsListener = APNSListener();
	
	private var signalingListener = SignalingListener();
	
	init(channel: FlutterMethodChannel) {
		self.channel = channel
	}

	/**
	* 登录
	*/
	public func login(call: FlutterMethodCall, result: @escaping FlutterResult) {
		if let userID = CommonUtils.getParam(call: call, result: result, param: "userID") as? String,
		   let userSig = CommonUtils.getParam(call: call, result: result, param: "userSig") as? String  {
			// 登录操作
			V2TIMManager.sharedInstance().login(
				userID,
				userSig: userSig,
				succ: {
					// self.invokeListener(type: ListenerType.onConnecting, params: nil);
					CommonUtils.resultSuccess(call: call, result: result, data: "login success");
				},
				fail: TencentImUtils.returnErrorClosures(call: call, result: result)
			)
		}
	}
	
	/**
	* 登出
	*/
	public func logout(call: FlutterMethodCall, result: @escaping FlutterResult) {
		V2TIMManager.sharedInstance().logout({
			CommonUtils.resultSuccess(call: call, result: result, data: "logout success");
		}, fail: TencentImUtils.returnErrorClosures(call: call, result: result));
	}

	/// 获得登录状态
    public func getLoginStatus(call: FlutterMethodCall, result: @escaping FlutterResult) {
		CommonUtils.resultSuccess(call: call, result: result, data: V2TIMManager.sharedInstance().getLoginStatus().rawValue);
    }
	
	/**
	* 初始化本地存储
	*/
	public func initStorage(call: FlutterMethodCall, result: @escaping FlutterResult) {
		// if let identifier = CommonUtils.getParam(call: call, result: result, param: "identifier") as? String {
		//     V2TIMManager.sharedInstance().initStorage(identifier, succ: {
		//         result("777");
		//     }, fail: TencentImUtils.returnErrorClosures(call: call, result: result));
		// }
	}
	
	/**
	* 获取版本号
	*/
	public func getVersion(call: FlutterMethodCall, result: @escaping FlutterResult) {
		CommonUtils.resultSuccess(call: call, result: result, data: V2TIMManager.sharedInstance().getVersion());
	}
	
	/**
	* 获取服务器时间
	*/
	public func getServerTime(call: FlutterMethodCall, result: @escaping FlutterResult) {
		CommonUtils.resultSuccess(call: call, result: result, data: V2TIMManager.sharedInstance().getServerTime());
	}
	
	/**
	* 获取登录用户
	*/
	public func getLoginUser(call: FlutterMethodCall, result: @escaping FlutterResult) {
		CommonUtils.resultSuccess(call: call, result: result, data: V2TIMManager.sharedInstance().getLoginUser());
	}
	
	/**
	* 获取版本号
	*/
	public func unInitSDK(call: FlutterMethodCall, result: @escaping FlutterResult) {
		CommonUtils.resultSuccess(call: call, result: result);
	}
	
	
	
	/**
	* 初始化腾讯云IM，TODO：config需要配置更多信息
	*/
	public func `initSDK`(call: FlutterMethodCall, result: @escaping FlutterResult) {
		if let sdkAppID = CommonUtils.getParam(call: call, result: result, param: "sdkAppID") as? Int32,
		   let logLevel = CommonUtils.getParam(call: call, result: result, param: "logLevel") as? Int {
			let config = V2TIMSDKConfig()
			
			config.logLevel = V2TIMLogLevel(rawValue: logLevel)!
			V2TIMManager.sharedInstance().initSDK(sdkAppID, config: config, listener: sdkListenr)
			
			CommonUtils.resultSuccess(call: call, result: result);
		}
	}
	
	
	public func setConversationListener(call: FlutterMethodCall, result: @escaping FlutterResult) {
		V2TIMManager.sharedInstance().setConversationListener(conversationListener)
		CommonUtils.resultSuccess(call: call, result: result, data: "setConversationListener is done");
	}
	
	public func addAdvancedMsgListener(call: FlutterMethodCall, result: @escaping FlutterResult) {
		V2TIMManager.sharedInstance()?.removeAdvancedMsgListener(listener: advancedMsgListener)
		V2TIMManager.sharedInstance()?.addAdvancedMsgListener(listener: advancedMsgListener)
		CommonUtils.resultSuccess(call: call, result: result, data: "addAdvancedMsgListener is done");
	}

	public func removeAdvancedMsgListener(call: FlutterMethodCall, result: @escaping FlutterResult) {
		V2TIMManager.sharedInstance()?.removeAdvancedMsgListener(listener: advancedMsgListener)
		CommonUtils.resultSuccess(call: call, result: result, data: "removeAdvancedMsgListener is done");
	}
	
	public func setFriendListener(call: FlutterMethodCall, result: @escaping FlutterResult) {
		V2TIMManager.sharedInstance().setFriendListener(friendshipListener)
		CommonUtils.resultSuccess(call: call, result: result, data: "setFriendListener is done");
	}
	
	public func setGroupListener(call: FlutterMethodCall, result: @escaping FlutterResult) {
		V2TIMManager.sharedInstance().setGroupListener(groupListener)
		CommonUtils.resultSuccess(call: call, result: result, data: "setGroupListener is done");
	}
	
	public func setAPNSListener(call: FlutterMethodCall, result: @escaping FlutterResult) {
		V2TIMManager.sharedInstance().setAPNSListener(apnsListener)
		CommonUtils.resultSuccess(call: call, result: result, data: "setAPNSListener is done")
	}
	
	public func addSignalingListener(call: FlutterMethodCall, result: @escaping FlutterResult) {
		V2TIMManager.sharedInstance().removeSignalingListener(listener: signalingListener)
		V2TIMManager.sharedInstance().addSignalingListener(listener: signalingListener)
		CommonUtils.resultSuccess(call: call, result: result, data: "addSignalingListener is done");
	}
	
	public func removeSignalingListener(call: FlutterMethodCall, result: @escaping FlutterResult) {
		V2TIMManager.sharedInstance().removeSignalingListener(listener: signalingListener)
		CommonUtils.resultSuccess(call: call, result: result, data: "removeSignalingListener is done");
	}
	
	public func addSimpleMsgListener(call: FlutterMethodCall, result: @escaping FlutterResult) {
		V2TIMManager.sharedInstance()?.removeSimpleMsgListener(listener: simpleMsgListener)
		V2TIMManager.sharedInstance()?.addSimpleMsgListener(listener: simpleMsgListener)
		CommonUtils.resultSuccess(call: call, result: result, data: "addSimpleMsgListener is done");
	}

	public func removeSimpleMsgListener(call: FlutterMethodCall, result: @escaping FlutterResult) {
		V2TIMManager.sharedInstance()?.removeSimpleMsgListener(listener: simpleMsgListener)
		CommonUtils.resultSuccess(call: call, result: result, data: "removeSimpleMsgListener is done");
	}

	
	
	public func setAPNS(call: FlutterMethodCall, result: @escaping FlutterResult) {
		if let businessID = CommonUtils.getParam(call: call, result: result, param: "businessID") as? Int32,
			let token = CommonUtils.getParam(call: call, result: result, param: "token") as? String {
			let config = V2TIMAPNSConfig()
			
			config.token = token.hexadecimal()
			config.businessID = businessID
			V2TIMManager.sharedInstance().setAPNS(config, succ: {
				CommonUtils.resultSuccess(call: call, result: result);
			}, fail: TencentImUtils.returnErrorClosures(call: call, result: result));
		}
	}

	public func getUsersInfo(call: FlutterMethodCall, result: @escaping FlutterResult) { 
		let userIDList = CommonUtils.getParam(call: call, result: result, param: "userIDList") as! Array<String>;
		
		V2TIMManager.sharedInstance()?.getUsersInfo(userIDList, succ: {
			(array) -> Void in
			
			var res: [[String: Any]] = []
			
			for info in array! {
				let item = V2UserFullInfoEntity.getDict(info: info)
				res.append(item)
			}
			
			CommonUtils.resultSuccess(call: call, result: result, data: res);
			
		}, fail: TencentImUtils.returnErrorClosures(call: call, result: result));
	}

	public func setUnreadCount(call: FlutterMethodCall, result: @escaping FlutterResult) {
		if let unreadCount = CommonUtils.getParam(call: call, result: result, param: "unreadCount") as? UInt32 {
			APNSListener.count = unreadCount
		}
	}

	public func setSelfInfo(call: FlutterMethodCall, result: @escaping FlutterResult) {
		let nickName = CommonUtils.getParam(call: call, result: result, param: "nickName") as? String;
		let faceURL = CommonUtils.getParam(call: call, result: result, param: "faceUrl") as? String;
		let selfSignature = CommonUtils.getParam(call: call, result: result, param: "selfSignature") as? String;
		let gender = CommonUtils.getParam(call: call, result: result, param: "gender") as? Int;
		let allowType = CommonUtils.getParam(call: call, result: result, param: "allowType") as? Int;
		let customInfo = CommonUtils.getParam(call: call, result: result, param: "customInfo") as? Dictionary<String, String>;
		var customInfoData: [String: Data] = [:]
		
		var info = V2TIMUserFullInfo();
		if nickName != nil {
			info.nickName = nickName
		}
		if faceURL != nil {
			info.faceURL = faceURL
		}
		if selfSignature != nil {
			info.selfSignature = selfSignature
		}
		if gender != nil {
			info.gender = V2TIMGender(rawValue: gender!)!
		}
		if allowType != nil {
			info.allowType = V2TIMFriendAllowType(rawValue: allowType!)!
		}
		if customInfo != nil {
			for (key, value) in customInfo! {
				customInfoData[key] = value.data(using: String.Encoding.utf8, allowLossyConversion: true);
			}
			info.customInfo = customInfoData
		}
		
		V2TIMManager.sharedInstance()?.setSelfInfo(info, succ: {
			() -> Void in
			
			CommonUtils.resultSuccess(call: call, result: result)
		}, fail: TencentImUtils.returnErrorClosures(call: call, result: result));
	}
	
	func createGroupSimple(call: FlutterMethodCall, result: @escaping FlutterResult) {
		let groupID = CommonUtils.getParam(call: call, result: result, param: "groupID") as? String;
		let groupType = CommonUtils.getParam(call: call, result: result, param: "groupType") as? String;
		let groupName = CommonUtils.getParam(call: call, result: result, param: "groupName") as? String;
		
		let info = V2TIMGroupInfo();
		info.groupID = groupID;
		info.groupType = groupType as String?;
		info.groupName = groupName;
		
		V2TIMManager.sharedInstance()?.createGroup(groupType, groupID: groupID, groupName: groupName, succ: {
			(id) -> Void in
			
			CommonUtils.resultSuccess(call: call, result: result, data: id!)
		}, fail: TencentImUtils.returnErrorClosures(call: call, result: result));
	}
}


extension String {

	/// Create `Data` from hexadecimal string representation
	///
	/// This takes a hexadecimal representation and creates a `Data` object. Note, if the string has any spaces or non-hex characters (e.g. starts with '<' and with a '>'), those are ignored and only hex characters are processed.
	///
	/// - returns: Data represented by this hexadecimal string.

	func hexadecimal() -> Data? {
		var data = Data(capacity: count / 2)

		let regex = try! NSRegularExpression(pattern: "[0-9a-f]{1,2}", options: .caseInsensitive)
		regex.enumerateMatches(in: self, range: NSMakeRange(0, utf16.count)) { match, flags, stop in
			let byteString = (self as NSString).substring(with: match!.range)
			var num = UInt8(byteString, radix: 16)!
			data.append(&num, count: 1)
		}

		guard data.count > 0 else { return nil }

		return data
	}

}
