package com.jikexueyuan.animationseries.redpoint;

import android.util.SparseArray;
import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 红点管理类
 * <p>
 * 每个view为一个RedpointParent,需要实现此接口,
 * 根据具体view显示小红点的方式不同,实现管理小红点的showPoint和hindPoint方法。
 * 控制线对应RedpointGroup,存在groupId唯一标识
 */
public class RedpointManager {

    //回复我的红点GroupId
    public static final int REPLY_ME_RED_GROUP = 0x001;
    //社区消息红点GroupId -- 赞
    public static final int MY_COMMUNITY_ZAN_RED_GROUP = 0x002;
    //社区消息红点GroupId -- 回复
    public static final int MY_COMMUNITY_REPLY_RED_GROUP = 0x003;
    //社区消息红点GroupId -- 系统
    public static final int MY_COMMUNITY_SYS_RED_GROUP = 0x004;
    //新版本安装红点GroupId
    public static final int NEW_VERSION_RED_GROUP = 0x005;
    //push推送红点GroupId
    public static final int PUSH_RED_GROUP = 0x006;

    /**
     * 红点组集合
     */
    private static SparseArray<RedpointGroup> redpointGroups = new SparseArray<>();

    /**
     * 红点映射
     */
    private static HashMap<RedpointParent, RedpointNode> redpointRecord = new HashMap<>();

    /**
     * 初始化红点组
     */
    static {
        redpointGroups.put(REPLY_ME_RED_GROUP, new RedpointGroup());
        redpointGroups.put(MY_COMMUNITY_ZAN_RED_GROUP, new RedpointGroup());
        redpointGroups.put(MY_COMMUNITY_REPLY_RED_GROUP, new RedpointGroup());
        redpointGroups.put(MY_COMMUNITY_SYS_RED_GROUP, new RedpointGroup());
        redpointGroups.put(NEW_VERSION_RED_GROUP, new RedpointGroup());
        redpointGroups.put(PUSH_RED_GROUP, new RedpointGroup());
    }

    /**
     * 注册红点到指定组 (红点View初始化时调用)
     *
     * @param redpointParent 红点View
     * @param groupId        组名
     */
    public static void register(RedpointParent redpointParent, int groupId) {
        RedpointGroup redpointGroup = redpointGroups.get(groupId);
        RedpointNode redpoint = redpointWarpper(redpointParent);
        redpoint.addGroup(groupId);
        redpointGroup.addNode(redpoint);
    }

    /**
     * 解绑红点将所有组中的该红点节点均移除(红点View销毁时调用)
     *
     * @param redpointParent 红点View
     */
    public static void unregister(RedpointParent redpointParent) {
        RedpointNode needRemoveRedpointNode = redpointRecord.remove(redpointParent);
        if(needRemoveRedpointNode == null) return;
        
        int size = redpointGroups.size();
        for (int i = 0; i < size; i++) {
            RedpointGroup redpointGroup = redpointGroups.valueAt(i);
            redpointGroup.remove(needRemoveRedpointNode);
        }
        needRemoveRedpointNode.deleteNode();
    }

    /**
     * 维护红点映射
     */
    private static RedpointNode redpointWarpper(RedpointParent redpointParent) {
        if (redpointRecord.containsKey(redpointParent)) {
            return redpointRecord.get(redpointParent);
        } else {
            RedpointNode redpoint = new RedpointNode(redpointParent);
            redpointRecord.put(redpointParent, redpoint);
            return redpoint;
        }
    }

    /**
     * 通知显示该组的所有节点
     *
     * @param groupId 组名
     */
    public static void notifyShowGroup(int groupId) {
        RedpointGroup group = redpointGroups.get(groupId);
        group.show();
    }

    /**
     * 通知隐藏该组的所有节点
     * 
     * @param groupId 组名
     */
    public static void notifyHideGroup(int groupId) {
        RedpointGroup group = redpointGroups.get(groupId);
        group.hide();
    }

    /**
     * 获取所有红点信息
     */
    public static HashMap<RedpointParent, RedpointNode> getRedpoints() {
        return redpointRecord;
    }

    /**
     * 红点View需要实现的接口
     */
    public interface RedpointParent {
        void registerRedGroup(int... groupIds);//注册红点组
        void unregisterRedGroup();//解绑红点组
        void showPoint();//显示红点
        void hindPoint();//隐藏红点
    }

    /**
     * 红点组 (一个组包含多个节点)
     */
    private static class RedpointGroup {
        public boolean statuIsshow;//显隐状态
        private ArrayList<RedpointNode> redpoints = new ArrayList<>(); //该组下的节点

        //添加节点 
        public void addNode(RedpointNode redpoint) {
            if (!redpoints.contains(redpoint)) {
                redpoints.add(redpoint);
            }
            if (statuIsshow) {
                show();
            } else {
                hide();
            }
        }

        //移除节点
        public void remove(RedpointNode redpoint) {
            if (redpoints.contains(redpoint)) {
                redpoints.remove(redpoint);
            }
        }

        //显示红点组
        public void show() {
            statuIsshow = true;
            for (RedpointNode redpoint : redpoints) {
                redpoint.show();
            }
        }

        //隐藏红点组
        public void hide() {
            statuIsshow = false;
            for (RedpointNode redpoint : redpoints) {
                redpoint.hind();
            }
        }

        @Override
        public String toString() {
            return "RedGroup " + statuIsshow + "\n" + redpoints.toString() + "\n";
        }
    }

    /**
     * 红点节点 (一个节点包含 一个红点view + 该红点所属的多个组)
     * eg: 我的tab 属于多个组
     */
    private static class RedpointNode {
        private RedpointParent mRedpointParent;
        private boolean statuIsshow;
        private SparseBooleanArray groupInfos = new SparseBooleanArray();//组信息

        //构造器
        private RedpointNode(RedpointParent redpointParent) {
            mRedpointParent = redpointParent;
        }
        
        //为节点添加组
        private void addGroup(int groupId){
            RedpointGroup redpointGroup = redpointGroups.get(groupId);
            groupInfos.put(groupId, redpointGroup.statuIsshow);
        }
        
        //删除节点 (防止内存泄漏)
        private void deleteNode() {
            groupInfos.clear();
            statuIsshow = false;
            mRedpointParent = null;
        }
        

        //展示节点
        void show() {
            statuIsshow = true;
            mRedpointParent.showPoint();
        }

        //隐藏节点 (只有该节点没有任何所属组时才能隐藏)
        void hind() {
            int size = groupInfos.size();
            for (int i = 0; i < size; i++) {
                if (groupInfos.valueAt(i)) {
                    return;
                }
            }
            statuIsshow = false;
            mRedpointParent.hindPoint();
        }

        @Override
        public String toString() {
            return "RedpointNode{" +
                    "mRedpointParent=" + mRedpointParent +
                    ", statuIsshow=" + statuIsshow +
                    ", groupInfos=" + groupInfos +
                    '}';
        }
    }

    
}
