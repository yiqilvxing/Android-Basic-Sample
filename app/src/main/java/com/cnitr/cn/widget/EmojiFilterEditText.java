package com.cnitr.cn.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by YangChen on 2018/7/20.
 */

public class EmojiFilterEditText extends AppCompatEditText {

    public EmojiFilterEditText(Context context) {
        super(context);
        setInputEmojiFilter();
    }

    public EmojiFilterEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setInputEmojiFilter();
    }

    public EmojiFilterEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setInputEmojiFilter();
    }

    /**
     * 设置过滤Emoji
     */
    private void setInputEmojiFilter() {
        this.setFilters(new InputFilter[]{new InputEmojiFilter()});
    }

    /**
     * InputEmojiFilter
     */
    public class InputEmojiFilter implements InputFilter {

        private Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                return "";
            }
            return null;
        }

    }


}
