package dudzixx.com.sensorgame;

import android.graphics.Canvas;

public interface GameObject {
    public void draw(Canvas canvas);    //draws player to canvas
    public void update();
}
