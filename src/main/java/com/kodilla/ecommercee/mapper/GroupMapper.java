package com.kodilla.ecommercee.mapper;

import com.kodilla.ecommercee.domain.Group;
import com.kodilla.ecommercee.domain.GroupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupMapper {


    public Group mapToGroup(final GroupDto groupDto) {
        return new Group (
                groupDto.getGroupId(),
                groupDto.getName(),
                groupDto.getDescription(),
                ProductMapper.mapToProductList(groupDto.getProductDtoList())
        );
    }

    public GroupDto mapToGroupDto(final Group group) {
        return new GroupDto(
                group.getGroupId(),
                group.getName(),
                group.getDescription(),
                ProductMapper.mapToProductDtoList(group.getProductList())
        );
    }

    public List<GroupDto> mapToGroupDtoList(final List<Group> groupList) {
        return groupList.stream()
                .map(this::mapToGroupDto)
                .collect(Collectors.toList());
    }
}
