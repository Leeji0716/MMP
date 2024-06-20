package com.example.MMP.trainer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CheckboxFilter extends JFrame {
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private JCheckBox checkBox3;
    private JList<String> itemList;
    private DefaultListModel<String> listModel;
    private List<Item> items;

    public CheckboxFilter() {
        setTitle("Checkbox Filter Example");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 아이템 리스트 초기화
        items = new ArrayList<>();
        items.add(new Item("gender, classType, specialization", "A"));
        items.add(new Item("classType", "B"));
        items.add(new Item("specialization", "A"));
        items.add(new Item("Item 4", "C"));

        // 체크박스 초기화
        checkBox1 = new JCheckBox("A");
        checkBox2 = new JCheckBox("B");
        checkBox3 = new JCheckBox("C");

        // 체크박스 패널
        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.add(checkBox1);
        checkBoxPanel.add(checkBox2);
        checkBoxPanel.add(checkBox3);

        // 아이템 리스트 초기화
        listModel = new DefaultListModel<>();
        itemList = new JList<>(listModel);
        updateItemList();

        // 체크박스 리스너 추가
        ItemListener itemListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateItemList();
            }
        };

        checkBox1.addItemListener(itemListener);
        checkBox2.addItemListener(itemListener);
        checkBox3.addItemListener(itemListener);

        // 레이아웃 구성
        setLayout(new BorderLayout());
        add(checkBoxPanel, BorderLayout.NORTH);
        add(new JScrollPane(itemList), BorderLayout.CENTER);
    }

    // 아이템 리스트 업데이트
    private void updateItemList() {
        listModel.clear();
        List<String> filteredItems = items.stream()
                .filter(item -> (checkBox1.isSelected() && "A".equals(item.getAttribute())) ||
                        (checkBox2.isSelected() && "B".equals(item.getAttribute())) ||
                        (checkBox3.isSelected() && "C".equals(item.getAttribute())))
                .map(Item::getName)
                .collect(Collectors.toList());

        for (String itemName : filteredItems) {
            listModel.addElement(itemName);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CheckboxFilter example = new CheckboxFilter();
            example.setVisible(true);
        });
    }

    // 아이템 클래스
    class Item {
        private String name;
        private String attribute;

        public Item(String name, String attribute) {
            this.name = name;
            this.attribute = attribute;
        }

        public String getName() {
            return name;
        }

        public String getAttribute() {
            return attribute;
        }
    }
}

