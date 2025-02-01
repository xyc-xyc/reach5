package com.zz.reach5.ui;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@Route("tool")
public class Tool extends VerticalLayout {
    LinkedList<Move> undos = new LinkedList<>();
    Button[][] buttons = new Button[60][30];
    Map<Button, Integer> yMap = new HashMap<>();
    Map<Button, Integer> xMap = new HashMap<>();
    TextField filename = new TextField("");

    public Tool() {
        var list = new VerticalLayout();
        var tools = new HorizontalLayout();
        list.add(tools);

        var undoBtn = new Button("Undo");
        undoBtn.setText("Undo");
        undoBtn.addClickListener(this::undo);
        undoBtn.getStyle().set("color", "blue");
        tools.add(undoBtn);

        filename.setValue("buttons.txt");
        tools.add(filename);

        var saveBtn = new Button("Save");
        saveBtn.setText("Save");
        saveBtn.addClickListener(this::save);
        saveBtn.getStyle().set("color", "blue");
        tools.add(saveBtn);

        var loadBtn = new Button("Load");
        loadBtn.setText("Load");
        loadBtn.addClickListener(this::load);
        loadBtn.getStyle().set("color", "blue");
        tools.add(loadBtn);


        for (int y = 0; y < buttons.length; y++) {
            var row = new HorizontalLayout();
            for (int x = 0; x < buttons[0].length; x++) {
                var btn = new Button("");
                btn.setWidth(1, Unit.REM);
                btn.setMaxWidth(1, Unit.REM);
                btn.getStyle().set("margin", "0px");
                btn.getStyle().set("padding", "0px");
                row.add(btn);

                if (//y==0 && x == 0
                        y == 0 && x < 1 ||
                                y == 1 && x < 6 ||
                                y == 2 && x <= 6 ||
                                y == 3 && x <= 6 ||
                                y == 4 && x < 1
                ) {
                    btn.setText("X");
                }
                btn.addClickListener(this::click);
                if (y < 1) {
//                    btn.getStyle().set("background-color", "orange");
//                }else if(y-2 == x){
                    btn.getStyle().set("background-color", "yellow");
                }
                buttons[y][x] = btn;
                this.yMap.put(btn, y);
                this.xMap.put(btn, x);
            }
            list.add(row);
        }
        add(list);
    }

    public void undo(ClickEvent<Button> event) {
        if (!undos.isEmpty()) {
            undos.removeLast().undo();
        }
    }

    public void save(ClickEvent<Button> event) {
        StringBuilder sb = new StringBuilder();
        for (Button[] button : buttons) {
            for (int x = 0; x < buttons[0].length; x++) {
                sb.append(button[x].getText().equals("X") ? 1 : 0).append(" ");
            }
        }
        try {
            Files.writeString(Paths.get(filename.getValue()), sb.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void load(ClickEvent<Button> event) {
        try {
            String[] btns = Files.readString(Paths.get(filename.getValue())).split(" ");
            var i = 0;
            for (Button[] button : buttons) {
                for (int x = 0; x < buttons[0].length; x++) {
                    button[x].setText(btns[i++].equals("1") ? "X" : "");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void click(ClickEvent<Button> event) {
        Button btn = event.getSource();
        var btnX = xMap.get(btn);
        var btnY = yMap.get(btn);
        Move undo = new Move();

        if (btn.getText().isEmpty()) {
            List<Button> btns = getNeighbours(btnX, btnY);
            if (btns.size() > 1) {
                btns = btns.stream().filter(bt -> bt.getText().equals("O")).toList();
                if (btns.isEmpty()) {
                    undo.record(btn);
                    btn.setText("O");
                }
            }

            if (btns.size() == 1) {
                var btnX2 = xMap.get(btns.getFirst());
                var btnY2 = yMap.get(btns.getFirst());
                if (btns.getFirst().getText().equals("X")) {
                    move(buttons[2 * btnY - btnY2][2 * btnX - btnX2], undo);
                } else {
                    move(buttons[2 * btnY2 - btnY][2 * btnX2 - btnX], undo);
                }

                move(btns.getFirst(), undo);
                move(btn, undo);
            }
        } else if (btn.getText().equals("O")) {
            btn.setText("");
        }
        if (!undo.buttons.isEmpty()) {
            undos.add(undo);
        }
    }

    private List<Button> getNeighbours(Integer btnX, Integer btnY) {
        int radius = 1;
        return getNeighbours(btnX, btnY, radius);
    }

    private List<Button> getNeighbours(Integer btnX, Integer btnY, int radius) {
        return Stream.of(
                get(btnX - radius, btnY),
                get(btnX + radius, btnY),
                get(btnX, btnY - radius),
                get(btnX, btnY + radius)
        ).filter(Objects::nonNull).toList();
    }

    private Button get(int x, int y) {
        if (x >= 0 && x < buttons[0].length && y >= 0 && y < buttons.length) {
            return !buttons[y][x].getText().isEmpty() ? buttons[y][x] : null;
        } else {
            return null;
        }
    }

    private void move(Button btn, Move undo) {
        undo.record(btn);

        if (btn.getText().equals("X")) {
            btn.setText("");
        } else {
            btn.setText("X");
        }
    }
}