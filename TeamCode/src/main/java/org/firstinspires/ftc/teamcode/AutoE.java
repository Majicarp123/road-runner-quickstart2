package org.firstinspires.ftc.teamcode;

import static org.opencv.core.Core.log;

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

    @Config
    @Autonomous(name = "AutoOp State Machines", group = "16481-Power-Play")
    public static class AutoE extends LinearOpMode {
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

            /*
             *   Below is an example of a lambda expression which is in simply an anonymous function.
             *   Since we are only executing one statement we are able to remove the curly braces and semicolon
             *   making it look much cleaner.
             *   Note that this is a feature strictly for SDK 8+, if Java 7 is being used use this code instead.
             *   To change preferences press command and ; to open up preference window.
             *
             *   * Lambda Expression *
             *   camera.openCameraDeviceAsync(() -> camera.startStreaming(320,240, OpenCvCameraRotation.UPRIGHT));
             */
            camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
                @Override
                public void onOpened() {
                    /*
                     * Tell the webcam to start streaming images to us! Note that you must make sure
                     * the resolution you specify is supported by the camera. If it is not, an exception
                     * will be thrown.
                     *
                     * Keep in mind that the SDK's UVC driver (what OpenCvWebcam uses under the hood) only
                     * supports streaming from the webcam in the uncompressed YUV image format. This means
                     * that the maximum resolution you can stream at and still get up to 30FPS is 480p (640x480).
                     * Streaming at e.g. 720p will limit you to up to 10FPS and so on and so forth.
                     *
                     * Also, we specify the rotation that the webcam is used in. This is so that the image
                     * from the camera sensor can be rotated such that it is always displayed with the image upright.
                     * For a front facing camera, rotation is defined assuming the user is looking at the screen.
                     * For a rear facing camera or a webcam, rotation is defined assuming the camera is facing
                     * away from the user.
                     */


                }

                @Override
                public void onError(int errorCode) {
                    /*
                     * This will be called if the camera could not be opened
                     */

                }
            });

            FtcDashboard.getInstance().startCameraStream(camera, 0);

            while (opModeIsActive() && !startedStreaming) {

                sleep(50);
            }

            OpenCVDetection.Location location = null;

            while (opModeIsActive() && location == null) {

                sleep(50);
                location = detector.getLocation();
            }

            waitForStart();

            location = detector.getLocation();

            camera.stopStreaming();

            camera.closeCameraDevice();

            if (isStopRequested()) return;





            // Delcare Trajectory as such

            Action TrajectoryAction1 = drive.actionBuilder(new Pose2d(12.00, 60.00, Math.toRadians(90.00)))
                    .splineToLinearHeading(new Pose2d(12.00, 40.00, Math.toRadians(90.00)), Math.toRadians(90.00))
                    .build();


            while (!isStopRequested() && !opModeIsActive()) {

            }

            waitForStart();

            if (isStopRequested()) return;


            Actions.runBlocking(

                    TrajectoryAction1 // Example of a drive action

                    // This action and the following action do the same thing

            );


        }

    }
}