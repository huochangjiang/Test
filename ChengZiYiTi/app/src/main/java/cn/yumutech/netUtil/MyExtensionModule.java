package cn.yumutech.netUtil;

import android.view.KeyEvent;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.RongExtension;
import io.rong.imkit.emoticon.EmojiTab;
import io.rong.imkit.emoticon.IEmojiItemClickListener;
import io.rong.imkit.emoticon.IEmoticonTab;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.ImagePlugin;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by 霍长江 on 2016/11/30.
 */
public class MyExtensionModule extends DefaultExtensionModule{
    private static final String TAG = DefaultExtensionModule.class.getSimpleName();
    private EditText mEditText;

    public MyExtensionModule() {
    }

    public void onInit(String appKey) {
    }

    public void onConnect(String token) {
    }

    public void onAttachedToExtension(RongExtension extension) {
        this.mEditText = extension.getInputEditText();
    }

    public void onDetachedFromExtension() {
        this.mEditText = null;
    }

    public void onReceivedMessage(Message message) {
    }

    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        ArrayList pluginModuleList = new ArrayList();
        ImagePlugin image = new ImagePlugin();
        pluginModuleList.add(image);
        return pluginModuleList;
    }

    public List<IEmoticonTab> getEmoticonTabs() {
        EmojiTab emojiTab = new EmojiTab();
        emojiTab.setOnItemClickListener(new IEmojiItemClickListener() {
            public void onEmojiClick(String emoji) {
                int start = MyExtensionModule.this.mEditText.getSelectionStart();
                MyExtensionModule.this.mEditText.getText().insert(start, emoji);
            }

            public void onDeleteClick() {
                MyExtensionModule.this.mEditText.dispatchKeyEvent(new KeyEvent(0, 67));
            }
        });
        ArrayList list = new ArrayList();
        list.add(emojiTab);
        return list;
    }

    public void onDisconnect() {
    }
}

