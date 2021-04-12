package com.philipthedev.gamejam.paradox;

import com.philipthedev.gamejam.paradox.ui.IntroScene;
import com.philipthedev.gamejam.paradox.ui.MainFrame;

/**
 * Contains the main method to start this game.
 */
public class Main {


    public static void main(String[] args) {
        MainFrame mainFrame = MainFrame.get();
        mainFrame.setScene(new IntroScene());
    }

}
