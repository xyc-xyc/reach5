package com.zz.reach5.utils;

import com.vaadin.flow.component.UI;

public class ClipboardUtils {
    public static void copyToClipboard(String str0) {
        var str1 = "'" + str0.replaceAll("'", "\\\\'") + "'";
        String cmd = "navigator.clipboard.writeText(" + str1 + ");"; // old way: "copyToClipboard(" + str1 + ");";

        UI.getCurrent().getPage().executeJs(cmd);
        System.out.println(str1);
        System.out.println(cmd);
    }
}
