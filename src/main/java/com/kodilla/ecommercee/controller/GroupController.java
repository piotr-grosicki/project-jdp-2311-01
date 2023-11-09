package com.kodilla.ecommercee.controller;

import com.kodilla.ecommercee.domain.GroupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/v1/groups")
@RequiredArgsConstructor
public class GroupController {
    private final List<GroupDto> groupsDto;

    @GetMapping
    public ResponseEntity<List<GroupDto>> getGroupsDto() {
        return ResponseEntity.ok(groupsDto);
    }

    @GetMapping(value = "{groupId}")
    public ResponseEntity<GroupDto> getGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(groupsDto.get(groupId.intValue()));
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long groupId) {
        groupsDto.remove(groupId.intValue());
        System.out.println("Group with Id " + groupId + " deleted.");
        System.out.println("List of groups after this change:" + groupsDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<GroupDto> updateGroup(@PathVariable Long groupId, @RequestBody GroupDto groupDto) {
        System.out.println("Group to change:" + groupsDto.get(groupId.intValue()));
        groupsDto.get(groupId.intValue()).setName(groupDto.getName());
        groupsDto.get(groupId.intValue()).setDescription(groupDto.getDescription());
        System.out.println("Group after the change:" + groupsDto.get(groupId.intValue()));
        return ResponseEntity.ok(groupDto);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GroupDto> createGroup(@RequestBody GroupDto groupDto) {
        System.out.println("Adding group with id " + groupDto.getGroupId() + " named " + groupDto.getName() + " with description: " + groupDto.getDescription());
        groupsDto.add(groupDto);
        System.out.println("Group list after the change: ");
        System.out.println(groupsDto);
        return ResponseEntity.ok(groupDto);
    }
}
