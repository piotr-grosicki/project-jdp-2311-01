package com.kodilla.ecommercee.controller;

import com.kodilla.ecommercee.domain.GroupDto;
import com.kodilla.ecommercee.exception.GroupNotFoundException;
import com.kodilla.ecommercee.exception.GroupAlreadyExistsException;
import com.kodilla.ecommercee.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    @GetMapping
    public List<GroupDto> getAllGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping("{groupId}")
    public ResponseEntity<GroupDto> getGroupById(@PathVariable Long groupId) throws GroupNotFoundException {
        return ResponseEntity.ok(groupService.getGroupById(groupId));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createGroup(@RequestBody GroupDto groupDto) throws GroupAlreadyExistsException {
        groupService.createGroup(groupDto);
        return new ResponseEntity<>("The Group has been created", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<GroupDto> updateGroup(@RequestBody GroupDto groupDto) throws GroupNotFoundException {
        return ResponseEntity.ok(groupService.updateGroup(groupDto));
    }

    @DeleteMapping("{groupId}")
    public ResponseEntity<String> deleteGroup(@PathVariable Long groupId) throws GroupNotFoundException {
        groupService.deleteGroup(groupId);
        return new ResponseEntity<>("The Group has been deleted", HttpStatus.OK);
    }
}
