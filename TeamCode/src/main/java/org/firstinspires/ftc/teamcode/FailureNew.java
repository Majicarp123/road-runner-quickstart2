package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(preselectTeleOp = "Failure")
public class FailureAutonomous extends OpMode {

    public DcMotor frontLeft, frontRight, rearLeft, rearRight;
    public boolean finished = false;
    public double timeToRun = 2; //4.85 for 8ft drive
    public double timeToRunLong = 4.85;
    public double timetoRunShort = 4.85/2;
    public int drive = 0;
    public int strafe = 1;
    public int rotate = 0;
    private ElapsedTime runtime = new ElapsedTime();
    @Override
    public void init()
    {
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        rearLeft = hardwareMap.get(DcMotor.class, "rearLeft");
        rearRight = hardwareMap.get(DcMotor.class, "rearRight");

        double frontLeftPower = Range.clip(drive + strafe + rotate, -1.0, 1.0); //will's stuff
        double frontRightPower = Range.clip(drive - strafe - rotate, -1.0, 1.0);
        double rearLeftPower = Range.clip(drive - strafe + rotate, -1.0, 1.0);
        double rearRightPower = Range.clip(drive + strafe - rotate, -1.0, 1.0);

    }

    @Override
    public void start() {

        frontLeft.setPower(frontLeftPower); //will's stuff
        frontRight.setPower(frontRightPower);
        rearLeaft.setPower(rearLeftPower);
        rearRight.setPower(rearRightPower);

        runtime.reset();
    }


    @Override
    public void loop() {
        if (!finished)
        {   double time = runtime.seconds();
            if (time = timeToRun)
            {
                //finished = true;

                frontRight.setPower(0);
                frontLeft.setPower(0);
                rearRight.setPower(0);
                rearLeft.setPower(0);
            }
        }

        telemetry.addData("Elapsed Time: ", runtime.seconds());
    }
}
