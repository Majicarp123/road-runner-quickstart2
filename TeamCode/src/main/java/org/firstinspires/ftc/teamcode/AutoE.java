package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="RightBlue", group="AutoOpModes")
public class AutoE extends LinearOpMode
{

    public boolean clawClosed = false;
    public double maxPower = .5;
    private ElapsedTime runtime = new ElapsedTime();
    DcMotor frontRight, frontLeft, rearRight, rearLeft, armMotor;
    Servo  clawLeft, clawRight;


    {


        clawLeft = hardwareMap.get(Servo.class, "clawLeft");
        clawRight = hardwareMap.get(Servo.class, "clawRight");
        clawLeft.setDirection(Servo.Direction.REVERSE);
        clawLeft.setPosition(0);
        clawRight.setPosition(.1);

        frontRight = hardwareMap.get(DcMotor.class, "frontRightMotor");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        rearRight = hardwareMap.get(DcMotor.class, "rearRightMotor");
        rearLeft = hardwareMap.get(DcMotor.class, "rearLeftMotor");

        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        rearLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        rearRight.setDirection(DcMotorSimple.Direction.REVERSE);

        armMotor = hardwareMap.get(DcMotor.class, "arm");
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        armMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        frontRight.setPower(0);
        frontLeft.setPower(0);
        rearLeft.setPower(0);
        rearRight.setPower(0);
    }


    @Override
    public void runOpMode() throws InterruptedException {

    }
}




