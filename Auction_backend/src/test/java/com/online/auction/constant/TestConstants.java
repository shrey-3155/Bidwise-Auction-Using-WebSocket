package com.online.auction.constant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestConstants {
    public static final String JWT_TOKEN = "jwtToken123";

    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String VALID_REFRESH_TOKEN = "validRefreshToken";

    public static final String NEW_ACCESS_TOKEN = "newAccessToken";

    public static final String TEST_EMAIL = "test@example.com";

    public static final String TOKEN_EMAIL = "sylv.s@motupatlu.com";

    public static final String PASSWORD = "password";

    public static final int INTEGER_ONE = 1;

    public static final int INTEGER_TWO_HUNDRED = 200;

    public static final String BEARER = "Bearer ";

    public static final String BAD_CREDENTIALS = "Bad credentials";

    public static final String USER_REGISTRATION_SUCCESS_MSG = "User Registered Successfully";

    public static final String SECRET_KEY = "secretKey";

    public static final String SECRET_KEY_VALUE = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    public static final String JWT_EXPIRATION = "jwtExpiration";

    public static final long JWT_EXPIRATION_VALUE = 86400000;

    public static final String REFRESH_EXPIRATION = "refreshExpiration";

    public static final long REFRESH_EXPIRATION_VALUE = 604800000;

    public static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzeWx2LnNAbW90dXBhdGx1LmNvbSIsImlhdCI6MTcxODM0MTM1NiwiZXhwIjoxNzE4NDI3NzU2fQ.vRzfW-y-aJ8XHBCgDuz3nXN-0G61sR0QyKTMptufBlk";

    public static final String NON_EXPIRED_ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzeWx2LnNAbW90dXBhdGx1LmNvbSIsImlhdCI6MTcxODMzNzg5MCwiZXhwIjoxNzE4NDI0MjkwfQ.K535YSpotJIsumSUV3_QpxZlbQiaPWc_ejGvoLlc1R4";

    public static final String ROLE = "role";

    public static final String ADMIN = "admin";

    public static final String CITY_HALIFAX = "Halifax";
    public static final String RECEPIENT_EMAIL = "test@example.com";
    public static final String EMAIL_SUBJECT = "Test Subject";
    public static final String EMAIL_BODY = "Test Body";

    // Define the formatter to parse the ISO-8601 date-time string
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    // Use the formatter to parse the date-time string into a LocalDateTime object
    public static final LocalDateTime START_TIME = LocalDateTime.parse("2016-12-01T00:00:00", FORMATTER);

    public static final LocalDateTime END_TIME = LocalDateTime.parse("2016-12-01T00:00:00");

    public static final String USER_NOT_FOUND = "User Not Found";

    public static final String BID_AMOUNT_ONE_HUNDRED = "100";

    public static final String AUCTION_BID_LESS_THAN_HIGHEST_BID_ERROR_MSG = "Please enter a Bid higher than highest bid";

    public static final String PAINTING_ITEM_CATEGORY = "Paintings";

    public static final String ITEM_NAME_1 = "Item 1";

    public static final String AUCTION_RECORD_NOT_FOUND_MSG = "Record not found to update post Auction state";

    public static final String USA = "USA";

    public static final String TEST_AUCTION = "Test Auction";
    public static final String AUCTION_PHOTO_URL = "Photo Url";

    public static final String AUCTION_ITEM_MAKER = "Item Maker";
    public static final String AUCTION_ITEM_DESCRIPTION = "Item DESCRIPTION";
    public static final String AUCTION_PRICE_PAID = "200";
    public static final String AUCTION_USD_CURRENCY = "USD";

    public static final String AUCTION_CITY_NAME = "HALIFAX";





}
