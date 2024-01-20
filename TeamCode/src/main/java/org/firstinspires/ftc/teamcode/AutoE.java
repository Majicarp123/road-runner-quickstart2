package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


// Localization is doesn't show drift, follower if it does

@Config
@Autonomous(name = "AutoOp State Machines", group = "16481-Power-Play")
public class AutoE extends LinearOpMode {
    private DcMotor leftFront;
    private DcMotor rightFront;
    private DcMotor rightRear;
    private DcMotor leftRear;
    boolean finished = false;

    @Override
    public void runOpMode() {

        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(12,60,90));

        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftRear = hardwareMap.get(DcMotor.class, "leftRear");
        rightRear = hardwareMap.get(DcMotor.class, "rightRear");

        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftRear .setDirection(DcMotorSimple.Direction.REVERSE);

        // Delcare Trajectory as such

        Action TrajectoryAction1 = drive.actionBuilder(new Pose2d(12.00, 60.00, Math.toRadians(90.00)))
                .splineToLinearHeading(new Pose2d(12.00, 40.00, Math.toRadians(90.00)), Math.toRadians(90.00))
                .build();












        while(!isStopRequested() && !opModeIsActive()) {

        }

        waitForStart();

        if (isStopRequested()) return;



        Actions.runBlocking(

                        TrajectoryAction1 // Example of a drive action

                        // This action and the following action do the same thing

        );

                rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);




    }

}