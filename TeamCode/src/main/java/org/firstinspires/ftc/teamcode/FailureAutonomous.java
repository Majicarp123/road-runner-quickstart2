package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(preselectTeleOp = "Failure")
@Disabled
public class FailureAutonomous extends LinearOpMode {

    public DcMotor frontLeft, frontRight, rearLeft, rearRight;
    public Servo clawLeft, clawRight;
    public boolean finished = false;
    public int drive = 0;
    public int strafe = 1;
    public int rotate = 0;
    public double timeToRunShort = 4.6 / 2;
    public double timeToRunLong = 4.6;
    public boolean runLong;
    public boolean clawClosed = false;
    private ElapsedTime runtime = new ElapsedTime();
    private Gamepad prevPad1 = new Gamepad();
    public void toggleClaw()
    {
        if (clawClosed)
        {
            clawLeft.setPosition(0);
            clawRight.setPosition(.1);
            clawClosed = false;
        } else
        {
            clawLeft.setPosition(.155);
            clawRight.setPosition(.255);
            clawClosed = true;
        }
    }

    public void runOpMode()
    {
        frontRight = hardwareMap.get(DcMotor.class, "frontRightMotor");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        rearLeft = hardwareMap.get(DcMotor.class, "rearLeftMotor");
        rearRight = hardwareMap.get(DcMotor.class, "rearRightMotor");

        clawLeft = hardwareMap.get(Servo.class, "clawLeft");
        clawRight = hardwareMap.get(Servo.class, "clawRight");
        clawLeft.setDirection(Servo.Direction.REVERSE);
        clawLeft.setPosition(0);
        clawRight.setPosition(0);

        frontLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        rearLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        rearRight.setDirection(DcMotorSimple.Direction.REVERSE);

        boolean confirmed = false;
        while (!confirmed)
        {
            if (gamepad1.right_bumper)
                runLong = true;
            else if (gamepad1.left_bumper)
                runLong = false;

            if (gamepad1.start && gamepad1.a && !prevPad1.a && clawClosed)
                confirmed = true;

            if (gamepad1.b && !prevPad1.b)
                toggleClaw();



            telemetry.addData("Travel Long? ", runLong);
            telemetry.addData("Claw Closed? ", clawClosed);
            telemetry.addData("Confirmed? ", confirmed);
            telemetry.update();

            prevPad1.copy(gamepad1);
        }

        telemetry.addLine("Waiting...");
        telemetry.update();

        waitForStart();
        // start
        frontRight.setPower(1);
        frontLeft.setPower(1);
        rearLeft.setPower(1);
        rearRight.setPower(1);

        telemetry.clear();
        runtime.reset();
        while (opModeIsActive())
        {

            if (!finished)
            {
                double time = runtime.seconds();
                if (time >= ((runLong) ? timeToRunLong : timeToRunShort))
                {
                    finished = true;

                    frontRight.setPower(0);
                    frontLeft.setPower(0);
                    rearRight.setPower(0);
                    rearLeft.setPower(0);
                }
            }

            telemetry.addData("Elapsed Time: ", runtime.seconds());
            telemetry.update();
        }
    }
}
