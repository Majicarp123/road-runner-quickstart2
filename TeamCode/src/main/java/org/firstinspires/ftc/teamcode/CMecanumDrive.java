package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="CMecanumDrive")
public class CMecanumDrive extends OpMode {
    public boolean clawClosed = false;
    public double maxPower = .5;
    private ElapsedTime runtime = new ElapsedTime();
    DcMotor frontRight, frontLeft, rearRight, rearLeft, armMotor;
    Servo launchArm, launchRelease, clawLeft, clawRight;
    Gamepad prevPad1 = new Gamepad();
    Gamepad prevPad2 = new Gamepad();

    @Override
    public void init()
    {
        launchArm = hardwareMap.get(Servo.class, "launchArm");
        launchArm.setPosition(.5);
        launchRelease = hardwareMap.get(Servo.class, "launchRelease");
        launchRelease.setPosition(.7);

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
    void handleClaw()
    {
        // right bumper gets pressed
        if (gamepad2.right_bumper && !prevPad2.right_bumper)
        {
            // the claw is already closed, so it opens
            if (clawClosed)
            {
                clawLeft.setPosition(0);
                clawRight.setPosition(.1);
                clawClosed = false;
                return;
            }
            // if holding left bumper, it closes to size for 2 pixels
            if (gamepad2.left_bumper) {
                clawLeft.setPosition(.1);
                clawRight.setPosition(.2);
            }
            // if not, close to the max
            else {
                clawLeft.setPosition(.3);
                clawRight.setPosition(.4);
            }
            clawClosed = true;
        }
    }

    @Override
    public void loop() {
       float drive, strafe, rotation, power;
       drive = -gamepad1.left_stick_y;
       strafe = gamepad1.left_stick_x;
       rotation = -gamepad1.right_stick_x;
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

        if (gamepad2.right_stick_button && !prevPad2.right_stick_button)
            maxPower = (maxPower == 1) ? .5 : 1;

        double armPower = Range.clip(-(gamepad2.right_stick_y), -(maxPower / 2), maxPower);
        armMotor.setPower(armPower);

        // this makes it only run once per button press
        if (gamepad2.a && !prevPad2.a)
            launchArm.setPosition((launchArm.getPosition() == 1) ? 0 : 1); // sets the position to either lifted up or down
        // only runs if the airplane lift is up
        if (gamepad2.b && !prevPad2.b && launchArm.getPosition() == 1)
            launchRelease.setPosition((launchRelease.getPosition() == 1) ? 0 : 1);

        handleClaw();
        prevPad1.copy(gamepad1);
        prevPad2.copy(gamepad2);

        telemetry.addData("Front Right Power: ", frontRight.getPower());
        telemetry.addData("Front Left Power: ", frontLeft.getPower());
        telemetry.addData("Rear Left Power: ", rearLeft.getPower());
        telemetry.addData("Rear Right Power: ", rearRight.getPower());
        telemetry.addData("Arm Motor Power: ", armMotor.getPower());
        telemetry.addData("Claw Left Position: ", clawLeft.getPosition());
        telemetry.addData("Claw Right Position: ", clawRight.getPosition());
        telemetry.addData("Max Arm Power: ", maxPower);
        telemetry.addData("Drive: ", drive);
        telemetry.addData("Strafe: ", strafe);
        telemetry.addData("Rotation: ", rotation);
    }
    @Override
    public void stop()
    {
        frontRight.setPower(0);
        frontLeft.setPower(0);
        rearLeft.setPower(0);
        rearRight.setPower(0);
        armMotor.setPower(0);
    }

}
