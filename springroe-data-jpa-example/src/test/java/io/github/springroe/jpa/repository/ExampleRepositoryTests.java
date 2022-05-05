package io.github.springroe.jpa.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import io.github.springroe.jpa.example.ExampleApplication;
import io.github.springroe.jpa.example.repository.ExampleRepository;
import io.github.springroe.jpa.example.repository.entity.Example;
import io.github.springroe.jpa.example.repository.entity.QExample;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ExampleApplication.class})
@Transactional
public class ExampleRepositoryTests {

    @Autowired
    private ExampleRepository exampleRepository;

    @BeforeEach
    public void before() {
        Example example = saveExample();
        Assert.assertNotNull(example.getId());
    }

    public Example saveExample() {
        Example example = new Example();
        example.setName("示例");
        example.setCreatedDate(new Date());
        exampleRepository.save(example);
        return example;
    }

    @Test
    public void findAllTest() {
        List<Example> list = exampleRepository.findAll();
        Assert.assertEquals(list.size(), 1);
    }

    @Test
    public void findPageTest() {
        Pageable pageable = Pageable.ofSize(1);
        Page<Example> page = exampleRepository.findAll(pageable);
        Assert.assertEquals(1, page.getNumberOfElements());

        page = exampleRepository.findAll(QExample.example.name.eq("示例"), pageable);
        Assert.assertEquals(1, page.getNumberOfElements());

    }

    @Test
    public void findBySortTest() throws InterruptedException {
        Thread.sleep(100);
        saveExample();
        List<Example> list = exampleRepository.findAll(Sort.by(Sort.Direction.DESC, "createdDate"));
        Assert.assertTrue(list.get(0).getCreatedDate().after(list.get(1).getCreatedDate()));
    }

    @Test
    public void findByExample() {
        Example example = new Example();
        example.setName("示例");
        List<Example> list = exampleRepository.findAll(org.springframework.data.domain.Example.of(example));
        Assert.assertEquals(1, list.size());

        saveExample();
        list = exampleRepository.findAll(org.springframework.data.domain.Example.of(example), Sort.by(Sort.Direction.DESC, "createdDate"));
        Assert.assertTrue(list.get(0).getCreatedDate().after(list.get(1).getCreatedDate()));
    }

    @Test
    public void findBySpecification() {
        List<Example> list = exampleRepository.findAll((root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.equal(root.get("name"), "示例");
            return criteriaBuilder.and(predicate);
        });
        Assert.assertEquals(1, list.size());

        saveExample();
        list = exampleRepository.findAll((root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.equal(root.get("name"), "示例");
            return criteriaBuilder.and(predicate);
        }, Sort.by(Sort.Direction.DESC, "createdDate"));
        Assert.assertTrue(list.get(0).getCreatedDate().after(list.get(1).getCreatedDate()));

    }

    @Test
    public void findByOrderSpecifier() throws InterruptedException {
        Thread.sleep(100);
        saveExample();
        OrderSpecifier orderSpecifier = new OrderSpecifier(Order.DESC, QExample.example.createdDate);
        Iterable<Example> iterable = exampleRepository.findAll(orderSpecifier);
        Iterator<Example> iterator = iterable.iterator();
        Assert.assertTrue(iterator.next().getCreatedDate().after(iterator.next().getCreatedDate()));
    }

    @Test
    public void findByPredicate() {
        Iterable<Example> iterable = exampleRepository.findAll(QExample.example.name.eq("示例"));
        Iterator<Example> iterator = iterable.iterator();
        Assert.assertTrue(iterator.hasNext());
        Example example = saveExample();
        iterable = exampleRepository.findAll(QExample.example.name.eq("示例"), Sort.by(Sort.Direction.DESC, "createdDate"));
        iterator = iterable.iterator();
        Assert.assertTrue(iterator.next().getCreatedDate().after(iterator.next().getCreatedDate()));
    }

    @Test
    public void findByIdTest() {
        Example example = saveExample();
        Optional<Example> optional = exampleRepository.findById(example.getId());
        Assert.assertTrue(optional.isPresent());
    }

}
