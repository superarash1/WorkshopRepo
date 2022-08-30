package org.firstinspires.ftc.teamcode.OpModes.TeleOp_OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Control.Autonomous.Chain_Auton_Control;
import org.firstinspires.ftc.teamcode.Control.TeleOp.Chain_TeleOp_Control;

public class chainTeleOp extends LinearOpMode {
    @Override
    public void runOpMode() {
        Chain_TeleOp_Control teleOp = new Chain_TeleOp_Control("chainMotor", hardwareMap, telemetry);

        telemetry.addLine("Waiting for start");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()){

        }
    }
}
