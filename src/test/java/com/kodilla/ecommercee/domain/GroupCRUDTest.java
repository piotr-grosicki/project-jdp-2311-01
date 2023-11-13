package com.kodilla.ecommercee.domain;

import com.kodilla.ecommercee.repository.GroupRepository;
import com.kodilla.ecommercee.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class GroupCRUDTest {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ProductRepository productRepository;

    private Group group;

    @BeforeEach
    public void setUp() {
        group = new Group();
        group.setGroupId(1L);
        group.setName("electronics");
        group.setDescription("some equipment");

    }

    @Test
    @DirtiesContext
    public void testCreateGroup() {

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

        //When
        groupRepository.save(group);
        Group foundGroup = groupRepository.findById(group.getGroupId()).orElse(null);

        //Then
        assertNotNull(foundGroup);
        assertEquals(group.getGroupId(),foundGroup.getGroupId());
        assertEquals(group.getGroupId(), foundGroup.getGroupId());
        assertEquals(group.getName(), foundGroup.getName());
        assertEquals(group.getDescription(), foundGroup.getDescription());
    }

    @Test
    @DirtiesContext
    public void testFindAllGroups() {

        //Given
        Group group2 = new Group();
        group2.setGroupId(2L);
        group2.setName("electric stuff");
        group2.setDescription("jigsaw");

        //When
        groupRepository.save(group);
        groupRepository.save(group2);
        List<Group> groupList = groupRepository.findAll();

        //Then
        assertThat(groupList.size()).isEqualTo(2);
    }

    @Test
    @DirtiesContext
    public void testUpdateGroup() {

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

        //When
        Group groupAdd = groupRepository.save(group);
        groupRepository.deleteById(groupAdd.getGroupId());

        //Then
        Group groupDelete = groupRepository.findById(groupAdd.getGroupId()).orElse(null);
        assertNull(groupDelete);
    }

    @Test
    @DirtiesContext
    public void testGroupProductRelation() {

        //Given
        Product product = new Product();
        product.setProductId(100L);
        product.setNameProduct("Circular Saw");
        Product product2 = new Product();
        product2.setProductId(101L);
        product2.setNameProduct("Electric screwdriver");
        productRepository.save(product);
        productRepository.save(product2);

        //When
        group.getProductList().add(product);
        group.getProductList().add(product2);
        groupRepository.save(group);
        product.setGroup(group);
        product2.setGroup(group);
        Group productGroup = product.getGroup();

        //Then
        assertEquals(2, group.getProductList().size());
        assertEquals(group.getGroupId(), productGroup.getGroupId());
        assertEquals(product.getNameProduct(), productGroup.getProductList().get(0).getNameProduct());
        assertEquals(product.getProductId(), productGroup.getProductList().get(0).getProductId());
        assertEquals(product.getDescriptionProduct(), productGroup.getProductList().get(0).getDescriptionProduct());
        assertEquals(product.getPrice(), productGroup.getProductList().get(0).getPrice());
        assertEquals(product.getGroup(), productGroup.getProductList().get(0).getGroup());
        assertEquals(product2.getNameProduct(), productGroup.getProductList().get(1).getNameProduct());
        assertEquals(product2.getProductId(), productGroup.getProductList().get(1).getProductId());
        assertEquals(product.getDescriptionProduct(), productGroup.getProductList().get(1).getDescriptionProduct());
        assertEquals(product.getPrice(), productGroup.getProductList().get(1).getPrice());
        assertEquals(product.getGroup(), productGroup.getProductList().get(1).getGroup());
    }

    @Test
    @DirtiesContext
    public void testExistByName() {

        //When
        boolean isExisting = groupRepository.existsByName(group.getName());
        boolean isExisting2 = groupRepository.existsByName("stuff");

        //Then
        assertTrue(isExisting);
        assertFalse(isExisting2);
    }
}
