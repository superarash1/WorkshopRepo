package org.firstinspires.ftc.teamcode.Control.Autonomous;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Control.MotionControl.PIDF_Controller;
import org.firstinspires.ftc.teamcode.Hardware.BotMechanisms.Chain_Mechanism;

public class Chain_Auton_Control {

    Chain_Mechanism chainMechanism;
    public Telemetry telemetry;

    public enum chainState {
        CONTINUOUS,
        TARGET,
        IDLE
    }

    public chainState ChainState;
    public PIDF_Controller ChainPID;

    public Chain_Auton_Control(String name, HardwareMap hardwareMap, Telemetry telemetry){
        ChainPID = new PIDF_Controller(1);
        chainMechanism = new Chain_Mechanism(name, hardwareMap);

        this.telemetry = telemetry;

        ChainState = chainState.IDLE;
    }

    public void chainMotion(){
        switch (ChainState){
            case CONTINUOUS:

                break;
            case TARGET:

                break;
            case IDLE:

                break;
        }
    }
}
