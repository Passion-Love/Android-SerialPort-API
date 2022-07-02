package android.serialport.sample;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class SubArea {

    Integer areaShape;

    //初始点坐标
    Integer longitude;
    Integer latitude;

    //圆和扇形
    Double radius;
    Double leftBoundary;
    Double rightBoundary;

    //方形
    Double eDimension;
    Double nDimension;
    Double Orientation;

    //折线
    List<Point> points;

    //文本类型
    String text;

    //图像类型
    String attach;
}

class Point{
    Double pointAngle;
    Double distance;
}
