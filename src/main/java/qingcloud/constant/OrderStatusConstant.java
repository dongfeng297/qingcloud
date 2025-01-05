package qingcloud.constant;

public class OrderStatusConstant {
    //订单状态(1:未支付 2:已支付 3:已完成 4:已取消 5:退款中 6:已退款)
    public static final int UNPAID = 1;
    public static final int PAID = 2;
    public static final int COMPLETED = 3;
    public static final int CANCELED = 4;
    public static final int REFUNDING = 5;
    public static final int REFUNDED = 6;
}
