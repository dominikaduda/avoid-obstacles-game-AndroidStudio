package dudzixx.com.sensorgame;

import android.graphics.Canvas;
import android.view.MotionEvent;

public interface Scene {
    public void update();
    public void draw(Canvas canvas);

    public void terminate();    /*when the scene is supposed to end it will call terminate method
    and communicate with SceneManager telling him to switch to active scene*/

    public void receiveTouch(MotionEvent event);

}
