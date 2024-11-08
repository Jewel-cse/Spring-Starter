package dev.start.init.web.payload.pojo;

import dev.start.init.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromotionPayload {
    private User user;
    private String productImageUrl;
    private List<String> features;
    private String ctaLink;
}
