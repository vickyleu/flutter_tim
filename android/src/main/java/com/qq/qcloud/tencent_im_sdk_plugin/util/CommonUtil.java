package com.qq.qcloud.tencent_im_sdk_plugin.util;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMFaceElem;
import com.tencent.imsdk.v2.V2TIMFileElem;
import com.tencent.imsdk.v2.V2TIMFriendApplication;
import com.tencent.imsdk.v2.V2TIMFriendApplicationResult;
import com.tencent.imsdk.v2.V2TIMFriendCheckResult;
import com.tencent.imsdk.v2.V2TIMFriendGroup;
import com.tencent.imsdk.v2.V2TIMFriendInfo;
import com.tencent.imsdk.v2.V2TIMFriendInfoResult;
import com.tencent.imsdk.v2.V2TIMFriendOperationResult;
import com.tencent.imsdk.v2.V2TIMGroupApplication;
import com.tencent.imsdk.v2.V2TIMGroupApplicationResult;
import com.tencent.imsdk.v2.V2TIMGroupAtInfo;
import com.tencent.imsdk.v2.V2TIMGroupChangeInfo;
import com.tencent.imsdk.v2.V2TIMGroupInfo;
import com.tencent.imsdk.v2.V2TIMGroupInfoResult;
import com.tencent.imsdk.v2.V2TIMGroupMemberChangeInfo;
import com.tencent.imsdk.v2.V2TIMGroupMemberFullInfo;
import com.tencent.imsdk.v2.V2TIMGroupMemberInfo;
import com.tencent.imsdk.v2.V2TIMGroupMemberInfoResult;
import com.tencent.imsdk.v2.V2TIMGroupMemberOperationResult;
import com.tencent.imsdk.v2.V2TIMGroupTipsElem;
import com.tencent.imsdk.v2.V2TIMImageElem;
import com.tencent.imsdk.v2.V2TIMLocationElem;
import com.tencent.imsdk.v2.V2TIMMergerElem;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMMessageReceipt;
import com.tencent.imsdk.v2.V2TIMOfflinePushInfo;
import com.tencent.imsdk.v2.V2TIMReceiveMessageOptInfo;
import com.tencent.imsdk.v2.V2TIMSoundElem;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.imsdk.v2.V2TIMUserInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.imsdk.v2.V2TIMVideoElem;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

/**
 * 工具类
 */
public class CommonUtil {
    /**
     * 主线程处理器
     */
    private final static Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    /**
     * 通用方法，获得参数值，如未找到参数，则直接中断
     *
     * @param methodCall 方法调用对象
     * @param result     返回对象
     * @param param      参数名
     */
    public static <T> T getParam(MethodCall methodCall, MethodChannel.Result result, String param) {
        T par = methodCall.argument(param);
//        if (par == null) {
//            result.error("Missing parameter", "Cannot find parameter `" + param + "` or `" + param + "` is null!", 5);
//            throw new RuntimeException("Cannot find parameter `" + param + "` or `" + param + "` is null!");
//        }
        return par;
    }

    /**
     * 运行主线程返回结果执行
     *
     * @param result 返回结果对象
     * @param param  返回参数
     */
    public static void runMainThreadReturn(final MethodChannel.Result result, final Object param) {
        MAIN_HANDLER.post(new Runnable() {
            @Override
            public void run() {
                result.success(param);
            }
        });
    }
    public static void returnError(final MethodChannel.Result result, int i,String s) {
        HashMap<String,Object> err = new HashMap<String,Object>();
        err.put("code",i);
        err.put("desc",s);
        result.success(err);
    }
    public static void returnError(final MethodChannel.Result result, int i,String s,HashMap<String,Object> data) {
        HashMap<String,Object> err = new HashMap<String,Object>();
        err.put("code",i);
        err.put("desc",s);
        err.put("data",data);
        result.success(err);
    }
    public static <T> void returnSuccess(final MethodChannel.Result result,T data) {
        HashMap<String,Object> succ = new HashMap<String,Object>();
        succ.put("code",0);
        succ.put("desc","ok");
        succ.put("data",data);
        result.success(succ);
    }
    public static <T> void emitEvent(MethodChannel channel,String method,String type,T data){
        HashMap<String,Object> res = new HashMap<String,Object>();
        res.put("type",type);
        res.put("data",data);
        channel.invokeMethod(method,res);
    }
    public static HashMap<String,Object> convertV2TIMUserFullInfoToMap(V2TIMUserFullInfo info){
        HashMap<String,Object> userInfo = new HashMap<String,Object>();
        userInfo.put("userID",info.getUserID());
        userInfo.put("nickName",info.getNickName());
        userInfo.put("faceUrl",info.getFaceUrl());
        userInfo.put("selfSignature",info.getSelfSignature());
        userInfo.put("gender",info.getGender());
        userInfo.put("allowType",info.getAllowType());
        HashMap<String,byte[]> customInfo = info.getCustomInfo();
        HashMap<String,String> customInfoTypeString = new HashMap<String, String>();
        Iterator it = customInfo.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry =(Map.Entry) it.next();
            String key = (String) entry.getKey();
            String value = (byte[])entry.getValue() ==null ? "": new String((byte[])entry.getValue());
            customInfoTypeString.put(key,value);
        }
        userInfo.put("customInfo",customInfoTypeString);
        userInfo.put("role",info.getRole());
        userInfo.put("level",info.getLevel());
        return userInfo;
    }
    public static HashMap<String,Object> convertV2TIMUserInfotoMap(V2TIMUserInfo userInfo){
        HashMap<String,Object> info = new HashMap<String,Object>();
        info.put("userID",userInfo.getUserID());
        info.put("nickName",userInfo.getNickName());
        info.put("faceUrl",userInfo.getFaceUrl());
        return info;
    }
    public static HashMap<String,Object> convertV2TIMGroupMemberInfoToMap(V2TIMGroupMemberInfo info){
        HashMap<String,Object> ginfo = new HashMap<String,Object>();
        ginfo.put("faceUrl",info.getFaceUrl());
        ginfo.put("friendRemark",info.getFriendRemark());
        ginfo.put("nameCard",info.getNameCard());
        ginfo.put("nickName",info.getNickName());
        ginfo.put("userID",info.getUserID());
        return  ginfo;
    }
    public static HashMap<String,Object> convertV2TIMMessageToMap(V2TIMMessage msg,Object ...progress){
        final HashMap<String,Object> message = new HashMap<String, Object>();
        message.put("elemType",msg.getElemType());
        message.put("msgID",msg.getMsgID());
        message.put("timestamp",msg.getTimestamp());

        if(progress.length == 0){
            message.put("progress",100);
        }else{
            message.put("progress",progress[0]);
        }
        message.put("sender",msg.getSender());
        message.put("nickName",msg.getNickName());
        message.put("friendRemark",msg.getFriendRemark());
        message.put("faceUrl",msg.getFaceUrl());
        message.put("nameCard",msg.getNameCard());
        message.put("groupID",msg.getGroupID());
        message.put("userID",msg.getUserID());
        message.put("status",msg.getStatus());
        message.put("localCustomData",msg.getLocalCustomData());
        message.put("localCustomInt",msg.getLocalCustomInt());
        message.put("cloudCustomData",msg.getCloudCustomData());
        message.put("isSelf",msg.isSelf());
        message.put("isRead",msg.isRead());
        message.put("isPeerRead",msg.isPeerRead());
        message.put("priority",msg.getPriority());
        message.put("seq",msg.getSeq());
        message.put("groupAtUserList",msg.getGroupAtUserList());
        message.put("random",msg.getRandom());
        message.put("isExcludedFromUnreadCount",msg.isExcludedFromUnreadCount());
        V2TIMOfflinePushInfo info =  msg.getOfflinePushInfo();

        HashMap<String,Object> offlinePushInfo = new HashMap<String, Object>();
        try{
            offlinePushInfo.put("desc",info.getDesc());
            offlinePushInfo.put("title",info.getTitle());
            offlinePushInfo.put("isDisablePush",info.isDisablePush());
        }catch (Exception e){
            
        }
        message.put("offlinePushInfo",offlinePushInfo);

        int type = msg.getElemType();
        if(type == V2TIMMessage.V2TIM_ELEM_TYPE_TEXT){
            HashMap<String,Object> textMap = new HashMap<String,Object>();
            textMap.put("text",msg.getTextElem().getText());
            message.put("textElem",textMap);
        }else if(type == V2TIMMessage.V2TIM_ELEM_TYPE_CUSTOM){
            HashMap<String,Object> customMap = new HashMap<String,Object>();
            customMap.put("data",msg.getCustomElem().getData()==null ? "" : new String(msg.getCustomElem().getData()));
            customMap.put("desc",msg.getCustomElem().getDescription());
            customMap.put("extension",msg.getCustomElem().getExtension()==null ? "" : new String(msg.getCustomElem().getExtension()));
            message.put("customElem",customMap);
        }else if(type == V2TIMMessage.V2TIM_ELEM_TYPE_IMAGE){
            HashMap<String,Object> img = new HashMap<String,Object>();
            img.put("path",msg.getImageElem().getPath());
            LinkedList<Object> imgList = new LinkedList<Object>();
            for (int i=0;i<msg.getImageElem().getImageList().size();i++){
                HashMap<String,Object> item = new HashMap<String,Object>();
                V2TIMImageElem.V2TIMImage imgInstance = msg.getImageElem().getImageList().get(i);
                item.put("size",imgInstance.getSize());
                item.put("height",imgInstance.getHeight());
                item.put("type",imgInstance.getType());
                item.put("url",imgInstance.getUrl());
                item.put("UUID",imgInstance.getUUID());
                item.put("width",imgInstance.getWidth());
                imgList.add(item);
            }
            img.put("imageList",imgList);
            message.put("imageElem",img);
        }else if(type == V2TIMMessage.V2TIM_ELEM_TYPE_SOUND){
            final V2TIMSoundElem soundElem = msg.getSoundElem();
            final HashMap<String,Object> sound = new HashMap<String,Object>();
            sound.put("dataSize",soundElem.getDataSize());
            sound.put("duration",soundElem.getDuration());
            sound.put("path",soundElem.getPath());
            sound.put("UUID",soundElem.getUUID());
            soundElem.getUrl(new V2TIMValueCallback<String>() {
                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onSuccess(String s) {
                    sound.put("url",s);
                }
            });
            message.put("soundElem",sound);
        }else if(type == V2TIMMessage.V2TIM_ELEM_TYPE_VIDEO){
            V2TIMVideoElem videoElem =  msg.getVideoElem ();
           final HashMap<String,Object> video = new HashMap<String,Object>();
            videoElem.getVideoUrl(new V2TIMValueCallback<String>() {
                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onSuccess(String s) {
                    video.put("videoUrl",s);
                }
            });
            videoElem.getSnapshotUrl(new V2TIMValueCallback<String>() {
                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onSuccess(String s) {
                    video.put("snapshotUrl",s);
                }
            });
            video.put("duration",videoElem.getDuration());
            video.put("snapshotHeight",videoElem.getSnapshotHeight());
            video.put("snapshotPath",videoElem.getSnapshotPath());
            video.put("snapshotSize",videoElem.getSnapshotSize());
            video.put("snapshotUUID",videoElem.getSnapshotUUID());
            video.put("snapshotWidth",videoElem.getSnapshotWidth());
            video.put("videoPath",videoElem.getVideoPath());
            video.put("videoSize",videoElem.getVideoSize());
            video.put("videoUUID",videoElem.getVideoUUID());
            message.put("videoElem",video);
        }else if(type == V2TIMMessage.V2TIM_ELEM_TYPE_FILE){
            final V2TIMFileElem fileElem = 	msg.getFileElem();
            final HashMap<String,Object> file = new HashMap<String,Object>();
            file.put("fileName",fileElem.getFileName());
            file.put("fileSize",fileElem.getFileSize());
            file.put("path",fileElem.getPath());
            file.put("UUID",fileElem.getUUID());
            fileElem.getUrl(new V2TIMValueCallback<String>() {
                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onSuccess(String s) {
                    file.put("url",s);
                }
            });
            message.put("fileElem",file);
        }else if(type == V2TIMMessage.V2TIM_ELEM_TYPE_FACE){
            V2TIMFaceElem faceElem = 	msg.getFaceElem();
            HashMap<String,Object> face = new HashMap<String,Object>();
            face.put("data",faceElem.getData() == null ? "" : new String(faceElem.getData()));
            face.put("index",faceElem.getIndex());
            message.put("faceElem",face);
        }else if(type == V2TIMMessage.V2TIM_ELEM_TYPE_LOCATION){
            V2TIMLocationElem locationElem 	 = msg.getLocationElem();
            HashMap<String,Object> location = new HashMap<String,Object>();
            location.put("desc",locationElem.getDesc());
            location.put("latitude",locationElem.getLatitude());
            location.put("longitude",locationElem.getLongitude());
            message.put("locationElem",location);
        }else if(type == V2TIMMessage.V2TIM_ELEM_TYPE_GROUP_TIPS) {
            V2TIMGroupTipsElem groupTipsElem = msg.getGroupTipsElem();

            HashMap<String,Object> groupTips = new HashMap<String,Object>();

            List<V2TIMGroupChangeInfo> groupChangeInfoList = groupTipsElem.getGroupChangeInfoList();
            LinkedList<HashMap<String,Object>> gchangeInfoList = new LinkedList<HashMap<String,Object>>();

            for (int i = 0; i < groupChangeInfoList.size(); i++) {

                String key = groupChangeInfoList.get(i).getKey();
                int types = groupChangeInfoList.get(i).getType();
                String value = groupChangeInfoList.get(i).getValue();
                HashMap<String,Object> item = new HashMap<String,Object>();
                item.put("key", key);
                item.put("type", types);
                item.put("value", value);
                gchangeInfoList.add(item);
            }

            groupTips.put("groupChangeInfoList", gchangeInfoList);

            groupTips.put("groupID", groupTipsElem.getGroupID());
            groupTips.put("memberCount", groupTipsElem.getMemberCount());
            groupTips.put("type", groupTipsElem.getType());

            List<V2TIMGroupMemberChangeInfo> mlist =  groupTipsElem.getMemberChangeInfoList();
            LinkedList<Object> memberChangeInfoList = new LinkedList<Object>();
            for(int i=0;i<mlist.size();i++){
                HashMap<String,Object> item = new HashMap<String,Object>();
                item.put("userID",mlist.get(i).getUserID());
                item.put("muteTime",mlist.get(i).getMuteTime());
                memberChangeInfoList.add(item);
            }
            groupTips.put("memberChangeInfoList", memberChangeInfoList);

            List<V2TIMGroupMemberInfo> memberlist =  groupTipsElem.getMemberList();
            LinkedList<Object> memberInfoList = new LinkedList<Object>();
            for(int i=0;i<memberlist.size();i++){
                HashMap<String,Object> item = new HashMap<String,Object>();
                item.put("userID",memberlist.get(i).getUserID());
                item.put("faceUrl",memberlist.get(i).getFaceUrl());
                item.put("friendRemark",memberlist.get(i).getFriendRemark());
                item.put("nameCard",memberlist.get(i).getNameCard());
                item.put("nickName",memberlist.get(i).getNickName());
                memberInfoList.add(item);
            }
            groupTips.put("memberList", memberInfoList);

            V2TIMGroupMemberInfo opmember =  groupTipsElem.getOpMember();
            HashMap<String,Object> opmemberItem = new HashMap<String,Object>();
            opmemberItem.put("userID",opmember.getUserID());
            opmemberItem.put("faceUrl",opmember.getFaceUrl());
            opmemberItem.put("friendRemark",opmember.getFriendRemark());
            opmemberItem.put("nameCard",opmember.getNameCard());
            opmemberItem.put("nickName",opmember.getNickName());

            groupTips.put("opMember",opmemberItem);

            message.put("groupTipsElem", groupTips);
        }else if(type == V2TIMMessage.V2TIM_ELEM_TYPE_MERGER){
            V2TIMMergerElem mergerElem 	 = msg.getMergerElem();
            HashMap<String,Object> elem = new HashMap<String,Object>();
            elem.put("isLayersOverLimit",mergerElem.isLayersOverLimit());
            elem.put("abstractList",mergerElem.getAbstractList());
            elem.put("title",mergerElem.getTitle());
            message.put("mergerElem",elem);
        }
        return message;
    }

    public static HashMap<String,Object> convertV2TIMGroupMemberChangeInfoToMap(V2TIMGroupMemberChangeInfo info){
        HashMap<String,Object> cinfo =  new HashMap<String,Object>();
        cinfo.put("muteTime",info.getMuteTime());
        cinfo.put("userID",info.getUserID());
        return cinfo;
    }

    public static HashMap<String,Object> convertV2TIMGroupChangeInfoToMap(V2TIMGroupChangeInfo info){
        HashMap<String,Object> rinfo = new HashMap<String,Object>();

        rinfo.put("value", info.getValue());
        rinfo.put("type", info.getType());
        rinfo.put("key", info.getKey());
        return rinfo;
    }
    public static HashMap<String,Object> convertV2TIMReceiveMessageOptInfoToMap(V2TIMReceiveMessageOptInfo info){
        HashMap<String,Object> rinfo = new HashMap<String,Object>();
        rinfo.put("c2CReceiveMessageOpt", info.getC2CReceiveMessageOpt());
        rinfo.put("userID", info.getUserID());
        return rinfo;
    }

    public static HashMap<String,Object> convertV2TIMFriendInfoToMap(V2TIMFriendInfo info){
        HashMap<String,Object> rinfo = new HashMap<String,Object>();
        List<String> ulists = info.getFriendGroups();
        rinfo.put("friendGroups",ulists);
        rinfo.put("friendRemark", info.getFriendRemark());
        rinfo.put("userID", info.getUserID());
        rinfo.put("userProfile", CommonUtil.convertV2TIMUserFullInfoToMap(info.getUserProfile()));
        HashMap<String,byte[]> customInfo = info.getFriendCustomInfo();
        HashMap<String,String> customInfoTypeString = new HashMap<String, String>();
        Iterator it = customInfo.entrySet().iterator();
        while (it.hasNext()) {
             Map.Entry entry =(Map.Entry) it.next();
             String key = (String) entry.getKey();
             String value = (byte[])entry.getValue() ==null ? "": new String((byte[])entry.getValue());
            customInfoTypeString.put(key,value);
        }
        rinfo.put("friendCustomInfo",customInfoTypeString);
        return rinfo;
    }
    public static HashMap<String,Object> convertV2TIMFriendInfoResultToMap(V2TIMFriendInfoResult info){
        HashMap<String,Object> rinfo = new HashMap<String,Object>();

        rinfo.put("friendInfo",CommonUtil.convertV2TIMFriendInfoToMap(info.getFriendInfo()));
        rinfo.put("relation", info.getRelation());
        rinfo.put("resultCode", info.getResultCode());
        rinfo.put("resultInfo", info.getResultInfo());
        return rinfo;
    }
    public static HashMap<String,Object> convertV2TIMFriendOperationResultToMap(V2TIMFriendOperationResult info){
        HashMap<String,Object> rinfo = new HashMap<String,Object>();

        rinfo.put("resultCode",info.getResultCode());
        rinfo.put("resultInfo",info.getResultInfo());
        rinfo.put("userID",info.getUserID());
        return rinfo;
    }
    public static HashMap<String,Object> convertV2TIMFriendCheckResultToMap(V2TIMFriendCheckResult info){
        HashMap<String,Object> rinfo = new HashMap<String,Object>();
        rinfo.put("resultType",info.getResultType());
        rinfo.put("resultCode",info.getResultCode());
        rinfo.put("resultInfo",info.getResultInfo());
        rinfo.put("userID",info.getUserID());
        return rinfo;
    }
    public static HashMap<String,Object> convertV2TIMFriendApplicationResultToMap(V2TIMFriendApplicationResult info){
        HashMap<String,Object> rinfo = new HashMap<String,Object>();
        LinkedList<Object> list  = new LinkedList<Object>();
        List<V2TIMFriendApplication> ulist =  info.getFriendApplicationList();
        for(int i = 0;i<ulist.size();i++){
            V2TIMFriendApplication item = ulist.get(i);
            HashMap<String,Object> data = new HashMap<String,Object>();
            data.put("addSource",item.getAddSource());
            data.put("addTime",item.getAddTime());
            data.put("addWording",item.getAddWording());
            data.put("faceUrl",item.getFaceUrl());
            data.put("nickname",item.getNickname());
            data.put("type",item.getType());
            data.put("userID",item.getUserID());
            list.add(data);
        }
        rinfo.put("friendApplicationList",list);
        rinfo.put("unreadCount",info.getUnreadCount());

        return rinfo;
    }
    public static HashMap<String,Object> convertV2TIMFriendGroupToMap(V2TIMFriendGroup info){
        HashMap<String,Object> rinfo = new HashMap<String,Object>();

        rinfo.put("friendCount",info.getFriendCount());
        rinfo.put("friendIDList",info.getFriendIDList());
        rinfo.put("name",info.getName());
        return rinfo;
    }
    public static HashMap<String,Object> convertV2TIMMessageReceiptToMap(V2TIMMessageReceipt info){
        HashMap<String,Object> rinfo = new HashMap<String,Object>();

        rinfo.put("timestamp",info.getTimestamp());
        rinfo.put("userID",info.getUserID());
        return rinfo;
    }
    public static HashMap<String,Object> convertV2TIMGroupMemberInfoResultToMap(V2TIMGroupMemberInfoResult info){
        HashMap<String,Object> rinfo = new HashMap<String,Object>();

        LinkedList<HashMap<String,Object>> list = new LinkedList<HashMap<String,Object>>();
        for(int i=0;i<info.getMemberInfoList().size();i++){
            list.add(CommonUtil.convertV2TIMGroupMemberFullInfoToMap(info.getMemberInfoList().get(i)));
        }
        rinfo.put("memberInfoList",list);
        rinfo.put("nextSeq",info.getNextSeq());

        return rinfo;
    }
    public static HashMap<String,Object> convertV2TIMGroupMemberFullInfoToMap(V2TIMGroupMemberFullInfo info){
        HashMap<String,Object> rinfo = new HashMap<String,Object>();
        rinfo.put("userID",info.getUserID ());
        rinfo.put("role",info.getRole ());
        rinfo.put("muteUntil",info.getMuteUntil ());
        rinfo.put("joinTime",info.getJoinTime ());
        rinfo.put("nickName",info.getNickName ());
        rinfo.put("nameCard",info.getNameCard ());
        rinfo.put("friendRemark",info.getFriendRemark ());
        rinfo.put("faceUrl",info.getFaceUrl ());
        Map<String,byte[]> custinfo =  info.getCustomInfo();
        HashMap<String,String> customInfoTypeString = new HashMap<String, String>();
        Iterator it = custinfo.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry =(Map.Entry) it.next();
            String key = (String) entry.getKey();
            String value = (byte[])entry.getValue() ==null ? "": new String((byte[])entry.getValue());
            customInfoTypeString.put(key,value);
        }
        rinfo.put("customInfo",customInfoTypeString);
        return rinfo;
    }
    public static HashMap<String,Object> convertV2TIMGroupMemberOperationResultToMap(V2TIMGroupMemberOperationResult info){
        HashMap<String,Object> rinfo = new HashMap<String,Object>();
        rinfo.put("memberID",info.getMemberID());
        rinfo.put("result",info.getResult());

        return rinfo;
    }
    public static HashMap<String,Object> convertV2TIMGroupApplicationResultToMap(V2TIMGroupApplicationResult info){
        HashMap<String,Object> rinfo = new HashMap<String,Object>();
        LinkedList<HashMap<String,Object>> list = new LinkedList<HashMap<String,Object>>();
        for(int i=0;i<info.getGroupApplicationList().size();i++){
            list.add(CommonUtil.convertV2TimGroupApplicationToMap(info.getGroupApplicationList().get(i)));
        }
        rinfo.put("groupApplicationList",list);
        rinfo.put("unreadCount",info.getUnreadCount());

        return rinfo;
    }
    public static HashMap<String,Object> convertV2TimGroupApplicationToMap(V2TIMGroupApplication info){
        HashMap<String,Object> rinfo = new HashMap<String,Object>();


        rinfo.put("addTime",info.getAddTime());
        rinfo.put("fromUser",info.getFromUser());
        rinfo.put("fromUserFaceUrl",info.getFromUserFaceUrl());
        rinfo.put("fromUserNickName",info.getFromUserNickName());
        rinfo.put("groupID",info.getGroupID());
        rinfo.put("handledMsg",info.getHandledMsg());
        rinfo.put("handleResult",info.getHandleResult());
        rinfo.put("handleStatus",info.getHandleStatus());
        rinfo.put("requestMsg",info.getRequestMsg());
        rinfo.put("toUser",info.getToUser());
        rinfo.put("type",info.getType());

        return rinfo;
    }

    public static HashMap<String,Object> convertV2TIMFriendApplicationToMap(V2TIMFriendApplication info){
        HashMap<String,Object> rinfo = new HashMap<String,Object>();
        rinfo.put("userID",info.getUserID());
        rinfo.put("type",info.getType());
        rinfo.put("nickname",info.getNickname());
        rinfo.put("faceUrl",info.getFaceUrl());
        rinfo.put("addWording",info.getAddWording());
        rinfo.put("addTime",info.getAddTime());
        rinfo.put("addSource",info.getAddSource());
        return rinfo;
    }
    public static HashMap<String,Object> convertV2TIMConversationToMap(V2TIMConversation info){
        HashMap<String,Object> rinfo = new HashMap<String,Object>();

        rinfo.put("conversationID",info.getConversationID());
        rinfo.put("draftText",info.getDraftText());
        rinfo.put("draftTimestamp",info.getDraftTimestamp());
        rinfo.put("faceUrl",info.getFaceUrl());
        rinfo.put("groupID",info.getGroupID());
        rinfo.put("groupType",info.getGroupType());
        rinfo.put("lastMessage",CommonUtil.convertV2TIMMessageToMap(info.getLastMessage()));
        rinfo.put("showName",info.getShowName());
        rinfo.put("type",info.getType());
        rinfo.put("unreadCount",info.getUnreadCount());
        rinfo.put("userID",info.getUserID());
        rinfo.put("isPinned",info.isPinned());
        rinfo.put("recvOpt",info.getRecvOpt());
        List<V2TIMGroupAtInfo> atList =  info.getGroupAtInfoList();
        List<Map<String,Object>> groupAtInfoList = new LinkedList<Map<String,Object>>();
        for(int i = 0;i<atList.size();i++){
            V2TIMGroupAtInfo item =   atList.get(i);
            Map<String,Object> itemMap = new HashMap<String,Object>();
            itemMap.put("atType",item.getAtType());
            itemMap.put("seq",item.getSeq());
            groupAtInfoList.add(itemMap);
        }
        rinfo.put("groupAtInfoList",groupAtInfoList);
        return rinfo;
    }
    public static HashMap<String,Object> convertV2TIMGroupInfoToMap(V2TIMGroupInfo info){
        HashMap<String,Object> rinfo = new HashMap<String,Object>();
        rinfo.put("createTime",info.getCreateTime());
        rinfo.put("faceUrl",info.getFaceUrl());
        rinfo.put("groupAddOpt",info.getGroupAddOpt());
        rinfo.put("groupID",info.getGroupID());
        rinfo.put("groupName",info.getGroupName());
        rinfo.put("groupType",info.getGroupType());
        rinfo.put("introduction",info.getIntroduction());
        rinfo.put("joinTime",info.getJoinTime());
        rinfo.put("lastInfoTime",info.getLastInfoTime());
        rinfo.put("lastMessageTime",info.getLastMessageTime());
        rinfo.put("memberCount",info.getMemberCount());
        rinfo.put("notification",info.getNotification());
        rinfo.put("onlineCount",info.getOnlineCount());
        rinfo.put("owner",info.getOwner());
        rinfo.put("recvOpt",info.getRecvOpt());
        rinfo.put("role",info.getRole());
        rinfo.put("isAllMuted",info.isAllMuted());
        Map<String,byte[]> customInfo = info.getCustomInfo();

        HashMap<String,String> customInfoTypeString = new HashMap<String, String>();
        Iterator it = customInfo.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry =(Map.Entry) it.next();
            String key = (String) entry.getKey();
            String value = (byte[])entry.getValue() ==null ? "": new String((byte[])entry.getValue());
            customInfoTypeString.put(key,value);
        }
        rinfo.put("customInfo",customInfoTypeString);
        return rinfo;
    }
    public static HashMap<String,Object> convertV2TIMGroupInfoResultToMap(V2TIMGroupInfoResult info){
        HashMap<String,Object> rinfo = new HashMap<String,Object>();
        rinfo.put("groupInfo",CommonUtil.convertV2TIMGroupInfoToMap(info.getGroupInfo()));
        rinfo.put("resultCode",info.getResultCode());
        rinfo.put("resultMessage",info.getResultMessage());
        return rinfo;
    }

    public static HashMap<String,Object> convertV2TIMConversationResultToMap(V2TIMConversationResult info){
        HashMap<String,Object> rinfo = new HashMap<String,Object>();
        rinfo.put("nextSeq",info.getNextSeq());
        rinfo.put("isFinished",info.isFinished());
        List<V2TIMConversation> list = info.getConversationList();
        LinkedList<Object> clist = new LinkedList<Object>();
        for(int i = 0;i<list.size();i++){
            V2TIMConversation item = list.get(i);
            HashMap<String,Object> citem  = CommonUtil.convertV2TIMConversationToMap(item);
            clist.add(citem);
        }
        rinfo.put("conversationList",clist);
        return rinfo;
    }


    /**
     * 运行主线程返回错误结果执行
     *
     * @param result       返回结果对象
     * @param errorCode    错误码
     * @param errorMessage 错误信息
     * @param errorDetails 错误内容
     */
    public static void runMainThreadReturnError(final MethodChannel.Result result, final String errorCode, final String errorMessage, final Object errorDetails) {
        MAIN_HANDLER.post(new Runnable() {
            @Override
            public void run() {
                result.error(errorCode, errorMessage, errorDetails);
            }
        });
    }
}
