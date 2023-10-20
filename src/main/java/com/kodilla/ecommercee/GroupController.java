package com.kodilla.ecommercee;

import com.kodilla.ecommercee.domain.Group;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/v1/groups")
@RequiredArgsConstructor
public class GroupController {
    private final List<Group> groups;
    public GroupController() {
        groups = new ArrayList<>();
        groups.add(new Group(1L, "Group#1", "Description of group#1"));
        groups.add(new Group(2L, "Group#2", "Description of group#2"));
        groups.add(new Group(3L, "Group#3", "Description of group#3"));
    }

    @GetMapping
    public ResponseEntity<String> getGroups() {
        return ResponseEntity.ok(groups.toString());
    }

    @GetMapping(value = "{groupId}")
    public ResponseEntity<String> getGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(groups.get(groupId.intValue()).toString());
    }

    @DeleteMapping("{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long groupId) {
        groups.remove(groupId.intValue());
        System.out.println("Group with Id " + groupId + " deleted.");
        System.out.println("List of groups after this change:" + groups.toString());
        return ResponseEntity.ok().build();
    }

    @PutMapping("{groupId}")
    public ResponseEntity<String> updateGroup(@PathVariable Long groupId, @RequestBody String dataToEdit) {
        String testResponse = "Updating group with Id " + groupId + " to " + dataToEdit;
        return ResponseEntity.ok(testResponse);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createGroup(@RequestBody Long groupId, @RequestBody String groupNameToAdd, @RequestBody String groupDescriptionToAdd) {
        String testResponse = "Adding group with id " + groupId + " named " + groupNameToAdd + " with description: " + groupDescriptionToAdd;
        return ResponseEntity.ok(testResponse);
    }
}
