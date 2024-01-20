package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(preselectTeleOp = "Failure")
public class FailureAutonomous extends OpMode {

    public DcMotor frontLeft, frontRight, rearLeft, rearRight;
    public boolean finished = false;
    public double timeToRun = 10;
    private ElapsedTime runtime = new ElapsedTime();
    @Override
    public void init()
    {
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        rearLeft = hardwareMap.get(DcMotor.class, "rearLeft");
        rearRight = hardwareMap.get(DcMotor.class, "rearRight");
    }

    @Override
    public void start() {
        frontRight.setPower(1);
        frontLeft.setPower(1);
        rearLeft.setPower(1);
        rearRight.setPower(1);
        runtime.reset();
    }


    @Override
    public void loop() {
        if (!finished)
        {
            double time = runtime.seconds();
            if (time == timeToRun)
            {
                finished = true;

                frontRight.setPower(0);
                frontLeft.setPower(0);
                rearRight.setPower(0);
                rearLeft.setPower(0);
            }
        }

        telemetry.addData("Elapsed Time: ", runtime.seconds());
    }
}
