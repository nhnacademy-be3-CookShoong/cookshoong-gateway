package store.cookshoong.www.cookshoonggateway.filter;

/**
 * 글로벌 필터 순서를 정의하기위한 클래스.
 *
 * @author koesnam (추만석)
 * @since 2023.07.27
 */
public final class FilterOrders {
    private FilterOrders() {}

    public static final int AUTHORIZATION = -1;
    public static final int BLACK_LIST = AUTHORIZATION + 1;
}
