import Foundation
import  ImSDK_Smart

/// 自定义会话结果实体
class V2ConversationResultEntity: NSObject {
    /// 下一次分页拉取的游标
    var nextSeq: UInt64?;

    /// 是否拉取完毕
    var finished: Bool?;

    /// 会话列表
    var conversationList: [[String: Any]]?;

    required public override init() {
    }

	func getDict() -> [String: Any] {
        var result: [String: Any] = [:]

        result["nextSeq"] = self.nextSeq
        result["isFinished"] = self.finished
        result["conversationList"] = self.conversationList

        return result
    }

    init(conversations: [V2TIMConversation], nextSeq: UInt64, finished: Bool) {
        super.init();
        self.nextSeq = nextSeq;
        self.finished = finished;
        conversationList = [];
        for item in conversations {
            conversationList!.append(V2ConversationEntity.getDict(info: item));
        }
    }
}
