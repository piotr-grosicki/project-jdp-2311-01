package com.kodilla.ecommercee.controller;

import com.kodilla.ecommercee.domain.Group;
import com.kodilla.ecommercee.domain.Product;
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
    private final List<Group> groups;

    @GetMapping
    public ResponseEntity<List<Group>> getGroups() {
        return ResponseEntity.ok(groups);
    }

    @GetMapping(value = "{groupId}")
    public ResponseEntity<Group> getGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(groups.get(groupId.intValue()));
    }

    @DeleteMapping("{groupId}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long groupId) {
        groups.remove(groupId.intValue());
        System.out.println("Group with Id " + groupId + " deleted.");
        System.out.println("List of groups after this change:" + groups);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{groupId}")
    public ResponseEntity<Group> updateGroup(@PathVariable Long groupId, @RequestBody Group group) {
        System.out.println("Group to change:" + groups.get(groupId.intValue()));
        groups.get(groupId.intValue()).setName(group.getName());
        groups.get(groupId.intValue()).setDescription(group.getDescription());
        groups.get(groupId.intValue()).setProduct(new Product());
        System.out.println("Group after the change:" + groups.get(groupId.intValue()));
        return ResponseEntity.ok(group);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        System.out.println("Adding group with id " + group.getGroupId() + " named " + group.getName() + " with description: " + group.getDescription());
        groups.add(group);
        System.out.println("Group list after the change: ");
        System.out.println(groups);
        return ResponseEntity.ok(group);
    }
}
