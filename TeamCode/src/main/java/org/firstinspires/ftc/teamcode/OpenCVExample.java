package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

@Disabled
public class OpenCVExample extends LinearOpMode{

    OpenCvCamera webcam;
    static TestPipeline pipeline;

    Hardware TIseBot = new Hardware();
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        double drive;
        double turn;
        double strafe;
        double fLeft;
        double fRight;
        double bLeft;
        double bRight;
        double max;

        TIseBot.init(hardwareMap);

        TIseBot.frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        TIseBot.frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        TIseBot.backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        TIseBot.backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        pipeline = new TestPipeline();
        webcam.setPipeline(pipeline);

        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {
                /*
                 * This will be called if the camera could not be opened
                 */
            }
        });

        telemetry.addLine("Waiting for start");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Region 1", pipeline.region1Avg());
            telemetry.addData("Region 2", pipeline.region2Avg());
            telemetry.addData("Region 3", pipeline.region3Avg());

            if ((pipeline.region1Avg() > pipeline.region2Avg()) && (pipeline.region1Avg() > pipeline.region3Avg())){
                telemetry.addLine("Bottom");
            }
            if ((pipeline.region2Avg() > pipeline.region1Avg()) && (pipeline.region2Avg() > pipeline.region3Avg())){
                telemetry.addLine("Middle");
            }
            if ((pipeline.region3Avg() > pipeline.region1Avg()) && (pipeline.region3Avg() > pipeline.region2Avg())){
                telemetry.addLine("Top");
            }

            telemetry.update();

            drive = gamepad1.left_stick_y; //Between -1 and 1
            turn = gamepad1.right_stick_x;
            strafe = gamepad1.dpad_left? -1:gamepad1.dpad_right? 1: gamepad1.left_stick_x;

            fLeft = -.7*drive + .8*strafe - .75*turn;
            fRight = -.7*drive - .8*strafe + .75*turn;
            bRight = -.7*drive + .8*strafe + .75*turn;
            bLeft = -.7*drive - .8*strafe - .75*turn;

            max = Math.max(Math.max(Math.abs(fLeft), Math.abs(fRight)), Math.max(Math.abs(bLeft), Math.abs(bRight)));
            if (max > 1.0) {
                fLeft /= max;
                fRight /= max;
                bLeft /= max;
                bRight /= max;
            }

            TIseBot.frontRightMotor.setPower(fRight);
            TIseBot.frontLeftMotor.setPower(fLeft);
            TIseBot.backRightMotor.setPower(bRight);
            TIseBot.backLeftMotor.setPower(bLeft);

            sleep(20);
        }
    }

    public static class TestPipeline extends OpenCvPipeline {

        /** Most important section of the code: Colors **/
        static final Scalar CRIMSON = new Scalar(220, 20, 60);
        static final Scalar AQUA = new Scalar(79, 195, 247);
        static final Scalar PARAKEET = new Scalar(3, 192, 74);
        static final Scalar GOLD = new Scalar(255, 215, 0);
        static final Scalar CYAN = new Scalar(0, 139, 139);

        static final Point REGION1_TOPLEFT_ANCHOR_POINT = new Point(2, 70);
        static final int REGION1_WIDTH = 105;
        static final int REGION1_HEIGHT = 105;
        static final Point REGION2_TOPLEFT_ANCHOR_POINT = new Point(111,70);
        static final int REGION2_WIDTH = 105;
        static final int REGION2_HEIGHT = 105;
        static final Point REGION3_TOPLEFT_ANCHOR_POINT = new Point(214,70);
        static final int REGION3_WIDTH = 105;
        static final int REGION3_HEIGHT = 105;
        static final Point REGION4_TOPLEFT_ANCHOR_POINT = new Point(100,70);
        static final int REGION4_WIDTH = 105;
        static final int REGION4_HEIGHT = 105;


        Point region1_pointA = new Point(REGION1_TOPLEFT_ANCHOR_POINT.x, REGION1_TOPLEFT_ANCHOR_POINT.y);
        Point region1_pointB = new Point(REGION1_TOPLEFT_ANCHOR_POINT.x + REGION1_WIDTH, REGION1_TOPLEFT_ANCHOR_POINT.y + REGION1_HEIGHT);

        Point region2_pointA = new Point(REGION2_TOPLEFT_ANCHOR_POINT.x, REGION2_TOPLEFT_ANCHOR_POINT.y);
        Point region2_pointB = new Point(REGION2_TOPLEFT_ANCHOR_POINT.x + REGION2_WIDTH, REGION2_TOPLEFT_ANCHOR_POINT.y + REGION2_HEIGHT);

        Point region3_pointA = new Point(REGION3_TOPLEFT_ANCHOR_POINT.x, REGION3_TOPLEFT_ANCHOR_POINT.y);
        Point region3_pointB = new Point(REGION3_TOPLEFT_ANCHOR_POINT.x + REGION3_WIDTH, REGION2_TOPLEFT_ANCHOR_POINT.y + REGION3_HEIGHT);

        Point region4_pointA = new Point(REGION3_TOPLEFT_ANCHOR_POINT.x, REGION3_TOPLEFT_ANCHOR_POINT.y);
        Point region4_pointB = new Point(REGION3_TOPLEFT_ANCHOR_POINT.x + REGION3_WIDTH, REGION2_TOPLEFT_ANCHOR_POINT.y + REGION3_HEIGHT);

        Mat region1_G, region2_G, region3_G, region4_G;
        Mat HLS = new Mat();
        Mat L = new Mat();
        int avg1, avg2, avg3, avg4;

        /*
         * This function takes the RGB frame, converts to YCrCb,
         * and extracts the Cb channel to the 'Cb' variable
         */
        void inputToG(Mat input) {
            Imgproc.cvtColor(input, HLS, Imgproc.COLOR_RGB2HLS);
            Core.extractChannel(HLS, L, 1);
        }

        @Override
        public void init(Mat firstFrame) {
            inputToG(firstFrame);

            region1_G = L.submat(new Rect(region1_pointA, region1_pointB));
            region2_G = L.submat(new Rect(region2_pointA, region2_pointB));
            region3_G = L.submat(new Rect(region3_pointA, region3_pointB));
            region4_G = L.submat(new Rect(region4_pointA, region4_pointB));
        }

        @Override
        public Mat processFrame(Mat input) {

            inputToG(input);

            avg1 = (int) Core.mean(region1_G).val[0];
            avg2 = (int) Core.mean(region2_G).val[0];
            avg3 = (int) Core.mean(region3_G).val[0];
            avg4 = (int) Core.mean(region4_G).val[0];

            Imgproc.rectangle(input, region1_pointA, region1_pointB, CRIMSON,2);
            Imgproc.rectangle(input, region2_pointA, region2_pointB, AQUA,2);
            Imgproc.rectangle(input, region3_pointA, region3_pointB, PARAKEET,2);
            Imgproc.rectangle(input, region4_pointA, region4_pointB,GOLD,2);

            return input;
        }

        public int region1Avg() {
            return avg1;
        }
        public int region2Avg() {
            return avg2;
        }
        public int region3Avg() {
            return avg3;
        }
        public int region4Avg() {
            return avg4;
        }

    }
}