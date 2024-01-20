

/*import static org.opencv.core.Core.log;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.OpenCVDetection;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;


// Localization is doesn't show drift, follower if it does
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
    PARK_RIGHT;
}

@Config
@Autonomous(name = "AutoOp State Machines", group = "16481-Power-Play")
public class AutoE extends LinearOpMode {
    // (other code...)
    OpenCvWebcam camera;
    private boolean startedStreaming;
    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor rightRear;
    private DcMotor leftRear;
    boolean finished = false;

    private Boolean redAlliance = null;
    private Boolean startLeft = null;
    private Boolean parkLeft = null;

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
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(12, 60, 90));

        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftRear = hardwareMap.get(DcMotor.class, "leftRear");
        rightRear = hardwareMap.get(DcMotor.class, "rightRear");

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftRear.setDirection(DcMotorSimple.Direction.REVERSE);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        camera = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        OpenCVDetection detector = new OpenCVDetection(parkLeft, redAlliance, startLeft, telemetry);
        camera.setPipeline(detector);
        camera.setMillisecondsPermissionTimeout(5000); // Timeout for obtaining permission is configurable. Set before opening.


        // (other camera setup...)

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                // (camera start streaming...)
            }

            @Override
            public void onError(int errorCode) {
                // (camera error handling...)
            }
        });

        FtcDashboard.getInstance().startCameraStream(camera, 0);

        // (wait for camera streaming to start...)

        OpenCVDetection.Location location = null;

        while (opModeIsActive() && location == null) {
            // (wait for location from detector...)
        }

        waitForStart();

        location = detector.getLocation();

        camera.stopStreaming();
        camera.closeCameraDevice();

        if (isStopRequested()) return;

        // Use if-else statements based on enums for different trajectory actions
        Action trajectoryAction;
        if (ROUTE == Route.RED_LEFT_DIRECT) {
            trajectoryAction = drive.actionBuilder(new Pose2d(12.00, 60.00, Math.toRadians(90.00)))
                    .splineToLinearHeading(new Pose2d(12.00, 40.00, Math.toRadians(90.00)), Math.toRadians(90.00))
                    .build();
        } else if (ROUTE == Route.RED_LEFT_INDIRECT) {
            // Add action for RED_LEFT_INDIRECT
        } else if (ROUTE == Route.RED_RIGHT_DIRECT) {
            // Add action for RED_RIGHT_DIRECT
        } else if (ROUTE == Route.RED_RIGHT_INDIRECT) {
            // Add action for RED_RIGHT_INDIRECT
        } else if (ROUTE == Route.BLUE_LEFT_DIRECT) {
            // Add action for BLUE_LEFT_DIRECT
        } else if (ROUTE == Route.BLUE_LEFT_INDIRECT) {
            // Add action for BLUE_LEFT_INDIRECT
        } else if (ROUTE == Route.BLUE_RIGHT_DIRECT) {
            // Add action for BLUE_RIGHT_DIRECT
        } else if (ROUTE == Route.BLUE_RIGHT_INDIRECT) {
            // Add action for BLUE_RIGHT_INDIRECT
        } else {
            // Default case, throw an exception or provide a default action
            throw new IllegalArgumentException("Invalid route: " + ROUTE);
        }

        while (!isStopRequested() && !opModeIsActive()) {
            // (do something while waiting...)
        }

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                TrajectoryAction1
                // Add other actions if needed
        );
    }
}*/