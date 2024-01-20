package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.openftc.easyopencv.OpenCvWebcam;

enum Route {
    RED_LEFT_DIRECT,
    RED_LEFT_INDIRECT,
    RED_RIGHT_DIRECT,
    RED_RIGHT_INDIRECT,
    BLUE_LEFT_DIRECT,
    BLUE_LEFT_INDIRECT,
    BLUE_RIGHT_DIRECT,
    BLUE_RIGHT_INDIRECT,
    PARK_LEFT,
    PARK_RIGHT

}

@Autonomous(preselectTeleOp = "TeleOpA")
//@Disabled
public class AutoE extends LinearOpMode {


    public static final Vector2d RED_LEFT_START = new Vector2d(-36, -61);
    public static final Vector2d RED_RIGHT_START = new Vector2d(12, -61);
    public static final Vector2d RED_RIGHT_LEFT_POSITION = new Vector2d(14, -30);
    public static final Vector2d RED_RIGHT_RIGHT_POSITION = new Vector2d(23, -30);


    public static final Vector2d BLUE_LEFT_START = new Vector2d(12, 61);
    public static final Vector2d BLUE_RIGHT_START = new Vector2d(-36, 61);
    public static final Vector2d BLUE_RIGHT_LEFT_POSITION = new Vector2d(23, 30);
    public static final Vector2d BLUE_RIGHT_RIGHT_POSITION = new Vector2d(14, 30);

    private Boolean redAlliance = null;
    private Boolean startLeft = null;
    private Boolean parkLeft = null;

    OpenCvWebcam camera;
    private boolean startedStreaming;


    public static final Route ROUTE = Route.RED_LEFT_DIRECT;
    public static final double DELAY = 0.5;

    @Override
    public void runOpMode() {
        Gamepad previousGamepad = new Gamepad();
        Gamepad currentGamepad = new Gamepad();

        while (true) {
            previousGamepad.copy(currentGamepad);
            currentGamepad.copy(gamepad1);

            if (redAlliance == null) {
                telemetry.addData("Alliance", "X = blue, B = red");
                telemetry.update();
                if (currentGamepad.x && !previousGamepad.x) {
                    redAlliance = false;
                }
                if (currentGamepad.b && !previousGamepad.b) {
                    redAlliance = true;
                }
            } else if (startLeft == null) {
                telemetry.addData("Start", "X = left, B = right");
                telemetry.update();
                if (currentGamepad.x && !previousGamepad.x) {
                    startLeft = true;
                }
                if (currentGamepad.b && !previousGamepad.b) {
                    startLeft = false;
                }
            } else if (parkLeft == null) {
                telemetry.addData("Park", "X = left, B = right");
                telemetry.update();
                if (currentGamepad.x && !previousGamepad.x) {
                    parkLeft = true;
                }
                if (currentGamepad.b && !previousGamepad.b) {
                    parkLeft = false;
                }
            } else {
                break;
            }
        }


    }


    //red left park left
    //red left park right
    //red right park right
    //red right park left


// Blue
//blue left park right
    //blue left park left
    //blue right park right
    //blue right park left

}

