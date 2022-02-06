package org.firstinspires.ftc.teamcode.old.robot;

import com.acmerobotics.dashboard.FtcDashboard;
import com.amarcolini.joos.command.Component;
import com.amarcolini.joos.command.Robot;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.old.DuckDetector;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvWebcam;

public class Camera implements Component {
    private final OpenCvWebcam webcam;
    private final DuckDetector detector = new DuckDetector();

    public Camera(HardwareMap hMap, String id) {
        this.webcam = OpenCvCameraFactory.getInstance().createWebcam(
                hMap.get(WebcamName.class, id)
        );
    }

    public void start() {
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.setPipeline(detector);
                webcam.startStreaming(800, 600);
                FtcDashboard.getInstance().startCameraStream(webcam, 60);
            }

            @Override
            public void onError(int errorCode) {

            }
        });
    }

    public void stop() {
        webcam.stopStreaming();
    }

    public DuckDetector.Position getLastPosition() {
        return detector.getLastPosition();
    }

    public void close() {
        webcam.closeCameraDevice();
    }
}
