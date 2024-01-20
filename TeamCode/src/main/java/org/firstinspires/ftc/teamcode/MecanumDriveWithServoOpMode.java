package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "MecanumDriveWithServo")
public class MecanumDriveWithServoOpMode extends OpMode {

    private DcMotor frontLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor rearRightMotor;
    private DcMotor rearLeftMotor;
    private DcMotor armMotor;

    private Servo clawLeft;
    private Servo clawRight;
    private Servo launchArm;
    private Servo launchRelease;

    private double launchServoPosition = 0.5;
    private double launchReleasePosition = 0.5;
    private double armMotorPower = 0.0;

    private final double SERVO_INCREMENT = 0.1;

    @Override
    public void init() {
        frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
        frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
        rearLeftMotor = hardwareMap.dcMotor.get("rearLeftMotor");
        rearRightMotor = hardwareMap.dcMotor.get("rearRightMotor");

        frontLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        rearLeftMotor.setDirection(DcMotor.Direction.REVERSE);
        rearRightMotor.setDirection(DcMotor.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotor.Direction.FORWARD);

        armMotor = hardwareMap.dcMotor.get("arm");
        armMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        armMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        clawLeft = hardwareMap.servo.get("clawLeft");
        clawRight = hardwareMap.servo.get("clawRight");
        launchArm = hardwareMap.servo.get("launchArm");
        launchRelease = hardwareMap.servo.get("launchRelease");

        launchArm.setPosition(launchServoPosition);
        launchRelease.setPosition(launchReleasePosition);
        clawLeft.setPosition(0.4);
        clawRight.setPosition(0.6);
        launchArm.setPosition(0.0);
        launchRelease.setPosition(0.5);
    }

    @Override
    public void loop() {
        double drive = -gamepad1.left_stick_y;  // Reverse the drive direction
        double strafe = -gamepad1.left_stick_x;  // Reverse the strafe direction
        double rotate = gamepad1.right_stick_x;

        double frontLeftPower = Range.clip(drive + strafe + rotate, -1.0, 1.0);
        double frontRightPower = Range.clip(drive - strafe - rotate, -1.0, 1.0);
        double rearLeftPower = Range.clip(drive - strafe + rotate, -1.0, 1.0);
        double rearRightPower = Range.clip(drive + strafe - rotate, -1.0, 1.0);



        frontLeftMotor.setPower(frontLeftPower);
        frontRightMotor.setPower(frontRightPower);
        rearLeftMotor.setPower(rearLeftPower);
        rearRightMotor.setPower(rearRightPower);

        // Control "Arm" motor with left stick of gamepad2
        armMotorPower = Range.clip(gamepad2.left_stick_y, 1.0, - 1.0);
        armMotor.setPower(armMotorPower);

        // Control "clawLeft" and "clawRight" servos with bumpers
        if (gamepad2.right_bumper) {
            // both open to max position
            clawRight.setPosition(1.0);
            clawLeft.setPosition(0.0);
        } else if (gamepad2.left_bumper) {
            // both close to max position
            clawRight.setPosition(0.0);
            clawLeft.setPosition(1.0);
        } else {
            // closed to a medium position (2 pixels)
            clawRight.setPosition(0.4);
            clawLeft.setPosition(0.6);
        }

        // Control "launchArm" servo with D-pad up and down
        if (gamepad2.dpad_up) {
            launchArm.setPosition(1.0);
        } else if (gamepad2.dpad_down) {
            launchArm.setPosition(0.0);
        }

        // Control "launchRelease" servo with D-pad left and right
        if (gamepad2.dpad_left) {
            launchRelease.setPosition(0.0);
        } else if (gamepad2.dpad_right) {
            launchRelease.setPosition(1.0);
        }

        telemetry.addData("Front Left Power", frontLeftPower);
        telemetry.addData("Front Right Power", frontRightPower);
        telemetry.addData("rear Left Power", rearLeftPower);
        telemetry.addData("rear Right Power", rearRightPower);
        telemetry.addData("Arm Motor Power", armMotorPower);
        telemetry.addData("ClawLeft Position", clawLeft.getPosition());
        telemetry.addData("ClawRight Position", clawRight.getPosition());
        telemetry.addData("LaunchArm Position", launchArm.getPosition());
        telemetry.addData("LaunchRelease Position", launchRelease.getPosition());
        telemetry.update();
    }

    @Override
    public void stop() {
        frontLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        rearRightMotor.setPower(0);
        rearLeftMotor.setPower(0);
        armMotor.setPower(0);
    }
}