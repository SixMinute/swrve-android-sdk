package com.swrve.sdk.conversations.ui;

import android.content.Context;
import android.webkit.WebView;

import com.swrve.sdk.conversations.engine.model.Content;
import com.swrve.sdk.conversations.engine.model.ConversationAtom;

public class HtmlSnippetView extends WebView implements ConversationContent {
    private Content model;

    public HtmlSnippetView(Context context, Content model) {
        super(context);
        init(model);
    }

    protected void init(Content model) {
        this.model = model;
        this.loadDataWithBaseURL("fake://fake", model.getValue(), "text/html", "UTF-8", null);
    }

    @Override
    public ConversationAtom getModel() {
        return model;
    }
}
