package cn.yumutech.netUtil;

import android.content.Context;
import android.text.Spannable;
import android.view.View;
import android.view.ViewGroup;

import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.message.FileMessage;

/**
 * Created by 霍长江 on 2016/11/30.
 */
public class FileMessageProvider extends IContainerItemProvider.MessageProvider<FileMessage> {
    @Override
    public void bindView(View view, int i, FileMessage fileMessage, UIMessage uiMessage) {

    }

    @Override
    public Spannable getContentSummary(FileMessage fileMessage) {
        return null;
    }

    @Override
    public void onItemClick(View view, int i, FileMessage fileMessage, UIMessage uiMessage) {

    }

    @Override
    public void onItemLongClick(View view, int i, FileMessage fileMessage, UIMessage uiMessage) {

    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        return null;
    }
}
