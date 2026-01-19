package com.karyalinman;

import javax.swing.*;
import java.awt.*;

public abstract class MenuPanel extends JPanel {
    protected Color pinkPastel = new Color(255, 219, 233);
    protected Color hijauNeon = new Color(57, 255, 20);
    protected Color merahNeon = new Color(255, 49, 49);

    public MenuPanel() {
        setBackground(pinkPastel);
        setLayout(new BorderLayout(10, 10));
    }
    public abstract void refreshData();
}