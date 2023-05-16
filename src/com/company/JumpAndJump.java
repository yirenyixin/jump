package com.company;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class JumpAndJump extends JFrame implements MouseListener,Runnable{

    private static int X2 = 0;
    private static int Y2 = 0;
    private static int X1 = 0;
    private static int Y1 = 0;

    public void Jump(int ms) {
//        int a = (int) (Math.random() * 1000);
//        int b = (int) (Math.random() * 1000);
//        int c = (int) (Math.random() * 1000);
//        int d = (int) (Math.random() * 1000);
//        System.out.println(a+" "+b+"  "+c+"  "+d);
        try {
            int a = (int) (Math.random() * 1);
            int b = (int) (Math.random() * 1);
            int c = (int) (Math.random() * 1);
            int d = (int) (Math.random() * 1);
            Runtime.getRuntime().exec("adb shell input swipe " + a + " " + b + " " + c + " " + d + " " + ms);
        } catch (IOException e) {

        }
    }

    public void getPNG() {
        try {
            Runtime.getRuntime().exec("adb shell screencap -p /sdcard/1.png");
            Runtime.getRuntime().exec("adb pull /sdcard/1.png e://adb");

        } catch (Exception e) {
            System.out.println("保存错误");
        }

    }


    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }
    public int sign = 0;
    public int one = 0;
    private static final long serialVersionUID = 1L;


    public JumpAndJump(String title) {
        super(title);
        this.setSize(800, 800);
        this.setLocation(600, 100);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);
        this.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println(e.getX()+"..."+e.getY());
        if (e.getX() < 550) {
            if (sign == 0) {
                X1 = e.getX();
                Y1 = e.getY();
                sign++;
            } else {
                sign = 0;
                X2 = e.getX();
                Y2 = e.getY();
                int res = (X2 - X1) * (X2 - X1) + (Y2 - Y1) * (Y2 - Y1);
                double b = Math.sqrt(res);
                System.out.println(b);
                Jump((int) b * 3);
            }
        }else if(e.getX()>690&&e.getX()<760&&e.getY()>60&&e.getY()<90) {

            System.out.println("获取图片");
            this.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void paint(Graphics g) {

        BufferedImage image = null;
        try {

            File file = new File("E:\\adb\\1.png");
            image = ImageIO.read(file);
        } catch (Exception e) {

        }
        if (image != null) {
            g.drawImage(image, 10, 10, image.getWidth(this) / 2, image.getHeight(this) / 2, null);
//            System.out.println(image.getWidth(this) / 2+"..."+image.getHeight(this) / 2);
        }
    }

    public static void main(String[] args) {
        JumpAndJump w = new JumpAndJump("跳一跳");
        w.run();
    }
    @Override
    public void run() {
        while(true) {
            getPNG();
            try {
                Thread.sleep(4000);
                this.repaint();
                contour();
            } catch (InterruptedException e) {

            }
        }
    }
    public void contour() {
        int x1=0;
         int y1=0;
         int person_x=99999;
         int person_y=99999;
       int x2=0;
        int y2=0;
         int index=1;
        int flag=1;
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
//            System.out.println(rect.x+"  "+rect.y+"  "+rect.width+"  "+rect.height);
            //人物中心点
            if(rect.width==62) {
                x1= rect.x+ rect.width/2;
                y1= rect.y+144+rect.height;
                person_x= rect.x;
                person_y= rect.x;
//                System.out.println("x1: "+x1+"  y1:  "+y1);
//                System.out.println("per_y: "+person_y);
                Imgproc.rectangle(src, new Point(x1, y1), new Point(x1+10 , y1+10), new Scalar(255, 0, 0), 1, Imgproc.LINE_AA);
                Imgproc.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255), 1, Imgproc.LINE_AA);
            }
            //方块在右边
            else if(rect.x>person_x&&rect.y<person_y+150) {
                flag=0;
                x2= rect.x+ rect.width/2+50;
                y2= rect.y+ rect.height/2-60;
                Imgproc.rectangle(src, new Point(x2, y2), new Point(x2+10 , y2+10), new Scalar(255, 0, 0), 1, Imgproc.LINE_AA);
                Imgproc.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255), 1, Imgproc.LINE_AA);
            }
            //方块在组上面
            else if(rect.y>person_y&&index==1&&rect.y!=444&&rect.y!=460){
                index=0;
                x2= rect.x+ rect.width/2+50;
                y2= rect.y+ rect.height/2-70;
                Imgproc.rectangle(src, new Point(x2, y2), new Point(x2+10 , y2+10), new Scalar(255, 0, 0), 1, Imgproc.LINE_AA);
                Imgproc.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255), 1, Imgproc.LINE_AA);
            }
//            Imgproc.rectangle(src, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255), 1, Imgproc.LINE_AA);
        }
        //方块相邻
        if(flag==1&&index==1){
            x2=x1+300;
            y2=y1-135;
            Imgproc.rectangle(src, new Point(x2, y2), new Point(x2+10 , y2+10), new Scalar(255, 0, 0), 1, Imgproc.LINE_AA);
        }
        System.out.println(x1+"  "+y1+"  "+x2+"  "+y2);
        int res = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
        double b = Math.sqrt(res);
        Jump((int) b * 2);
        // 6 缩小图片
//        Imgproc.resize(src, src, new Size(src.width() / 2, src.height() / 2));
//        // 7 显示结果
//        HighGui.imshow("结果", src);
//        HighGui.waitKey(0);
//        HighGui.destroyAllWindows();

    }
}
