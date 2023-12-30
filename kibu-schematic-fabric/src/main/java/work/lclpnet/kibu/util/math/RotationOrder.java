package work.lclpnet.kibu.util.math;

public enum RotationOrder {

    XYZ(0, 1, 2),
    XZY(0, 2, 1),
    YXZ(1, 0, 2),
    YZX(1, 2, 0),
    ZXY(2, 0, 1),
    ZYX(2, 1, 0);

    public final int first, second, third;

    RotationOrder(int first, int second, int third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }
}
