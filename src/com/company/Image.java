package com.company;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Image {
    static int x1;
    static int y1;
    static int person_x=99999;
    static int person_y=99999;
    static int x2;
    static int y2;
    static int index=1;
    static int flag=1;
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        contour();
    }
    public static void contour() {
        // 1 获取原图
        Mat src = Imgcodecs.imread("E:\\adb\\1.png", Imgcodecs.IMREAD_ANYCOLOR);
        // 2 图片灰度化
        Mat gary = new Mat();
        Imgproc.cvtColor(src, gary, Imgproc.COLOR_RGB2GRAY);
        // 3 图像边缘处理
        Mat edges = new Mat();
        Imgproc.Canny(gary, edges, 200, 500, 3, false);
        // 4 发现轮廓
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat(edges.size(), CvType.CV_32S);
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        // 5 绘制轮廓
        for (int i = 0, len = contours.size(); i < len; i++) {
            Rect rect = Imgproc.boundingRect(contours.get(i));
            System.out.println(rect.x+"  "+rect.y+"  "+rect.width+"  "+rect.height);
                            Imgproc.rectangle(src, new Point(x1, y1), new Point(x1+10 , y1+10), new Scalar(255, 0, 0), 1, Imgproc.LINE_AA);
                Imgproc.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255), 1, Imgproc.LINE_AA);
            //人物中心点
//            if(rect.width==69) {
////                x1= rect.x+ rect.width/2;
//                y1= rect.y+144+rect.height;
//                person_x= rect.x;
//                person_y= rect.x;
////                System.out.println("x1: "+x1+"  y1:  "+y1);
////                System.out.println("per_y: "+person_y);
//                Imgproc.rectangle(src, new Point(x1, y1), new Point(x1+10 , y1+10), new Scalar(255, 0, 0), 1, Imgproc.LINE_AA);
//                Imgproc.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255), 1, Imgproc.LINE_AA);
//            }
//            //方块在右边
//            else if(rect.x>person_x&&rect.y<person_y+150) {
//                flag=0;
//                x2= rect.x+ rect.width/2+50;
//                y2= rect.y+ rect.height/2-60;
//                Imgproc.rectangle(src, new Point(x2, y2), new Point(x2+10 , y2+10), new Scalar(255, 0, 0), 1, Imgproc.LINE_AA);
//                Imgproc.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255), 1, Imgproc.LINE_AA);
//            }
//            //方块在组上面
//            else if(rect.y>person_y&&index==1&&rect.y!=444&&rect.y!=460){
//                index=0;
//                x2= rect.x+ rect.width/2+50;
//                y2= rect.y+ rect.height/2-70;
//                Imgproc.rectangle(src, new Point(x2, y2), new Point(x2+10 , y2+10), new Scalar(255, 0, 0), 1, Imgproc.LINE_AA);
//                Imgproc.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255), 1, Imgproc.LINE_AA);
//            }
//            Imgproc.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255), 1, Imgproc.LINE_AA);
        }
        //方块相邻
//        if(flag==1&&index==1){
//            x2=x1+300;
//            y2=y1-135;
//            Imgproc.rectangle(src, new Point(x2, y2), new Point(x2+10 , y2+10), new Scalar(255, 0, 0), 1, Imgproc.LINE_AA);
//        }
        // 6 缩小图片
        Imgproc.resize(src, src, new Size(src.width() / 2, src.height() / 2));
        // 7 显示结果
        HighGui.imshow("结果", src);
        HighGui.waitKey(0);
        HighGui.destroyAllWindows();
    }

}


