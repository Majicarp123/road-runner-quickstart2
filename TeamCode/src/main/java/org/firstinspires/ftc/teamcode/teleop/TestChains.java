package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Basic: Linear OpMode", group="Linear OpMode")
@Disabled
public class TestChains extends LinearOpMode {

    // Declare OpMode members.

    private double leftServoOpen;
    private double leftServoClosed1;
    private double leftServoClosed2;
    private double rightServoOpen;
    private double rightServoClosed1;
    private double rightServoClosed2;
    private ElapsedTime runtime = new ElapsedTime();
    DcMotor frontRight, frontLeft, rearRight, rearLeft, chainMotor;
    Servo airplaneLift, airplaneLaunch, servoLeft, servoRight;
    Gamepad prevPad1 = new Gamepad();
    public void initMotors()
    {
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        rearRight = hardwareMap.get(DcMotor.class, "rearRight");
        rearLeft = hardwareMap.get(DcMotor.class, "rearLeft");

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        rearRight.setDirection(DcMotorSimple.Direction.REVERSE);

        chainMotor = hardwareMap.get(DcMotor.class, "chainMotor");
    }
    public void initServos()
    {
        airplaneLift = hardwareMap.get(Servo.class, "planeLift");
        airplaneLaunch = hardwareMap.get(Servo.class, "planeLaunch");
        servoLeft = hardwareMap.get(Servo.class, "servoLeft");

        servoRight = hardwareMap.get(Servo.class, "servoRight");
        // I'm just putting this because one of them has to be reversed. It might be the left one.
        servoRight.setDirection(Servo.Direction.REVERSE);
    }
    

    @Override
    public void runOpMode() {
        // init everything
        initMotors();
        initServos();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        float drive, strafe, rotation, power;
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            drive = gamepad1.left_stick_y;
            strafe = gamepad1.left_stick_x;
            rotation = gamepad1.right_stick_x;
            // front right
            power = Range.clip(drive - strafe - rotation, -1, 1);
            frontRight.setPower(power);
            // front left
            power = Range.clip(drive + strafe + rotation, -1, 1);
            frontLeft.setPower(power);
            // rear left
            power = Range.clip(drive - strafe + rotation, -1, 1);
            rearLeft.setPower(power);
            // rear right
            power = Range.clip(drive + strafe - rotation, -1, 1);
            rearRight.setPower(power);

            chainMotor.setPower(gamepad1.right_stick_y);

            // this makes it only run once per button press
            if (gamepad1.a && !prevPad1.a)
                airplaneLift.setPosition((airplaneLift.getPosition() == 1) ? 0 : 1); // sets the position to either lifted up or down
            // only runs if the airplane lift is up
            if (gamepad1.b && !prevPad1.b && airplaneLift.getPosition() == 1)
                airplaneLaunch.setPosition(1);



            prevPad1.copy(gamepad1);
        }
    }
}
