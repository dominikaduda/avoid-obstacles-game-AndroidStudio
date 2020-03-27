package dudzixx.com.sensorgame;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class OrientationData implements SensorEventListener {
    /*
    attaches sensors to this class and then whenever those sensor have a change in their values
    this class will be listening for it and allow us to do stuff with those new values
    */
    private SensorManager manager;
    private Sensor accelerometer;
    private Sensor magnometer;

    // storing event.values
    private float[] accelOutput;
    private float[] magOutput;

    private float[] orientation = new float[3];
    public float[] getOrientation() {
        return orientation;
    }

    private float[] startOrientation = null;    //to compare how much the player move
    public float[] getStartOrientation() {
        return startOrientation;
    }
    public void newGame() {
        //resets the reference point to what phone is when it starts each game
        startOrientation = null;
    }

    public OrientationData() {
        manager = (SensorManager)Constants.CURRENT_CONTEXT.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnometer = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void register() {
        /*
        register the sensors to this class so that we're going to be listening for value changes
        in them
         */
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        manager.registerListener(this, magnometer, SensorManager.SENSOR_DELAY_GAME);
    }

    public void pause() {
        manager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /*
    avoiding overload thanks to SENSOR_DELAY_GAME by setting up a delay of how often
    onSensorChanged is called
    */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            accelOutput = event.values;
        else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            magOutput = event.values;
        if(accelOutput != null && magOutput != null) {  //then we are using ththem to calculate orientation
            float[] R = new float[9];   //for rotation matrix 3x3
            float[] I = new float[9];   //for inclination matrix

            //successful or nah
            boolean success = SensorManager.getRotationMatrix(R, I, accelOutput, magOutput);

            if(success) {
                SensorManager.getOrientation(R, orientation);
                if(startOrientation == null) {
                    startOrientation = new float[orientation.length];
                    System.arraycopy(orientation, 0, startOrientation, 0, orientation.length);  //copies orientation array into dart orientation
                    /*
                    NOTE!
                    startOrientation = orientation is NOT ok!
                    BECAUSE: float arrays and arrays in general are not primitive types,
                    they reference a object in the memory, basically these variables
                    just store reference not the actual object and if we do that we set them
                    to reference the same object in the memory! Then when we change orientation
                    and hence that object in the memory startOrientation will still be referencing
                    that object so startOrientation will change with orientation, it won't stay
                    the same and we don't want that. We want it to be assigned once and stay that way.
                    */
                }
            }
        }
    }
}
