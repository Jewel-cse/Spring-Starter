package dev.start.init.util;

import dev.start.init.constants.ErrorConstants;
import net.datafaker.Faker;

import java.util.List;

public final class MerketingUtils {

    private static final Faker FAKER = new Faker();

    private MerketingUtils() {
        throw new AssertionError(ErrorConstants.NOT_INSTANTIABLE);
    }

    //get product image
    public static String getProductImageUrl(){
        return  FAKER.internet().url() + "/product-image.jpg";
    }

    //get feature list
    public static List<String> getFeatureList(){
        return List.of(
                FAKER.commerce().material(),
                FAKER.commerce().productName(),
                FAKER.commerce().promotionCode()
        );
    }
    //get ctaLink
    public static String getCtaLink(){
        return FAKER.internet().url() + "/promo";
    }

}
