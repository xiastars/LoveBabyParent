package com.summer.parent.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by xiastars on 2017/8/22.
 */

public class MovementInfo extends BmobObject {

    String content;
    int type;//0进入应用，1.安装应用；2.卸载应用

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
