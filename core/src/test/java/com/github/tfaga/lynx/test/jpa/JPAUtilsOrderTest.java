package com.github.tfaga.lynx.test.jpa;

import com.github.tfaga.lynx.beans.QueryOrder;
import com.github.tfaga.lynx.beans.QueryParameters;
import com.github.tfaga.lynx.enums.OrderDirection;
import com.github.tfaga.lynx.enums.QueryFormatError;
import com.github.tfaga.lynx.exceptions.NoSuchEntityFieldException;
import com.github.tfaga.lynx.test.entities.User;
import com.github.tfaga.lynx.test.rules.JpaRule;
import com.github.tfaga.lynx.utils.JPAUtils;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

import java.util.List;

import javax.persistence.EntityManager;

/**
 * @author Tilen Faganel
 */
public class JPAUtilsOrderTest {

    @ClassRule
    public static JpaRule server = new JpaRule();

    private EntityManager em = server.getEntityManager();

    @Test
    public void testSingleOrder() {

        QueryOrder qo = new QueryOrder();
        qo.setField("firstname");
        qo.setOrder(OrderDirection.ASC);

        QueryParameters q = new QueryParameters();
        q.getOrder().add(qo);

        List<User> users = JPAUtils.queryEntities(em, User.class, q);

        Assert.assertNotNull(users);
        Assert.assertEquals(users.size(), 100);
        Assert.assertNotNull(users.get(0).getFirstname());
        Assert.assertEquals("Adam", users.get(0).getFirstname());
        Assert.assertNotNull(users.get(99).getFirstname());
        Assert.assertEquals("Walter", users.get(99).getFirstname());
    }

    @Test
    public void testSingleOrderDesc() {

        QueryOrder qo = new QueryOrder();
        qo.setField("lastname");
        qo.setOrder(OrderDirection.DESC);

        QueryParameters q = new QueryParameters();
        q.getOrder().add(qo);

        List<User> users = JPAUtils.queryEntities(em, User.class, q);

        Assert.assertNotNull(users);
        Assert.assertEquals(users.size(), 100);
        Assert.assertNotNull(users.get(0).getLastname());
        Assert.assertEquals("Willis", users.get(0).getLastname());
        Assert.assertNotNull(users.get(99).getLastname());
        Assert.assertEquals("Allen", users.get(99).getLastname());
    }

    @Test
    public void testMultipleOrders() {

        QueryParameters q = new QueryParameters();

        QueryOrder qo = new QueryOrder();
        qo.setField("role");
        qo.setOrder(OrderDirection.DESC);
        q.getOrder().add(qo);

        qo = new QueryOrder();
        qo.setField("country");
        qo.setOrder(OrderDirection.ASC);
        q.getOrder().add(qo);

        qo = new QueryOrder();
        qo.setField("lastname");
        qo.setOrder(OrderDirection.DESC);
        q.getOrder().add(qo);

        List<User> users = JPAUtils.queryEntities(em, User.class, q);

        Assert.assertNotNull(users);
        Assert.assertEquals(users.size(), 100);
        Assert.assertNotNull(users.get(0).getFirstname());
        Assert.assertNotNull(users.get(0).getLastname());
        Assert.assertEquals("Nicholas", users.get(0).getFirstname());
        Assert.assertEquals("Ramirez", users.get(0).getLastname());
        Assert.assertNotNull(users.get(99).getFirstname());
        Assert.assertNotNull(users.get(99).getLastname());
        Assert.assertEquals("Diane", users.get(99).getFirstname());
        Assert.assertEquals("Allen", users.get(99).getLastname());
    }

    @Test
    public void testNullDirection() {

        QueryOrder qo = new QueryOrder();
        qo.setField("lastname");

        QueryParameters q = new QueryParameters();
        q.getOrder().add(qo);

        List<User> users = JPAUtils.queryEntities(em, User.class, q);

        Assert.assertNotNull(users);
        Assert.assertEquals(users.size(), 100);
        Assert.assertNotNull(users.get(0).getLastname());
        Assert.assertEquals("Allen", users.get(0).getLastname());
        Assert.assertNotNull(users.get(99).getLastname());
        Assert.assertEquals("Willis", users.get(99).getLastname());
    }

    @Test
    public void testNullField() {

        QueryOrder qo = new QueryOrder();

        QueryParameters q = new QueryParameters();
        q.getOrder().add(qo);

        List<User> users = JPAUtils.queryEntities(em, User.class, q);

        Assert.assertNotNull(users);
        Assert.assertEquals(users.size(), 100);
        Assert.assertNotNull(users.get(0).getLastname());
        Assert.assertEquals("Campbell", users.get(0).getLastname());
        Assert.assertNotNull(users.get(99).getLastname());
        Assert.assertEquals("George", users.get(99).getLastname());
    }

    @Test
    public void testNonExistentColumn() {

        QueryOrder qo = new QueryOrder();
        qo.setField("lstnm");
        qo.setOrder(OrderDirection.DESC);

        QueryParameters q = new QueryParameters();
        q.getOrder().add(qo);

        try {

            JPAUtils.queryEntities(em, User.class, q);
            Assert.fail("No exception was thrown");
        } catch (NoSuchEntityFieldException e) {

            Assert.assertEquals(e.getField(), "lstnm");
        }
    }

    @Test
    public void testCaseSensitiveField() {

        QueryOrder qo = new QueryOrder();
        qo.setField("firsTNAmE");
        qo.setOrder(OrderDirection.ASC);

        QueryParameters q = new QueryParameters();
        q.getOrder().add(qo);

        try {

            JPAUtils.queryEntities(em, User.class, q);
            Assert.fail("No exception was thrown");
        } catch (NoSuchEntityFieldException e) {

            Assert.assertEquals(e.getField(), "firsTNAmE");
        }
    }
}