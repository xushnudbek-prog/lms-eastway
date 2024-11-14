package uz.eastwaysolutions.lms.eastwaylms.utils.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uz.eastwaysolutions.lms.eastwaylms.dto.user.UserProfileDto;
import uz.eastwaysolutions.lms.eastwaylms.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "phoneNumber", target = "phoneNumber")
    @Mapping(source = "role", target = "role")
    UserProfileDto toUserProfileResponseDto(User user);
}
