package com.philipthedev.gamejam.paradox;

import com.philipthedev.gamejam.paradox.model.Model;
import com.philipthedev.gamejam.paradox.test.TestScene;
import com.philipthedev.gamejam.paradox.ui.MainFrame;
import com.philipthedev.gamejam.paradox.ui.ingame.InGameScene;

/**
 * Contains the main method to start this game.
 */
public class Main {


    public static void main(String[] args) {
        MainFrame mainFrame = MainFrame.get();
        mainFrame.setScene(new TestScene());
        Model model = new Model();
        mainFrame.setScene(new InGameScene(model));
    }

}
