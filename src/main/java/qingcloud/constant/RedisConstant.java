package qingcloud.constant;

public class RedisConstant {
    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final Long LOGIN_CODE_TTL = 1L;
    public static final String LOGIN_USER_KEY = "login:token:";
    public static final Long LOGIN_USER_TTL = 300L;

    public static final Long CACHE_NULL_TTL = 2L;

    public static final Long CACHE_COURSE_TTL = 30L;
    public static final String CACHE_COURSE_KEY = "cache:course:";

    public static final String LOCK_COURSE_KEY = "lock:course:";
    public static final Long LOCK_COURSE_TTL = 10L;

    public static final String SECKILL_STOCK_KEY = "voucher:stock:";
    public static final String BLOG_LIKED_KEY = "blog:liked:";
    public static final String FEED_KEY = "feed:";

    public static final String USER_SIGN_KEY = "sign:";

    // 课程统计信息（购买数、点赞数）
    public static final String COURSE_PURCHASE_KEY = "course:purchase:";//使用 zset
    public static final String COURSE_LIKE_KEY = "course:like:"; //使用string


    public static final String TEACHER_FANS_KEY = "teacher:fans"; //使用 zset


}
