package com.kodilla.ecommercee.service;

import com.kodilla.ecommercee.domain.Group;
import com.kodilla.ecommercee.domain.GroupDto;
import com.kodilla.ecommercee.exceptions.GroupAlreadyExistsException;
import com.kodilla.ecommercee.exceptions.GroupNotFoundException;
import com.kodilla.ecommercee.mapper.GroupMapper;
import com.kodilla.ecommercee.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    public List<GroupDto> getAllGroups() {
        List<Group> groupList = groupRepository.findAll();
        return groupMapper.mapToGroupDtoList(groupList);
    }

    public GroupDto getGroupById(final Long groupId) throws GroupNotFoundException {
        Group group = groupRepository.findById(groupId).orElseThrow(GroupNotFoundException::new);
        return groupMapper.mapToGroupDto(group);
    }

    public void createGroup(final GroupDto groupDto) throws GroupAlreadyExistsException {
        Group group = groupMapper.mapToGroup(groupDto);
        boolean isExisting = groupRepository.existsByName(group.getName());
        if(!isExisting) {
            groupRepository.save(group);
        } else {
            throw new GroupAlreadyExistsException();
        }
    }

    public GroupDto updateGroup(final GroupDto groupDto) throws  GroupNotFoundException {
        groupRepository.findById(groupDto.getGroupId()).orElseThrow(GroupNotFoundException::new);
        Group group = groupMapper.mapToGroup(groupDto);
        Group updatedGroup = groupRepository.save(group);
        return groupMapper.mapToGroupDto(updatedGroup);
    }

    public void deleteGroup(final Long groupId) throws  GroupNotFoundException {
        try {
            groupRepository.deleteById(groupId);
        } catch(Exception exception) {
            throw new GroupNotFoundException();
        }
    }
}
