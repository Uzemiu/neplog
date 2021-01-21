package cn.neptu.neplog.constant;

public interface ArticleConstant {

    Integer STATUS_DRAFT = 0;

    Integer STATUS_PUBLISHED = 4;

    Integer VIEW_PERMISSION_ANYBODY = 0;

    Integer VIEW_PERMISSION_USER_ONLY = 4;

    Integer VIEW_PERMISSION_PRIVATE = 16;

    Integer COMMENT_PERMISSION_ANYBODY = 0;

    Integer COMMENT_PERMISSION_REQUIRE_REVIEW = 4;

    Integer COMMENT_PERMISSION_USER_ONLY = 8;

    Integer COMMENT_PERMISSION_CLOSED = 16;
}
