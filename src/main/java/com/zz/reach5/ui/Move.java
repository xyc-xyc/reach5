package com.zz.reach5.ui;

import com.vaadin.flow.component.button.Button;

import java.util.ArrayList;
import java.util.List;

public class Move {
    List<Button> buttons = new ArrayList<>();
    List<String> texts = new ArrayList<>();

    public void undo() {
        for(int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setText(texts.get(i));
        }
    }

    public void record(Button btn) {
        buttons.add(btn);
        texts.add(btn.getText());
    }
}
