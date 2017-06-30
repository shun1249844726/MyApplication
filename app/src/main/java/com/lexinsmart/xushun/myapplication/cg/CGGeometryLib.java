package com.lexinsmart.xushun.myapplication.cg;

/**
 * Created by xushun on 2017/6/20.
 */

public class CGGeometryLib {

    /**
     * @param p0    第一个点的坐标
     * @param p1    第二个点的坐标
     * @return      两个点之间的距离
     * 计算出两点之间的距离
     */
    public static double getDistanceBetween2Points(CGPoint p0, CGPoint p1) {
        double distance = Math.sqrt(Math.pow(p0.y - p1.y, 2) + Math.pow(p0.x - p1.x, 2));
        return distance;
    }

    /**
     * @param p
     * @param l
     * @return      该方法用于获取某点在某条直线上的投影点的坐标
     */
    public static CGPoint getProjectivePoint(CGPoint p, CGLine l) {
        CGPoint target = null;
        if(l.iskExists()) {
            if(l.k != 0) {
                CGLine newLine = new CGLine(-1/l.k, p.y -(-1/l.k)*p.x);
                target = getCrossPoint(l, newLine);
            } else {    // 如果直线l的斜率存在且斜率的值为0，明显是一条平行于x轴的直线~
                // 此时，点 p 到直线 l 的距离为：Math.abs(p.y-l.b)
                target = new CGPoint(p.x, l.b);
            }
        } else {    // 如果直线l的斜率不存在，明显是一条垂直于x轴的直线~
            // 此时，点 p 到直线 l 的距离为：Math.abs(p.x-l.extraX)
            target = new CGPoint(l.extraX, p.y);
        }
        CGDbg.println("点 ("+p.x+", "+p.y+") 在直线：y="+l.k+"x+"+l.b+" 上的投影点为 ("+target.x+", "+target.y+")");
        return target;
    }

    /**
     * 该方法用于求出两条直线的交点坐标
     * 这个方法是定制的，只有 l1, l2 均存在斜率 k 时方能使用（限制取消）~
     * @param l1
     * @param l2
     * @return
     */
    public static CGPoint getCrossPoint(CGLine l1, CGLine l2) {
//      dbgPrintln("into the getCrossPoint, l1: " + l1);
//      dbgPrintln("into the getCrossPoint, l2: " + l2);
        double x, y;
        if(l1.iskExists() && l2.iskExists()) {
            x = (l2.b - l1.b) / (l1.k - l2.k);
            y = l1.k * x + l1.b;
        } else if(!l1.iskExists() && l2.iskExists()) {
            x = l1.extraX;
            y = l2.k * x + l2.b;
        } else if(l1.iskExists() && !l2.iskExists()) {
            x = l2.extraX;
            y = l1.k * x + l1.b;
        } else {
            // 两条直线的斜率都不存在？！，不可能发生的情况！！
            x = 0;
            y = 0;
        }
        CGDbg.println("getCrossPoint, CGPoint(x=" + x + ", y=" + y + ")");
        return new CGPoint(x, y);
    }

    /**
     * @param line
     * 怎样判断是否符合要求？将过每组3个点中除开多边形顶点的两个点的直线方程求出来
     * 比较求出的 4 个 候选圆心点 哪个与此直线离的比较近，哪个就是符合要求的圆心点
     * 以下方法用于获取离特定直线距离最近的一个点(目前只支持斜率k存在的直线，以后慢慢扩充)！
     * 要得到距离特定直线距离最远的一个点只要稍作改动即可！
     */
    public static CGPoint getNearestPoint(CGPoint[] points, CGLine line) {
        double minDistance = 0;
        int minIndex = 0;
        if(line.iskExists()) {
            // 直线斜率存在的分支~
            for(int i = 0; i < points.length; ++ i) {
                CGPoint p = points[i];
                double d = Math.abs(line.k*p.x-p.y+line.b)/Math.sqrt(Math.pow(line.k,2)+1);
                if(i == 0) {
                    // 赋予初值，不然 minDistance 的值就为 0 了~
                    minDistance = d;
                }
                if(d < minDistance) {
                    minDistance = d;
                    minIndex = i;
                }
            }
        } else {
            // 直线斜率不存在的分支(亦即直线垂直于 x 轴)~
            for(int i = 0; i < points.length; ++ i) {
                CGPoint p = points[i];
                double d = Math.abs(p.x - line.extraX);
                if(i == 0) {
                    // 赋予初值，不然minDistance的值就为0了~
                    minDistance = d;
                }
                if(d < minDistance) {
                    minDistance = d;
                    minIndex = i;
                }
            }
        }
        CGPoint dest = points[minIndex];
        CGDbg.println("即将离开chooseOne()方法，圆心点为：("+dest.x+", "+dest.y+")");
        return dest;
    }

    /**
     * 获取传入两点的中点~
     * @param p1
     * @param p2
     * @return
     */
    public static CGPoint getMiddlePoint(CGPoint p1, CGPoint p2) {
        return new CGPoint((p1.x + p2.x) / 2.0f, (p1.y + p2.y) / 2.0f);
    }

    /**
     * 封装一下 Math 的 pow 、sqrt 方法，调用起来方便一些~
     * @param d1
     * @param d2
     * @return
     */
    public static double pow(double d1, double d2) {
        return Math.pow(d1, d2);
    }
    public static double sqrt(double d) {
        return Math.sqrt(d);
    }
    public static double sin(double theta) {
        return Math.sin(theta);
    }
    public static double cos(double theta) {
        return Math.cos(theta);
    }

    /**
     * 传入线段的两个端点，获取中点，以该中点为圆心做半径为 radius 的圆，
     * 经过线段中点做线段的垂线，返回垂线与圆的两个交点~
     * Objective-C 里面的结果有点儿问题，不知道是什么原因，来java 里面碰碰有运气~
     * @param p1        线段端点1
     * @param p2        线段端点2
     * @param radius    圆半径
     * @return          线段中垂线与圆的两个交点~
     */
    public static CGPoint[] getWhatIWanted(CGPoint p1, CGPoint p2, double radius) {
        CGPoint[] target = new CGPoint[2];
        CGPoint pMiddle = getMiddlePoint(p1, p2);
//      double segLength = getDistanceBetween2Points(p1, p2);

        CGLine l1 = new CGLine(p1, p2);
        if(l1.iskExists()) {
            if(l1.k != 0) {
                double kOfNewLine = -1 / l1.k;
                CGLine newLine = new CGLine(kOfNewLine, pMiddle);

                // 经过数学运算，得出二元一次方程组的表达式
                double A = pow(newLine.k, 2) + 1;
                double B = 2 * (newLine.k * newLine.b - newLine.k * pMiddle.y - pMiddle.x);
                double C = pow(pMiddle.x, 2) + pow((newLine.b - pMiddle.y), 2) - pow(radius, 2);
                double delta = pow(B, 2) - 4 * A * C;

                if(delta < 0) {    // 经实践检验有一定几率走入该分支，必须做特殊化处理~
                    // 2012。04。28。20。01，精度不够所致，换成double后无该情况出现~
                    CGDbg.println("竟然会无解，他妈的怎么回事儿啊！");
                    target[0] = new CGPoint(pMiddle.x, pMiddle.y - radius);
                    target[1] = new CGPoint(pMiddle.x, pMiddle.y + radius);
                } else {
                    double x1 = (-B + sqrt(delta)) / (2 * A);
                    double y1 = newLine.k * x1 + newLine.b;
                    target[0] = new CGPoint(x1, y1);

                    double x2 = (-B - sqrt(delta)) / (2 * A);
                    double y2 = newLine.k * x2 + newLine.b;
                    target[1] = new CGPoint(x2, y2);
                }
            } else {
                target[0] = new CGPoint(pMiddle.x, pMiddle.y - radius);
                target[1] = new CGPoint(pMiddle.x, pMiddle.y + radius);
            }
        } else {
            target[0] = new CGPoint(pMiddle.x - radius, pMiddle.y);
            target[1] = new CGPoint(pMiddle.x + radius, pMiddle.y);
        }
        System.out.println("target[0] ：[" + target[0].x + "\t" +target[0].y +"]"  );
        System.out.println("target[1] ：[" + target[1].x + "\t" +target[1].y +"]"  );

        System.out.println("target[0] 距离中点的距离为：" + getDistanceBetween2Points(target[0], pMiddle));
        System.out.println("target[1] 距离中点的距离为：" + getDistanceBetween2Points(target[1], pMiddle));
        return target;
    }


}