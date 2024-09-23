package dev.start.init.mapper;

import dev.start.init.dto.user.UserHistoryDto;
import dev.start.init.entity.user.UserHistory;
import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 * The UserDtoMapper class outlines the supported conversions between User and other objects.
 *
 * @author Md Jewel
 * @version 1.0
 * @since 1.0
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserHistoryMapper {

    UserHistoryMapper MAPPER = Mappers.getMapper(UserHistoryMapper.class);

    /**
     * Convert and populate a userHistories to userHistoryDto object.
     *
     * @param userHistories the userHistories
     * @return the userHistoryDto
     */
    List<UserHistoryDto> toUserHistoryDto(Set<UserHistory> userHistories);

    /**
     * Convert and populate a userHistory to userHistoryDto object.
     *
     * @param userHistory the userHistory
     * @return the userHistoryDto
     */
    @Mapping(
            target = "timeElapsedDescription",
            expression =
                    "java(dev.start.init.util.core.DateUtils.getTimeElapsedDescription(userHistory.getCreatedAt()))")
    @Mapping(
            target = "separateDateFormat",
            expression =
                    "java(dev.start.init.util.core.DateUtils.getTimeElapsed(userHistory.getCreatedAt()))")
    UserHistoryDto toUserHistoryDto(UserHistory userHistory);
}
