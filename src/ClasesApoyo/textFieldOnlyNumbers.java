package ClasesApoyo;


import javafx.scene.control.TextField;

import javafx.scene.input.KeyEvent;



public class textFieldOnlyNumbers extends TextField  {



    public void keyPressed(KeyEvent e) {

        if(e.getCode().isDigitKey()){

            e.consume();
        }
    }


}
