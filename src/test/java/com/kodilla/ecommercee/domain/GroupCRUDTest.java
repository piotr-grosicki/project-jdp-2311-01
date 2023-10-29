package com.kodilla.ecommercee.domain;

import com.kodilla.ecommercee.repository.GroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GroupCRUDTest {

    @Autowired
    private GroupRepository groupRepository;

    @Test
    @DirtiesContext
    public void testCreateGroup() {
        //Given
        Group group = new Group();
        group.setName("electronics");
        group.setDescription("some equipment");

        //When
        groupRepository.save(group);
        String nG = group.getName();
        Group group1 = groupRepository.findById(group.getGroupId()).orElse(null);

        //Then
        assertEquals(nG, "electronics");
        assertNotNull(group1);
    }

    @Test
    @DirtiesContext
    public void testFindGroupById() {
        //Given
        Group group = new Group();
        group.setName("electronics");
        group.setDescription("some equipment");

        //When
        groupRepository.save(group);
        Group foundGroup = groupRepository.findById(group.getGroupId()).orElse(null);

        //Then
        assertNotNull(foundGroup);
        assertEquals(group.getGroupId(),foundGroup.getGroupId());
    }

    @Test
    @DirtiesContext
    public void testUpdateGroup() {
        //Given
        Group group = new Group();
        group.setName("electronics");
        group.setDescription("some equipment");

        //When
        Group group1 = groupRepository.save(group);
        group1.setName("thing");
        group1.setDescription("new stuff");
        groupRepository.save(group1);
        Group groupUpdate = groupRepository.findById(group1.getGroupId()).orElse(null);

        //Then
        assertNotNull(groupUpdate);
        assertEquals("thing",groupUpdate.getName());
        assertEquals("new stuff", groupUpdate.getDescription());
    }

    @Test
    @DirtiesContext
    public void testDeleteGroup() {
        //Given
        Group group = new Group();
        group.setName("electronics");
        group.setDescription("some equipment");

        //When
        Group groupAdd = groupRepository.save(group);
        groupRepository.deleteById(groupAdd.getGroupId());

        //Then
        Group groupDelete = groupRepository.findById(groupAdd.getGroupId()).orElse(null);
        assertNull(groupDelete);
    }

}
